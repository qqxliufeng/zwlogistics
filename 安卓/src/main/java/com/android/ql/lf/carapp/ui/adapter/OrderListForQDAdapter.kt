package com.android.ql.lf.carapp.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import cn.iwgang.countdownview.CountdownView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.application.CarApplication
import com.android.ql.lf.carapp.data.OrderBean
import com.android.ql.lf.carapp.present.ServiceOrderPresent
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.util.*

/**
 * Created by lf on 18.1.25.
 * @author lf on 18.1.25
 */
class OrderListForQDAdapter(var context: Context, var layoutId: Int, var list: ArrayList<OrderBean>) : BaseQuickAdapter<OrderBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: OrderBean?) {
        (helper as MyViewHolder).bindData(this, item!!)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false))
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder?) {
        super.onViewAttachedToWindow(holder)
        if (holder is MyViewHolder) {
            holder.refreshTime(list[holder.adapterPosition].endTime - System.currentTimeMillis())
        }
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder?) {
        super.onViewDetachedFromWindow(holder)
        if (holder is MyViewHolder) {
            holder.stopTime()
        }
    }

    class MyViewHolder(var view: View) : BaseViewHolder(view) {

        var orderBean: OrderBean? = null

        private val mCvCountdownView by lazy {
            view.findViewById<CountdownView>(R.id.mCvOrderListForQDItemTime)
        }

        fun bindData(adapter: OrderListForQDAdapter, orderBean: OrderBean) {
            this.orderBean = orderBean
            addOnClickListener(R.id.mBtOrderListForQDItem)
            val bt_status = getView<Button>(R.id.mBtOrderListForQDItem)
            bt_status.isEnabled = orderBean.qorder_token != "${ServiceOrderPresent.OrderStatus.HAD_EXPIRE.index}"
            setText(R.id.mTvOrderListForQDItemName, orderBean.qorder_name)
            setText(R.id.mTvOrderListForQDItemTime, orderBean.qorder_time ?: "暂无")
            setText(R.id.mTvOrderListForQDItemTitle, Html.fromHtml("<font color='${ContextCompat.getColor(CarApplication.application, R.color.colorPrimary)}'>项目：</font>${orderBean.qorder_project}"))
            setText(R.id.mTvOrderListForQDItemMoney, "￥${orderBean.qorder_price}")
            setText(R.id.mTvOrderListForQDItemContent, Html.fromHtml("<font color='${ContextCompat.getColor(CarApplication.application, R.color.colorPrimary)}'>备注：</font>${orderBean.qorder_content}"))
            if (orderBean.qorder_remaining_time > 0) {
                refreshTime(orderBean.endTime - System.currentTimeMillis())
            } else {
                mCvCountdownView.allShowZero()
            }
            mCvCountdownView.setOnCountdownEndListener {
                this.orderBean!!.qorder_token = "${ServiceOrderPresent.OrderStatus.HAD_EXPIRE.index}"
                adapter.notifyDataSetChanged()
            }
        }

        fun refreshTime(leftTime: Long) {
            if (leftTime > 0) {
                mCvCountdownView.start(leftTime)
            } else {
                mCvCountdownView.stop()
                mCvCountdownView.allShowZero()
            }
        }

        fun stopTime() {
            mCvCountdownView.stop()
        }
    }
}