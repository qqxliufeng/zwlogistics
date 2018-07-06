package com.android.ql.lf.carapp.ui.adapter

import android.widget.ImageView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.GoodsBean
import com.android.ql.lf.carapp.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 18.3.20.
 * @author lf on 18.3.20
 */
class GoodsMallItemAdapter(layoutId: Int, list: ArrayList<GoodsBean>) : BaseQuickAdapter<GoodsBean, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: GoodsBean?) {
        val imageView = helper!!.getView<ImageView>(R.id.mTvGoodsInfoItemPic)
        GlideManager.loadImage(mContext, item!!.product_main_pic, imageView)
        helper.setText(R.id.mTvGoodsInfoItemName, item.product_name)
        helper.setText(R.id.mTvGoodsInfoItemPrice, "￥${item.product_price}")
        val imageCollect = helper.getView<ImageView>(R.id.mIvGoodsInfoItemCollection)
        if (item.product_collect == "1") { //1 收藏
            imageCollect.setImageResource(R.drawable.img_icon_goods_select)
        } else {
            imageCollect.setImageResource(R.drawable.img_icon_goods_unselect)
        }
        helper.addOnClickListener(R.id.mIvGoodsInfoItemCollection)
    }
}