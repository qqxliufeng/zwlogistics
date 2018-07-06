package com.android.ql.lf.carapp.ui.fragments.order

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.ui.fragments.BaseFragment
import com.android.ql.lf.carapp.ui.fragments.user.mine.MineEnsureMoneyFragment
import com.android.ql.lf.carapp.utils.RxBus
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_pay_result_layout.*

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class PayResultFragment : BaseFragment() {

    companion object {
        val PAY_ORDER_RESULT_JSON_FLAG = "create_order_result_json"
        val PAY_CODE_FLAG = "code"
        val PAY_SUCCESS_CODE = 0
        val PAY_FAIL_CODE = -1

        fun newInstance(code: Int): PayResultFragment {
            val fragment = PayResultFragment()
            val bundle = Bundle()
            bundle.putInt(PAY_CODE_FLAG, code)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_pay_result_no_order_layout

    override fun initView(view: View?) {
        mBtBack.setOnClickListener {
            finish()
        }
        if (arguments != null) {
            when (arguments.getInt(PAY_CODE_FLAG)) {
                PAY_SUCCESS_CODE -> {
                    mTvPayResultTitle.text = "支付成功"
                    mBtBack.text = "立即返回"
                    RxBus.getDefault().post(MineEnsureMoneyFragment.WxPaySuccessBean(true))
                    mTvPayResultTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_pitchon_pay_success, 0, 0, 0)
                    mBtBack.setOnClickListener {
                        finish()
                    }
                }
                else -> {
                    mTvPayResultTitle.text = "支付失败"
                    mBtBack.text = "立即返回"
                    mBtBack.setOnClickListener {
                        finish()
                    }
                    mTvPayResultTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_pitchon_pay_fail, 0, 0, 0)
                }
            }
        }
    }
}