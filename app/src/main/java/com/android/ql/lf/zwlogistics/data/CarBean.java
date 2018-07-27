package com.android.ql.lf.zwlogistics.data;

import java.util.ArrayList;

public class CarBean {

    private boolean isManagerMode = false;
    private boolean isSelect = false;


    private String vehicle_id;
    private String vehicle_uid;
    private String vehicle_driving;
    private String vehicle_run;
    private String vehicle_number;
    private String vehicle_type;
    private String vehicle_length;
    private String vehicle_weight;
    private String vehicle_name;
    private String vehicle_year;
    private int vehicle_is_state;
    private String vehicle_is_show;
    private String vehicle_times;
    private String vehicle_content;
    private int vehicle_biao = 1;


    public ArrayList<String> getImageList(){
        if (vehicle_driving == null || vehicle_run == null || vehicle_number == null){
            return null;
        }
        ArrayList<String> imageList = new ArrayList<>();
        imageList.add(vehicle_driving);
        imageList.add(vehicle_run);
        imageList.add(vehicle_number);
        return imageList;
    }



    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_uid() {
        return vehicle_uid;
    }

    public void setVehicle_uid(String vehicle_uid) {
        this.vehicle_uid = vehicle_uid;
    }

    public String getVehicle_driving() {
        return vehicle_driving;
    }

    public void setVehicle_driving(String vehicle_driving) {
        this.vehicle_driving = vehicle_driving;
    }

    public String getVehicle_run() {
        return vehicle_run;
    }

    public void setVehicle_run(String vehicle_run) {
        this.vehicle_run = vehicle_run;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setVehicle_number(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_length() {
        return vehicle_length;
    }

    public void setVehicle_length(String vehicle_length) {
        this.vehicle_length = vehicle_length;
    }

    public String getVehicle_weight() {
        return vehicle_weight;
    }

    public void setVehicle_weight(String vehicle_weight) {
        this.vehicle_weight = vehicle_weight;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getVehicle_year() {
        return vehicle_year;
    }

    public void setVehicle_year(String vehicle_year) {
        this.vehicle_year = vehicle_year;
    }

    public int getVehicle_is_state() {
        return vehicle_is_state;
    }

    public void setVehicle_is_state(int vehicle_is_state) {
        this.vehicle_is_state = vehicle_is_state;
    }

    public String getVehicle_is_show() {
        return vehicle_is_show;
    }

    public void setVehicle_is_show(String vehicle_is_show) {
        this.vehicle_is_show = vehicle_is_show;
    }

    public String getVehicle_times() {
        return vehicle_times;
    }

    public void setVehicle_times(String vehicle_times) {
        this.vehicle_times = vehicle_times;
    }

    public boolean isManagerMode() {
        return isManagerMode;
    }

    public void setManagerMode(boolean managerMode) {
        isManagerMode = managerMode;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getVehicle_content() {
        return vehicle_content;
    }

    public void setVehicle_content(String vehicle_content) {
        this.vehicle_content = vehicle_content;
    }

    public int getVehicle_biao() {
        return vehicle_biao;
    }

    public void setVehicle_biao(int vehicle_biao) {
        this.vehicle_biao = vehicle_biao;
    }
}
