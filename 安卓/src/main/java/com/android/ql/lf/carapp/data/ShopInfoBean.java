package com.android.ql.lf.carapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lf on 18.4.14.
 *
 * @author lf on 18.4.14
 */

public class ShopInfoBean implements Parcelable {

    private String shop_id;
    private String shop_mpic;
    private String shop_sypic;
    private String shop_name;
    private String shop_phone;
    private String shop_address;
    private String shop_num;
    private String shop_content;
    private String shop_type;
    private String shop_token;
    private String shop_time;
    private String shop_uid;
    private String shop_stoken;
    private String shop_sn;
    private String shop_coorp;
    private String shop_ppa;
    private String shop_start_time;
    private String shop_end_time;
    private String shop_isinsured;
    private String shop_d;
    private String shop_atn;
    private String shop_attention;

    public String getShop_attention() {
        return shop_attention;
    }

    public void setShop_attention(String shop_attention) {
        this.shop_attention = shop_attention;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_mpic() {
        return shop_mpic;
    }

    public void setShop_mpic(String shop_mpic) {
        this.shop_mpic = shop_mpic;
    }

    public String getShop_sypic() {
        return shop_sypic;
    }

    public void setShop_sypic(String shop_sypic) {
        this.shop_sypic = shop_sypic;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_phone() {
        return shop_phone;
    }

    public void setShop_phone(String shop_phone) {
        this.shop_phone = shop_phone;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_num() {
        return shop_num;
    }

    public void setShop_num(String shop_num) {
        this.shop_num = shop_num;
    }

    public String getShop_content() {
        return shop_content;
    }

    public void setShop_content(String shop_content) {
        this.shop_content = shop_content;
    }

    public String getShop_type() {
        return shop_type;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }

    public String getShop_token() {
        return shop_token;
    }

    public void setShop_token(String shop_token) {
        this.shop_token = shop_token;
    }

    public String getShop_time() {
        return shop_time;
    }

    public void setShop_time(String shop_time) {
        this.shop_time = shop_time;
    }

    public String getShop_uid() {
        return shop_uid;
    }

    public void setShop_uid(String shop_uid) {
        this.shop_uid = shop_uid;
    }

    public String getShop_stoken() {
        return shop_stoken;
    }

    public void setShop_stoken(String shop_stoken) {
        this.shop_stoken = shop_stoken;
    }

    public String getShop_sn() {
        return shop_sn;
    }

    public void setShop_sn(String shop_sn) {
        this.shop_sn = shop_sn;
    }

    public String getShop_coorp() {
        return shop_coorp;
    }

    public void setShop_coorp(String shop_coorp) {
        this.shop_coorp = shop_coorp;
    }

    public String getShop_ppa() {
        return shop_ppa;
    }

    public void setShop_ppa(String shop_ppa) {
        this.shop_ppa = shop_ppa;
    }

    public String getShop_start_time() {
        return shop_start_time;
    }

    public void setShop_start_time(String shop_start_time) {
        this.shop_start_time = shop_start_time;
    }

    public String getShop_end_time() {
        return shop_end_time;
    }

    public void setShop_end_time(String shop_end_time) {
        this.shop_end_time = shop_end_time;
    }

    public String getShop_isinsured() {
        return shop_isinsured;
    }

    public void setShop_isinsured(String shop_isinsured) {
        this.shop_isinsured = shop_isinsured;
    }

    public String getShop_d() {
        return shop_d;
    }

    public void setShop_d(String shop_d) {
        this.shop_d = shop_d;
    }

    public String getShop_atn() {
        return shop_atn;
    }

    public void setShop_atn(String shop_atn) {
        this.shop_atn = shop_atn;
    }

    public ShopInfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shop_id);
        dest.writeString(this.shop_mpic);
        dest.writeString(this.shop_sypic);
        dest.writeString(this.shop_name);
        dest.writeString(this.shop_phone);
        dest.writeString(this.shop_address);
        dest.writeString(this.shop_num);
        dest.writeString(this.shop_content);
        dest.writeString(this.shop_type);
        dest.writeString(this.shop_token);
        dest.writeString(this.shop_time);
        dest.writeString(this.shop_uid);
        dest.writeString(this.shop_stoken);
        dest.writeString(this.shop_sn);
        dest.writeString(this.shop_coorp);
        dest.writeString(this.shop_ppa);
        dest.writeString(this.shop_start_time);
        dest.writeString(this.shop_end_time);
        dest.writeString(this.shop_isinsured);
        dest.writeString(this.shop_d);
        dest.writeString(this.shop_atn);
        dest.writeString(this.shop_attention);
    }

    protected ShopInfoBean(Parcel in) {
        this.shop_id = in.readString();
        this.shop_mpic = in.readString();
        this.shop_sypic = in.readString();
        this.shop_name = in.readString();
        this.shop_phone = in.readString();
        this.shop_address = in.readString();
        this.shop_num = in.readString();
        this.shop_content = in.readString();
        this.shop_type = in.readString();
        this.shop_token = in.readString();
        this.shop_time = in.readString();
        this.shop_uid = in.readString();
        this.shop_stoken = in.readString();
        this.shop_sn = in.readString();
        this.shop_coorp = in.readString();
        this.shop_ppa = in.readString();
        this.shop_start_time = in.readString();
        this.shop_end_time = in.readString();
        this.shop_isinsured = in.readString();
        this.shop_d = in.readString();
        this.shop_atn = in.readString();
        this.shop_attention = in.readString();
    }

    public static final Creator<ShopInfoBean> CREATOR = new Creator<ShopInfoBean>() {
        @Override
        public ShopInfoBean createFromParcel(Parcel source) {
            return new ShopInfoBean(source);
        }

        @Override
        public ShopInfoBean[] newArray(int size) {
            return new ShopInfoBean[size];
        }
    };
}
