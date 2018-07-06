package com.android.ql.lf.zwlogistics.ui.fragment.mine.driver

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseFragment

class MineDriverInfoEmptyFragment :BaseFragment(){


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId() = R.layout.fragment_mine_driver_info_empty_layout

    override fun initView(view: View?) {

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
       inflater!!.inflate(R.menu.driver_auth_empty,menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.mMenuEmptyDriverAuth){
            FragmentContainerActivity.from(mContext).setClazz(MinePersonAuthFragment::class.java).setTitle("我的认证").start()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}