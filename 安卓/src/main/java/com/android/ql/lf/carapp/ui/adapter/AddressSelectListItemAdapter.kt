package com.android.ql.lf.carapp.ui.adapter

import android.text.Html
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.AddressBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class AddressSelectListItemAdapter(layoutId: Int, list: ArrayList<AddressBean>) : BaseQuickAdapter<AddressBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: AddressBean?) {
        helper!!.setText(R.id.mTvAddressSelectName, item!!.address_name)
        helper.setText(R.id.mTvAddressSelectPhone, item.address_phone)
        val default = if (item.address_token == "1") { "[默认地址]" } else { "" }
        val html = Html.fromHtml("<span style='color:#78BFFF'>$default</span> ${item.address_addres}  ${item.address_detail}")
        helper.setText(R.id.mTvAddressSelectDetail, html)
    }
}