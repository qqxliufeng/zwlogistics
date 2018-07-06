package com.android.ql.lf.carapp.data;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * Created by lf on 2017/12/1 0001.
 *
 * @author lf on 2017/12/1 0001
 */

public class ClassifyItemEntity extends SectionEntity<ClassifyBean.ClassifySubItemBean>{

    public ClassifyItemEntity(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public ClassifyItemEntity(ClassifyBean.ClassifySubItemBean classifySubItemBean) {
        super(classifySubItemBean);
    }

}
