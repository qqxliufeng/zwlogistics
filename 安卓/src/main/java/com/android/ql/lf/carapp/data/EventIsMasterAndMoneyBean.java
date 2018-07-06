package com.android.ql.lf.carapp.data;

/**
 * Created by lf on 18.2.12.
 *
 * @author lf on 18.2.12
 */

public class EventIsMasterAndMoneyBean {

    private boolean isMaster;
    private boolean isEnsureMoney;

    private EventIsMasterAndMoneyBean() {
    }

    private static EventIsMasterAndMoneyBean instance;

    public static EventIsMasterAndMoneyBean getInstance() {
        if (instance == null) {
            synchronized (EventIsMasterAndMoneyBean.class) {
                if (instance == null) {
                    instance = new EventIsMasterAndMoneyBean();
                }
            }
        }
        return instance;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }

    public boolean isEnsureMoney() {
        return isEnsureMoney;
    }

    public void setEnsureMoney(boolean ensureMoney) {
        isEnsureMoney = ensureMoney;
    }

}
