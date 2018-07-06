package com.android.ql.lf.carapp.ui.adapter

import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.ui.fragments.message.MineMessageListFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 18.1.27.
 * @author lf on 18.1.27
 */
class MineMessageListAdapter(layoutId: Int, list: ArrayList<MineMessageListFragment.MineMessageItem>) : BaseQuickAdapter<MineMessageListFragment.MineMessageItem, BaseViewHolder>(layoutId, list) {
    override fun convert(helper: BaseViewHolder?, item: MineMessageListFragment.MineMessageItem?) {
        helper!!.setImageResource(R.id.mIvMineMessageListIcon,item!!.icon)
        helper.setText(R.id.mTvMineMessageListTitle,item.title)
        helper.setText(R.id.mTvMineMessageListDescription,item.description)
        val viewNotify = helper.getView<View>(R.id.mViewMineMessageListNotify)
        viewNotify.visibility = if(item.isRead) View.GONE else View.VISIBLE
    }
}