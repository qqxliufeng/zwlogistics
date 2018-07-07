package com.android.ql.lf.zwlogistics.interfaces;


import com.android.ql.lf.carapp.action.IViewUserAction;
import com.android.ql.lf.zwlogistics.application.MyApplication;
import com.android.ql.lf.zwlogistics.data.UserInfo;
import com.android.ql.lf.zwlogistics.utils.PreferenceUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Created by lf on 18.2.8.
 *
 * @author lf on 18.2.8
 */

public class ViewUserAction implements IViewUserAction {

    @Override
    public boolean onLogin(@NotNull JSONObject result) {
        try {


//                    "user_id":"id",
//                    "user_phone":"用户手机号",
//                    "user_code":"邀请码",
//                    "user_nickname":"用户昵称",
//                    "user_pic":"用户昵称",
//                    "user_y_sum":"已发放收益",
//                    "user_w_sum":"未发放收益",
//                    "user_is_rank":"用户审核状态 1 = 未审核 2 = 审核中 3 = 审核通过 4 = 审核失败",
//                    "user_is_vehicle":"车辆审核状态 1 = 未审核 2 = 已审核",
//                    "kephone":"客服电话",

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

            PreferenceUtils.setPrefString(MyApplication.application, UserInfo.USER_ID_FLAG, UserInfo.getInstance().getUser_id());
            return true;
        } catch (Exception e) {
            return false;
        }
    }



    @Override
    public boolean onLogout() {
        UserInfo.getInstance().loginOut();
        UserInfo.clearUserCache(MyApplication.application);
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
