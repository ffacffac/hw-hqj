package szu.wifichat.android.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.util.ArrayList;

import szu.wifichat.android.R;

public class FragmentTabUtils implements OnCheckedChangeListener {
    private ArrayList<Fragment> mFragments;
    private FragmentManager mFManager;
    private int mContainerId;
    private RadioGroup rg;
    private Context mContext;

    public FragmentTabUtils(Context context, ArrayList<Fragment> fragments,
                            FragmentManager FManager, RadioGroup rg, int containerId) {
        this.mContext = context;
        this.mFragments = fragments;
        this.mFManager = FManager;
        this.rg = rg;
        this.mContainerId = containerId;
        this.mContext = context;

        // 首次显示第一个Fragment
        FragmentTransaction ft = mFManager.beginTransaction();
        ft.add(mContainerId, fragments.get(0));
        ft.show(fragments.get(0));
        ft.commit();
        RadioButton rb = (RadioButton) rg.getChildAt(0);// 设置第一项被选中
        rb.setChecked(true);
        rg.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < rg.getChildCount(); i++) {
            Fragment fragment = mFragments.get(i);
            if (checkedId == group.getChildAt(i).getId()) {
                addFragment(fragment);// 显示当前的Fragment
            } else {
                // 隐藏掉其他Fragment
                FragmentTransaction ft = mFManager.beginTransaction();
                ft.hide(fragment);
                ft.commit();
            }
        }
    }

    /**
     * 添加Fragment
     */
    private void addFragment(Fragment fragment) {
        FragmentTransaction ft = mFManager.beginTransaction();
        if (!fragment.isAdded()) {// 如果Fragment没有添加过则添加进来，如果添加了则直接显示出来
            ft.add(mContainerId, fragment);
        }
        AnimationUtils.loadAnimation(mContext, R.anim.push_right_in);
        ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_left_out);
        ft.show(fragment);
        ft.commit();
    }
}
