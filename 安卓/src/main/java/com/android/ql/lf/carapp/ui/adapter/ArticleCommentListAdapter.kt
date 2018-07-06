package com.android.ql.lf.carapp.ui.adapter

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ArticleAnswerBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 18.2.6.
 * @author lf on 18.2.6
 */
class ArticleCommentListAdapter(resId: Int, list: ArrayList<ArticleAnswerBean>) : BaseQuickAdapter<ArticleAnswerBean, BaseViewHolder>(resId, list) {
    override fun convert(helper: BaseViewHolder?, item: ArticleAnswerBean?) {
        if (!TextUtils.isEmpty(item!!.member_pic)) {
            GlideManager.loadFaceCircleImage(mContext, item.member_pic, helper!!.getView(R.id.mIvAnswerInfoItemFace))
        }
        helper!!.setText(R.id.mTvAnswerInfoItemName, if (TextUtils.isEmpty(item.member_name)) {
            "暂无"
        } else {
            item.member_name
        })
        helper.setText(R.id.mTvAnswerInfoItemContent, item.answer_content)
        helper.setText(R.id.mTvAnswerInfoItemTime, item.answer_time)
        val mTvDelete = helper.getView<TextView>(R.id.mTvAnswerInfoItemDelete)
        if (UserInfo.getInstance().memberId == item.member_id) {
            mTvDelete.visibility = View.VISIBLE
            helper.addOnClickListener(R.id.mTvAnswerInfoItemDelete)
        } else {
            mTvDelete.visibility = View.GONE
        }
        if (item.answer_click == "0") {
            helper.setText(R.id.mTvPraiseText, "赞")
        } else {
            helper.setText(R.id.mTvPraiseText, item.answer_click)
        }
        helper.addOnClickListener(R.id.mPraiseView)
    }
}