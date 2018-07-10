package com.android.ql.lf.zwlogistics.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.android.ql.lf.carapp.data.ImageBean;
import com.android.ql.lf.carapp.utils.ViewKtKt;

import java.util.ArrayList;

public class PostDriverAuthBean implements Parcelable {

    private String faceUrl;
    private String idCarFrontdUrl;
    private String idCardBackgroundkUrl;
    private String driverUrl;
    private String cyzgUrl;
    private String driverName;
    private String driverIdCardNum;
    private String driverPhone;

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public String getIdCarFrontdUrl() {
        return idCarFrontdUrl;
    }

    public void setIdCarFrontdUrl(String idCarFrontdUrl) {
        this.idCarFrontdUrl = idCarFrontdUrl;
    }

    public String getIdCardBackgroundkUrl() {
        return idCardBackgroundkUrl;
    }

    public void setIdCardBackgroundkUrl(String idCardBackgroundkUrl) {
        this.idCardBackgroundkUrl = idCardBackgroundkUrl;
    }

    public String getDriverUrl() {
        return driverUrl;
    }

    public void setDriverUrl(String driverUrl) {
        this.driverUrl = driverUrl;
    }

    public String getCyzgUrl() {
        return cyzgUrl;
    }

    public void setCyzgUrl(String cyzgUrl) {
        this.cyzgUrl = cyzgUrl;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverIdCardNum() {
        return driverIdCardNum;
    }

    public void setDriverIdCardNum(String driverIdCardNum) {
        this.driverIdCardNum = driverIdCardNum;
    }

    public String getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(String driverPhone) {
        this.driverPhone = driverPhone;
    }


    public ArrayList<String> getKeys(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("user_rank_pic");
        arrayList.add("user_rank_idcard_front");
        arrayList.add("user_rank_idcard_back");
        arrayList.add("user_rank_driving");
        arrayList.add("user_rank_appraisal");
        return arrayList;
    }

    public ArrayList<ImageBean> getPhotos(){
        ArrayList<ImageBean> arrayList = new ArrayList<>();
        arrayList.add(new ImageBean(null,faceUrl));
        arrayList.add(new ImageBean(null,idCarFrontdUrl));
        arrayList.add(new ImageBean(null,idCardBackgroundkUrl));
        arrayList.add(new ImageBean(null,driverUrl));
        arrayList.add(new ImageBean(null,cyzgUrl));
        return arrayList;
    }



    public String isFeildEmpty(){
        if (TextUtils.isEmpty(faceUrl)){
            return "请选择头像";
        }
        if (TextUtils.isEmpty(idCarFrontdUrl)){
            return "请选择身份证正面";
        }
        if (TextUtils.isEmpty(idCardBackgroundkUrl)){
            return "请选择身份证反面";
        }
        if (TextUtils.isEmpty(driverUrl)){
            return "请选择驾驶证";
        }
        if (TextUtils.isEmpty(cyzgUrl)){
            return "请选择从业资格证";
        }
        if (TextUtils.isEmpty(driverName)){
            return "请输入姓名";
        }
        if (TextUtils.isEmpty(driverIdCardNum)){
            return "请输入身份证号";
        }
        if (!ViewKtKt.isIdCard(driverIdCardNum)){
            return "请输入正确的身份证号";
        }
        if (TextUtils.isEmpty(driverPhone)){
            return "请输入手机号";
        }
        if (!ViewKtKt.isPhone(driverPhone)){
            return "请输入正确的手机号";
        }
        return "";
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.faceUrl);
        dest.writeString(this.idCarFrontdUrl);
        dest.writeString(this.idCardBackgroundkUrl);
        dest.writeString(this.driverUrl);
        dest.writeString(this.cyzgUrl);
        dest.writeString(this.driverName);
        dest.writeString(this.driverIdCardNum);
        dest.writeString(this.driverPhone);
    }

    public PostDriverAuthBean() {
    }

    protected PostDriverAuthBean(Parcel in) {
        this.faceUrl = in.readString();
        this.idCarFrontdUrl = in.readString();
        this.idCardBackgroundkUrl = in.readString();
        this.driverUrl = in.readString();
        this.cyzgUrl = in.readString();
        this.driverName = in.readString();
        this.driverIdCardNum = in.readString();
        this.driverPhone = in.readString();
    }

    public static final Parcelable.Creator<PostDriverAuthBean> CREATOR = new Parcelable.Creator<PostDriverAuthBean>() {
        @Override
        public PostDriverAuthBean createFromParcel(Parcel source) {
            return new PostDriverAuthBean(source);
        }

        @Override
        public PostDriverAuthBean[] newArray(int size) {
            return new PostDriverAuthBean[size];
        }
    };
}
