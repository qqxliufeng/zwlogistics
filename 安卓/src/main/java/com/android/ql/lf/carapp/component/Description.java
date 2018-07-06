package com.android.ql.lf.carapp.component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by feng on 2017/7/7.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    //用于对字段进行排序
    int index() default -1;

    //用于提示信息
    String description() default "";

    //上传参数对应的Key
    String formAlias() default "";

    //是否是手机号 若是则进行校验
    boolean isPhone() default false;

    //是否是邮箱 若是则进行校验
    boolean isEmail() default false;
}