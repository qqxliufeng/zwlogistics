package com.android.ql.lf.zwlogistics.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import okhttp3.internal.Util;

public class ThirdShareManager {

    public static void qqShare(Activity context, Tencent mTencent, IUiListener uiListener) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "要分享的标题");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "要分享的摘要");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "测试应用222222");
        mTencent.shareToQQ(context, params, uiListener);
    }

    public static void zoneShare(Activity context, Tencent mTencent, IUiListener uiListener) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "标题");//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "摘要");//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");//必填
        ArrayList<String> images = new ArrayList<String>();
        images.add("http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, images);
        mTencent.shareToQzone(context, params, uiListener);
    }


    public static void wxShare(IWXAPI api, Bitmap bitmap, int type) {
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = "http://www.baidu.com";
        WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
        wxMediaMessage.description = "this is description";
        wxMediaMessage.title = "this is title";
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
}
