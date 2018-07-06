package com.android.ql.lf.carapp.ui.adapter

import android.widget.TextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.CommentForGoodsBean
import com.android.ql.lf.carapp.ui.views.ImageContainerLinearLayout
import com.android.ql.lf.carapp.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 18.3.21.
 * @author lf on 18.3.21
 */
class GoodsCommentAdapter(layoutId: Int, list: ArrayList<CommentForGoodsBean>) : BaseQuickAdapter<CommentForGoodsBean, BaseViewHolder>(layoutId, list) {

    private var maxLines:Int = 4

    override fun convert(helper: BaseViewHolder?, item: CommentForGoodsBean?) {
        GlideManager.loadCircleImage(mContext, item!!.member_pic, helper!!.getView(R.id.mIvGoodsInfoCommentItemFace))
        helper.setText(R.id.mTvGoodsInfoCommentItemName, item.member_name)
        helper.setText(R.id.mTvGoodsInfoCommentItemTime, item.comment_time)
        val tv_content =  helper.getView<TextView>(R.id.mTvGoodsInfoCommentItemContent)
        tv_content.maxLines = maxLines
        helper.setText(R.id.mTvGoodsInfoCommentItemContent, item.comment_content)
        val picContainer = helper.getView<ImageContainerLinearLayout>(R.id.mLlGoodsInfoCommentItemPicContainer)
        picContainer.setImages(item.comment_pic)
    }

    fun setContentLines(maxLines:Int = 4){
        this.maxLines = maxLines
    }

}