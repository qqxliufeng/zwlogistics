package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.SearchParamBean
import com.android.ql.lf.carapp.present.MallOrderPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import kotlinx.android.synthetic.main.fragment_main_mall_order_layout.*
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 18.3.27.
 * @author lf on 18.3.27
 */
class MineMallOrderFragment : BaseFragment() {

    companion object {
        val CURRENT_ITEM_FLAG = "current_item_flag"
        val TITLES = arrayListOf("待付款", "待发货", "待收货", "待评价", "售后中")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId() = R.layout.fragment_main_mall_order_layout

    override fun initView(view: View?) {
        mVpMainMallOrder.adapter = MainMallOrderViewPagerAdapter(childFragmentManager)
        mVpMainMallOrder.offscreenPageLimit = TITLES.size
        mTlMainMallOrder.setupWithViewPager(mVpMainMallOrder)
        if (arguments != null && arguments.getInt(CURRENT_ITEM_FLAG, 0) != -1) {
            mVpMainMallOrder.currentItem = arguments.getInt(CURRENT_ITEM_FLAG)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.mall_order_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.mAllOrder) {
            FragmentContainerActivity
                    .from(mContext)
                    .setClazz(MainMallOrderAllListFragment::class.java)
                    .setNeedNetWorking(true)
                    .setTitle("全部订单")
                    .start()
        }
        return true
    }

    class MainMallOrderViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int): MainMallOrderItemFragment {
            val searchParam = SearchParamBean()
            searchParam.model = RequestParamsHelper.MEMBER_MODEL
            searchParam.act = RequestParamsHelper.ACT_MYORDER_STATUS
            val map = HashMap<String, String>()
            when (position) {
                0 -> {
                    map.put("status", MallOrderPresent.MallOrderStatus.WAITING_FOR_MONEY.index)
                    searchParam.params = map
                }
                1 -> {
                    map.put("status", MallOrderPresent.MallOrderStatus.WAITING_FOR_SEND.index)
                    searchParam.params = map
                }
                2 -> {
                    map.put("status", MallOrderPresent.MallOrderStatus.WAITING_FOR_RECEIVER.index)
                    searchParam.params = map
                }
                3 -> {
                    map.put("status", MallOrderPresent.MallOrderStatus.WAITING_FOR_EVALUATE.index)
                    searchParam.params = map
                }
                4 -> {
                    map.put("status", MallOrderPresent.MallOrderStatus.MALL_ORDER_APPLY_BACK.index)
                    searchParam.params = map
                }
            }
            return MainMallOrderItemFragment.newInstance(bundleOf(Pair(MainMallOrderItemFragment.SEARCH_PARAM_FLAG, searchParam)))
        }

        override fun getCount() = TITLES.size

        override fun getPageTitle(position: Int) = TITLES[position]

    }
}