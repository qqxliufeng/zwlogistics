package com.android.ql.lf.carapp.utils;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class ApiParams extends HashMap<String, Object> {

    public ApiParams addParam(String paramName, Object paramValue) {
        put(paramName, paramValue);
        return this;
    }
}
