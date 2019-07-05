// package com.hw.ourlife.activity;
//
// import android.support.annotation.NonNull;
// import android.support.v4.app.ActivityCompat;
//
// import com.hw.library.PermissionRequest;
// import com.hw.library.PermissionUtils;
// import com.hw.library.RequestPermission;
//
// import java.lang.ref.WeakReference;
//
// /**
//  * @author huangqj
//  *         Created by huangqj on 2019-04-16.
//  */
//
// public class MainActivity$Permissions implements RequestPermission<RequestPermissionActivity> {
//
//     private static final int REQUEST_SHOWCAMERA = 666;
//     private static String[] PERMISSION_SHOWCAMERA;
//
//
//     public MainActivity$Permissions() {
//
//     }
//
//     @Override
//     public void requestPermission(RequestPermissionActivity target, String[] permissions) {
//         PERMISSION_SHOWCAMERA = permissions;
//         if (PermissionUtils.hasSelfParmissions(target, PERMISSION_SHOWCAMERA)) {
//             target.showCamera();
//         } else if (PermissionUtils.shouldShowRequesrPermissionRatianals(target, PERMISSION_SHOWCAMERA)) {
//             target.showRationaleForCamera(new PermissionRequestIml(target));
//         } else {
//             ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);
//         }
//     }
//
//     @Override
//     public void onRequestPermissionResult(RequestPermissionActivity target, int requestCode,
//                                           @NonNull int[] grantResults) {
//         switch (requestCode) {
//             case REQUEST_SHOWCAMERA:
//                 if (PermissionUtils.verifyParmissions(grantResults)) {
//                     target.showCamera();
//                 } else if (PermissionUtils.shouldShowRequesrPermissionRatianals(target, PERMISSION_SHOWCAMERA)) {
//                     target.showNeverAskForCamera();
//                 } else {
//                     target.showDeniedForCamera();
//                 }
//                 break;
//             default:
//                 break;
//         }
//     }
//
//     private static final class PermissionRequestIml implements PermissionRequest {
//
//         private final WeakReference<RequestPermissionActivity> weakTarget;
//
//         private PermissionRequestIml(RequestPermissionActivity target) {
//             this.weakTarget = new WeakReference(target);
//         }
//
//         @Override
//         public void proceed() {
//             RequestPermissionActivity target = this.weakTarget.get();
//             if (target != null) {
//                 ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);
//             }
//         }
//     }
// }
