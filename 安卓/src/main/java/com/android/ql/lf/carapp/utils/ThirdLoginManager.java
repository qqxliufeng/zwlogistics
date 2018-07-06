package com.android.ql.lf.carapp.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

/**
 * Created by lf on 18.2.24.
 *
 * @author lf on 18.2.24
 */

public class ThirdLoginManager {

    public static void qqLogin(Tencent tencent, Fragment fragment, IUiListener listener) {
        tencent.login(fragment, "all", listener);
    }

    public static void wxLogin(IWXAPI iwxApi) {
        iwxApi.registerApp(Constants.WX_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_ql_bs";
        iwxApi.sendReq(req);
    }


    public static QQLoginInfoBean getQQLoginInfo(JSONObject jsonObject) {
        return new Gson().fromJson(jsonObject.toString(), QQLoginInfoBean.class);
    }

    public static class QQLoginInfoBean implements Parcelable {

        private int ret;
        private String openid;
        private String access_token;
        private String pay_token;
        private int expires_in;
        private String pf;
        private String pfkey;
        private String msg;
        private int login_cost;
        private int query_authority_cost;
        private int authority_cost;
        private long expires_time;

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getPay_token() {
            return pay_token;
        }

        public void setPay_token(String pay_token) {
            this.pay_token = pay_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getPf() {
            return pf;
        }

        public void setPf(String pf) {
            this.pf = pf;
        }

        public String getPfkey() {
            return pfkey;
        }

        public void setPfkey(String pfkey) {
            this.pfkey = pfkey;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getLogin_cost() {
            return login_cost;
        }

        public void setLogin_cost(int login_cost) {
            this.login_cost = login_cost;
        }

        public int getQuery_authority_cost() {
            return query_authority_cost;
        }

        public void setQuery_authority_cost(int query_authority_cost) {
            this.query_authority_cost = query_authority_cost;
        }

        public int getAuthority_cost() {
            return authority_cost;
        }

        public void setAuthority_cost(int authority_cost) {
            this.authority_cost = authority_cost;
        }

        public long getExpires_time() {
            return expires_time;
        }

        public void setExpires_time(long expires_time) {
            this.expires_time = expires_time;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.ret);
            dest.writeString(this.openid);
            dest.writeString(this.access_token);
            dest.writeString(this.pay_token);
            dest.writeInt(this.expires_in);
            dest.writeString(this.pf);
            dest.writeString(this.pfkey);
            dest.writeString(this.msg);
            dest.writeInt(this.login_cost);
            dest.writeInt(this.query_authority_cost);
            dest.writeInt(this.authority_cost);
            dest.writeLong(this.expires_time);
        }

        public QQLoginInfoBean() {
        }

        protected QQLoginInfoBean(Parcel in) {
            this.ret = in.readInt();
            this.openid = in.readString();
            this.access_token = in.readString();
            this.pay_token = in.readString();
            this.expires_in = in.readInt();
            this.pf = in.readString();
            this.pfkey = in.readString();
            this.msg = in.readString();
            this.login_cost = in.readInt();
            this.query_authority_cost = in.readInt();
            this.authority_cost = in.readInt();
            this.expires_time = in.readLong();
        }

        public static final Parcelable.Creator<QQLoginInfoBean> CREATOR = new Parcelable.Creator<QQLoginInfoBean>() {
            @Override
            public QQLoginInfoBean createFromParcel(Parcel source) {
                return new QQLoginInfoBean(source);
            }

            @Override
            public QQLoginInfoBean[] newArray(int size) {
                return new QQLoginInfoBean[size];
            }
        };
    }

    public static class WXUserInfo implements Parcelable {

        private String openid;
        private String nickname;
        private int sex;
        private String language;
        private String city;
        private String province;
        private String country;
        private String headimgurl;
        private String unionid;

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.openid);
            dest.writeString(this.nickname);
            dest.writeInt(this.sex);
            dest.writeString(this.language);
            dest.writeString(this.city);
            dest.writeString(this.province);
            dest.writeString(this.country);
            dest.writeString(this.headimgurl);
            dest.writeString(this.unionid);
        }

        public WXUserInfo() {
        }

        protected WXUserInfo(Parcel in) {
            this.openid = in.readString();
            this.nickname = in.readString();
            this.sex = in.readInt();
            this.language = in.readString();
            this.city = in.readString();
            this.province = in.readString();
            this.country = in.readString();
            this.headimgurl = in.readString();
            this.unionid = in.readString();
        }

        public static final Parcelable.Creator<WXUserInfo> CREATOR = new Parcelable.Creator<WXUserInfo>() {
            @Override
            public WXUserInfo createFromParcel(Parcel source) {
                return new WXUserInfo(source);
            }

            @Override
            public WXUserInfo[] newArray(int size) {
                return new WXUserInfo[size];
            }
        };
    }
}
