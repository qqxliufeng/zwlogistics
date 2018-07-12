package com.android.ql.lf.zwlogistics.ui.fragment.order

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.View
import com.android.ql.lf.carapp.utils.fromHtml
import com.android.ql.lf.carapp.utils.setDiffColorText
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.OrderBean
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.utils.GlideManager
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_my_order_info_layout.*
import kotlinx.android.synthetic.main.layout_order_info.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class MyOrderInfoFragment : BaseNetWorkingFragment() {

    companion object {

        fun startMyOrderInfo(mContext:Context,id:String) {
            FragmentContainerActivity
                    .from(mContext)
                    .setTitle("订单详情")
                    .setClazz(MyOrderInfoFragment::class.java)
                    .setExtraBundle(bundleOf(Pair("oid",id)))
                    .setNeedNetWorking(true)
                    .start()
        }
    }

    private var orderBean: OrderBean? = null



    override fun getLayoutId() = R.layout.fragment_my_order_info_layout

    override fun initView(view: View?) {
        mPresent.getDataByPost(0x0, RequestParamsHelper.getOrderInfoParam(arguments!!.getString("oid")))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        when (requestID) {
            0x0 -> getFastProgressDialog("正在加载详情……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        handleSuccess(requestID, result)
    }


    override fun onHandleSuccess(requestID: Int, jsonObject: JSONObject?) {
        super.onHandleSuccess(requestID, jsonObject)
        when (requestID) {
            0x0 -> {
                orderBean = Gson().fromJson(jsonObject?.toString(), OrderBean::class.java)
                bindData()
            }
        }
    }

    private fun bindData() {
        mTvOrderInfoOrderSn.setDiffColorText("订单编号：", if (orderBean != null) orderBean!!.need_order else "暂无")
        mTvOrderInfoOrderSendTime.setDiffColorText("发货日期：", if (orderBean != null) orderBean!!.need_time_top else "暂无")
        mTvOrderInfoOrderReceiverTime.setDiffColorText("交货日期：", if (orderBean != null) orderBean!!.need_time_end else "暂无")
        mTvOrderInfoOrderPayType.setDiffColorText("支付方式：", if (orderBean != null) orderBean!!.need_type_pay else "暂无")
        mTvOrderInfoOrderPersonNum.setDiffColorText("竞标人数：", if (orderBean != null) "${orderBean!!.need_user_cou}人" else "暂无", color2 = "#ff2222")
        mTvOrderInfoGoodsZXType.setDiffColorText("装卸方式：", if (orderBean != null) orderBean!!.need_xie else "暂无")

        mTvOrderInfoGoodsInfo.text = Html.fromHtml("重量：".fromHtml() + (if (orderBean != null) orderBean!!.need_zhong else "暂无").fromHtml("#545557") + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;体积：".fromHtml() + (if (orderBean != null) orderBean!!.need_ti else "暂无").fromHtml("#545557"))
        mTvOrderInfoGoodsSendAddress.setDiffColorText("出发地：", (if (orderBean != null) orderBean!!.need_f_str else "暂无"))
        mTvOrderInfoGoodsTJAddress.setDiffColorText("途径地：", (if (orderBean != null) orderBean!!.need_t_site else "暂无"))
        mTvOrderInfoGoodsReceiverAddress.setDiffColorText("目的地：", (if (orderBean != null) orderBean!!.need_m_str else "暂无"))

        mTvOrderInfoCarType.setDiffColorText("用车类型：", "整车")
        mTvOrderInfoCarHeight.setDiffColorText("车长限制：", (if (orderBean != null) "${orderBean!!.need_length_top}米" else "暂无"))
        mTvOrderInfoCarCX.setDiffColorText("车型限制：", (if (orderBean != null) orderBean!!.need_model else "暂无"))
        mTvOrderInfoRemark.setDiffColorText("备注：","${if (orderBean != null && !TextUtils.isEmpty(orderBean!!.need_content)) orderBean!!.need_content else "暂无"}")

        mTvOrderInfoOrderPhone.setDiffColorText("联系电话：", (if (orderBean != null) orderBean!!.need_user_phone else "暂无"))
        mTvOrderInfoOrderPrice.setDiffColorText("我的报价：", (if (orderBean != null) "￥${orderBean!!.need_user_sum}元" else "暂无"))
        mTvOrderInfoOrderCarTpe.setDiffColorText("运载车型：", (if (orderBean != null) orderBean!!.need_user_model else "暂无"))
        mTvOrderInfoOrderUserRemark.setDiffColorText("备注：", (if (orderBean != null && !TextUtils.isEmpty(orderBean!!.need_user_content)) orderBean!!.need_user_content else "暂无"))

        GlideManager.loadRoundImage(mContext, orderBean?.need_pic, mTvOrderInfoPic, 20)
    }


    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        when (requestID) {
            0x0 -> toast("加载详情失败")
        }
    }

}