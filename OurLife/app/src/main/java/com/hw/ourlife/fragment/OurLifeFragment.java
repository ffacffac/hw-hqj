package com.hw.ourlife.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hw.baselibrary.constant.Eenum;
import com.hw.baselibrary.db.been.OurFile;
import com.hw.baselibrary.util.PhotoUtils;
import com.hw.ourlife.R;
import com.hw.ourlife.activity.OurLifeActivity;
import com.hw.ourlife.adapter.AddLifeFileAdapter;
import com.hw.ourlife.component.FragmentComponent;
import com.hw.ourlife.mvp.presenter.OurLifeFragmentPresenter;
import com.hw.ourlife.mvp.view.OurLifeFragmentContract;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

public class OurLifeFragment extends BasePresenterFragment<OurLifeFragmentPresenter>
        implements OurLifeFragmentContract.IOurLifeFragmentView {

    public static final int CHOSE_IMAGE_CODE = 100;
    public static final int CAMERA_IMAGE_CODE = 101;
    @BindView(R.id.rv_life_fragment_file)
    RecyclerView rvFile;
    FragmentComponent fragmentComponent;
    private AddLifeFileAdapter fileAdapter;
    private List<OurFile> files = new ArrayList<>();
    private OurLifeActivity activity;

    public static void gotoFragment(int layoutResId, Bundle bundle, FragmentManager fragmentManager) {
        OurLifeFragment fragment = new OurLifeFragment();
        gotoFragment(fragment, layoutResId, bundle, fragmentManager);
    }

    @Override
    protected void initInject() {
        fragmentComponent = getFragmentComponent();
        fragmentComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_life;
    }

    @Override
    protected void initEventAndData() {
        activity = (OurLifeActivity) fragmentComponent.getActivity();
        presenter.initData();
        fileAdapter = new AddLifeFileAdapter(R.layout.item_add_file, files);
        GridLayoutManager manager = new GridLayoutManager(activity, 4);
        rvFile.setLayoutManager(manager);
        rvFile.setAdapter(fileAdapter);
        //item点击事件
        fileAdapter.setOnItemClickListener((adapter, view, position) -> {
            toast.showToastCenter("点击到了----" + position);
            if (files.get(position).getFileType() == Eenum.LifeFileType.RES.value) {
                checkPermissions(files.get(position).getResId());
            } else {

            }
        });
    }

    @Override
    public void onInitData(List<OurFile> ourFiles) {
        files.addAll(ourFiles);
    }

    private void checkPermissions(int resId) {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO).subscribe(aBoolean -> {
            if (aBoolean) {
                if (resId == R.mipmap.ic_camera) {
                    //拍照
                    TakePictureDialog takePictureDialog = new TakePictureDialog();
                    takePictureDialog.setCameraDialogCallback(new TakePictureCallback());
                    FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
                    takePictureDialog.show(ft, "TakePictureDialog");
                } else if (resId == R.mipmap.ic_add_black) {
                    //添加本地相册，先检查权限
                    PhotoUtils.choseImageFromGallery(fragmentComponent.getActivity(), CHOSE_IMAGE_CODE);
                }
            } else {
                toast.showToastCenter("请授予相机、录音权限");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case CHOSE_IMAGE_CODE:
                break;
            default:
                break;
        }
    }

    private class TakePictureCallback implements TakePictureDialog.CameraDialogCallback {

        @Override
        public void onPictureTaken(Bitmap bitmap) {
            presenter.savePicture(bitmap, 0, Eenum.LifeFileType.FILE_IMG.value);
        }

        @Override
        public void onVideoTaken(String videoPath, String videoName) {

        }

        @Override
        public void back() {

        }
    }
}
