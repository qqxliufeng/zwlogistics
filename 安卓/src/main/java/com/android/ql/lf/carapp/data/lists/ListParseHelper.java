package com.android.ql.lf.carapp.data.lists;

import android.util.Log;

import com.android.ql.lf.carapp.data.BaseJsonData;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by lf on 2017/11/23 0023.
 *
 * @author lf on 2017/11/23 0023
 */

public class ListParseHelper<T> extends BaseJsonData {

    public ArrayList<T> fromJson(String content, Class<T> clazz) {
        try {
            if (content != null) {
                String newContent = content.replace("\"\"", "暂无");
                ListParseHelper m = new Gson().fromJson(newContent, ListParseHelper.class);
                if (m != null) {
                    if (m.getResult() != null) {
                        ArrayList<T> result = new ArrayList<>();
                        for (int i = 0; i < m.getResult().size(); i++) {
                            result.add(new Gson().fromJson(new Gson().toJson(m.getResult().get(i)), clazz));
                        }
                        return result;
                    }
                    return null;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
    }

}
