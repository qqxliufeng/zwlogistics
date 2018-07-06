package com.android.ql.lf.carapp.action;

import com.android.ql.lf.carapp.data.ImageBean;
import com.android.ql.lf.carapp.utils.ImageUploadHelper;

import java.util.ArrayList;

/**
 * Created by lf on 18.2.13.
 *
 * @author lf on 18.2.13
 */

public interface IViewCommunityAction {

    /**
     * 处理上传图片
     *
     * @param imageBeans     要处理的图片集合
     * @param maxSize        每个图片最大大小
     * @param uploadListener 处理事件监听器
     * @return
     */
    boolean processImage(ArrayList<ImageBean> imageBeans, int maxSize, ImageUploadHelper.OnImageUploadListener uploadListener);

}
