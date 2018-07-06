package com.android.ql.lf.carapp.ui.adapter

import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.TextUtils
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.OrderBean
import com.android.ql.lf.carapp.present.ServiceOrderPresent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 18.2.27.
 * @author lf on 18.2.27
 */
class OrderListForMineForWaitingCalculateAdapter(layoutId: Int, list: ArrayList<OrderBean>) : BaseQuickAdapter<OrderBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: OrderBean?) {
        helper!!.setText(R.id.mTvOrderListForItemProjectName, Html.fromHtml("<font color='${ContextCompat.getColor(mContext, R.color.colorPrimary)}'>项目：</font>${item!!.qorder_project}"))
        helper.setText(R.id.mTvOrderListForItemProjectContent, Html.fromHtml("<font color='${ContextCompat.getColor(mContext, R.color.colorPrimary)}'>备注：</font>${item.qorder_content}"))
        helper.setText(R.id.mTvOrderListForItemProjectSn, Html.fromHtml("<font color='${ContextCompat.getColor(mContext, R.color.colorPrimary)}'>订单号：</font>${item.qorder_sn}"))
        helper.setText(R.id.mTvOrderListForItemProjectOrderTime, Html.fromHtml("<font color='${ContextCompat.getColor(mContext, R.color.colorPrimary)}'>预约时间：</font>${if (TextUtils.isEmpty(item.qorder_appointment_time)) {
            "暂无"
        } else {
            item.qorder_appointment_time
        }}"))
        helper.setText(R.id.mTvOrderListForItemCustomPhone, item.qorder_phone)
        helper.setText(R.id.mTvOrderListForItemName, item.qorder_name)
        helper.setText(R.id.mTvOrderListForItemProjectTime, item.qorder_time)
        helper.setText(R.id.mTvOrderListForItemProjectPrice, "￥${item.qorder_price}")
        helper.setText(R.id.mTvOrderListForItemStatus, ServiceOrderPresent.OrderStatus.getDescriptionByIndex(item.qorder_token))
        helper.addOnClickListener(R.id.mBtOrderListForWaitingCalculateSubmit)
        helper.addOnClickListener(R.id.mTvOrderListForItemCustomPhone)
    }
}