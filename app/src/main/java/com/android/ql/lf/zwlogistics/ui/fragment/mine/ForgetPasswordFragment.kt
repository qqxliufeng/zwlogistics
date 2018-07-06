package com.android.ql.lf.zwlogistics.ui.fragment.mine

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.CountDownTimer
import android.view.View
import com.android.ql.lf.carapp.utils.isEmpty
import com.android.ql.lf.carapp.utils.isPhone
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseFragment
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.utils.Constants
import kotlinx.android.synthetic.main.fragment_forget_password_layout.*
import org.jetbrains.anko.support.v4.toast

class ForgetPasswordFragment : BaseNetWorkingFragment(){

    private var code: String? = null
    private val timeCount: CountDownTimer by lazy {
        object : CountDownTimer(Constants.MAX_COUNT_DOWN, 1000) {
            override fun onFinish() {
                mTvForgetPasswordCode.text = "没有收到验证码？"
                mTvForgetPasswordCode.isEnabled = true
            }

            override fun onTick(millisUntilFinished: Long) {
                mTvForgetPasswordCode.text = "${millisUntilFinished / 1000}秒"
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_forget_password_layout

    override fun initView(view: View?) {
        (mContext as FragmentContainerActivity).setToolBarBackgroundColor(Color.WHITE)
        (mContext as FragmentContainerActivity).setStatusBarLightColor(false)
        val toolbar = (mContext as FragmentContainerActivity).toolbar
        toolbar.setTitleTextColor(Color.DKGRAY)
        toolbar.navigationIcon!!.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP)
        mTvForgetPasswordCode.setOnClickListener {
            if (mEtForgetPasswordPhone.isEmpty()){
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!mEtForgetPasswordPhone.isPhone()){
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            timeCount.start()
        }
    }

    override fun onDestroyView() {
        timeCount.cancel()
        super.onDestroyView()
    }

}