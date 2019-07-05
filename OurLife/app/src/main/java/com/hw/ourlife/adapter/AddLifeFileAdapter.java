package com.hw.ourlife.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hw.baselibrary.constant.Eenum;
import com.hw.baselibrary.db.been.OurFile;
import com.hw.ourlife.R;

import java.util.List;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-11.
 */

public class AddLifeFileAdapter extends BaseQuickAdapter<OurFile, BaseViewHolder> {

    public AddLifeFileAdapter(int layoutResId, @Nullable List<OurFile> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OurFile item) {
        int type = item.getFileType();
        if (type == Eenum.LifeFileType.RES.value) {
            helper.setImageResource(R.id.iv_add_file, item.getResId());
        } else {
            Glide.with(mContext).load(item.getPath()).into((ImageView) helper.getView(R.id.iv_add_file));
        }
    }
}
