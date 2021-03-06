package com.android.ql.lf.zwlogistics.ui.fragment.order

import android.view.View
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.activity.MainActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_tender_success_layout.*

class TenderSuccessFragment : BaseFragment() {


    override fun getLayoutId() = R.layout.fragment_tender_success_layout

    override fun initView(view: View?) {
        mBtTenderSuccessBack.setOnClickListener {
            MainActivity.startMainActivity(mContext)
            finish()
        }
        mBtTenderSuccessOrder.setOnClickListener {
            MyOrderInfoFragment.startMyOrderInfo(mContext, arguments!!.getString("oid"))
            finish()
        }
    }
}