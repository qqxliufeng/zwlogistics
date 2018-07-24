package com.android.ql.lf.zwlogistics.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.text.TextUtils
import android.view.WindowManager
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.service.LocationService
import com.android.ql.lf.zwlogistics.ui.fragment.bottom.IndexFragment
import com.android.ql.lf.zwlogistics.ui.fragment.bottom.MineFragment
import com.android.ql.lf.zwlogistics.ui.fragment.bottom.OrderFragment
import com.android.ql.lf.zwlogistics.ui.fragment.order.MyOrderInfoFragment
import com.android.ql.lf.zwlogistics.utils.Constants
import com.android.ql.lf.zwlogistics.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_main_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    companion object {
        fun startMainActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    private var exitTime: Long = 0L

    override fun getLayoutId() = R.layout.activity_main_layout

    override fun initView() {
        mBnvMain.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.mMenuIndex -> {
                    mMainContainer.currentItem = 0
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.mMenuOrder -> {
                    mMainContainer.currentItem = 1
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.mMenuMine -> {
                    mMainContainer.currentItem = 2
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
        mMainContainer.offscreenPageLimit = 3
        mMainContainer.adapter = MainAdapter(supportFragmentManager)
        if (UserInfo.getInstance().isLogin && UserInfo.getInstance().isNeedGps){
            startService(Intent(this.applicationContext, LocationService::class.java))
        }
    }


    override fun onBackPressed() {
        moveTaskToBack(false)
    }


    class MainAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> {
                    IndexFragment()
                }
                1 -> {
                    OrderFragment()
                }
                2 -> {
                    MineFragment()
                }
                else -> {
                    IndexFragment()
                }
            }
        }

        override fun getCount() = 3

    }

}