package com.hw.ourlife.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hw.baselibrary.util.Toast;
import com.hw.ourlife.ComponentHolder;
import com.hw.ourlife.MyApplication;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

public abstract class AbstractSimpleFragment extends Fragment {

    private Unbinder unBinder;
    private long clickTime;
    private CompositeDisposable compositeDisposable;
    public boolean isInnerFragment;
    protected Toast toast;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toast = ComponentHolder.getAppComponent().myApplication().toast;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unBinder = ButterKnife.bind(this, view);
        compositeDisposable = new CompositeDisposable();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEventAndData();
    }

    /**
     * 跳转
     *
     * @param fragment
     * @param layoutResId
     * @param fragmentManager
     */
    public static void gotoFragment(Fragment fragment, int layoutResId, Bundle bundle, FragmentManager fragmentManager) {
        MyFragmentManager.gotoFragment(fragment, layoutResId, bundle, fragmentManager);
    }

    /**
     * 模拟回退栈
     */
    public static void backFragment() {
        MyFragmentManager.backFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        unBinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //LeakCanary
        MyApplication.getRefWatcher().watch(this);
    }

    /**
     * 获取当前Activity的UI布局
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initEventAndData();
}
