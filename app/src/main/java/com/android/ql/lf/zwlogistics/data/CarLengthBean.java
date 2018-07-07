package com.android.ql.lf.zwlogistics.data;

public class CarLengthBean {

    private int type = 0;
    private boolean isSelect = false;
    private String name = "";

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
