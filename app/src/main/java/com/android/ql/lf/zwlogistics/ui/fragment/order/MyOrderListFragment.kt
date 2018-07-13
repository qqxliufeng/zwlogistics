package com.android.ql.lf.zwlogistics.ui.fragment.order

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.OrderBean
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.adapter.OrderItemAdapter
import com.android.ql.lf.zwlogistics.ui.fragment.base.AbstractLazyLoadFragment
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

class MyOrderListFragment : AbstractLazyLoadFragment<OrderBean>() {

    companion object {
        const val HAVED_TENDER = 3
        const val TENDERING = 1
        const val COMPLEMENT_TENDER = 2

        fun newInstance(position: Int): MyOrderListFragment {
            val myOrderListFragment = MyOrderListFragment()
            myOrderListFragment.arguments = bundleOf(Pair("index", position))
            return myOrderListFragment
        }
    }

    private val currentStatus by lazy {
        when (arguments!!.getInt("index")) {
            0 -> {
                HAVED_TENDER
            }
            1 -> {
                TENDERING
            }
            2 -> {
                COMPLEMENT_TENDER
            }
            else -> {
                HAVED_TENDER
            }
        }
    }

    override fun createAdapter() = object : OrderItemAdapter(R.layout.adapter_index_order_item_layout, mArrayList) {
        override fun convert(helper: BaseViewHolder?, item: OrderBean?) {
            super.convert(helper, item)
            helper!!.setText(R.id.mTvOrderItemStatus, when (item!!.need_state) {
                OrderBean.OrderStatus.TENDERING.statusCode.toString() -> {
                    "竞标中"
                }
                OrderBean.OrderStatus.WEI_TENDER.statusCode.toString() -> {
                    "未标中"
                }
                OrderBean.OrderStatus.TENDERED.statusCode.toString() -> {
                    "竞标成功"
                }
                OrderBean.OrderStatus.TENDER_START.statusCode.toString() -> {
                    "已开始"
                }
                OrderBean.OrderStatus.TENDER_WEI_START.statusCode.toString() -> {
                    "未开始"
                }
                OrderBean.OrderStatus.TENDER_COMPLEMENT.statusCode.toString() -> {
                    "已完成"
                }
                else -> {
                    ""
                }
            })
        }
    }

    override fun initView(view: View?) {
        super.initView(view)
        registerLoginSuccessBus()
        registerLogoutSuccessBus()
    }

    override fun loadData() {
        if (UserInfo.getInstance().isLogin) {
            isLoad = true
            setRefreshEnable(true)
            mPresent.getDataByPost(0x0, RequestParamsHelper.getMyTenderListParams(currentStatus.toString()))
        } else {
            setEmptyViewNoLoginStatus()
        }
    }

    override fun getEmptyMessage() = if (!UserInfo.getInstance().isLogin) {
        "登录"
    } else {
        "暂无订单"
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(result as String, OrderBean::class.java)
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.TRANSPARENT))
        return itemDecoration
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        MyOrderInfoFragment.startMyOrderInfo(mContext, mArrayList[position].need_id)
    }

    override fun onLoginSuccess(userInfo: UserInfo?) {
        super.onLoginSuccess(userInfo)
        onLoginRefresh()
    }

    override fun onLogoutSuccess(logout: String?) {
        super.onLogoutSuccess(logout)
        if (!mArrayList.isEmpty()) {
            mArrayList.clear()
            mBaseAdapter.notifyDataSetChanged()
        }
        setEmptyViewNoLoginStatus()
    }

}