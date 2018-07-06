package com.android.ql.lf.carapp.ui.fragments.order

import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.EventOrderStatusBean
import com.android.ql.lf.carapp.data.OrderBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.present.ServiceOrderPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.adapter.OrderListForMineForWaitingCalculateAdapter
import com.android.ql.lf.carapp.ui.fragments.AbstractLazyLoadFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.RxBus
import com.android.ql.lf.carapp.utils.startPhone
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 18.1.25.
 * @author lf on 18.1.25
 */
class OrderListForMineForWaitingCalculateFragment : AbstractLazyLoadFragment<OrderBean>() {

    companion object {
        fun newInstance(): OrderListForMineForWaitingCalculateFragment {
            return OrderListForMineForWaitingCalculateFragment()
        }
    }

    private val updateOrderStatusSubscription by lazy {
        RxBus.getDefault().toObservable(EventOrderStatusBean::class.java).subscribe {
            if (it.orderStatus == ServiceOrderPresent.OrderStatus.WAITING_CALCULATE.index.toInt()) {
                onLoginRefresh()
            }
        }
    }

    override fun createAdapter(): BaseQuickAdapter<OrderBean, BaseViewHolder>
            = OrderListForMineForWaitingCalculateAdapter(R.layout.adapter_order_list_for_mine_for_waiting_calculate_item_layout, mArrayList)

    override fun initView(view: View?) {
        super.initView(view)
        updateOrderStatusSubscription
        registerLoginSuccessBus()
        registerLogoutSuccessBus()
    }

    override fun getEmptyMessage() = if (!UserInfo.getInstance().isLogin) {
        resources.getString(R.string.login_notify_title)
    } else {
        "暂没有待结算订单"
    }!!

    override fun loadData() {
        if (!UserInfo.getInstance().isLogin) {
            setEmptyViewStatus()
        } else {
            isLoad = true
            setRefreshEnable(true)
            mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_MY_QORDER, RequestParamsHelper.getMyQorderParam(ServiceOrderPresent.OrderStatus.WAITING_CALCULATE.index, currentPage))
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_MY_QORDER, RequestParamsHelper.getMyQorderParam(ServiceOrderPresent.OrderStatus.WAITING_CALCULATE.index, currentPage))
    }

    override fun onLoginSuccess(userInfo: UserInfo?) {
        super.onLoginSuccess(userInfo)
        onLoginRefresh()
    }

    override fun onLogoutSuccess(userInfo: String?) {
        onLogoutRefresh()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(result as String,OrderBean::class.java)
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemChildClick(adapter, view, position)
        if (view!!.id == R.id.mBtOrderListForWaitingCalculateSubmit){
            FragmentContainerActivity
                    .from(mContext)
                    .setTitle("订单详情")
                    .setNeedNetWorking(true)
                    .setExtraBundle(bundleOf(Pair(OrderDetailForHavingWorkFragment.ORDER_BEAN_FLAG, mArrayList[position].qorder_id)))
                    .setClazz(OrderDetailForHavingWorkFragment::class.java)
                    .start()
        }else if (view.id == R.id.mTvOrderListForItemCustomPhone){
            startPhone(mArrayList[position].qorder_phone)
        }
    }

    override fun onDestroyView() {
        unsubscribe(updateOrderStatusSubscription)
        super.onDestroyView()
    }

}