package com.android.ql.lf.zwlogistics.ui.fragment.bottom

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.MainActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.order.MyOrderListFragment
import kotlinx.android.synthetic.main.fragment_order_layout.*

class OrderFragment :BaseNetWorkingFragment(){

    companion object {
        val TITLES = arrayOf("已中标","竞标中","已完成")
    }

    override fun getLayoutId() = R.layout.fragment_order_layout

    override fun initView(view: View?) {
        val param = mTvMainOrderTitle.layoutParams as ViewGroup.MarginLayoutParams
        param.topMargin = (mContext as MainActivity).statusHeight

        mVpMyOrderContainer.offscreenPageLimit = TITLES.size
        mVpMyOrderContainer.adapter = MyOrderAdapter(childFragmentManager)
        mTlMyOrderTitle.setupWithViewPager(mVpMyOrderContainer)

    }


    class MyOrderAdapter(fragmentManager: FragmentManager) :FragmentStatePagerAdapter(fragmentManager){

        override fun getItem(position: Int): Fragment {
            return MyOrderListFragment.newInstance(position)
        }

        override fun getCount() = TITLES.size

        override fun getPageTitle(position: Int) = TITLES[position]

    }


}