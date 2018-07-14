package com.android.ql.lf.zwlogistics.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.ql.lf.zwlogistics.R;
import com.android.ql.lf.zwlogistics.application.MyApplication;
import com.android.ql.lf.zwlogistics.data.UserInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class ThirdShareManager {

    public static void qqShare(Activity context, Tencent mTencent, IUiListener uiListener) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, getShareTitle(context));

        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, getShareContent(context));
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, UserInfo.getInstance().getShareUrl());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, UserInfo.getInstance().getSharePic());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getPackageName());
        mTencent.shareToQQ(context, params, uiListener);
    }

    public static void zoneShare(Activity context, Tencent mTencent, IUiListener uiListener) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, getShareTitle(context));//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, getShareContent(context));//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, UserInfo.getInstance().getShareUrl());//必填
        ArrayList<String> images = new ArrayList<String>();
        images.add(UserInfo.getInstance().getSharePic());
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
        mTencent.shareToQzone(context, params, uiListener);
    }


    public static void wxShare(IWXAPI api, Bitmap bitmap, int type) {
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = UserInfo.getInstance().getShareUrl();
        WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
        wxMediaMessage.description = getShareContent(MyApplication.getInstance());
        wxMediaMessage.title = getShareContent(MyApplication.getInstance());
        wxMediaMessage.thumbData = bmpToByteArray(Bitmap.createScaledBitmap(bitmap, 150, 150, true), true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = wxMediaMessage;
        req.scene = type;
        api.sendReq(req);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static String getShareTitle(Context context){
        String shareTitle = context.getResources().getString(R.string.app_name);
        if (!TextUtils.isEmpty(UserInfo.getInstance().getShareTitle())){
            shareTitle = UserInfo.getInstance().getShareTitle();
        }
        return shareTitle;
    }

    public static String getShareContent(Context context){
        String shareContent = context.getResources().getString(R.string.app_name);
        if (!TextUtils.isEmpty(UserInfo.getInstance().getShareIntro())){
            shareContent = UserInfo.getInstance().getShareIntro();
        }
        return shareContent;
    }

}
