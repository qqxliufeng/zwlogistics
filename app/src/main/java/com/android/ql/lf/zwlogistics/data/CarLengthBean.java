package com.android.ql.lf.zwlogistics.data;

public class CarLengthBean {

    private int type = 0;
    private boolean isSelect = false;
    private String length_id;
    private String length_name;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getLength_id() {
        return length_id;
    }

    public void setLength_id(String length_id) {
        this.length_id = length_id;
    }

    public String getLength_name() {

        return length_name;
    }

    public void setLength_name(String length_name) {
        this.length_name = length_name;
    }
}
