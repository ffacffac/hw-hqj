package com.hw.ourlife.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hw.baselibrary.db.been.OurLife;
import com.hw.baselibrary.util.CommonUtils;
import com.hw.ourlife.R;
import com.hw.ourlife.adapter.LifeAdapter;
import com.hw.ourlife.fragment.MyFragmentManager;
import com.hw.ourlife.fragment.OurLifeFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页内容
 *
 * @author huangqj
 */
public class OurLifeActivity extends BaseActivity {

    @BindView(R.id.v_life_refresh)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_life)
    RecyclerView rvLife;
    private LifeAdapter lifeAdapter;
    private List<OurLife> lifeList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_our_life;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initAdapter();
        serRefreshLayout();
    }

    @Override
    protected void initEventAndData() {
    }

    private void initAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        lifeAdapter = new LifeAdapter(R.layout.item_life, lifeList);
        rvLife.setAdapter(lifeAdapter);
        rvLife.setLayoutManager(manager);
    }

    @OnClick({R.id.btn_add_life})
    void onMyClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_life:
                OurLifeFragment.gotoFragment(R.id.rl_our_life, null, getSupportFragmentManager());
                break;
            default:
                break;
        }
    }

    private void serRefreshLayout() {
        refreshLayout.setEnableLoadMore(true);
        //内容不偏移
        refreshLayout.setEnableHeaderTranslationContent(false);
        // mMaterialHeader.setShowBezierWave(true);//官方主题，打开贝塞尔背景
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                //加载更多
                toast.showToastCenter("加载更多");
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                toast.showToastCenter("下拉刷新111");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (MyFragmentManager.getAppFragmentSize() > 0) {
            OurLifeFragment.backFragment();
            return;
        }
        if (CommonUtils.onFinishByTime(toast)) {
            finishActivity();
        }
    }

}
