package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.os.Bundle
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.CommentForGoodsBean
import com.android.ql.lf.carapp.ui.adapter.GoodsCommentAdapter
import com.android.ql.lf.carapp.ui.adapter.MineEvaluateItemAdapter
import com.android.ql.lf.carapp.ui.fragments.AbstractLazyLoadFragment
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by liufeng on 2018/2/25.
 */
class MineEvaluateItemFragment : AbstractLazyLoadFragment<CommentForGoodsBean>() {

    companion object {

        val COMMENT_FLAG = "comment_flag"

        fun newInstance(bundle: Bundle): MineEvaluateItemFragment {
            val mineEvaluateItemFragment = MineEvaluateItemFragment()
            mineEvaluateItemFragment.arguments = bundle
            return mineEvaluateItemFragment
        }
    }

    override fun createAdapter(): BaseQuickAdapter<CommentForGoodsBean, BaseViewHolder> =
            MineEvaluateItemAdapter(R.layout.adapter_mine_goods_comment_item_layout, mArrayList)


    override fun loadData() {
        isLoad = true
        setData()
    }

    override fun onLoadMore() {
        super.onLoadMore()
        setData()
    }

    private fun setData() {
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.MEMBER_MODEL,
                RequestParamsHelper.ACT_MY_COMMENT,
                RequestParamsHelper.getMyCommentParam(arguments.getString(COMMENT_FLAG), currentPage))
    }

    override fun getEmptyMessage() = "暂无评价哦~~"

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(result as String, CommentForGoodsBean::class.java)
    }

}