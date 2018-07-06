package com.android.ql.lf.carapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lf on 18.3.24.
 *
 * @author lf on 18.3.24
 */

public class StoreInfoBean implements Parcelable {

    private String wholesale_shop_id;
    private String wholesale_shop_name;
    private ArrayList<String> wholesale_shop_pic;
    private String wholesale_shop_attention;
    private String wholesale_shop_uid;
    private String wholesale_shop_num;
    private String wholesale_shop_hxname;
    private String wholesale_shop_ping;

    public String getWholesale_shop_ping() {
        return wholesale_shop_ping;
    }

    public void setWholesale_shop_ping(String wholesale_shop_ping) {
        this.wholesale_shop_ping = wholesale_shop_ping;
    }

    public String getWholesale_shop_hxname() {
        return wholesale_shop_hxname;
    }

    public void setWholesale_shop_hxname(String wholesale_shop_hxname) {
        this.wholesale_shop_hxname = wholesale_shop_hxname;
    }

    public String getWholesale_shop_id() {
        return wholesale_shop_id;
    }

    public void setWholesale_shop_id(String wholesale_shop_id) {
        this.wholesale_shop_id = wholesale_shop_id;
    }

    public String getWholesale_shop_name() {
        return wholesale_shop_name;
    }

    public void setWholesale_shop_name(String wholesale_shop_name) {
        this.wholesale_shop_name = wholesale_shop_name;
    }

    public ArrayList<String> getWholesale_shop_pic() {
        return wholesale_shop_pic;
    }

    public void setWholesale_shop_pic(ArrayList<String> wholesale_shop_pic) {
        this.wholesale_shop_pic = wholesale_shop_pic;
    }

    public String getWholesale_shop_attention() {
        return wholesale_shop_attention;
    }

    public void setWholesale_shop_attention(String wholesale_shop_attention) {
        this.wholesale_shop_attention = wholesale_shop_attention;
    }

    public String getWholesale_shop_uid() {
        return wholesale_shop_uid;
    }

    public void setWholesale_shop_uid(String wholesale_shop_uid) {
        this.wholesale_shop_uid = wholesale_shop_uid;
    }

    public String getWholesale_shop_num() {
        return wholesale_shop_num;
    }

    public void setWholesale_shop_num(String wholesale_shop_num) {
        this.wholesale_shop_num = wholesale_shop_num;
    }

    public StoreInfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.wholesale_shop_id);
        dest.writeString(this.wholesale_shop_name);
        dest.writeStringList(this.wholesale_shop_pic);
        dest.writeString(this.wholesale_shop_attention);
        dest.writeString(this.wholesale_shop_uid);
        dest.writeString(this.wholesale_shop_num);
        dest.writeString(this.wholesale_shop_hxname);
        dest.writeString(this.wholesale_shop_ping);
    }

    protected StoreInfoBean(Parcel in) {
        this.wholesale_shop_id = in.readString();
        this.wholesale_shop_name = in.readString();
        this.wholesale_shop_pic = in.createStringArrayList();
        this.wholesale_shop_attention = in.readString();
        this.wholesale_shop_uid = in.readString();
        this.wholesale_shop_num = in.readString();
        this.wholesale_shop_hxname = in.readString();
        this.wholesale_shop_ping = in.readString();
    }

    public static final Creator<StoreInfoBean> CREATOR = new Creator<StoreInfoBean>() {
        @Override
        public StoreInfoBean createFromParcel(Parcel source) {
            return new StoreInfoBean(source);
        }

        @Override
        public StoreInfoBean[] newArray(int size) {
            return new StoreInfoBean[size];
        }
    };
}
