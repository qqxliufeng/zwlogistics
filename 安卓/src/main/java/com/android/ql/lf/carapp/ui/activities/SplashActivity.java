package com.android.ql.lf.carapp.ui.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.android.ql.lf.carapp.R;
import com.android.ql.lf.carapp.application.CarApplication;
import com.android.ql.lf.carapp.component.ApiServerModule;
import com.android.ql.lf.carapp.component.DaggerApiServerComponent;
import com.android.ql.lf.carapp.data.UserInfo;
import com.android.ql.lf.carapp.present.GetDataFromNetPresent;
import com.android.ql.lf.carapp.present.UserPresent;
import com.android.ql.lf.carapp.utils.RequestParamsHelper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by lf on 18.2.8.
 *
 * @author lf on 18.2.8
 */

public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    /**
     * 权限标识
     */
    private static final int WRITE_AND_CAMERA = 0;

    /**
     * 需要的权限
     */
    private static final String[] REQUEST_PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final String[] REQUEST_PERMISSIONS_DESCRIPTION = new String[]{"相机", "读取SD卡"};

    @Inject
    GetDataFromNetPresent mPresent;

    private UserPresent userPresent = new UserPresent();


    @Override
    public int getLayoutId() {
        return R.layout.activity_splash_layout;
    }

    @Override
    public void initView() {
        DaggerApiServerComponent.builder().apiServerModule(new ApiServerModule()).appComponent(CarApplication.getInstance().getAppComponent()).build().inject(this);
        mPresent.setNetDataPresenter(this);
        if (hasPermissions()) {
            findViewById(R.id.mIvSplash).postDelayed(new Runnable() {
                @Override
                public void run() {
                    isLogin();
                }
            }, 2500);
        } else {
            requestPermission();
        }
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        EasyPermissions.requestPermissions(this, "需要相机和存储权限", WRITE_AND_CAMERA, REQUEST_PERMISSIONS);
    }

    /**
     * 检测是否有相应的权限
     *
     * @return true if all permissions already have granted ,otherwise false
     */
    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(this, REQUEST_PERMISSIONS);
    }

    /**
     * 请求权限回调的方法
     *
     * @param requestCode  请求code
     * @param permissions  权限列表
     * @param grantResults 请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (hasPermissions()) {
            isLogin();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri packageURI = Uri.parse("package:" + getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    startActivityForResult(intent, AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setTitle("系统需要以下权限").setItems(REQUEST_PERMISSIONS_DESCRIPTION, null).create().show();

        } else {
            requestPermission();
        }
    }

    /**
     * 设置好权限回调的方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (hasPermissions()) {
                isLogin();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public <T> void onRequestSuccess(int requestID, T result) {
        super.onRequestSuccess(requestID, result);
        try {
            JSONObject json = new JSONObject(result.toString());
            if ("200".equals(json.optString("code"))) {
                userPresent.onLogin(json.optJSONObject("result"), json.optJSONObject("arr"));
                JPushInterface.setDebugMode(true);
                JPushInterface.setAlias(this, 0, UserInfo.getInstance().getMemberAlias());
                JPushInterface.init(getApplicationContext());
                startMain();
            } else {
                startMain();
            }
        } catch (JSONException e) {
            startMain();
        }
    }

    @Override
    public void onRequestFail(int requestID, @NotNull Throwable e) {
        super.onRequestFail(requestID, e);
        startMain();
    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * 所有有权限都已经请求到了，直接进入到主页面
     */
    private void isLogin() {
        if (UserInfo.isCacheUserId(this)) {
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.Companion.getMEMBER_MODEL(),
                    RequestParamsHelper.Companion.getACT_PERSONAL(),
                    RequestParamsHelper.Companion.getPersonalParam(UserInfo.getUserIdFromCache(this)));
        } else {
            startMain();
        }
    }
}
