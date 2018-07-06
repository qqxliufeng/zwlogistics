package com.android.ql.lf.carapp.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ql.lf.carapp.R;

/**
 * Created by lf on 2017/12/11 0011.
 *
 * @author lf on 2017/12/11 0011
 */

public class SelectPayTypeView extends LinearLayout {

    public static final String WX_PAY = "wxpay";
    public static final String ALI_PAY = "alipay";
    public static final String ACCOUNT_PAY = "balance";

    private CheckBox cb_ali;
    private CheckBox cb_wx;
    private CheckBox cb_account;
    private TextView tv_confirm;

    private boolean isShowAccount = false;

    private OnConfirmClickListener onConfirmClickListener;

    public SelectPayTypeView(Context context) {
        this(context, null);
    }

    public SelectPayTypeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectPayTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.SelectPayTypeView);
        isShowAccount = typedArray.getBoolean(R.styleable.SelectPayTypeView_isShowAccount, false);
        typedArray.recycle();
        init();
    }

    public void init() {
        View contentView = View.inflate(getContext(), R.layout.dialog_pay_layout, null);
        cb_ali = contentView.findViewById(R.id.mCbALiPay);
        cb_wx = contentView.findViewById(R.id.mCbWX);
        cb_account = contentView.findViewById(R.id.mCbAccount);
        tv_confirm = contentView.findViewById(R.id.mTvPayDialogConfirm);
        tv_confirm.setVisibility(GONE);
        contentView.findViewById(R.id.mRlSubmitOrderWXContainer).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_wx.setChecked(true);
                cb_ali.setChecked(false);
                cb_account.setChecked(false);
            }
        });
        contentView.findViewById(R.id.mRlSubmitOrderAliPayContainer).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_wx.setChecked(false);
                cb_ali.setChecked(true);
                cb_account.setChecked(false);
            }
        });
        View accountContainer = contentView.findViewById(R.id.mRlSubmitOrderAccountPayContainer);
        accountContainer.setVisibility(isShowAccount ? View.VISIBLE : View.GONE);
        if (isShowAccount) {
            accountContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    cb_wx.setChecked(false);
                    cb_ali.setChecked(false);
                    cb_account.setChecked(true);
                }
            });
        }
        addView(contentView);
    }

    public void setShowAccount(boolean showAccount) {
        isShowAccount = showAccount;
    }

    public String getPayType() {
        if (cb_wx.isChecked()) {
            return WX_PAY;
        } else if (cb_ali.isChecked()) {
            return ALI_PAY;
        } else {
            return ACCOUNT_PAY;
        }
    }

    public void setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener;
    }

    public void setShowConfirmView(int visible) {
        tv_confirm.setVisibility(visible);
        if (View.VISIBLE == visible) {
            tv_confirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onConfirmClickListener != null) {
                        onConfirmClickListener.onConfirmClick();
                    }
                }
            });
        }
    }


    public interface OnConfirmClickListener {
        void onConfirmClick();
    }

}
