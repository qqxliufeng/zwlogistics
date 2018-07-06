package com.android.ql.lf.zwlogistics.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

open class OrderItemAdapter(res:Int,list:ArrayList<String>): BaseQuickAdapter<String, BaseViewHolder>(res,list) {

    override fun convert(helper: BaseViewHolder?, item: String?) {
    }
}