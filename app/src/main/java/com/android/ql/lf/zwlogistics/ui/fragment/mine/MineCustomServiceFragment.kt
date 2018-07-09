package com.android.ql.lf.zwlogistics.ui.fragment.mine

import android.view.View
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseFragment
import com.android.ql.lf.zwlogistics.utils.startPhone
import kotlinx.android.synthetic.main.fragment_custom_service_layout.*

class MineCustomServiceFragment :BaseFragment(){

    override fun getLayoutId() = R.layout.fragment_custom_service_layout

    override fun initView(view: View?) {
        mTvCustomServicePhone.text = UserInfo.getInstance().kephone
        mTvCustomServicePhone.setOnClickListener {
            startPhone(mTvCustomServicePhone.text.toString())
        }
    }
}