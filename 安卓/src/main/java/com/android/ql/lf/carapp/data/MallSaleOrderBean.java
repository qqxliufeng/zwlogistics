package com.android.ql.lf.carapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lf on 18.3.27.
 *
 * @author lf on 18.3.27
 */

public class MallSaleOrderBean implements Parcelable {

    private String product_id;
    private String product_name;
    private String product_md;
    private String product_mdprice;
    private String product_price;
    private String product_shopname;
    private String product_shoppic;
    private String order_num;
    private String order_sn;
    private String order_oprice;
    private String order_fc;
    private String order_token;
    private String order_id;
    private String order_specification;
    private String order_uid;
    private String order_ctime;
    private String order_tn;
    private String order_mdprice;
    private String order_sku_pic;
    private String order_price;

    public String getOrder_price() {
        return order_price;
    }

    public void setOrder_price(String order_price) {
        this.order_price = order_price;
    }

    public String getOrder_sku_pic() {
        return order_sku_pic;
    }

    public void setOrder_sku_pic(String order_sku_pic) {
        this.order_sku_pic = order_sku_pic;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_md() {
        return product_md;
    }

    public void setProduct_md(String product_md) {
        this.product_md = product_md;
    }

    public String getProduct_mdprice() {
        return product_mdprice;
    }

    public void setProduct_mdprice(String product_mdprice) {
        this.product_mdprice = product_mdprice;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_oprice() {
        return order_oprice;
    }

    public void setOrder_oprice(String order_oprice) {
        this.order_oprice = order_oprice;
    }

    public String getOrder_fc() {
        return order_fc;
    }

    public void setOrder_fc(String order_fc) {
        this.order_fc = order_fc;
    }

    public String getOrder_token() {
        return order_token;
    }

    public void setOrder_token(String order_token) {
        this.order_token = order_token;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_specification() {
        return order_specification;
    }

    public void setOrder_specification(String order_specification) {
        this.order_specification = order_specification;
    }

    public String getOrder_uid() {
        return order_uid;
    }

    public void setOrder_uid(String order_uid) {
        this.order_uid = order_uid;
    }

    public String getOrder_ctime() {
        return order_ctime;
    }

    public void setOrder_ctime(String order_ctime) {
        this.order_ctime = order_ctime;
    }

    public String getOrder_tn() {
        return order_tn;
    }

    public void setOrder_tn(String order_tn) {
        this.order_tn = order_tn;
    }

    public String getOrder_mdprice() {
        return order_mdprice;
    }

    public void setOrder_mdprice(String order_mdprice) {
        this.order_mdprice = order_mdprice;
    }

    public String getProduct_shopname() {
        return product_shopname;
    }

    public void setProduct_shopname(String product_shopname) {
        this.product_shopname = product_shopname;
    }

    public String getProduct_shoppic() {
        return product_shoppic;
    }

    public void setProduct_shoppic(String product_shoppic) {
        this.product_shoppic = product_shoppic;
    }

    public MallSaleOrderBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.product_id);
        dest.writeString(this.product_name);
        dest.writeString(this.product_md);
        dest.writeString(this.product_mdprice);
        dest.writeString(this.product_price);
        dest.writeString(this.product_shopname);
        dest.writeString(this.product_shoppic);
        dest.writeString(this.order_num);
        dest.writeString(this.order_sn);
        dest.writeString(this.order_oprice);
        dest.writeString(this.order_fc);
        dest.writeString(this.order_token);
        dest.writeString(this.order_id);
        dest.writeString(this.order_specification);
        dest.writeString(this.order_uid);
        dest.writeString(this.order_ctime);
        dest.writeString(this.order_tn);
        dest.writeString(this.order_mdprice);
        dest.writeString(this.order_sku_pic);
        dest.writeString(this.order_price);
    }

    protected MallSaleOrderBean(Parcel in) {
        this.product_id = in.readString();
        this.product_name = in.readString();
        this.product_md = in.readString();
        this.product_mdprice = in.readString();
        this.product_price = in.readString();
        this.product_shopname = in.readString();
        this.product_shoppic = in.readString();
        this.order_num = in.readString();
        this.order_sn = in.readString();
        this.order_oprice = in.readString();
        this.order_fc = in.readString();
        this.order_token = in.readString();
        this.order_id = in.readString();
        this.order_specification = in.readString();
        this.order_uid = in.readString();
        this.order_ctime = in.readString();
        this.order_tn = in.readString();
        this.order_mdprice = in.readString();
        this.order_sku_pic = in.readString();
        this.order_price = in.readString();
    }

    public static final Creator<MallSaleOrderBean> CREATOR = new Creator<MallSaleOrderBean>() {
        @Override
        public MallSaleOrderBean createFromParcel(Parcel source) {
            return new MallSaleOrderBean(source);
        }

        @Override
        public MallSaleOrderBean[] newArray(int size) {
            return new MallSaleOrderBean[size];
        }
    };
}
