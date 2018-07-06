package com.android.ql.lf.carapp.data;

import java.util.ArrayList;

/**
 * Created by lf on 2017/12/1 0001.
 *
 * @author lf on 2017/12/1 0001
 */

public class ClassifyBean {

    private String classify_id;
    private String classify_title;
    private String classify_content;
    private String classify_pid;
    private String classify_path;
    private String classify_token;
    private String classify_url;
    private String classify_pic;
    private String classify_sort;
    private String classify_url1;
    private String classify_time;
    private String classify_sign;
    private int mImageRes = 0;
    private ArrayList<ClassifySubItemBean> sub;

    private boolean isChecked = false;

    public void setImageRes(int mImageRes) {
        this.mImageRes = mImageRes;
    }

    public int getImageRes() {
        return mImageRes;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getClassify_id() {
        return classify_id;
    }

    public void setClassify_id(String classify_id) {
        this.classify_id = classify_id;
    }

    public String getClassify_title() {
        return classify_title;
    }

    public void setClassify_title(String classify_title) {
        this.classify_title = classify_title;
    }

    public String getClassify_content() {
        return classify_content;
    }

    public void setClassify_content(String classify_content) {
        this.classify_content = classify_content;
    }

    public String getClassify_pid() {
        return classify_pid;
    }

    public void setClassify_pid(String classify_pid) {
        this.classify_pid = classify_pid;
    }

    public String getClassify_path() {
        return classify_path;
    }

    public void setClassify_path(String classify_path) {
        this.classify_path = classify_path;
    }

    public String getClassify_token() {
        return classify_token;
    }

    public void setClassify_token(String classify_token) {
        this.classify_token = classify_token;
    }

    public String getClassify_url() {
        return classify_url;
    }

    public void setClassify_url(String classify_url) {
        this.classify_url = classify_url;
    }

    public String getClassify_pic() {
        return classify_pic;
    }

    public void setClassify_pic(String classify_pic) {
        this.classify_pic = classify_pic;
    }

    public String getClassify_sort() {
        return classify_sort;
    }

    public void setClassify_sort(String classify_sort) {
        this.classify_sort = classify_sort;
    }

    public String getClassify_url1() {
        return classify_url1;
    }

    public void setClassify_url1(String classify_url1) {
        this.classify_url1 = classify_url1;
    }

    public String getClassify_time() {
        return classify_time;
    }

    public void setClassify_time(String classify_time) {
        this.classify_time = classify_time;
    }

    public String getClassify_sign() {
        return classify_sign;
    }

    public void setClassify_sign(String classify_sign) {
        this.classify_sign = classify_sign;
    }

    public ArrayList<ClassifySubItemBean> getSub() {
        return sub;
    }

    public void setSub(ArrayList<ClassifySubItemBean> sub) {
        this.sub = sub;
    }

    public static class ClassifySubItemBean {
        private String classify_id;
        private String classify_title;
        private String classify_content;
        private String classify_pid;
        private String classify_path;
        private String classify_token;
        private String classify_url;
        private String classify_pic;
        private String classify_sort;
        private String classify_url1;
        private String classify_time;
        private String classify_sign;

        public String getClassify_id() {
            return classify_id;
        }

        public void setClassify_id(String classify_id) {
            this.classify_id = classify_id;
        }

        public String getClassify_title() {
            return classify_title;
        }

        public void setClassify_title(String classify_title) {
            this.classify_title = classify_title;
        }

        public String getClassify_content() {
            return classify_content;
        }

        public void setClassify_content(String classify_content) {
            this.classify_content = classify_content;
        }

        public String getClassify_pid() {
            return classify_pid;
        }

        public void setClassify_pid(String classify_pid) {
            this.classify_pid = classify_pid;
        }

        public String getClassify_path() {
            return classify_path;
        }

        public void setClassify_path(String classify_path) {
            this.classify_path = classify_path;
        }

        public String getClassify_token() {
            return classify_token;
        }

        public void setClassify_token(String classify_token) {
            this.classify_token = classify_token;
        }

        public String getClassify_url() {
            return classify_url;
        }

        public void setClassify_url(String classify_url) {
            this.classify_url = classify_url;
        }

        public String getClassify_pic() {
            return classify_pic;
        }

        public void setClassify_pic(String classify_pic) {
            this.classify_pic = classify_pic;
        }

        public String getClassify_sort() {
            return classify_sort;
        }

        public void setClassify_sort(String classify_sort) {
            this.classify_sort = classify_sort;
        }

        public String getClassify_url1() {
            return classify_url1;
        }

        public void setClassify_url1(String classify_url1) {
            this.classify_url1 = classify_url1;
        }

        public String getClassify_time() {
            return classify_time;
        }

        public void setClassify_time(String classify_time) {
            this.classify_time = classify_time;
        }

        public String getClassify_sign() {
            return classify_sign;
        }

        public void setClassify_sign(String classify_sign) {
            this.classify_sign = classify_sign;
        }
    }
}