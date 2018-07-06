package com.android.ql.lf.carapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.android.ql.lf.carapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by lf on 2017/11/22 0022.
 *
 * @author lf on 2017/11/22 0022
 */

public class GlideManager {

    public static void loadImage(Context context, String path, ImageView imageView) {
        Glide.with(context).load(Constants.BASE_IP + path)
                .error(R.drawable.img_glide_load_default)
                .placeholder(R.drawable.img_glide_load_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadRoundImage(Context context, String path, ImageView imageView,int rounded) {
        Glide.with(context)
                .load(Constants.BASE_IP + path)
                .error(R.drawable.img_glide_load_default)
                .placeholder(R.drawable.img_glide_load_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, rounded, 0))
                .into(imageView);
    }

    public static void loadFaceCircleImage(Context context, String path, ImageView imageView) {
        String tempPath;
        if (path != null) {
            if (path.startsWith("http://") || path.startsWith("http://")) {
                tempPath = path;
            } else {
                tempPath = Constants.BASE_IP + path;
            }
            Glide.with(context)
                    .load(tempPath)
                    .error(R.drawable.img_glide_load_default)
                    .placeholder(R.drawable.img_glide_load_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(context), new CenterCrop(context))
                    .into(imageView);
        }
    }

    public static void loadCircleImage(Context context, String path, ImageView imageView) {
        String tempPath;
        if (path != null) {
            if (path.startsWith("http://") || path.startsWith("https://")) {
                tempPath = path;
            } else {
                tempPath = Constants.BASE_IP + path;
            }
        }else {
            tempPath = "";
        }
        Glide.with(context)
                .load(tempPath)
                .error(R.drawable.img_glide_circle_load_default)
                .placeholder(R.drawable.img_glide_circle_load_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(context), new CenterCrop(context))
                .into(imageView);
    }


}
