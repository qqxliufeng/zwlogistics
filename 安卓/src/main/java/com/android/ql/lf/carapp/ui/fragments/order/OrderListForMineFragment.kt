package com.android.ql.lf.carapp.ui.fragments.order

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.ui.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_order_list_for_mine_layout.*

/**
 * Created by lf on 18.1.25.
 * @author lf on 18.1.25
 */
class OrderListForMineFragment : BaseFragment() {

    companion object {

        fun newInstance(): OrderListForMineFragment {
            return OrderListForMineFragment()
        }

        val TITLES = listOf("待施工", "已施工", "待结算", "已结算")
    }

    override fun getLayoutId() = R.layout.fragment_order_list_for_mine_layout

    override fun initView(view: View?) {
        mVpOrderListForMine.adapter = OrderListForMineViewPagerAdapter(childFragmentManager)
        mVpOrderListForMine.offscreenPageLimit = TITLES.size
        mTlOrderListForMine.setupWithViewPager(mVpOrderListForMine)
    }

    class OrderListForMineViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment = when (position) {
            0 -> {
                OrderListForMineForWaitingWorkFragment.newInstance()
            }
            1 -> {
                OrderListForMineForHavingWorkFragment.newInstance()
            }
            2 -> {
                OrderListForMineForWaitingCalculateFragment.newInstance()
            }
            else -> {
                OrderListForMineForHavingCalculateFragment.newInstance()
            }
        }

        override fun getCount() = TITLES.size

        override fun getPageTitle(position: Int) = TITLES[position]
    }


}