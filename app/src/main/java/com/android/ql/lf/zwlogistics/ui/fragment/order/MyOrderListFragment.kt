package com.android.ql.lf.zwlogistics.ui.fragment.order

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.adapter.OrderItemAdapter
import com.android.ql.lf.zwlogistics.ui.fragment.base.AbstractLazyLoadFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

class MyOrderListFragment : AbstractLazyLoadFragment<String>() {

    companion object {
        val HAVED_TENDER = 0
        val TENDERING = 1
        val COMPLEMENT_TENDER = 2

        fun newInstance(position: Int): MyOrderListFragment {
            val myOrderListFragment = MyOrderListFragment()
            myOrderListFragment.arguments = bundleOf(Pair("index", position))
            return myOrderListFragment
        }
    }


    private val currentStatus by lazy {
        when(arguments!!.getInt("index")){
            0->{
                HAVED_TENDER
            }
            1->{
                TENDERING
            }
            2->{
                COMPLEMENT_TENDER
            }
            else->{
                HAVED_TENDER
            }
        }
    }

    override fun createAdapter() = object: OrderItemAdapter(R.layout.adapter_index_order_item_layout, mArrayList){
        override fun convert(helper: BaseViewHolder?, item: String?) {
            helper!!.setText(R.id.mTvOrderItemStatus,"已开始")
        }
    }

    override fun loadData() {
        isLoad = true
        when(arguments!!.getInt("index")){
            HAVED_TENDER->{
                testAdd("")
            }
            TENDERING->{
                testAdd("")
            }
            COMPLEMENT_TENDER->{
                onRequestEnd(-1)
                setEmptyView()
            }
        }
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.TRANSPARENT))
        return itemDecoration
    }


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setClazz(MyOrderInfoFragment::class.java).setTitle("订单信息").start()
    }

}