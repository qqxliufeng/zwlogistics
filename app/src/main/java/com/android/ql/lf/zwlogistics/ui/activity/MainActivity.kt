package com.android.ql.lf.zwlogistics.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.WindowManager
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.ui.fragment.bottom.IndexFragment
import com.android.ql.lf.zwlogistics.ui.fragment.bottom.MineFragment
import com.android.ql.lf.zwlogistics.ui.fragment.bottom.OrderFragment
import com.android.ql.lf.zwlogistics.utils.alert
import com.android.ql.lf.zwlogistics.utils.checkGpsIsOpen
import com.android.ql.lf.zwlogistics.utils.openGpsPage
import kotlinx.android.synthetic.main.activity_main_layout.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    companion object {
        fun startMainActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//        if (!this.checkGpsIsOpen()) {
//            //开启定位服务
//            alert(null, "请开启定位功能，否则部分功能将不能使用", "开启", "取消", { _, _ ->
//                this.openGpsPage()
//            }, null)
//        }
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
    }


    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            toast("再按一次退出")
            exitTime = System.currentTimeMillis()
        } else {
            UserInfo.getInstance().loginOut()
            finish()
        }
    }


    override fun onDestroy() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onDestroy()
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