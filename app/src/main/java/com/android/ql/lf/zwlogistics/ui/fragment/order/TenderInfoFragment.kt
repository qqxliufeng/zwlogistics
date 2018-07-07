package com.android.ql.lf.zwlogistics.ui.fragment.order

import android.view.View
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_tender_info_layout.*

class TenderInfoFragment :BaseNetWorkingFragment(){

    override fun getLayoutId() = R.layout.fragment_tender_info_layout

    override fun initView(view: View?) {
        mBtTenderInfoSubmit.setOnClickListener {
            FragmentContainerActivity.from(mContext).setClazz(TenderSuccessFragment::class.java).setTitle("竞标成功").start()
        }
    }

}