package com.android.ql.lf.zwlogistics.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import com.android.ql.lf.zwlogistics.R;

public class AuthStepView extends LinearLayout {

    private CheckedTextView ct_step1;
    private CheckedTextView ct_step2;
    private CheckedTextView ct_line;
    private CheckedTextView ct_des1;
    private CheckedTextView ct_des2;

    private int step = 1;

    public AuthStepView(Context context) {
        this(context, null);
    }

    public AuthStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AuthStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(ContextCompat.getColor(context, R.color.lineColor));
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.auth_step_view);
        step = array.getInt(R.styleable.auth_step_view_step,1);
        array.recycle();
        init();
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.layout_auth_step_view_layout, this);
        ct_step1 = contentView.findViewById(R.id.mTvMinePersonAuth1);
        ct_step2 = contentView.findViewById(R.id.mTvMinePersonAuth2);
        ct_line = contentView.findViewById(R.id.mTvMinePersonLine);
        ct_des1 = contentView.findViewById(R.id.mTvMinePersonDes1);
        ct_des2 = contentView.findViewById(R.id.mTvMinePersonDes2);
        setStep();
    }

    public void setStep() {
        ct_step1.setChecked(step == 1);
        ct_step2.setChecked(step != 1);
        ct_line.setChecked(step == 1);
        ct_des1.setChecked(step == 1);
        ct_des2.setChecked(step != 1);
    }

}
