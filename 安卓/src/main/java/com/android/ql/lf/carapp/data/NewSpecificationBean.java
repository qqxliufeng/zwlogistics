package com.android.ql.lf.carapp.data;

import java.util.ArrayList;

/**
 * Created by lf on 18.5.15.
 *
 * @author lf on 18.5.15
 */

public class NewSpecificationBean {
    private String title;
    private ArrayList<String> item;
    private ArrayList<String> item_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getItem() {
        return item;
    }

    public void setItem(ArrayList<String> item) {
        this.item = item;
    }

    public ArrayList<String> getItem_id() {
        return item_id;
    }

    public void setItem_id(ArrayList<String> item_id) {
        this.item_id = item_id;
    }
}
