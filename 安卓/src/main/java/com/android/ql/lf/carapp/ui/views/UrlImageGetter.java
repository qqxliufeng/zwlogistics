package com.android.ql.lf.carapp.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.widget.TextView;

import com.android.ql.lf.carapp.R;
import com.bumptech.glide.Glide;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Administrator
 */
public class UrlImageGetter implements Html.ImageGetter {

    Context c;
    TextView container;
    int width;

    /**
     * @param t
     * @param c
     */
    public UrlImageGetter(TextView t, Context c) {
        this.c = c;
        this.container = t;
        width = c.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public Drawable getDrawable(String source) {
        final UrlDrawable urlDrawable = new UrlDrawable();
        try {
            Observable.just(source).map(new Func1<String, Bitmap>() {
                @Override
                public Bitmap call(String s) {
                    try {
                        return Glide.with(c).load(s).asBitmap().into(500, 500).get();
                    } catch (Exception e) {
                        return null;
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Bitmap>() {
                        @Override
                        public void call(Bitmap loadedImage) {
                            if (loadedImage != null) {
                                float scaleWidth = ((float) width) / loadedImage.getWidth();
                                Matrix matrix = new Matrix();
                                matrix.postScale(scaleWidth, scaleWidth);
                                loadedImage = Bitmap.createBitmap(loadedImage, 0, 0, loadedImage.getWidth(), loadedImage.getHeight(), matrix, true);
                                urlDrawable.bitmap = loadedImage;
                                urlDrawable.setBounds(0, 0, loadedImage.getWidth(), loadedImage.getHeight());
                                container.invalidate();
                                container.setText(container.getText()); // 解决图文重叠
                            }
                        }
                    });
            return urlDrawable;
        } catch (Exception e) {
            return ContextCompat.getDrawable(c, R.drawable.img_glide_load_default);
        }
    }

    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        protected Bitmap bitmap;

        @Override
        public void draw(Canvas canvas) {
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, 0, 0, getPaint());
            }
        }
    }
}
