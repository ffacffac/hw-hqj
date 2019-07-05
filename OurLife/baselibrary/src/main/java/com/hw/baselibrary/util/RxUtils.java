package com.hw.baselibrary.util;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava工具类
 *
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

public class RxUtils {

    public static <T> FlowableTransformer<T, T> rxFlSchedulerHelper() {
        // return FlowableTransformer flowableTransformer = new FlowableTransformer<T, T>() {
        //     @Override
        //     public Publisher<T> apply(Flowable<T> upstream) {
        //         return upstream.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        //     }
        // };
        return upstream -> upstream.observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 统一线程处理
     *
     * @param <T> 指定的泛型
     * @return ObservableTransformer
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        // return new ObservableTransformer<T, T>() {
        //     @Override
        //     public ObservableSource<T> apply(Observable<T> upstream) {
        //         return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        //     }
        // };
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
