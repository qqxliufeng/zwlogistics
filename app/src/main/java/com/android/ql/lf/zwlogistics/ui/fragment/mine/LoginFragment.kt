package com.android.ql.lf.zwlogistics.ui.fragment.mine

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import com.android.ql.lf.carapp.utils.isEmpty
import com.android.ql.lf.carapp.utils.isPhone
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseFragment
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import kotlinx.android.synthetic.main.fragment_login_layout.*
import org.jetbrains.anko.support.v4.toast

class LoginFragment : BaseNetWorkingFragment(){

    companion object {
        fun startLogin(context: Context) {
            FragmentContainerActivity.from(context).setClazz(LoginFragment::class.java).setNeedNetWorking(true).setTitle("登录").start()
        }
    }

    override fun getLayoutId() = R.layout.fragment_login_layout

    override fun initView(view: View?) {
        (mContext as FragmentContainerActivity).setToolBarBackgroundColor(Color.WHITE)
        (mContext as FragmentContainerActivity).setStatusBarLightColor(false)
        val toolbar = (mContext as FragmentContainerActivity).toolbar
        toolbar.setTitleTextColor(Color.DKGRAY)
        toolbar.navigationIcon!!.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP)
        mTvLoginRegister.setOnClickListener {
            FragmentContainerActivity.from(mContext).setClazz(RegisterFragment::class.java).setTitle("注册").setNeedNetWorking(true).start()
        }
        mTvLoginForgetPassword.setOnClickListener {
            FragmentContainerActivity.from(mContext).setClazz(ForgetPasswordFragment::class.java).setTitle("忘记密码").setNeedNetWorking(true).start()
        }
        mBtLogin.setOnClickListener {
            if (mEtLoginUserName.isEmpty()){
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!mEtLoginUserName.isPhone()){
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (mEtLoginUserPassword.isEmpty()){
                toast("请输入密码")
                return@setOnClickListener
            }
        }
        mFlLoginWxContainer.setOnClickListener {
            FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setTitle("手机号绑定").setClazz(BindPhoneFragment::class.java).start()
        }
    }


}