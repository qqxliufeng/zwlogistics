package com.android.ql.lf.carapp.ui.adapter

import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ImageBean
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 18.1.29.
 * @author lf on 18.1.29
 */
class OrderImageUpLoadAdapter(layoutId: Int, list: ArrayList<ImageBean>) : BaseQuickAdapter<ImageBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: ImageBean?) {
//        val iv_close = helper!!.getView<ImageView>(R.id.mIvUploadImageClose)
        if (item!!.resId != null){
//            iv_close.visibility = View.GONE
            Glide.with(mContext).load(item.resId).into(helper!!.getView(R.id.mIvUploadImage))
        }else {
//            iv_close.visibility = View.VISIBLE
//            iv_close.setColorFilter(mContext.resources.getColor(R.color.colorPrimary))
            Glide.with(mContext).load(item.uriPath).into(helper!!.getView(R.id.mIvUploadImage))
        }
    }
}