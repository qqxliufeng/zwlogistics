package com.android.ql.lf.carapp.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.ql.lf.carapp.R;
import com.android.ql.lf.carapp.utils.GlideManager;

/**
 * Created by lf on 18.3.23.
 *
 * @author lf on 18.3.23
 */

public class HotView extends LinearLayout {

    private TextView mTvTitle;
    private TextView mTvDescription;
    private ImageView mIvPic;

    public HotView(Context context) {
        this(context, null);
    }

    public HotView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View contentView = View.inflate(getContext(), R.layout.layout_hot_goods_layout, this);
        mTvTitle = contentView.findViewById(R.id.mTvMainMallHotTitle);
        mTvDescription = contentView.findViewById(R.id.mTvMainMallHotDescription);
        mIvPic = contentView.findViewById(R.id.mIvMainMallHotPic);
    }

    public void bindData(String title, String description, String pic, OnClickListener onClickListener) {
        mTvTitle.setText(TextUtils.isEmpty(title) ? "暂无" : title);
        mTvDescription.setText(TextUtils.isEmpty(description) ? "暂无" : description);
        GlideManager.loadImage(getContext(), pic, mIvPic);
        setOnClickListener(onClickListener);
    }
}
