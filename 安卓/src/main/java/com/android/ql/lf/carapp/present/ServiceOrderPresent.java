package com.android.ql.lf.carapp.present;

import com.android.ql.lf.carapp.action.IViewServiceOrderAction;
import com.android.ql.lf.carapp.action.ViewServiceOrderAction;
import com.android.ql.lf.carapp.data.EventOrderStatusBean;
import com.android.ql.lf.carapp.data.UserInfo;
import com.android.ql.lf.carapp.utils.RxBus;

/**
 * Created by lf on 18.2.8.
 *
 * @author lf on 18.2.8
 */

public class ServiceOrderPresent {

    private IViewServiceOrderAction serviceOrderAction;

    public ServiceOrderPresent() {
        serviceOrderAction = new ViewServiceOrderAction();
    }

    public void doAuthMaster() {
        UserInfo.getInstance().setMemberIsMaster("1");
        serviceOrderAction.doAuthMaster();
    }

    public void doAuthEnsureMoney() {
        UserInfo.getInstance().setMemberIsEnsureMoney("1");
        serviceOrderAction.doAuthEnsureMoney();
    }

    public void updateOrderStatus(int status) {
        RxBus.getDefault().post(new EventOrderStatusBean(status));
    }

    public enum OrderStatus {
        //0  待抢单 1 待施工  2 待确认  3  已施工 4 待结算 5  已结算  6  退款中 7 已退款  8 已过期 token  为空 是全部订单
        QD("0", "待抢单"),
        WAITING_WORK("1", "待施工"),
        WAITING_CONFIRM("2", "订单待确认"),
        HAVING_WORK("3", "已施工"),
        WAITING_CALCULATE("4", "待结算"),
        HAVING_CALCULATE("5", "已结算"),
        BACK_MONEY("6", "售后"),
        HAD_BACK_MONEY("7", "已退款"),
        HAD_EXPIRE("8", "已过期"),
        ALL("-1", "");
        public String index;
        public String description;

        private OrderStatus(String index, String description) {
            this.index = index;
            this.description = description;
        }

        public static String getDescriptionByIndex(String index) {
            for (OrderStatus status : OrderStatus.values()) {
                if (index.equals(status.index)) {
                    return status.description;
                }
            }
            return "";
        }
    }
}
