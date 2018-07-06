package com.android.ql.lf.carapp.ui.fragments.order

import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.EventOrderStatusBean
import com.android.ql.lf.carapp.data.OrderBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.present.ServiceOrderPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.adapter.OrderListForMineForHavingWorkAdapter
import com.android.ql.lf.carapp.ui.fragments.AbstractLazyLoadFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.RxBus
import com.android.ql.lf.carapp.utils.startPhone
import com.android.ql.lf.carapp.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf
import org.json.JSONObject

/**
 * Created by lf on 18.1.25.
 * @author lf on 18.1.25
 */
class OrderListForMineForHavingWorkFragment : AbstractLazyLoadFragment<OrderBean>() {

    companion object {
        fun newInstance(): OrderListForMineForHavingWorkFragment {
            return OrderListForMineForHavingWorkFragment()
        }
    }

    private val updateOrderStatusSubscription by lazy {
        RxBus.getDefault().toObservable(EventOrderStatusBean::class.java).subscribe {
            if (it.orderStatus == ServiceOrderPresent.OrderStatus.HAVING_WORK.index.toInt()) {
                onLoginRefresh()
            }
        }
    }

    private val serviceOrderPresent by lazy {
        ServiceOrderPresent()
    }

    override fun initView(view: View?) {
        super.initView(view)
        updateOrderStatusSubscription
        registerLoginSuccessBus()
        registerLogoutSuccessBus()
    }

    override fun createAdapter(): BaseQuickAdapter<OrderBean, BaseViewHolder>
            = OrderListForMineForHavingWorkAdapter(R.layout.adapter_order_list_for_mine_for_having_work_item_layout, mArrayList)

    override fun getEmptyMessage() = if (!UserInfo.getInstance().isLogin) {
        resources.getString(R.string.login_notify_title)
    } else {
        "暂没有已施工订单"
    }!!

    override fun loadData() {
        if (!UserInfo.getInstance().isLogin) {
            setEmptyViewStatus()
        } else {
            isLoad = true
            setRefreshEnable(true)
            mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_MY_ROADWORK_QORDER, RequestParamsHelper.getMyRoadworkQorder(currentPage))
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_MY_ROADWORK_QORDER, RequestParamsHelper.getMyRoadworkQorder(currentPage))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在提交……")
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x1) {
            toast("提现失败，请稍后重试……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            processList(result as String, OrderBean::class.java)
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                toast("申请成功，正等待审核")
                serviceOrderPresent.updateOrderStatus(ServiceOrderPresent.OrderStatus.WAITING_CALCULATE.index.toInt())
                onRefresh()
            } else {
                toast((check.obj as JSONObject).optString("msg"))
            }
        }
    }

    override fun onLoginSuccess(userInfo: UserInfo?) {
        super.onLoginSuccess(userInfo)
        onLoginRefresh()
    }

    override fun onLogoutSuccess(userInfo: String?) {
        onLogoutRefresh()
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity
                .from(mContext)
                .setTitle("订单详情")
                .setNeedNetWorking(true)
                .setExtraBundle(bundleOf(Pair(OrderDetailForHavingWorkFragment.ORDER_BEAN_FLAG, mArrayList[position].qorder_id)))
                .setClazz(OrderDetailForHavingWorkFragment::class.java)
                .start()
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemChildClick(adapter, view, position)
        when (view!!.id) {
            R.id.mTvOrderListForItemCustomPhone -> startPhone(mArrayList[position].qorder_phone)
            R.id.mBtmOrderListForItemCash -> {
                if (mArrayList[position].qorder_token == "2") {
                    toast("当前订单正在审核中……")
                } else {
                    mPresent.getDataByPost(0x1,
                            RequestParamsHelper.ORDER_MODEL,
                            RequestParamsHelper.ACT_QORDER_DEPOSIT,
                            RequestParamsHelper.getQorderDepositParam(mArrayList[position].qorder_id))
                }
            }
            else -> {
            }
        }
    }

    override fun onDestroyView() {
        unsubscribe(updateOrderStatusSubscription)
        super.onDestroyView()
    }

}