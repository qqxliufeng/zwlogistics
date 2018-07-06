package com.android.ql.lf.carapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lf on 18.3.24.
 *
 * @author lf on 18.3.24
 */

public class SearchParamBean implements Parcelable {

    private String model;
    private String act;
    private String searchAct;
    private Map<String,String> params;

    public Map<String, String> getParams() {
        return params;
    }

    public String getSearchAct() {
        return searchAct;
    }

    public void setSearchAct(String searchAct) {
        this.searchAct = searchAct;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public SearchParamBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.model);
        dest.writeString(this.act);
        dest.writeString(this.searchAct);
        dest.writeInt(this.params.size());
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    protected SearchParamBean(Parcel in) {
        this.model = in.readString();
        this.act = in.readString();
        this.searchAct = in.readString();
        int paramsSize = in.readInt();
        this.params = new HashMap<String, String>(paramsSize);
        for (int i = 0; i < paramsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.params.put(key, value);
        }
    }

    public static final Creator<SearchParamBean> CREATOR = new Creator<SearchParamBean>() {
        @Override
        public SearchParamBean createFromParcel(Parcel source) {
            return new SearchParamBean(source);
        }

        @Override
        public SearchParamBean[] newArray(int size) {
            return new SearchParamBean[size];
        }
    };
}
