package com.android.ql.lf.carapp.ui.adapter

import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ArticleBean
import com.android.ql.lf.carapp.ui.views.ImageContainerLinearLayout
import com.android.ql.lf.carapp.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 18.2.5.
 * @author lf on 18.2.5
 */
class ArticleListAdapter(resId: Int, list: ArrayList<ArticleBean>) : BaseQuickAdapter<ArticleBean, BaseViewHolder>(resId, list) {
    override fun convert(helper: BaseViewHolder?, item: ArticleBean?) {
        helper!!.setText(R.id.mTvArticleItemTime, item!!.quiz_time)
        helper.setText(R.id.mCtvArticleItemSeeCount, item.quiz_look)
        helper.setText(R.id.mCtvArticleItemCommentCount, item.quiz_replies)
        helper.setText(R.id.mCtvArticleItemPraiseCount, item.quiz_click)
        helper.setText(R.id.mTvArticleItemContent, item.quiz_title)
        if (item.member_pic != null) {
            GlideManager.loadFaceCircleImage(mContext, item.member_pic, helper.getView(R.id.mIvArticleItemUserFace))
        }
        helper.setText(R.id.mTvArticleItemUserName, item.member_name ?: "暂无")
        val imageContainer = helper.getView<ImageContainerLinearLayout>(R.id.mLlArticleItemImageContainer)
        if (item.quiz_pic.isEmpty()) {
            imageContainer.visibility = View.GONE
        } else {
            imageContainer.visibility = View.VISIBLE
            imageContainer.setImages(item.quiz_pic)
        }
    }
}