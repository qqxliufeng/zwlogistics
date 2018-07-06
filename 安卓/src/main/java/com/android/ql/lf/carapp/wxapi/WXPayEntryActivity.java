package com.android.ql.lf.carapp.wxapi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.ql.lf.carapp.R;
import com.android.ql.lf.carapp.present.MallOrderPresent;
import com.android.ql.lf.carapp.ui.fragments.mall.order.OrderPayResultFragment;
import com.android.ql.lf.carapp.ui.fragments.order.PayResultFragment;
import com.android.ql.lf.carapp.utils.Constants;
import com.android.ql.lf.carapp.utils.PreferenceUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by lf on 2017/12/5 0005.
 *
 * @author lf on 2017/12/5 0005
 */

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_pay_result_layout);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
        Toolbar mToolbar = findViewById(R.id.id_tl_activity_fragment_container);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp != null) {
            boolean isMallOrder = PreferenceUtils.getPrefBoolean(this, "is_mall_order", false);
            if (isMallOrder) {
                PreferenceUtils.setPrefBoolean(this, "is_mall_order", false);
                MallOrderPresent.notifyOnPay();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mFlWxPayResultContainer, OrderPayResultFragment.Companion.newInstance(baseResp.errCode))
                        .commit();
            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mFlWxPayResultContainer, PayResultFragment.Companion.newInstance(baseResp.errCode))
                        .commit();
            }
        }
    }
}
