package com.android.ql.lf.carapp.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lf on 18.3.27.
 *
 * @author lf on 18.3.27
 */

public class MallSubmitOrderBean implements Parcelable {

    private String gid;
    private String cid;
    private String price;
    private String address;
    private String num;
    private String ktype;
    private String mdtype;
    private String mliuyan;
    private String specification;
    private String mdprice;
    private String key;
    private String pic;
    private String couponId;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getBbs() {
        return bbs;
    }

    public void setBbs(String bbs) {
        this.bbs = bbs;
    }

    private String bbs;

    public String getMdprice() {
        return mdprice;
    }

    public void setMdprice(String mdprice) {
        this.mdprice = mdprice;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getKtype() {
        return ktype;
    }

    public void setKtype(String ktype) {
        this.ktype = ktype;
    }

    public String getMdtype() {
        return mdtype;
    }

    public void setMdtype(String mdtype) {
        this.mdtype = mdtype;
    }

    public String getMliuyan() {
        return mliuyan;
    }

    public void setMliuyan(String mliuyan) {
        this.mliuyan = mliuyan;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public MallSubmitOrderBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.gid);
        dest.writeString(this.cid);
        dest.writeString(this.price);
        dest.writeString(this.address);
        dest.writeString(this.num);
        dest.writeString(this.ktype);
        dest.writeString(this.mdtype);
        dest.writeString(this.mliuyan);
        dest.writeString(this.specification);
        dest.writeString(this.mdprice);
        dest.writeString(this.key);
        dest.writeString(this.pic);
        dest.writeString(this.couponId);
        dest.writeString(this.bbs);
    }

    protected MallSubmitOrderBean(Parcel in) {
        this.gid = in.readString();
        this.cid = in.readString();
        this.price = in.readString();
        this.address = in.readString();
        this.num = in.readString();
        this.ktype = in.readString();
        this.mdtype = in.readString();
        this.mliuyan = in.readString();
        this.specification = in.readString();
        this.mdprice = in.readString();
        this.key = in.readString();
        this.pic = in.readString();
        this.couponId = in.readString();
        this.bbs = in.readString();
    }

    public static final Creator<MallSubmitOrderBean> CREATOR = new Creator<MallSubmitOrderBean>() {
        @Override
        public MallSubmitOrderBean createFromParcel(Parcel source) {
            return new MallSubmitOrderBean(source);
        }

        @Override
        public MallSubmitOrderBean[] newArray(int size) {
            return new MallSubmitOrderBean[size];
        }
    };
}
