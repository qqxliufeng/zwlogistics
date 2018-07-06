package com.android.ql.lf.carapp.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ql.lf.carapp.R;
import com.android.ql.lf.carapp.ui.fragments.mall.normal.NewGoodsInfoFragment;

import java.util.ArrayList;

/**
 * Created by lf on 18.5.22.
 *
 * @author lf on 18.5.22
 */

public class CouponView extends LinearLayout {

    private TextView tv_price;
    private TextView tv_can_use;
    private TextView tv_time;


    public CouponView(Context context) {
        this(context, null);
    }

    public CouponView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CouponView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        final View contentView = View.inflate(getContext(), R.layout.layout_coupon_item_layout, this);
        tv_price = contentView.findViewById(R.id.mCouponItemMoney);
        tv_can_use = contentView.findViewById(R.id.mTvCouponItemCanUse);
        tv_time = contentView.findViewById(R.id.mTvCouponItemTime);
    }

    public void bindData(NewGoodsInfoFragment.CouponBean data) {
        tv_price.setText(data.getDiscount_price());
        tv_can_use.setText(String.format("满%s元\n可以使用", data.getDiscount_fr()));
        tv_time.setText(String.format("有效期%s-%s", data.getDiscount_time(), data.getDiscount_validity()));
    }
}
