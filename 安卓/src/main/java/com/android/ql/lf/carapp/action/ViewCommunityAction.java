package com.android.ql.lf.carapp.action;

import com.android.ql.lf.carapp.data.ImageBean;
import com.android.ql.lf.carapp.utils.ImageUploadHelper;

import java.util.ArrayList;

/**
 * Created by lf on 18.2.13.
 *
 * @author lf on 18.2.13
 */

public class ViewCommunityAction implements IViewCommunityAction {

    @Override
    public boolean processImage(ArrayList<ImageBean> imageBeans, int maxSize, ImageUploadHelper.OnImageUploadListener uploadListener) {
        try {
            new ImageUploadHelper(uploadListener).upload(imageBeans);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
