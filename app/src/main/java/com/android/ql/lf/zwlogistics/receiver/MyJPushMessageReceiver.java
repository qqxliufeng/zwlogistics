package com.android.ql.lf.zwlogistics.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.android.ql.lf.zwlogistics.R;
import com.android.ql.lf.zwlogistics.data.UserInfo;
import com.android.ql.lf.zwlogistics.service.LocationService;
import com.android.ql.lf.zwlogistics.ui.activity.SplashActivity;

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
                    if (UserInfo.getInstance().isLogin()) {
                        UserInfo.getInstance().setNeedGpsOrder(jsonObject.optString("id"));
                        if (UserInfo.getInstance().isNeedGps()) {
                            context.startService(new Intent(context, LocationService.class));
                        }
                    } else {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        Intent pendingIntent = new Intent(context, SplashActivity.class);//将要跳转的界面
                        builder.setAutoCancel(true);//点击后消失
                        builder.setSmallIcon(R.mipmap.ic_launcher);//设置通知栏消息标题的头像
                        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
                        builder.setContentTitle(context.getResources().getString(R.string.app_name));
                        builder.setContentText("订单开始，请注意查看");
                        PendingIntent intentPend = PendingIntent.getActivity(context, 0, pendingIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                        builder.setContentIntent(intentPend);
                        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        manager.notify(0, builder.build());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
