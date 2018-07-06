package com.android.ql.lf.carapp.ui.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

/**
 * Created by lf on 18.2.5.
 *
 * @author lf on 18.2.5
 */

public class FloatingBarBehavior extends FloatingActionButton.Behavior {

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private boolean mIsAnimatingOut = false;
    private boolean mIsAnimatingIn = false;

    private View mChild;
    int touchSlop = 0;


    private ViewPropertyAnimatorListener outListener = new ViewPropertyAnimatorListener() {
        @Override
        public void onAnimationStart(View view) {
            FloatingBarBehavior.this.mIsAnimatingOut = true;
        }

        @Override
        public void onAnimationCancel(View view) {
            FloatingBarBehavior.this.mIsAnimatingOut = false;
        }

        @Override
        public void onAnimationEnd(View view) {
            FloatingBarBehavior.this.mIsAnimatingOut = false;
            mChild.setVisibility(View.INVISIBLE);
        }
    };

    private ViewPropertyAnimatorListener inListener = new ViewPropertyAnimatorListener() {
        @Override
        public void onAnimationStart(View view) {
            FloatingBarBehavior.this.mIsAnimatingIn = true;
            mChild.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationCancel(View view) {
            FloatingBarBehavior.this.mIsAnimatingIn = false;
        }

        @Override
        public void onAnimationEnd(View view) {
            FloatingBarBehavior.this.mIsAnimatingIn = false;
        }
    };


    public FloatingBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        mChild = child;
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }


    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (mChild != null && dyConsumed > touchSlop && !this.mIsAnimatingOut && child.getVisibility() == View.VISIBLE) {
            animateOut();
        } else if (mChild != null && dyConsumed < -touchSlop && !this.mIsAnimatingIn && child.getVisibility() != View.VISIBLE) {
            animateIn();
        }
    }

    /**
     * 退出动画
     */
    private void animateOut() {
        ViewCompat.animate(mChild).translationY(mChild.getHeight() + getMarginBottom(mChild)).setInterpolator(INTERPOLATOR).withLayer()
                .setListener(outListener).start();
    }

    /**
     * 进入动画
     */
    private void animateIn() {
        ViewCompat.animate(mChild).translationY(0).setInterpolator(INTERPOLATOR).withLayer().setListener(inListener).start();
    }


    private int getMarginBottom(View v) {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

}
