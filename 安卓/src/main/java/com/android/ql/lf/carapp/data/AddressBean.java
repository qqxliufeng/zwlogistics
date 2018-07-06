package com.android.ql.lf.carapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by lf on 2017/12/1 0001.
 *
 * @author lf on 2017/12/1 0001
 */

public class AddressBean implements Parcelable {

    private String address_id;
    private String address_name;
    private String address_phone;
    private String address_addres;
    private String address_user;
    private String address_token;
    private String address_detail;
    private String address_postcode;

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getAddress_phone() {
        return address_phone;
    }

    public void setAddress_phone(String address_phone) {
        this.address_phone = address_phone;
    }

    public String getAddress_addres() {
        return address_addres;
    }

    public void setAddress_addres(String address_addres) {
        this.address_addres = address_addres;
    }

    public String getAddress_user() {
        return address_user;
    }

    public void setAddress_user(String address_user) {
        this.address_user = address_user;
    }

    public String getAddress_token() {
        return address_token;
    }

    public void setAddress_token(String address_token) {
        this.address_token = address_token;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public String getAddress_postcode() {
        return address_postcode;
    }

    public void setAddress_postcode(String address_postcode) {
        this.address_postcode = address_postcode;
    }

    public AddressBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address_id);
        dest.writeString(this.address_name);
        dest.writeString(this.address_phone);
        dest.writeString(this.address_addres);
        dest.writeString(this.address_user);
        dest.writeString(this.address_token);
        dest.writeString(this.address_detail);
        dest.writeString(this.address_postcode);
    }

    protected AddressBean(Parcel in) {
        this.address_id = in.readString();
        this.address_name = in.readString();
        this.address_phone = in.readString();
        this.address_addres = in.readString();
        this.address_user = in.readString();
        this.address_token = in.readString();
        this.address_detail = in.readString();
        this.address_postcode = in.readString();
    }

    public static final Creator<AddressBean> CREATOR = new Creator<AddressBean>() {
        @Override
        public AddressBean createFromParcel(Parcel source) {
            return new AddressBean(source);
        }

        @Override
        public AddressBean[] newArray(int size) {
            return new AddressBean[size];
        }
    };
}
