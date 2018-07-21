package com.android.ql.lf.zwlogistics.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.ql.lf.zwlogistics.data.UserInfo;
import com.android.ql.lf.zwlogistics.service.LocationService;
import com.android.ql.lf.zwlogistics.ui.activity.SplashActivity;
import com.android.ql.lf.zwlogistics.utils.Constants;
import com.android.ql.lf.zwlogistics.utils.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class MyJPushMessageReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String extra = intent.getExtras().getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject jsonObject = new JSONObject(extra);
                if (!TextUtils.isEmpty(extra) && !TextUtils.isEmpty(jsonObject.optString("id"))) {
                    PreferenceUtils.setPrefString(context, Constants.IS_ORDER_INFO_ID, jsonObject.optString("id"));
                    if (UserInfo.getInstance().isLogin()) {
                        context.startService(new Intent(context,LocationService.class));
                    } else {
                        Intent splashIntent = new Intent(context, SplashActivity.class);
                        splashIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(splashIntent);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
