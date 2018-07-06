package com.android.ql.lf.carapp.utils;

import android.app.Activity;
import android.widget.ImageView;


/**
 * Created by lf on 2017/12/7 0007.
 *
 * @author lf on 2017/12/7 0007
 */

public class GlideImageLoader {
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        GlideManager.loadImage(activity, path, imageView);
    }

    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {

    }

    public void clearMemoryCache() {

    }
}
