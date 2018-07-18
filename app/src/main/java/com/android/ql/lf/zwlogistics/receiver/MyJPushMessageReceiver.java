package com.android.ql.lf.zwlogistics.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.ql.lf.zwlogistics.data.UserInfo;
import com.android.ql.lf.zwlogistics.ui.activity.SplashActivity;
import com.android.ql.lf.zwlogistics.ui.fragment.order.MyOrderInfoFragment;
import com.android.ql.lf.zwlogistics.utils.Constants;
import com.android.ql.lf.zwlogistics.utils.PreferenceUtils;

import cn.jpush.android.api.JPushInterface;

public class MyJPushMessageReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
            if (UserInfo.getInstance().isLogin()){
                MyOrderInfoFragment.Companion.startMyOrderInfo(context, PreferenceUtils.getPrefString(context,Constants.IS_ORDER_INFO_ID,""));
            }else {
                PreferenceUtils.setPrefString(context,Constants.IS_ORDER_INFO_ID,"1");
                context.startActivity(new Intent(context,SplashActivity.class));
            }
        }
    }
}
