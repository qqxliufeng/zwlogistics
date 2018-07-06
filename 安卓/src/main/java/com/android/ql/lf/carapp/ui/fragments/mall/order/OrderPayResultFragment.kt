package com.android.ql.lf.carapp.ui.fragments.mall.order

import android.os.Bundle
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.RefreshData
import com.android.ql.lf.carapp.present.MallOrderPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseFragment
import com.android.ql.lf.carapp.ui.fragments.user.mine.MainMallOrderItemFragment
import com.android.ql.lf.carapp.ui.fragments.user.mine.MineMallOrderFragment
import com.android.ql.lf.carapp.utils.PreferenceUtils
import com.android.ql.lf.carapp.utils.RxBus
import kotlinx.android.synthetic.main.fragment_pay_result_layout.*
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 18.3.27.
 * @author lf on 18.3.27
 */
class OrderPayResultFragment : BaseFragment() {

    companion object {
        val PAY_CODE_FLAG = "code"
        val PAY_SUCCESS_CODE = 0
        val PAY_FAIL_CODE = -1

        fun newInstance(code: Int): OrderPayResultFragment {
            val fragment = OrderPayResultFragment()
            val bundle = Bundle()
            bundle.putInt(PAY_CODE_FLAG, code)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_pay_result_layout

    override fun initView(view: View?) {
        mBtBack.setOnClickListener {
            finish()
        }
        if (arguments != null) {
            when (arguments.getInt(PAY_CODE_FLAG)) {
                PAY_SUCCESS_CODE -> {
                    mTvPayResultTitle.text = "支付成功"
                    mBtOrderInfo.visibility = View.VISIBLE
                    mBtOrderInfo.setOnClickListener {
                        FragmentContainerActivity
                                .from(mContext)
                                .setNeedNetWorking(true)
                                .setClazz(MineMallOrderFragment::class.java)
                                .setExtraBundle(bundleOf(Pair(MineMallOrderFragment.CURRENT_ITEM_FLAG, 1)))
                                .setTitle("购物订单")
                                .start()
                        finish()
                    }
                    MallOrderPresent.notifyRefreshOrderList()
                    mTvPayResultTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_icon_pitchon_pay_success, 0, 0)
                }
                else -> {
                    mTvPayResultTitle.text = "支付失败"
                    mBtOrderInfo.visibility = View.GONE
                    mTvPayResultTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_icon_pitchon_pay_fail, 0, 0, 0)
                }
            }
        }
    }
}