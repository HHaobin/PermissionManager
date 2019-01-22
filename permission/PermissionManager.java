package com.forsafe.fuanyun.utils.permission;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.forsafe.fuanyun.utils.permission.support.PermissionsPageManager;

import io.reactivex.functions.Consumer;

/**
 * @Description: 权限管理
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 2017-04-21 11:21
 */
public class PermissionManager {
    private static PermissionManager permissionManager;

    private PermissionManager() {

    }

    public static PermissionManager instance() {
        if (permissionManager == null) {
            synchronized (PermissionManager.class) {
                if (permissionManager == null) {
                    permissionManager = new PermissionManager();
                }
            }
        }
        return permissionManager;
    }

    public void request(Activity activity, final OnPermissionCallback permissionCallback, final String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && permissionCallback != null) {
            RxPermissions rxPermissions = new RxPermissions(activity);
            rxPermissions.requestEach(permissions).subscribe(new Consumer<Permission>() {
                @Override
                public void accept(Permission permission) throws Exception {
                    if (permission.granted) {
                        permissionCallback.onRequestAllow(permission.name);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        permissionCallback.onRequestRefuse(permission.name);
                    } else {
                        permissionCallback.onRequestNoAsk(permission.name);
                    }
                }
            });
        }else {
            RxPermissions rxPermissions = new RxPermissions(activity);
            rxPermissions.requestEach(permissions).subscribe(new Consumer<Permission>() {
                @Override
                public void accept(Permission permission) throws Exception {
                    if (permission.granted) {
                        permissionCallback.onRequestAllow(permission.name);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        permissionCallback.onRequestRefuse(permission.name);
                    } else {
                        permissionCallback.onRequestNoAsk(permission.name);
                    }
                }
            });
        }
    }

    AlertDialog.Builder dialogBuilder;
    /**
     * 打开手机权限设置对话框
     *
     * @param permission
     */
    public void openPermissionDialog(final Activity activity, String permission) {

        dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle("手机权限申请");
        dialogBuilder.setMessage("使用该功能需要开启 " + permission + "权限 \n请点击前往权限设置页面进行设置");
        final AlertDialog dialog=dialogBuilder.show();
        dialogBuilder.setPositiveButton("前往设置页面", (dialog1, which) -> {
            // 前往设置页面
            activity.startActivity(PermissionsPageManager.getSettingIntent(
                    activity));
            dialog.dismiss();
        } );
        dialogBuilder.setNegativeButton("取消", (dialog1, which) -> {
            dialog.dismiss();
        } );
    }

}
