package com.android.ql.lf.carapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lf on 18.2.22.
 *
 * @author lf on 18.2.22
 */

public class ArticleBean implements Parcelable {

    private String quiz_id;
    private String quiz_content;
    private String quiz_type;
    private String quiz_uid;
    private String quiz_time;
    private String quiz_token;
    private String quiz_title;
    private String quiz_click;
    private String quiz_video;
    private String quiz_look;
    private String quiz_replies;
    private String member_id;
    private String member_name;
    private String member_pic;
    private ArrayList<String> quiz_pic;

    public String getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(String quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuiz_content() {
        return quiz_content;
    }

    public void setQuiz_content(String quiz_content) {
        this.quiz_content = quiz_content;
    }

    public String getQuiz_type() {
        return quiz_type;
    }

    public void setQuiz_type(String quiz_type) {
        this.quiz_type = quiz_type;
    }

    public String getQuiz_uid() {
        return quiz_uid;
    }

    public void setQuiz_uid(String quiz_uid) {
        this.quiz_uid = quiz_uid;
    }

    public String getQuiz_time() {
        return quiz_time;
    }

    public void setQuiz_time(String quiz_time) {
        this.quiz_time = quiz_time;
    }

    public String getQuiz_token() {
        return quiz_token;
    }

    public void setQuiz_token(String quiz_token) {
        this.quiz_token = quiz_token;
    }

    public String getQuiz_title() {
        return quiz_title;
    }

    public void setQuiz_title(String quiz_title) {
        this.quiz_title = quiz_title;
    }

    public String getQuiz_click() {
        return quiz_click;
    }

    public void setQuiz_click(String quiz_click) {
        this.quiz_click = quiz_click;
    }

    public String getQuiz_video() {
        return quiz_video;
    }

    public void setQuiz_video(String quiz_video) {
        this.quiz_video = quiz_video;
    }

    public String getQuiz_look() {
        return quiz_look;
    }

    public void setQuiz_look(String quiz_look) {
        this.quiz_look = quiz_look;
    }

    public String getQuiz_replies() {
        return quiz_replies;
    }

    public void setQuiz_replies(String quiz_replies) {
        this.quiz_replies = quiz_replies;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_pic() {
        return member_pic;
    }

    public void setMember_pic(String member_pic) {
        this.member_pic = member_pic;
    }

    public ArrayList<String> getQuiz_pic() {
        return quiz_pic;
    }

    public void setQuiz_pic(ArrayList<String> quiz_pic) {
        this.quiz_pic = quiz_pic;
    }

    public ArticleBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.quiz_id);
        dest.writeString(this.quiz_content);
        dest.writeString(this.quiz_type);
        dest.writeString(this.quiz_uid);
        dest.writeString(this.quiz_time);
        dest.writeString(this.quiz_token);
        dest.writeString(this.quiz_title);
        dest.writeString(this.quiz_click);
        dest.writeString(this.quiz_video);
        dest.writeString(this.quiz_look);
        dest.writeString(this.quiz_replies);
        dest.writeString(this.member_id);
        dest.writeString(this.member_name);
        dest.writeString(this.member_pic);
        dest.writeStringList(this.quiz_pic);
    }

    protected ArticleBean(Parcel in) {
        this.quiz_id = in.readString();
        this.quiz_content = in.readString();
        this.quiz_type = in.readString();
        this.quiz_uid = in.readString();
        this.quiz_time = in.readString();
        this.quiz_token = in.readString();
        this.quiz_title = in.readString();
        this.quiz_click = in.readString();
        this.quiz_video = in.readString();
        this.quiz_look = in.readString();
        this.quiz_replies = in.readString();
        this.member_id = in.readString();
        this.member_name = in.readString();
        this.member_pic = in.readString();
        this.quiz_pic = in.createStringArrayList();
    }

    public static final Creator<ArticleBean> CREATOR = new Creator<ArticleBean>() {
        @Override
        public ArticleBean createFromParcel(Parcel source) {
            return new ArticleBean(source);
        }

        @Override
        public ArticleBean[] newArray(int size) {
            return new ArticleBean[size];
        }
    };
}
