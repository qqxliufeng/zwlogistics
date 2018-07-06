package com.android.ql.lf.carapp.action;

import com.android.ql.lf.carapp.data.EventIsMasterAndMoneyBean;
import com.android.ql.lf.carapp.data.UserInfo;
import com.android.ql.lf.carapp.utils.RxBus;

/**
 * Created by lf on 18.2.8.
 *
 * @author lf on 18.2.8
 */

public class ViewServiceOrderAction implements IViewServiceOrderAction {

    @Override
    public void doAuthMaster() {
        RxBus.getDefault().post(EventIsMasterAndMoneyBean.getInstance());
    }

    @Override
    public void doAuthEnsureMoney() {
        RxBus.getDefault().post(EventIsMasterAndMoneyBean.getInstance());
    }


}
