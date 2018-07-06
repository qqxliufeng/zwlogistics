package com.android.ql.lf.carapp.ui.adapter

import android.widget.RatingBar
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.CommentForGoodsBean
import com.android.ql.lf.carapp.ui.views.ImageContainerLinearLayout
import com.android.ql.lf.carapp.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by liufeng on 2018/2/25.
 */
class MineEvaluateItemAdapter(resId: Int, list: ArrayList<CommentForGoodsBean>) : BaseQuickAdapter<CommentForGoodsBean, BaseViewHolder>(resId, list) {
    override fun convert(helper: BaseViewHolder?, item: CommentForGoodsBean?) {
        GlideManager.loadCircleImage(mContext, item!!.member_pic, helper!!.getView(R.id.mIvGoodsInfoCommentItemFace))
        helper.setText(R.id.mTvGoodsInfoCommentItemName, item.member_name)
        helper.setText(R.id.mTvGoodsInfoCommentItemContent, item.comment_content)
        val ratingBar = helper.getView<RatingBar>(R.id.mRbGoodsInfoCommentItemLevel)
        ratingBar.rating = item.comment_f.toFloat()
        val picContainer = helper.getView<ImageContainerLinearLayout>(R.id.mLlGoodsInfoCommentItemPicContainer)
        picContainer.setImages(item.comment_pic)
    }
}