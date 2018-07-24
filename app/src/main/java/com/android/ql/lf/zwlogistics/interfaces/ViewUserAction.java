package com.android.ql.lf.zwlogistics.interfaces;


import android.content.Intent;

import com.android.ql.lf.carapp.action.IViewUserAction;
import com.android.ql.lf.zwlogistics.application.MyApplication;
import com.android.ql.lf.zwlogistics.data.UserInfo;
import com.android.ql.lf.zwlogistics.service.LocationService;
import com.android.ql.lf.zwlogistics.utils.Constants;
import com.android.ql.lf.zwlogistics.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lf on 18.2.8.
 *
 * @author lf on 18.2.8
 */

public class ViewUserAction implements IViewUserAction {

    @Override
    public boolean onLogin(@NotNull JSONObject result) {
        try {
            UserInfo.getInstance().setUser_id(result.optString("user_id"));
            UserInfo.getInstance().setUser_nickname(result.optString("user_nickname"));
            UserInfo.getInstance().setUser_phone(result.optString("user_phone"));
            UserInfo.getInstance().setUser_pic(result.optString("user_pic"));
            UserInfo.getInstance().setUser_code(result.optString("user_code"));
            UserInfo.getInstance().setUser_is_rank(result.optString("user_is_rank"));
            UserInfo.getInstance().setUser_is_vehicle(result.optString("user_is_vehicle"));
            UserInfo.getInstance().setUser_w_sum(result.optString("user_w_sum"));
            UserInfo.getInstance().setUser_y_sum(result.optString("user_y_sum"));
            UserInfo.getInstance().setKephone(result.optString("kephone"));
            UserInfo.getInstance().setSharePic(result.optString("sharePic"));
            UserInfo.getInstance().setShareTitle(result.optString("shareTitle"));
            UserInfo.getInstance().setShareIntro(result.optString("shareIntro"));
            UserInfo.getInstance().setPushAlias(result.optString("user_as"));
            UserInfo.getInstance().setNeedGpsOrder(result.optString("is_need"));

            JPushInterface.setAlias(MyApplication.getInstance(),0,UserInfo.getInstance().getPushAlias());
            PreferenceUtils.setPrefString(MyApplication.application, UserInfo.USER_ID_FLAG, UserInfo.getInstance().getUser_id());
            return true;
        } catch (Exception e) {
            return false;
        }
    }



    @Override
    public boolean onLogout() {
        UserInfo.getInstance().loginOut();
        MyApplication.getInstance().stopService(new Intent(MyApplication.getInstance(),LocationService.class));
        PreferenceUtils.setPrefString(MyApplication.getInstance(),Constants.IS_ORDER_INFO_ID,"");
        return true;
    }

    @Override
    public void onRegister(@NotNull JSONObject result) {
    }

    @Override
    public void onForgetPassword(@NotNull JSONObject result) {
    }

    @Override
    public void onResetPassword(@NotNull JSONObject result) {
    }

    @Override
    public boolean modifyInfoForName(@NotNull String name) {
        try {
            UserInfo.getInstance().setUser_nickname(name);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean modifyInfoForPic(@NotNull String result) {
        try {
            UserInfo.getInstance().setUser_pic(result);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
