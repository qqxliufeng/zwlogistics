package com.android.ql.lf.carapp.ui.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CheckableImageButton;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.ql.lf.carapp.R;


/**
 * Created by lf on 2017/11/18 0018.
 *
 * @author lf on 2017/11/18 0018
 */

public class PraiseView extends FrameLayout implements Checkable {

    public static final String PRAISE_TEXT = "赞";
    public static final String ONLY_ONE_TEXT = "1";
    private boolean isChecked = false;
    private CheckableImageButton ivPraise;
    private TextView tvFly;
    private TextView tvPraiseCount;

    public PraiseView(@NonNull Context context) {
        super(context);
    }

    public PraiseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PraiseView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ivPraise = findViewById(R.id.mIvPraiseIcon);
        tvFly = findViewById(R.id.mTvPraiseFly);
        tvPraiseCount = findViewById(R.id.mTvPraiseText);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setChecked(boolean checked) {
        this.isChecked = checked;
        ivPraise.setChecked(isChecked);
        if (isChecked) {
            tvPraiseCount.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            if (PRAISE_TEXT.equals(tvPraiseCount.getText())) {
                tvPraiseCount.setText(ONLY_ONE_TEXT);
            } else {
                if (!TextUtils.isEmpty(tvPraiseCount.getText().toString())) {
                    tvPraiseCount.setText(String.valueOf(Integer.parseInt(tvPraiseCount.getText().toString()) + 1));
                }
            }
            startAnimator();
        } else {
            tvPraiseCount.setTextColor(ContextCompat.getColor(getContext(), R.color.text_deep_dark_color));
            if (ONLY_ONE_TEXT.equals(tvPraiseCount.getText())) {
                tvPraiseCount.setText(PRAISE_TEXT);
            } else {
                tvPraiseCount.setText(String.valueOf(Integer.parseInt(tvPraiseCount.getText().toString()) - 1));
            }
        }
    }

    private void startAnimator() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(ivPraise, "scaleX", 0.0f, 1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(ivPraise, "scaleY", 0.0f, 1.0f);
        tvFly.setVisibility(View.VISIBLE);
        ObjectAnimator animatorAdd = ObjectAnimator.ofFloat(tvFly, "translationY", 0.0f, -(getHeight() / 2));
        animatorAdd.setDuration(500);
        animatorAdd.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                ObjectAnimator animatorAddX = ObjectAnimator.ofFloat(tvFly, "scaleX", 1.0f, 1.2f);
                ObjectAnimator animatorAddY = ObjectAnimator.ofFloat(tvFly, "scaleY", 1.0f, 1.2f);
                AnimatorSet sets = new AnimatorSet();
                sets.setInterpolator(new BounceInterpolator());
                sets.playTogether(animatorAddX, animatorAddY);
                sets.setDuration(100);
                sets.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        tvFly.setVisibility(View.GONE);
                    }
                });
                sets.start();
            }
        });
        AnimatorSet sets = new AnimatorSet();
        sets.playTogether(animatorX, animatorY);
        sets.setInterpolator(new BounceInterpolator());
        sets.setDuration(500);
        animatorAdd.start();
        sets.start();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    public void setPraiseText(String count) {
        tvPraiseCount.setText(count);
    }

}
