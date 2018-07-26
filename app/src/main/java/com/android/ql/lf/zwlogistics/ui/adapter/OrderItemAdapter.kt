package com.android.ql.lf.zwlogistics.ui.adapter

import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.OrderBean
import com.android.ql.lf.zwlogistics.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

open class OrderItemAdapter(res: Int, list: ArrayList<OrderBean>) : BaseQuickAdapter<OrderBean, BaseViewHolder>(res, list) {

    override fun convert(helper: BaseViewHolder?, item: OrderBean?) {
        GlideManager.loadRoundImage(mContext, item!!.need_pic, helper!!.getView(R.id.mIvOrderItemPic), 20)
        helper.setText(R.id.mTvOrderItemAddress, "${item.need_f_str} → ${item.need_m_str}")
        helper.setText(R.id.mTvOrderItemParams, "重量：${item.need_zhong}吨  体积：${item.need_ti}立方米")
        helper.setText(R.id.mTvOrderItemZX, "半卸方式：${item.need_xie}")
        helper.setText(R.id.mTvOrderItemPayType, "支付方式：${item.need_type_pay}")
    }
}