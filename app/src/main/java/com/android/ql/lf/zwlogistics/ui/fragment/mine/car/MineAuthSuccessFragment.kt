package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.view.View
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.MainActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine_auth_success_layout.*

class MineAuthSuccessFragment :BaseFragment(){

    override fun getLayoutId() = R.layout.fragment_mine_auth_success_layout

    override fun initView(view: View?) {
        mBtAuthSuccessBack.setOnClickListener {
            MainActivity.startMainActivity(mContext)
            finish()
        }
    }
}