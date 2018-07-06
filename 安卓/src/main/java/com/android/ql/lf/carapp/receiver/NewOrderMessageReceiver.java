package com.android.ql.lf.carapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import com.android.ql.lf.carapp.data.NewOrderMessageBean;
import com.android.ql.lf.carapp.ui.activities.SplashActivity;
import com.android.ql.lf.carapp.utils.Constants;
import com.android.ql.lf.carapp.utils.PreferenceUtils;
import com.android.ql.lf.carapp.utils.RxBus;

import java.io.IOException;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lf on 18.2.28.
 *
 * @author lf on 18.2.28
 */

public class NewOrderMessageReceiver extends BroadcastReceiver {

    static boolean isPlaying = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                RxBus.getDefault().post(new NewOrderMessageBean(intent.getStringExtra(JPushInterface.EXTRA_EXTRA)));
                try {
                    if (!isPlaying) {
                        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("wrjtnofity.mp3");
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                isPlaying = false;
                            }
                        });
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                isPlaying = true;
                            }
                        });
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    }
                } catch (IOException e) {
                    Log.e("TAG", "提示失败"+e.getMessage());
                }
            }
            if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                if (!PreferenceUtils.getPrefBoolean(context, Constants.APP_IS_ALIVE, true)) {
                    startSplash(context);
                }
            }
        }
    }

    private void startSplash(Context context) {
        Intent forIntent = new Intent(context, SplashActivity.class);
        context.startActivity(forIntent);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        Intent forIntent = new Intent(context, SplashActivity.class);
//        //将要跳转的界面
//        //点击后消失
//        builder.setAutoCancel(true);
//        //设置通知栏消息标题的头像
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        //设置通知铃声
//        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
//        builder.setTicker("新消息");
//        builder.setContentText("您有新的订单，请注意查收！");
//        builder.setContentTitle("新消息提醒");
//        PendingIntent intentPend = PendingIntent.getActivity(context, 0, forIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//        builder.setContentIntent(intentPend);
//        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
//        managerCompat.notify(0, builder.build());
    }
}
