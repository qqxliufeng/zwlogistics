package com.android.ql.lf.zwlogistics.ui.fragment.bottom

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View

import android.view.ViewGroup
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.activity.MainActivity
import com.android.ql.lf.zwlogistics.ui.activity.SelectAddressActivity
import com.android.ql.lf.zwlogistics.ui.adapter.OrderItemAdapter
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseRecyclerViewFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.car.SelectMultiTypeFragment
import com.android.ql.lf.zwlogistics.ui.fragment.order.OrderInfoFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_index_layout.*


class IndexFragment :  BaseRecyclerViewFragment<String>(){


    override fun getLayoutId() = R.layout.fragment_index_layout


    override fun createAdapter() = OrderItemAdapter(R.layout.adapter_index_order_item_layout,mArrayList)

    private val selectMultiTypeFragment by lazy {
        SelectMultiTypeFragment()
    }

    override fun initView(view: View?) {
        super.initView(view)
        val param = mTvMainIndexTitle.layoutParams as ViewGroup.MarginLayoutParams
        param.topMargin = (mContext as MainActivity).statusHeight

        mLlIndexOrderSourceAddressContainer.setOnClickListener {
            mCtvIndexOrderSourceAddress.isChecked = true
            startActivity(Intent(mContext,SelectAddressActivity::class.java))
            (mContext as Activity).overridePendingTransition(R.anim.activity_open,0)
        }

        mLlIndexOrderDesAddressContainer.setOnClickListener {
            mCtvIndexOrderDesAddress.isChecked = true
        }

        mLlIndexOrderCarTypeContainer.setOnClickListener {
            mCtvIndexOrderCarType.isChecked = true
            selectMultiTypeFragment.show(childFragmentManager,"select_multi_type_dialog")
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        testAdd("")
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.TRANSPARENT))
        return itemDecoration
    }


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity.from(mContext).setTitle("我要竞标").setClazz(OrderInfoFragment::class.java).setNeedNetWorking(true).start()
    }

}