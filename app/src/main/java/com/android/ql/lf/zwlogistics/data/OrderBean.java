package com.android.ql.lf.zwlogistics.data;

public class OrderBean {

    public enum OrderStatus{
//        订单状态 1 = 竞标中 2 = 未中标 3 = 中标 4 = 开始 5 = 未开始 6 = 已完成"

        TENDERING(1,"竞标中"),
        WEI_TENDER(2,"未中标"),
        TENDERED(3,"中标成功"),
        TENDER_START(4,"已开始"),
        TENDER_WEI_START(5,"未开始"),
        TENDER_COMPLEMENT(6,"已完成");

        public int statusCode = -1;
        public String statusDes = "";

        OrderStatus(int statusCode,String statusDes){
            this.statusCode = statusCode;
            this.statusDes = statusDes;
        }
    }


    private String need_id;
    private String need_zhong;
    private String need_ti;
    private String need_xie;
    private String need_type_pay;
    private String need_f_str;
    private String need_m_str;
    private String need_pic;

    private String need_order;
    private String need_time_top;
    private String need_time_end;
    private String need_user_cou;
    private String need_f_site;
    private String need_t_site;
    private String need_m_site;
    private String need_length_top;
    private String need_model;
    private String need_state;
    private String need_is_show;
    private String need_times;
    private String need_content;

    private String need_user_id;
    private String need_user_phone;
    private String need_user_sum;
    private String need_user_model;
    private String need_user_content;
    private String need_user_is_show;
    private String need_user_times;
    private String need_user_pid;
    private String need_user_uid;

    public String getNeed_id() {
        return need_id;
    }

    public void setNeed_id(String need_id) {
        this.need_id = need_id;
    }

    public String getNeed_zhong() {
        return need_zhong;
    }

    public void setNeed_zhong(String need_zhong) {
        this.need_zhong = need_zhong;
    }

    public String getNeed_ti() {
        return need_ti;
    }

    public void setNeed_ti(String need_ti) {
        this.need_ti = need_ti;
    }

    public String getNeed_xie() {
        return need_xie;
    }

    public void setNeed_xie(String need_xie) {
        this.need_xie = need_xie;
    }

    public String getNeed_type_pay() {
        return need_type_pay;
    }

    public void setNeed_type_pay(String need_type_pay) {
        this.need_type_pay = need_type_pay;
    }

    public String getNeed_f_str() {
        return need_f_str;
    }

    public void setNeed_f_str(String need_f_str) {
        this.need_f_str = need_f_str;
    }

    public String getNeed_m_str() {
        return need_m_str;
    }

    public void setNeed_m_str(String need_m_str) {
        this.need_m_str = need_m_str;
    }

    public String getNeed_pic() {
        return need_pic;
    }

    public void setNeed_pic(String need_pic) {
        this.need_pic = need_pic;
    }

    public String getNeed_order() {
        return need_order;
    }

    public void setNeed_order(String need_order) {
        this.need_order = need_order;
    }

    public String getNeed_time_top() {
        return need_time_top;
    }

    public void setNeed_time_top(String need_time_top) {
        this.need_time_top = need_time_top;
    }

    public String getNeed_time_end() {
        return need_time_end;
    }

    public void setNeed_time_end(String need_time_end) {
        this.need_time_end = need_time_end;
    }

    public String getNeed_user_cou() {
        return need_user_cou;
    }

    public void setNeed_user_cou(String need_user_cou) {
        this.need_user_cou = need_user_cou;
    }

    public String getNeed_f_site() {
        return need_f_site;
    }

    public void setNeed_f_site(String need_f_site) {
        this.need_f_site = need_f_site;
    }

    public String getNeed_t_site() {
        return need_t_site;
    }

    public void setNeed_t_site(String need_t_site) {
        this.need_t_site = need_t_site;
    }

    public String getNeed_m_site() {
        return need_m_site;
    }

    public void setNeed_m_site(String need_m_site) {
        this.need_m_site = need_m_site;
    }

    public String getNeed_length_top() {
        return need_length_top;
    }

    public void setNeed_length_top(String need_length_top) {
        this.need_length_top = need_length_top;
    }

    public String getNeed_model() {
        return need_model;
    }

    public void setNeed_model(String need_model) {
        this.need_model = need_model;
    }

    public String getNeed_state() {
        return need_state;
    }

    public void setNeed_state(String need_state) {
        this.need_state = need_state;
    }

    public String getNeed_is_show() {
        return need_is_show;
    }

    public void setNeed_is_show(String need_is_show) {
        this.need_is_show = need_is_show;
    }

    public String getNeed_times() {
        return need_times;
    }

    public void setNeed_times(String need_times) {
        this.need_times = need_times;
    }

    public String getNeed_content() {
        return need_content;
    }

    public void setNeed_content(String need_content) {
        this.need_content = need_content;
    }

    public String getNeed_user_id() {
        return need_user_id;
    }

    public void setNeed_user_id(String need_user_id) {
        this.need_user_id = need_user_id;
    }

    public String getNeed_user_phone() {
        return need_user_phone;
    }

    public void setNeed_user_phone(String need_user_phone) {
        this.need_user_phone = need_user_phone;
    }

    public String getNeed_user_sum() {
        return need_user_sum;
    }

    public void setNeed_user_sum(String need_user_sum) {
        this.need_user_sum = need_user_sum;
    }

    public String getNeed_user_model() {
        return need_user_model;
    }

    public void setNeed_user_model(String need_user_model) {
        this.need_user_model = need_user_model;
    }

    public String getNeed_user_content() {
        return need_user_content;
    }

    public void setNeed_user_content(String need_user_content) {
        this.need_user_content = need_user_content;
    }

    public String getNeed_user_is_show() {
        return need_user_is_show;
    }

    public void setNeed_user_is_show(String need_user_is_show) {
        this.need_user_is_show = need_user_is_show;
    }

    public String getNeed_user_times() {
        return need_user_times;
    }

    public void setNeed_user_times(String need_user_times) {
        this.need_user_times = need_user_times;
    }

    public String getNeed_user_pid() {
        return need_user_pid;
    }

    public void setNeed_user_pid(String need_user_pid) {
        this.need_user_pid = need_user_pid;
    }

    public String getNeed_user_uid() {
        return need_user_uid;
    }

    public void setNeed_user_uid(String need_user_uid) {
        this.need_user_uid = need_user_uid;
    }
}
