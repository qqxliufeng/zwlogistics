package com.android.ql.lf.carapp.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by liufeng on 2018/3/11.
 */

public class DownLoadManagerHelper {

    public static void downLoadApk(final Context context, Uri uri,String fileName){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedOverMetered(true);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(true);
        request.setTitle("发现新版本");
        request.setDescription("正在下载……");
        request.setMimeType("application/vnd.android.package-archive");
        request.setVisibleInDownloadsUi(true);  //设置显示下载界面
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
        final String downPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+Environment.DIRECTORY_DOWNLOADS+File.separator+fileName+".apk";
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName+".apk");
        final long myDownId = downloadManager.enqueue(request);
        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
                if (downId == myDownId){
                    VersionHelp.install(context,downPath);
                }
            }
        };
        context.registerReceiver(broadcastReceiver,intentFilter);
    }
}
