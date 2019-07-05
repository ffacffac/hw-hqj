package com.hw.ourlife.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hw.baselibrary.db.been.OurLife;
import com.hw.ourlife.R;

import java.util.List;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

public class LifeAdapter extends BaseQuickAdapter<OurLife, BaseViewHolder> {

    public LifeAdapter(int layoutResId, @Nullable List<OurLife> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, OurLife item) {
        holder.setText(R.id.tv_item_life_date, item.getDate());
    }
}
