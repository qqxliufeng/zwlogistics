package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ArticleBean
import com.android.ql.lf.carapp.ui.adapter.ArticleListAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.community.WriteArticleFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.RxBus
import com.android.ql.lf.carapp.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by liufeng on 2018/2/5.
 */
class MineArticleWriteItemFragment : BaseRecyclerViewFragment<ArticleBean>() {

    companion object {
        fun newInstance(): MineArticleWriteItemFragment {
            return MineArticleWriteItemFragment()
        }
    }

    override fun createAdapter(): BaseQuickAdapter<ArticleBean, BaseViewHolder> =
            MineWriteArticleListAdapter(R.layout.adapter_mine_write_article_list_item_layout, mArrayList)

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_GET_MYQUIZ, RequestParamsHelper.getGetMyQuizParam(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_GET_MYQUIZ, RequestParamsHelper.getGetMyQuizParam(currentPage))
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            processList(result as String, ArticleBean::class.java)
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                toast("删除成功")
                RxBus.getDefault().post(WriteArticleFragment.ARTICLE_SEND_SUCCESS_FLAG)
                onPostRefresh()
            }
        }
    }

    override fun getEmptyMessage() = "暂无帖子"

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (view!!.id == R.id.mBtArticleDelete) {
            val build = AlertDialog.Builder(mContext)
            build.setMessage("是否要删除此帖？")
            build.setPositiveButton("是") { _, _ ->
                mPresent.getDataByPost(0x1, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_DEL_QAA, RequestParamsHelper.getDelQaaParam(qid = mArrayList[position].quiz_id))
            }
            build.setNegativeButton("否", null)
            build.create().show()
        }
    }


    class MineWriteArticleListAdapter(resId: Int, list: ArrayList<ArticleBean>) : BaseQuickAdapter<ArticleBean, BaseViewHolder>(resId, list) {
        override fun convert(helper: BaseViewHolder?, item: ArticleBean?) {
            helper!!.addOnClickListener(R.id.mBtArticleDelete)
            helper.setText(R.id.mTvMyArticleItemInfoTitle, item!!.quiz_title)
            helper.setText(R.id.mTvMyArticleItemInfoContent, "内容：${item.quiz_content}")
            helper.setText(R.id.mTvMyArticleItemInfoReplyCount, "${item.quiz_replies}回复")
            helper.setText(R.id.mTvMyArticleItemInfoPraiseCount, "${item.quiz_click}赞")
        }
    }

}