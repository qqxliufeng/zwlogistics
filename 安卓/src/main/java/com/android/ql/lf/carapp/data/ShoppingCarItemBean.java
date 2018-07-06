package com.android.ql.lf.carapp.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lf on 2017/12/5 0005.
 *
 * @author lf on 2017/12/5 0005
 */

public class ShoppingCarItemBean implements Parcelable {
    private boolean isSelector = false;
    private boolean isEditorMode = false;

    private String shopcart_id;
    private String shopcart_name;
    private String shopcart_pic;
    private String shopcart_specification;
    private String shopcart_price;
    private String shopcart_num;
    private String shopcart_gid;
    private String shopcart_time;
    private String shopcart_uid;
    private String shopcart_shopid;
    private String shopcart_mdprice;
    private String shop_shopname;
    private String shop_shoppic;
    private String shop_id;
    private String sku_pic;
    private String shopcart_freight;
    private String shopcart_key;

    public String getShopcart_freight() {
        return shopcart_freight;
    }

    public void setShopcart_freight(String shopcart_freight) {
        this.shopcart_freight = shopcart_freight;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShopcart_key() {
        return shopcart_key;
    }

    public void setShopcart_key(String shopcart_key) {
        this.shopcart_key = shopcart_key;
    }

    public String getSku_pic() {
        return sku_pic;
    }

    public void setSku_pic(String sku_pic) {
        this.sku_pic = sku_pic;
    }

    private String bbs;

    public String getShopcart_shopid() {
        return shopcart_shopid;
    }

    public void setShopcart_shopid(String shopcart_shopid) {
        this.shopcart_shopid = shopcart_shopid;
    }

    public String getShop_shopname() {
        return shop_shopname;
    }

    public void setShop_shopname(String shop_shopname) {
        this.shop_shopname = shop_shopname;
    }

    public String getShop_shoppic() {
        return shop_shoppic;
    }

    public void setShop_shoppic(String shop_shoppic) {
        this.shop_shoppic = shop_shoppic;
    }

    public String getShopcart_mdprice() {
        return shopcart_mdprice;
    }

    public void setShopcart_mdprice(String shopcart_mdprice) {
        this.shopcart_mdprice = shopcart_mdprice;
    }

    public String getBbs() {
        return bbs;
    }

    public void setBbs(String bbs) {
        this.bbs = bbs;
    }

    public boolean isSelector() {
        return isSelector;
    }

    public void setSelector(boolean selector) {
        isSelector = selector;
    }

    public boolean isEditorMode() {
        return isEditorMode;
    }

    public void setEditorMode(boolean editorMode) {
        isEditorMode = editorMode;
    }

    public String getShopcart_id() {
        return shopcart_id;
    }

    public void setShopcart_id(String shopcart_id) {
        this.shopcart_id = shopcart_id;
    }

    public String getShopcart_name() {
        return shopcart_name;
    }

    public void setShopcart_name(String shopcart_name) {
        this.shopcart_name = shopcart_name;
    }

    public String getShopcart_pic() {
        return shopcart_pic;
    }

    public void setShopcart_pic(String shopcart_pic) {
        this.shopcart_pic = shopcart_pic;
    }

    public String getShopcart_specification() {
        return shopcart_specification;
    }

    public void setShopcart_specification(String shopcart_specification) {
        this.shopcart_specification = shopcart_specification;
    }

    public String getShopcart_price() {
        return shopcart_price;
    }

    public void setShopcart_price(String shopcart_price) {
        this.shopcart_price = shopcart_price;
    }

    public String getShopcart_num() {
        return shopcart_num;
    }

    public void setShopcart_num(String shopcart_num) {
        this.shopcart_num = shopcart_num;
    }

    public String getShopcart_gid() {
        return shopcart_gid;
    }

    public void setShopcart_gid(String shopcart_gid) {
        this.shopcart_gid = shopcart_gid;
    }

    public String getShopcart_time() {
        return shopcart_time;
    }

    public void setShopcart_time(String shopcart_time) {
        this.shopcart_time = shopcart_time;
    }

    public String getShopcart_uid() {
        return shopcart_uid;
    }

    public void setShopcart_uid(String shopcart_uid) {
        this.shopcart_uid = shopcart_uid;
    }

    public ShoppingCarItemBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSelector ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEditorMode ? (byte) 1 : (byte) 0);
        dest.writeString(this.shopcart_id);
        dest.writeString(this.shopcart_name);
        dest.writeString(this.shopcart_pic);
        dest.writeString(this.shopcart_specification);
        dest.writeString(this.shopcart_price);
        dest.writeString(this.shopcart_num);
        dest.writeString(this.shopcart_gid);
        dest.writeString(this.shopcart_time);
        dest.writeString(this.shopcart_uid);
        dest.writeString(this.shopcart_shopid);
        dest.writeString(this.shopcart_mdprice);
        dest.writeString(this.shop_shopname);
        dest.writeString(this.shop_shoppic);
        dest.writeString(this.shop_id);
        dest.writeString(this.sku_pic);
        dest.writeString(this.shopcart_freight);
        dest.writeString(this.shopcart_key);
        dest.writeString(this.bbs);
    }

    protected ShoppingCarItemBean(Parcel in) {
        this.isSelector = in.readByte() != 0;
        this.isEditorMode = in.readByte() != 0;
        this.shopcart_id = in.readString();
        this.shopcart_name = in.readString();
        this.shopcart_pic = in.readString();
        this.shopcart_specification = in.readString();
        this.shopcart_price = in.readString();
        this.shopcart_num = in.readString();
        this.shopcart_gid = in.readString();
        this.shopcart_time = in.readString();
        this.shopcart_uid = in.readString();
        this.shopcart_shopid = in.readString();
        this.shopcart_mdprice = in.readString();
        this.shop_shopname = in.readString();
        this.shop_shoppic = in.readString();
        this.shop_id = in.readString();
        this.sku_pic = in.readString();
        this.shopcart_freight = in.readString();
        this.shopcart_key = in.readString();
        this.bbs = in.readString();
    }

    public static final Creator<ShoppingCarItemBean> CREATOR = new Creator<ShoppingCarItemBean>() {
        @Override
        public ShoppingCarItemBean createFromParcel(Parcel source) {
            return new ShoppingCarItemBean(source);
        }

        @Override
        public ShoppingCarItemBean[] newArray(int size) {
            return new ShoppingCarItemBean[size];
        }
    };
}
