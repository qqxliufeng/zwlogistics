package com.android.ql.lf.carapp.data;

import java.util.ArrayList;

/**
 * Created by lf on 2017/12/8 0008.
 *
 * @author lf on 2017/12/8 0008
 */

public class CommentForGoodsBean {
    private String comment_id;
    private String comment_uid;
    private ArrayList<String> comment_pic;
    private String comment_content;
    private String comment_time;
    private String comment_f;
    private String member_name;
    private String member_pic;

    public String getComment_f() {
        return comment_f;
    }

    public void setComment_f(String comment_f) {
        this.comment_f = comment_f;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment_uid() {
        return comment_uid;
    }

    public void setComment_uid(String comment_uid) {
        this.comment_uid = comment_uid;
    }

    public ArrayList<String> getComment_pic() {
        return comment_pic;
    }

    public void setComment_pic(ArrayList<String> comment_pic) {
        this.comment_pic = comment_pic;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
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
}
