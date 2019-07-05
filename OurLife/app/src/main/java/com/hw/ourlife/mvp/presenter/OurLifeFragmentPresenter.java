package com.hw.ourlife.mvp.presenter;

import android.graphics.Bitmap;

import com.hw.baselibrary.constant.Constant;
import com.hw.baselibrary.constant.Eenum;
import com.hw.baselibrary.db.been.OurFile;
import com.hw.baselibrary.db.been.OurLife;
import com.hw.baselibrary.db.load.OurLifeDb;
import com.hw.baselibrary.util.DateUtils;
import com.hw.baselibrary.util.ImageEncryptUtils;
import com.hw.baselibrary.util.PhotoUtils;
import com.hw.baselibrary.util.RxUtils;
import com.hw.ourlife.ComponentHolder;
import com.hw.ourlife.R;
import com.hw.ourlife.mvp.view.OurLifeFragmentContract;

import org.xutils.DbManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

public class OurLifeFragmentPresenter extends BasePresenter<OurLifeFragmentContract.IOurLifeFragmentView>
        implements OurLifeFragmentContract.IOurLifeFragmentPresenter {

    private DbManager db;

    @Inject
    public OurLifeFragmentPresenter() {
        db = ComponentHolder.getAppComponent().myApplication().db;
    }

    @Override
    public void initData() {
        List<OurFile> ourFiles = new ArrayList<>();
        OurFile cameraFile = new OurFile();
        cameraFile.setFileType(Eenum.LifeFileType.RES.value);
        cameraFile.setResId(R.mipmap.ic_camera);
        OurFile addFile = new OurFile();
        addFile.setFileType(Eenum.LifeFileType.RES.value);
        addFile.setResId(R.mipmap.ic_add_black);
        ourFiles.add(cameraFile);
        ourFiles.add(addFile);
        view.onInitData(ourFiles);
    }

    @Override
    public void savePicture(Bitmap bitmap, int ourLifeId, int lifeType) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<OurFile>) emitter -> {
            String fileName = PhotoUtils.getPhotoName();
            String fileDirPath =
                    Constant.MEDIA_FILE_DIR + File.separator + DateUtils.getNowDate(DateUtils.FORMATE_STRING_5);
            File fileDir = new File(fileDirPath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            String outFilePath = fileDirPath + File.separator + fileName;
            ImageEncryptUtils.addImgByte(bitmap, outFilePath);
            String date = DateUtils.getNowDate(DateUtils.FORMATE_STRING_5);
            OurLife findLife = OurLifeDb.get().getOurLiftByDate(db, date);
            OurLife ourLife = new OurLife();
            if (findLife == null) {
                //保存life记录
                ourLife.setCreater("FFAC");
                ourLife.setDate(date);
                ourLife.setLifeType(lifeType);
                OurLifeDb.get().save(db, ourLife);
            } else {
                OurFile ourFile = new OurFile();
                ourFile.setFileType(Eenum.LifeFileType.FILE_IMG.value);
                ourFile.setDate(date);
                ourFile.setFileName(fileName);
                ourFile.setPath(outFilePath);
            }
        }).compose(RxUtils.rxSchedulerHelper()).subscribe(ourFile -> {

        }, throwable -> {

        });
        addSubscribe(disposable);
    }
}
