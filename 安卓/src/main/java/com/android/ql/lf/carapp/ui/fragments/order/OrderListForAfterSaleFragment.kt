package com.android.ql.lf.carapp.ui.fragments.order

import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.EventOrderStatusBean
import com.android.ql.lf.carapp.data.OrderBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.present.ServiceOrderPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.adapter.OrderListForAfterSaleAdapter
import com.android.ql.lf.carapp.ui.adapter.OrderListForMineForWaitingWorkAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.RxBus
import com.android.ql.lf.carapp.utils.startPhone
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 18.1.27.
 * @author lf on 18.1.27
 */
class OrderListForAfterSaleFragment : BaseRecyclerViewFragment<OrderBean>() {

    companion object {
        fun newInstance(): OrderListForAfterSaleFragment {
            return OrderListForAfterSaleFragment()
        }
    }

    override fun initView(view: View?) {
        super.initView(view)
        registerLoginSuccessBus()
        registerLogoutSuccessBus()
        updateOrderStatusSubscription
    }

    private val updateOrderStatusSubscription by lazy {
        RxBus.getDefault().toObservable(EventOrderStatusBean::class.java).subscribe {
            if (it.orderStatus == ServiceOrderPresent.OrderStatus.WAITING_WORK.index.toInt()) {
                onPostRefresh()
            }
        }
    }

    override fun createAdapter(): BaseQuickAdapter<OrderBean, BaseViewHolder>
            = OrderListForMineForWaitingWorkAdapter(R.layout.adapter_order_list_for_mine_for_waiting_work_item_layout, mArrayList)

    override fun getEmptyMessage() = if (!UserInfo.getInstance().isLogin) {
        resources.getString(R.string.login_notify_title)
    } else {
        "暂没有售后订单"
    }!!

    override fun onRefresh() {
        super.onRefresh()
        if (!UserInfo.getInstance().isLogin) {
            setEmptyViewStatus()
        } else {
            setRefreshEnable(true)
            mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_MY_QORDER, RequestParamsHelper.getMyQorderParam(ServiceOrderPresent.OrderStatus.BACK_MONEY.index, currentPage))
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_MY_QORDER, RequestParamsHelper.getMyQorderParam(ServiceOrderPresent.OrderStatus.BACK_MONEY.index, currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(result as String,OrderBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity
                .from(mContext)
                .setTitle("订单详情")
                .setNeedNetWorking(true)
                .setExtraBundle(bundleOf(Pair(OrderDetailForWaitingWorkFragment.ORDER_BEAN_FLAG, mArrayList[position].qorder_id)))
                .setClazz(OrderDetailForWaitingWorkFragment::class.java)
                .start()
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemChildClick(adapter, view, position)
        when (view!!.id) {
            R.id.mBtOrderListForWaitingWorkCamera -> {
                FragmentContainerActivity
                        .from(mContext)
                        .setTitle("拍照")
                        .setNeedNetWorking(true)
                        .setClazz(OrderImageUpLoadFragment::class.java)
                        .setExtraBundle(bundleOf(Pair("oid", mArrayList[position].qorder_id)))
                        .start()
            }
            R.id.mTvOrderListForItemCustomPhone -> {
                startPhone(mArrayList[position].qorder_phone)
            }
        }
    }

    override fun onLogoutSuccess(logout: String?) {
        super.onLogoutSuccess(logout)
        onPostRefresh()
    }

    override fun onLoginSuccess(userInfo: UserInfo?) {
        super.onLoginSuccess(userInfo)
        onPostRefresh()
    }

    override fun onDestroyView() {
        unsubscribe(updateOrderStatusSubscription)
        super.onDestroyView()
    }

}