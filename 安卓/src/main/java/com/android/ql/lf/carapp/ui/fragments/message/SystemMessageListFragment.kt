package com.android.ql.lf.carapp.ui.fragments.message

import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.ui.adapter.SystemMessageListAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 18.1.27.
 * @author lf on 18.1.27
 */
class SystemMessageListFragment : BaseRecyclerViewFragment<SystemMessageListFragment.SystemMessageItem>() {

    companion object {
        val MESSAGE_TYPE_FLAG = "message_type_flag"
    }

    private var currentItemIndex: Int = 0

    override fun createAdapter(): BaseQuickAdapter<SystemMessageItem, BaseViewHolder> =
            SystemMessageListAdapter(R.layout.adapter_system_message_list_item_layout, mArrayList)

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_MSG_DETAIL, RequestParamsHelper.getMyMsgDetailParam(arguments.getString(MESSAGE_TYPE_FLAG), currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_MSG_DETAIL, RequestParamsHelper.getMyMsgDetailParam(arguments.getString(MESSAGE_TYPE_FLAG), currentPage))
    }

    override fun getEmptyMessage() = "暂无消息"

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            processList(result as String, SystemMessageItem::class.java)
            if (!mArrayList.isEmpty()) {
                mArrayList.forEach {
                    it.isRead = it.message_status == "1"
                }
                mBaseAdapter.notifyDataSetChanged()
            }
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                mArrayList[currentItemIndex].isRead = true
                mBaseAdapter.notifyItemChanged(currentItemIndex)
            }
        }
    }


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        val item = mArrayList[position]
        currentItemIndex = position
        mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_EDIT_MYMSG_STATUS, RequestParamsHelper.getEditMyMsgStatus(item.message_id!!))
    }

    class SystemMessageItem {
        var message_id: String? = null
        var message_title: String? = null
        var message_content: String? = null
        var message_time: String? = null
        var message_uid: String? = null
        var message_qid: String? = null
        var message_status: String? = null
        var message_token: String? = null
        var message_sym: String? = null
        var isRead: Boolean = false
    }
}