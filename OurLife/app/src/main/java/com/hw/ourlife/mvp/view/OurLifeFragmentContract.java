package com.hw.ourlife.mvp.view;

import android.graphics.Bitmap;

import com.hw.baselibrary.db.been.OurFile;
import com.hw.ourlife.mvp.presenter.AbstractPresenter;

import java.util.List;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

public interface OurLifeFragmentContract {

    interface IOurLifeFragmentView extends BaseView {
        /**
         * 完成初始化data
         *
         * @param ourFiles 数据
         */
        void onInitData(List<OurFile> ourFiles);
    }

    interface IOurLifeFragmentPresenter extends AbstractPresenter<OurLifeFragmentContract.IOurLifeFragmentView> {
        /**
         * 初始化数据
         */
        void initData();

        void savePicture(Bitmap bitmap, int ourLifeId, int lifeType);
    }
}
