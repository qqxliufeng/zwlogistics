package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.*
import com.android.ql.lf.carapp.present.MallOrderPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.adapter.MainMallOrderItemAdapter
import com.android.ql.lf.carapp.ui.fragments.AbstractLazyLoadFragment
import com.android.ql.lf.carapp.ui.fragments.mall.normal.ExpressInfoFragment
import com.android.ql.lf.carapp.ui.fragments.mall.normal.RefundFragment
import com.android.ql.lf.carapp.ui.fragments.mall.order.OrderCommentSubmitFragment
import com.android.ql.lf.carapp.ui.fragments.mall.order.OrderInfoFragment
import com.android.ql.lf.carapp.ui.fragments.mall.order.OrderPayResultFragment
import com.android.ql.lf.carapp.ui.fragments.mall.order.OrderSubmitFragment
import com.android.ql.lf.carapp.ui.views.SelectPayTypeView
import com.android.ql.lf.carapp.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf
import org.json.JSONObject

/**
 * Created by lf on 18.3.27.
 * @author lf on 18.3.27
 */
class MainMallOrderItemFragment : AbstractLazyLoadFragment<MallSaleOrderBean>() {

    companion object {
        val SEARCH_PARAM_FLAG = "search_param_flag"
        val REFRESH_ORDER_FLAG = "refresh order"


        fun newInstance(bundle: Bundle): MainMallOrderItemFragment {
            val fragment = MainMallOrderItemFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var currentOrder: MallSaleOrderBean? = null

    private var payFlag = false

    private var payType: String = SelectPayTypeView.WX_PAY

    private var orderType: String = MallOrderPresent.MallOrderStatus.WAITING_FOR_MONEY.index

    private val handle by lazy {
        @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                when (msg!!.what) {
                    PayManager.SDK_PAY_FLAG -> {
                        val payResult = PayResult(msg.obj as Map<String, String>)
                        val resultInfo = payResult.result// 同步返回需要验证的信息
                        val resultStatus = payResult.resultStatus
                        val bundle = Bundle()
                        payFlag = if (TextUtils.equals(resultStatus, "9000")) {
                            //支付成功
                            bundle.putInt(OrderPayResultFragment.PAY_CODE_FLAG, OrderPayResultFragment.PAY_SUCCESS_CODE)
                            true
                        } else {
                            //支付失败
                            bundle.putInt(OrderPayResultFragment.PAY_CODE_FLAG, OrderPayResultFragment.PAY_FAIL_CODE)
                            false
                        }
//                    OrderPresent.notifyRefreshOrderNum()
                        FragmentContainerActivity
                                .from(mContext)
                                .setTitle("支付结果")
                                .setNeedNetWorking(true)
                                .setExtraBundle(bundle)
                                .setClazz(OrderPayResultFragment::class.java)
                                .start()
                        if (payFlag) {
                            finish()
                        }
                    }
                }
            }
        }
    }

    private val searchParamBean by lazy {
        arguments.classLoader = this@MainMallOrderItemFragment.javaClass.classLoader
        arguments.getParcelable<SearchParamBean>(SEARCH_PARAM_FLAG)
    }

    private val postParam by lazy {
        val apiParam = RequestParamsHelper.getWithPageParams(currentPage)
        if (searchParamBean.params != null && !searchParamBean.params.isEmpty()) {
            orderType = searchParamBean.params["status"]!!
            searchParamBean.params.forEach {
                apiParam.put(it.key, it.value)
            }
        }
        apiParam
    }

    private val orderSubscription by lazy {
        RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (it.isRefresh && it.any == REFRESH_ORDER_FLAG) {
                when (orderType) {
                    MallOrderPresent.MallOrderStatus.WAITING_FOR_MONEY.index,
                    MallOrderPresent.MallOrderStatus.WAITING_FOR_RECEIVER.index,
                    MallOrderPresent.MallOrderStatus.WAITING_FOR_SEND.index,
                    MallOrderPresent.MallOrderStatus.WAITING_FOR_EVALUATE.index,
                    MallOrderPresent.MallOrderStatus.MALL_ORDER_APPLY_BACK.index -> {
                        onRefresh()
                    }
                }
            }
        }
    }

    private val paySubscription by lazy {
        RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (it.isRefresh && it.any == OrderSubmitFragment.PAY_MALL_ORDER_FLAG) {
                finish()
            }
        }
    }

    override fun initView(view: View?) {
        super.initView(view)
        orderSubscription
        paySubscription
    }

    override fun createAdapter(): BaseQuickAdapter<MallSaleOrderBean, BaseViewHolder> =
            MainMallOrderItemAdapter(R.layout.adapter_main_mall_order_item_layout, mArrayList)

    override fun loadData() {
        isLoad = true
        mPresent.getDataByPost(0x0, searchParamBean.model, searchParamBean.act, postParam.addParam("page", currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, searchParamBean.model, searchParamBean.act, postParam.addParam("page", currentPage))
    }

    override fun getEmptyMessage() = "暂无订单哦~~"

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        when (requestID) {
            0x1 -> getFastProgressDialog("正在取消订单……")
            0x2 -> getFastProgressDialog("正在支付……")
            0x4 -> getFastProgressDialog("正在收货……")
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        when (requestID) {
            0x1 -> toast("订单取消失败，请重试……")
            0x2 -> toast("订单支付失败，请重试……")
            0x4 -> toast("收货失败，请重试……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when (requestID) {
            0x0 -> {
                processList(result as String, MallSaleOrderBean::class.java)
            }
            0x1 -> {
                val check = checkResultCode(result)
                if (check != null) {
                    if (check.code == SUCCESS_CODE) {
                        toast("订单取消成功")
                        onPostRefresh()
                    } else {
                        toast((check.obj as JSONObject).optString(MSG_FLAG))
                    }
                }
            }
            0x2 -> {
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    MallOrderPresent.onOrderPaySuccess(mContext, (check.obj as JSONObject), payType, currentOrder!!.order_id, handle)
                } else {
                    toast((check.obj as JSONObject).optString(MSG_FLAG))
                }
            }
            0x4 -> {
                val check = checkResultCode(result)
                if (check != null) {
                    if (check.code == SUCCESS_CODE) {
                        toast("收货成功")
                        onPostRefresh()
                    }
                    toast((check.obj as JSONObject).optString(MSG_FLAG))
                }
            }
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        currentOrder = mArrayList[position]
        if (!TextUtils.isEmpty(currentOrder!!.order_token)) {
            when (currentOrder!!.order_token) {
                MallOrderPresent.MallOrderStatus.WAITING_FOR_MONEY.index -> {
                    if (view!!.id == R.id.mBtOrderListItemAction1) {
                        //取消订单
                        alert("是否要取消订单？", "是", "否") { dialog, which ->
                            mPresent.getDataByPost(0x1,
                                    RequestParamsHelper.MEMBER_MODEL,
                                    RequestParamsHelper.ACT_EDIT_ORDER_STATUS,
                                    RequestParamsHelper.getEditOrderStatusParam(currentOrder!!.order_id, MallOrderPresent.MallOrderStatus.MALL_ORDER_CANCEL.index))
                        }
                    } else if (view.id == R.id.mBtOrderListItemAction2) {
                        //去支付
                        MallOrderPresent.showPayDialog(mContext) {
                            payType = it
                            mPresent.getDataByPost(0x2, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_PAY,
                                    RequestParamsHelper.getPayParam(currentOrder!!.order_id, currentOrder!!.product_id, it))
                        }
                    }
                }
                MallOrderPresent.MallOrderStatus.WAITING_FOR_SEND.index -> {
                    if (view!!.id == R.id.mBtOrderListItemAction1) {
                        //申请退款
                        alert("是否要申请退款？", "是", "否") { dialog, which ->
                            FragmentContainerActivity
                                    .from(mContext)
                                    .setClazz(RefundFragment::class.java)
                                    .setTitle("申请退款")
                                    .setNeedNetWorking(true)
                                    .setExtraBundle(bundleOf(Pair(RefundFragment.OID_FLAG, currentOrder!!.order_id)))
                                    .start()
                        }
                    }
                }
                MallOrderPresent.MallOrderStatus.WAITING_FOR_RECEIVER.index -> {
                    if (view!!.id == R.id.mBtOrderListItemAction2) {
                        //确定收货
                        alert("是否要确定收货？", "是", "否") { dialog, which ->
                            mPresent.getDataByPost(0x4,
                                    RequestParamsHelper.MEMBER_MODEL,
                                    RequestParamsHelper.ACT_EDIT_ORDER_STATUS,
                                    RequestParamsHelper.getEditOrderStatusParam(currentOrder!!.order_id, MallOrderPresent.MallOrderStatus.WAITING_FOR_EVALUATE.index))
                        }
                    } else if (view.id == R.id.mBtOrderListItemAction1) {
                        //查看物流
                        FragmentContainerActivity
                                .from(mContext)
                                .setTitle("查看物流")
                                .setExtraBundle(bundleOf(Pair(ExpressInfoFragment.ORDER_BEAN_FLAG, currentOrder!!)))
                                .setNeedNetWorking(true)
                                .setClazz(ExpressInfoFragment::class.java)
                                .start()
                    }else if (view.id == R.id.mBtOrderListItemAction0){
                        //申请退款
                        alert("是否要申请退款？", "是", "否") { dialog, which ->
                            FragmentContainerActivity
                                    .from(mContext)
                                    .setClazz(RefundFragment::class.java)
                                    .setTitle("申请退款")
                                    .setNeedNetWorking(true)
                                    .setExtraBundle(bundleOf(Pair(RefundFragment.OID_FLAG, currentOrder!!.order_id)))
                                    .start()
                        }
                    }
                }
                MallOrderPresent.MallOrderStatus.WAITING_FOR_EVALUATE.index -> {
                    if (view!!.id == R.id.mBtOrderListItemAction2) {
                        //去评价
                        FragmentContainerActivity
                                .from(mContext)
                                .setTitle("评价")
                                .setNeedNetWorking(true)
                                .setClazz(OrderCommentSubmitFragment::class.java)
                                .setExtraBundle(bundleOf(Pair(OrderCommentSubmitFragment.ORDER_ID_FLAG, currentOrder!!.order_id),
                                        Pair(OrderCommentSubmitFragment.PRODUCT_ID_FLAG, currentOrder!!.product_id),
                                        Pair(OrderCommentSubmitFragment.ORDER_SN_FLAG, currentOrder!!.order_sn)))
                                .start()
                    }
                }
                MallOrderPresent.MallOrderStatus.MALL_ORDER_COMPLEMENT.index -> {
                    if (view!!.id == R.id.mBtOrderListItemAction1) {
                        //查看订单
                        FragmentContainerActivity
                                .from(mContext)
                                .setNeedNetWorking(true)
                                .setClazz(OrderInfoFragment::class.java)
                                .setExtraBundle(bundleOf(Pair(OrderInfoFragment.OID_FLAG, currentOrder!!.order_id)))
                                .setTitle("订单详情")
                                .start()
                    }
                }
            }
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        currentOrder = mArrayList[position]
        FragmentContainerActivity
                .from(mContext)
                .setNeedNetWorking(true)
                .setClazz(OrderInfoFragment::class.java)
                .setExtraBundle(bundleOf(Pair(OrderInfoFragment.OID_FLAG, currentOrder!!.order_id)))
                .setTitle("订单详情")
                .start()
    }

    override fun onDestroyView() {
        unsubscribe(orderSubscription)
        unsubscribe(paySubscription)
        super.onDestroyView()
    }

}