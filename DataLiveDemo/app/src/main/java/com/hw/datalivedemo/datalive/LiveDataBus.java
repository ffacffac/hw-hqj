package com.hw.datalivedemo.datalive;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * @author huangqj
 *         Created by huangqj on 2019-08-05.
 */

public final class LiveDataBus {

    private static final String TAG = LiveDataBus.class.getName();
    private final Map<String, BusMutableLiveData<Object>> bus;

    private LiveDataBus() {
        bus = new HashMap<>();
        Timber.tag(TAG);
    }

    private static class SingletonHolder {
        private static final LiveDataBus LIVE_DATA_BUS = new LiveDataBus();
    }

    public static LiveDataBus getInstance() {
        return SingletonHolder.LIVE_DATA_BUS;
    }

    public <T> MutableLiveData<T> with(String key, Class<T> type) {
        if (!bus.containsKey(key)) {
            bus.put(key, new BusMutableLiveData<>());
        }
        return (MutableLiveData<T>) bus.get(key);
    }

    public MutableLiveData<Object> with(String key) {
        return with(key, Object.class);
    }

    private static class ObserverWrapper<T> implements Observer<T> {

        private Observer<T> observer;

        ObserverWrapper(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onChanged(@Nullable T t) {
            if (observer != null) {
                if (isCallOnObserve()) {
                    return;
                }
                observer.onChanged(t);
            }
        }

        private boolean isCallOnObserve() {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            if (stackTraceElements != null && stackTraceElements.length > 0) {
                for (StackTraceElement element : stackTraceElements) {
                    if ("android.arch.lifecycle.LiveData".equals(element.getClassName()) && "observeForever"
                            .equals(element.getMethodName())) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private static class BusMutableLiveData<T> extends MutableLiveData<T> {

        private Map<Observer<T>, Observer<T>> observerMap = new HashMap<>();

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            super.observe(owner, observer);
            try {
                hook(observer);
            } catch (Exception e) {
                Timber.e(e, "observe: ");
            }
        }

        @Override
        public void observeForever(@NonNull Observer<T> observer) {
            if (!observerMap.containsKey(observer)) {
                observerMap.put(observer, new ObserverWrapper(observer));
            }
            super.observeForever(observer);
        }

        @Override
        public void removeObserver(@NonNull Observer<T> observer) {
            Observer<T> realObserver;
            if (observerMap.containsKey(observer)) {
                realObserver = observerMap.remove(observer);
            } else {
                realObserver = observer;
            }
            super.removeObserver(realObserver);
        }

        // private void hook(
        //         @NonNull Observer<T> observer) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //     //get wrapper's version
        //     Class<LiveData> classLiveData = LiveData.class;
        //     Field fieldObservers = classLiveData.getDeclaredField("mObservers");
        //     fieldObservers.setAccessible(true);
        //     Object objectObservers = fieldObservers.get(this);
        //     Class<?> classObservers = objectObservers.getClass();
        //     Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
        //     methodGet.setAccessible(true);
        //     Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
        //     Object objectWrapper = null;
        //     if (objectWrapperEntry instanceof Map.Entry) {
        //         objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
        //     }
        //     if (objectWrapper == null) {
        //         throw new NullPointerException("Wrapper can not be bull!");
        //     }
        //     Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
        //     Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
        //     fieldLastVersion.setAccessible(true);
        //     //get livedata's version
        //     Field fieldVersion = classLiveData.getDeclaredField("mVersion");
        //     fieldVersion.setAccessible(true);
        //     Object objectVersion = fieldVersion.get(this);
        //     //set wrapper's version
        //     fieldLastVersion.set(objectWrapper, objectVersion);
        // }

        /**
         * 反射技术  使observer.mLastVersion = mVersion
         *
         * @param observer ob
         */
        private void hook(Observer<T> observer) throws Exception {
            //根据源码 如果使observer.mLastVersion = mVersion; 就不会走 回调OnChange方法了，所以就算注册
            //也不会收到消息
            //首先获取liveData的class
            Class<LiveData> classLiveData = LiveData.class;
            //通过反射获取该类里mObserver属性对象
            Field fieldObservers = classLiveData.getDeclaredField("mObservers");
            //设置属性可以被访问
            fieldObservers.setAccessible(true);
            //获取的对象是this里这个对象值，他的值是一个map集合
            Object objectObservers = fieldObservers.get(this);
            //获取map对象的类型
            Class<?> classObservers = objectObservers.getClass();
            //获取map对象中所有的get方法
            Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
            //设置get方法可以被访问
            methodGet.setAccessible(true);
            //执行该get方法，传入objectObservers对象，然后传入observer作为key的值
            Object objectWrapperEntry = methodGet.invoke(objectObservers, observer);
            //定义一个空的object对象
            Object objectWrapper = null;
            //判断objectWrapperEntry是否为Map.Entry类型
            if (objectWrapperEntry instanceof Map.Entry) {
                objectWrapper = ((Map.Entry) objectWrapperEntry).getValue();
            }
            if (objectWrapper == null) {
                throw new NullPointerException("Wrapper can not be null!");
            }

            //如果不是空 就得到该object的父类
            Class<?> classObserverWrapper = objectWrapper.getClass().getSuperclass();
            //通过他的父类的class对象，获取mLastVersion字段
            Field fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion");
            fieldLastVersion.setAccessible(true);
            Field fieldVersion = classLiveData.getDeclaredField("mVersion");
            fieldVersion.setAccessible(true);
            Object objectVersion = fieldVersion.get(this);
            //把mVersion 字段的属性值设置给mLastVersion
            fieldLastVersion.set(objectWrapper, objectVersion);
        }
    }
}
