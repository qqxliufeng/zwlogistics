package com.android.ql.lf.carapp.ui.fragments.message

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.adapter.MineMessageListAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.collections.forEachWithIndex
import org.json.JSONObject

/**
 * Created by lf on 18.1.27.
 * @author lf on 18.1.27
 */
class MineMessageListFragment : BaseRecyclerViewFragment<MineMessageListFragment.MineMessageItem>() {

    companion object {
        val SECOND_LEVEL_ALL_MESSAGE_HAVE_RED_FLAG = "second level all message have read"
    }

    private var currentMessageItem: MineMessageItem? = null

    private val iconList = listOf(
            R.drawable.img_icon_message_for_system,
            R.drawable.img_icon_message_for_fix,
            R.drawable.img_icon_message_for_house,
            R.drawable.img_icon_message_for_shop,
            R.drawable.img_icon_message_for_community)
    private val titleList = listOf("系统消息", "维修订单", "商铺订单", "购物订单", "帖子评论")

    override fun createAdapter(): BaseQuickAdapter<MineMessageItem, BaseViewHolder>
            = MineMessageListAdapter(R.layout.adapter_mine_message_list_item_layout, mArrayList)

    override fun initView(view: View?) {
        super.initView(view)
        setRefreshEnable(false)
    }

    override fun onRefresh() {
        super.onRefresh()
        onRequestEnd(1)
        iconList.forEachIndexed { index, i ->
            mArrayList.add(MineMessageItem(index, i, titleList[index], "暂无消息", true))
        }
        mBaseAdapter.notifyDataSetChanged()
        setLoadEnable(false)
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_MSG, RequestParamsHelper.getMyMsgParam())
    }

    override fun getEmptyMessage() = "暂无消息"

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        try {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                val json = check.obj as JSONObject
                val arrJson = json.optJSONObject("arr")
                val arr1Json = json.optJSONObject("arr1")
                if (arrJson != null && arr1Json != null && arrJson.length() == arr1Json.length() && arrJson.length() == mArrayList.size) {
                    mArrayList.forEachWithIndex { i, mineMessageItem ->
                        mineMessageItem.isRead = arrJson.optString("s$i").toInt() <= 0
                        val des = arr1Json.optString("m$i")
                        mineMessageItem.description = if (!TextUtils.isEmpty(des) && des != "null") {
                            des
                        } else {
                            "暂无消息"
                        }
                    }
                    mBaseAdapter.notifyDataSetChanged()
                } else {
                    setEmptyView()
                }
            }
        } catch (e: Exception) {
            setEmptyView()
        }
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        currentMessageItem = mArrayList[position]
        if (!currentMessageItem!!.isRead) {
            currentMessageItem!!.isRead = true
            mBaseAdapter.notifyItemChanged(position)
        }
        FragmentContainerActivity
                .from(mContext)
                .setTitle(currentMessageItem!!.title)
                .setNeedNetWorking(true)
                .setExtraBundle(bundleOf(Pair(SystemMessageListFragment.MESSAGE_TYPE_FLAG, "${currentMessageItem!!.msgId}")))
                .setClazz(SystemMessageListFragment::class.java)
                .start()
    }

    data class MineMessageItem(
            var msgId: Int,
            var icon: Int,
            var title: String,
            var description: String,
            var isRead: Boolean)
}