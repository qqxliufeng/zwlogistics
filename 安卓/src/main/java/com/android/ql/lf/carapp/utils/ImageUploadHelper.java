package com.android.ql.lf.carapp.utils;


import android.text.TextUtils;
import android.util.Log;

import com.android.ql.lf.carapp.application.CarApplication;
import com.android.ql.lf.carapp.data.ImageBean;
import com.android.ql.lf.carapp.data.UserInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by lf on 2017/11/24 0024.
 *
 * @author lf on 2017/11/24 0024
 */

public class ImageUploadHelper {

    private OnImageUploadListener onImageUploadListener;

    public static MultipartBody.Builder createMultipartBody() {
        return new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("token", Constants.md5Token())
                .addFormDataPart("uid", UserInfo.getInstance().getMemberId());
    }

    public ImageUploadHelper(OnImageUploadListener onImageUploadListener) {
        this.onImageUploadListener = onImageUploadListener;
    }

    public void upload(final ArrayList<ImageBean> list, final int maxSize) {
        final File dir = new File(Constants.IMAGE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final ArrayList<String> compressList = new ArrayList<>();
        Observable.from(list).map(new Func1<ImageBean, String>() {
            @Override
            public String call(ImageBean imageItem) {
                try {
                    if (imageItem != null && !TextUtils.isEmpty(imageItem.getUriPath())) {
                        String path = dir + File.separator + System.currentTimeMillis() + ".jpg";
//                        ImageFactory.compressAndGenImage(imageItem.getUriPath(), path, maxSize, false);
                        ImageFactory.ratioAndGenThumb(imageItem.getUriPath(), path,700,700,false);
                        return path;
                    }
                } catch (IOException e) {
                    return null;
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (onImageUploadListener != null) {
                            onImageUploadListener.onActionStart();
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.getMessage());
                        if (onImageUploadListener != null) {
                            onImageUploadListener.onActionFailed();
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            if (s != null) {
                                compressList.add(s);
                                if (compressList.size() == list.size()) {
                                    if (onImageUploadListener != null) {
                                        MultipartBody.Builder builder = ImageUploadHelper.createMultipartBody();
                                        for (int i = 0; i < compressList.size(); i++) {
                                            File file = new File(compressList.get(i));
                                            builder.addFormDataPart(i + "", file.getName(), RequestBody.create(MultipartBody.FORM, file));
                                        }
                                        onImageUploadListener.onActionEnd(builder);
                                    }
                                }
                            } else {
                                if (onImageUploadListener != null) {
                                    onImageUploadListener.onActionFailed();
                                }
                            }
                        } catch (Exception e) {
                            Log.e("TAG", e.getMessage());
                        }
                    }
                });
    }

    public void upload(final ArrayList<ImageBean> list){
        if (list!=null  && !list.isEmpty()){
            File dir = new File(Constants.IMAGE_PATH);
            if (!dir.exists()){
                dir.mkdirs();
            }
            ArrayList<String> tempPath = new ArrayList<>();
            for (ImageBean imageBean : list) {
                tempPath.add(imageBean.getUriPath());
            }
            Observable.just(tempPath).map(new Func1<ArrayList<String>, ArrayList<File>>() {
                @Override
                public ArrayList<File> call(ArrayList<String> list) {
                    try {
                        return (ArrayList<File>) Luban
                                .with(CarApplication.getInstance())
                                .ignoreBy(100)
                                .setTargetDir(Constants.IMAGE_PATH)
                                .load(list)
                                .get();
                    } catch (IOException e) {
                        return null;
                    }
                }
            }).subscribeOn(Schedulers.io())
            .doOnSubscribe(new Action0() {
                @Override
                public void call() {
                    if (onImageUploadListener != null) {
                        onImageUploadListener.onActionStart();
                    }
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<ArrayList<File>>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("TAG", e.getMessage());
                    if (onImageUploadListener != null) {
                        onImageUploadListener.onActionFailed();
                    }
                }

                @Override
                public void onNext(ArrayList<File> files) {
                    if (onImageUploadListener != null && files!=null) {
                        MultipartBody.Builder builder = ImageUploadHelper.createMultipartBody();
                        for (int i = 0; i < files.size(); i++) {
                            File file = files.get(i);
                            builder.addFormDataPart(i + "", file.getName(), RequestBody.create(MultipartBody.FORM, file));
                        }
                        onImageUploadListener.onActionEnd(builder);
                    }else {
                        if (onImageUploadListener != null) {
                            onImageUploadListener.onActionFailed();
                        }
                    }
                }
            });
        }
    }

//    new Action1<String>() {
//        @Override
//        public void call(String s) {
//            if (s != null) {
//                compressList.add(s);
//                if (compressList.size() == list.size()) {
//                    if (onImageUploadListener != null) {
//                        MultipartBody.Builder builder = ImageUploadHelper.createMultipartBody();
//                        for (int i = 0; i < compressList.size(); i++) {
//                            File file = new File(compressList.get(i));
//                            builder.addFormDataPart(i + "", file.getName(), RequestBody.create(MultipartBody.FORM, file));
//                        }
//                        onImageUploadListener.onActionEnd(builder);
//                    }
//                }
//            } else {
//                if (onImageUploadListener != null) {
//                    onImageUploadListener.onActionFailed();
//                }
//            }
//        }
//    }

    public interface OnImageUploadListener {

        public void onActionStart();

        public void onActionEnd(MultipartBody.Builder builder);

        public void onActionFailed();
    }

}
