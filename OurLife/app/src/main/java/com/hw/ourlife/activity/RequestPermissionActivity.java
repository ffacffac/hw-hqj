// package com.hw.ourlife.activity;
//
// import android.Manifest;
// import android.os.Bundle;
// import android.support.annotation.NonNull;
// import android.support.annotation.Nullable;
// import android.support.v7.app.AppCompatActivity;
// import android.util.Log;
// import android.webkit.PermissionRequest;
//
// import com.hw.annotation.NeedsPermissin;
// import com.hw.annotation.OnNeverAskAgain;
// import com.hw.annotation.OnPermissinDenied;
// import com.hw.annotation.OnShowRationale;
// import com.hw.library.PermissionManager;
// import com.hw.library.RequestPermission;
// import com.hw.ourlife.R;
//
// /**
//  * @author huangqj
//  *         Created by huangqj on 2019-04-16.
//  */
//
// public class RequestPermissionActivity extends AppCompatActivity implements RequestPermission {
//     public static final String TAG = RequestPermissionActivity.class.getName();
//
//     @Override
//     protected void onCreate(@Nullable Bundle savedInstanceState) {
//         super.onCreate(savedInstanceState);
//         setContentView(R.layout.activity_main);
//         camera();
//     }
//
//     public void camera() {
//         PermissionManager
//                 .request(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
//                         Manifest.permission.READ_CALENDAR});
//     }
//
//     /**
//      * 需要获取权限
//      */
//     @NeedsPermissin()
//     public void showCamera() {
//         Log.e(TAG, "showCamera: " + "showCamera------");
//     }
//
//     /**
//      * 提示用户为何开启权限
//      */
//     @OnShowRationale()
//     public void showRationaleForCamera(final PermissionRequest request) {
//         Log.e(TAG, "showRatianalForCamera: " + "showRatianalForCamera--------->");
//         //show dialog
//
//         request.proceed();
//
//     }
//
//     /**
//      * 用户选择拒绝时的提示
//      */
//     @OnNeverAskAgain()
//     public void showNeverAskForCamera() {
//         Log.e(TAG, "showNeverAskForCamera: " + "showNeverAskForCamera--------->");
//     }
//
//     @OnPermissinDenied()
//     public void showDeniedForCamera() {
//         Log.e(TAG, "showDeniedForCamera: " + "showDeniedForCamera----------->");
//     }
//
//     @Override
//     public void requestPermission(Object target, String[] permissions) {
//         Log.e(TAG, "requestPermission: " + "requestPermission----->");
//     }
//
//     @Override
//     public void onRequestPermissionResult(Object target, int requestCode, @NonNull int[] grantResults) {
//         Log.e(TAG, "onRequestPermissionResult: " + "onRequestPermissionResult---->");
//     }
// }
