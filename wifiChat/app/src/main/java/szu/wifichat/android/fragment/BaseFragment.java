package szu.wifichat.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import szu.wifichat.android.view.DialogHelper;

public abstract class BaseFragment extends Fragment {
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(getLayoutInt(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();// 先初始化View
        initData();// 再初始化Data
        return view;
    }

    /**
     * 获取Fragment的布局
     *
     * @return
     */
    protected abstract int getLayoutInt();

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 加载显示的数据
     */
    protected abstract void initData();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);// 解绑
        unbinder.unbind();
    }

    /**
     * 显示正在加载数据...
     */
    protected void showLodaDataProgress() {
        DialogHelper.getInstance(getActivity()).showProgressDialog("", "正在加载...", null);
    }

    /**
     * 取消加载动画
     */
    protected void cancelProgressDialog() {
        DialogHelper.getInstance(getActivity()).cancelProgressDialog();
    }
}
