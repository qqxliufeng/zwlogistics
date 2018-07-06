package com.android.ql.lf.carapp.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.android.ql.lf.carapp.data.WXPayBean;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

/**
 * Created by lf on 2017/12/7 0007.
 *
 * @author lf on 2017/12/7 0007
 */

public class PayManager {

    public static final int SDK_PAY_FLAG = 1;

    public static void wxPay(Context context, WXPayBean wxPayBean) {
        if (wxPayBean != null) {
            IWXAPI iwxapi = WXAPIFactory.createWXAPI(context, Constants.WX_APP_ID, true);
            PayReq req = new PayReq();
            req.appId = wxPayBean.getAppid();
            req.partnerId = wxPayBean.getPartnerid();
            req.prepayId = wxPayBean.getPrepayid();
            req.nonceStr = wxPayBean.getNoncestr();
            req.timeStamp = wxPayBean.getTimestamp();
            req.packageValue = "Sign=WXPay";
            req.sign = wxPayBean.getSign();
            req.extData = "app data";
            iwxapi.registerApp(Constants.WX_APP_ID);
            iwxapi.sendReq(req);
        }
    }

    public static void aliPay(final Context context, final Handler handler,final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask pay = new PayTask((Activity) context);
                Map<String, String> result = pay.payV2(orderInfo,true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
