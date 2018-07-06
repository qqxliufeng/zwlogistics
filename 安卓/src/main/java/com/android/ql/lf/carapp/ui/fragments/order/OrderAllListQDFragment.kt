package com.android.ql.lf.carapp.ui.fragments.order

import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.EventIsMasterAndMoneyBean
import com.android.ql.lf.carapp.data.OrderBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.present.ServiceOrderPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.adapter.OrderListForQDAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.user.LoginFragment
import com.android.ql.lf.carapp.ui.fragments.user.mine.MineApplyMasterInfoSubmitFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.RxBus
import com.android.ql.lf.carapp.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_order_for_qd_layout.*

/**
 * Created by lf on 18.3.9.
 * @author lf on 18.3.9
 */
class OrderAllListQDFragment :BaseRecyclerViewFragment<OrderBean>(){

    private val serviceOrderPresent by lazy {
        ServiceOrderPresent()
    }

    private var currentOrderBean: OrderBean? = null


    //接收是否谁为师傅和是否交纳保证金的事件
    private val masterAndMoneySubscription by lazy {
        RxBus.getDefault().toObservable(EventIsMasterAndMoneyBean::class.java).subscribe {
            showNotify()
        }
    }

    override fun createAdapter(): BaseQuickAdapter<OrderBean, BaseViewHolder>
            = OrderListForQDAdapter(mContext, R.layout.adapter_order_list_for_qd_item_layout, mArrayList)

    override fun getEmptyLayoutId() = R.layout.layout_order_list_empty

    override fun getEmptyMessage(): String {
        return "这里还没有订单呢！"
    }

    override fun getLayoutId() = R.layout.fragment_order_for_qd_layout

    override fun initView(view: View?) {
        super.initView(view)
        masterAndMoneySubscription
        showNotify()
    }

    private fun showNotify() {
        if (UserInfo.getInstance().isLogin) {
            if (UserInfo.getInstance().isMaster) {
                mTvOrderQDNotify.visibility = View.GONE
                mBaseAdapter.notifyDataSetChanged()
                return
            }
            mTvOrderQDNotify.visibility = View.VISIBLE
            when (UserInfo.getInstance().authenticationStatus) {
                0 -> {
                    mTvOrderQDNotify.text = "当前帐号正在认证中……"
                    mTvOrderQDNotify.isEnabled = false
                }
                2 -> {
                    mTvOrderQDNotify.text = "审核失败，请重新提交资料……"
                    mTvOrderQDNotify.isEnabled = true
                    mTvOrderQDNotify.setOnClickListener {
                        FragmentContainerActivity.from(mContext).setTitle("申请成为师傅").setNeedNetWorking(true).setClazz(MineApplyMasterInfoSubmitFragment::class.java).start()
                    }
                }
                3 -> {
                    mTvOrderQDNotify.text = "当前帐号未认证，暂无法接单，请立即认证"
                    mTvOrderQDNotify.isEnabled = true
                    mTvOrderQDNotify.setOnClickListener {
                        FragmentContainerActivity.from(mContext).setTitle("申请成为师傅").setNeedNetWorking(true).setClazz(MineApplyMasterInfoSubmitFragment::class.java).start()
                    }
                }
            }
            mBaseAdapter.notifyDataSetChanged()
        } else {
            mTvOrderQDNotify.visibility = View.VISIBLE
            mTvOrderQDNotify.text = "登录后显示订单"
            mTvOrderQDNotify.setOnClickListener {
                UserInfo.loginToken = OrderListForQDFragment.RECEIVER_ORDER_FLAG
                LoginFragment.startLogin(mContext)
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_QORDER, RequestParamsHelper.getQorderParam(page = currentPage, location = if (UserInfo.getInstance().shopInfo != null) {
            UserInfo.getInstance().shopInfo.shop_address
        } else {
            ""
        }))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_QORDER, RequestParamsHelper.getQorderParam(page = currentPage, location = if (UserInfo.getInstance().shopInfo != null) {
            UserInfo.getInstance().shopInfo.shop_address
        } else {
            ""
        }))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在抢单……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            processList(result as String, OrderBean::class.java)
            if (!mArrayList.isEmpty()) {
                //校对时间
                val currentTime = System.currentTimeMillis()
                mArrayList.forEach {
                    it.endTime = currentTime + (it.qorder_remaining_time * 1000)
                }
            }
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                toast("恭喜，抢单成功，祝您工作愉快！")
                serviceOrderPresent.updateOrderStatus(ServiceOrderPresent.OrderStatus.WAITING_WORK.index.toInt())
            } else {
                toast("该订单已被抢了~~")
            }
            onPostRefresh()
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x1) {
            toast("抢单失败")
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (view!!.id == R.id.mBtOrderListForQDItem) {
            when (UserInfo.getInstance().authenticationStatus) {
                0 -> {
                    mTvOrderQDNotify.text = "当前帐号正在认证中……"
                }
                1 -> {
                    if (UserInfo.getInstance().isMaster) {
                        currentOrderBean = mArrayList[position]
                        mPresent.getDataByPost(0x1, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_ORDER_RECEIVING, RequestParamsHelper.getOrderReceivingParam(currentOrderBean!!.qorder_id))
                    }
                }
                2 -> {
                    mTvOrderQDNotify.text = "审核失败，请重新提交资料……"
                    mTvOrderQDNotify.setOnClickListener {
                        FragmentContainerActivity.from(mContext).setTitle("申请成为师傅").setNeedNetWorking(true).setClazz(MineApplyMasterInfoSubmitFragment::class.java).start()
                    }
                }
                3 -> {
                    mTvOrderQDNotify.text = "当前帐号未认证，暂无法接单，请立即认证"
                    mTvOrderQDNotify.setOnClickListener {
                        FragmentContainerActivity.from(mContext).setTitle("申请成为师傅").setNeedNetWorking(true).setClazz(MineApplyMasterInfoSubmitFragment::class.java).start()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        unsubscribe(masterAndMoneySubscription)
        super.onDestroyView()
    }

}