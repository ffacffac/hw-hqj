package com.hw.ourlife.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hw.ourlife.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-11.
 */

public class MyFragmentManager {

    private static final List<Fragment> APP_FRAGMENTS = new ArrayList<>();

    public static List<Fragment> getAppFragments() {
        return APP_FRAGMENTS;
    }

    public static void addFragment(Fragment fragment) {
        APP_FRAGMENTS.add(fragment);
    }

    /**
     * 跳转
     *
     * @param fragment
     * @param layoutResId
     * @param fragmentManager
     */
    public static void gotoFragment(Fragment fragment, int layoutResId, Bundle bundle,
                                    FragmentManager fragmentManager) {
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //动画，向做弹出
        ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_left_out);
        ft.add(layoutResId, fragment);
        ft.show(fragment);
        ft.commit();
        addFragment(fragment);
    }

    public static int getAppFragmentSize() {
        return APP_FRAGMENTS.size();
    }

    /**
     * 模拟回退栈
     */
    public static void backFragment() {
        if (MyFragmentManager.APP_FRAGMENTS.isEmpty()) {
            return;
        }
        Fragment fragmentLast = APP_FRAGMENTS.get(APP_FRAGMENTS.size() - 1);
        if (fragmentLast.isVisible()) {
            FragmentTransaction ft = fragmentLast.getFragmentManager().beginTransaction();
            //动画,向右退出
            ft.setCustomAnimations(R.anim.push_right_out, R.anim.push_right_out);
            ft.remove(fragmentLast);
            APP_FRAGMENTS.remove(APP_FRAGMENTS.size() - 1);
            ft.commit();
            //如果有多个fragment，则显示下一个
            if (!APP_FRAGMENTS.isEmpty()) {
                fragmentLast = APP_FRAGMENTS.get(APP_FRAGMENTS.size() - 1);
                ft = fragmentLast.getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_left_out);
                ft.show(fragmentLast);
                ft.commit();
            }
        }
    }
}
