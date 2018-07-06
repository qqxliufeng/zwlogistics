package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ArticleAnswerBean
import com.android.ql.lf.carapp.data.ArticleBean
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.community.ArticleInfoFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

/**
 * Created by liufeng on 2018/2/5.
 */
class MineArticleReplyItemFragment : BaseRecyclerViewFragment<ArticleAnswerBean>() {

    companion object {
        fun newInstance(): MineArticleReplyItemFragment {
            return MineArticleReplyItemFragment()
        }
    }

    override fun createAdapter(): BaseQuickAdapter<ArticleAnswerBean, BaseViewHolder> =
            MineReplyArticleListAdapter(R.layout.adapter_mine_reply_article_item_layout, mArrayList)

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_GET_MYANSWER, RequestParamsHelper.getGetMyAnswerParam(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_GET_MYANSWER, RequestParamsHelper.getGetMyAnswerParam(currentPage))
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun getEmptyMessage() = "暂无回帖"

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            processList(result as String, ArticleAnswerBean::class.java)
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                toast("删除成功")
                onPostRefresh()
            } else {
                toast("删除失败")
            }
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        val article = ArticleBean()
        article.quiz_id = mArrayList[position].answer_qid
        FragmentContainerActivity.from(mContext)
                .setExtraBundle(bundleOf(Pair(ArticleInfoFragment.ARTICLE_BEAN_FLAG, article)))
                .setNeedNetWorking(true)
                .setTitle("详情")
                .setClazz(ArticleInfoFragment::class.java)
                .start()
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (view!!.id == R.id.mBtArticleDelete) {
            val build = AlertDialog.Builder(mContext)
            build.setMessage("是否要删除此回帖？")
            build.setPositiveButton("删除") { _, _ ->
                mPresent.getDataByPost(0x1, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_DEL_QAA, RequestParamsHelper.getDelQaaParam(aid = mArrayList[position].answer_id))
            }
            build.setNegativeButton("否", null)
            build.create().show()
        }
    }

    class MineReplyArticleListAdapter(resId: Int, list: ArrayList<ArticleAnswerBean>) : BaseQuickAdapter<ArticleAnswerBean, BaseViewHolder>(resId, list) {
        override fun convert(helper: BaseViewHolder?, item: ArticleAnswerBean?) {
            helper!!.addOnClickListener(R.id.mBtArticleDelete)
            helper.setText(R.id.mTvMyArticleAnswerItemInfoTitle, item!!.answer_title)
            helper.setText(R.id.mTvMyArticleAnswerItemInfoContent, "回复：${item.answer_content}")
        }
    }
}