package com.android.ql.lf.zwlogistics.ui.fragment.mine

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import com.android.ql.lf.carapp.utils.getTextString
import com.android.ql.lf.carapp.utils.isEmpty
import com.android.ql.lf.carapp.utils.isPhone
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.BaseNetResult
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.present.UserPresent
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.car.MineAuthSuccessFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MinePersonAuthFragment
import com.android.ql.lf.zwlogistics.utils.Constants
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.android.ql.lf.zwlogistics.utils.ThirdLoginManager
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.android.synthetic.main.fragment_login_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class LoginFragment : BaseNetWorkingFragment(), IUiListener {


    companion object {
        fun startLogin(context: Context) {
            FragmentContainerActivity.from(context).setClazz(LoginFragment::class.java).setNeedNetWorking(true).setTitle("登录").start()
        }
    }

    private lateinit var qqLoginInfo: ThirdLoginManager.QQLoginInfoBean

    private val userPresent: UserPresent by lazy {
        UserPresent()
    }


    override fun getLayoutId() = R.layout.fragment_login_layout

    override fun initView(view: View?) {
        registerLoginSuccessBus()
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
        mFlLoginQQContainer.setOnClickListener {
            ThirdLoginManager.qqLogin(Tencent.createInstance(Constants.TENCENT_ID, mContext.applicationContext), this@LoginFragment, this@LoginFragment)
        }
        mEtLoginUserName.setText("15910101117")
        mEtLoginUserPassword.setText("123456")
        mBtLogin.setOnClickListener {
            if (mEtLoginUserName.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!mEtLoginUserName.isPhone()) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (mEtLoginUserPassword.isEmpty()) {
                toast("请输入密码")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0, RequestParamsHelper.getLoginParams(mEtLoginUserName.getTextString(), mEtLoginUserPassword.getTextString()))
        }
        mFlLoginWxContainer.setOnClickListener {
            FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setTitle("手机号绑定").setClazz(BindPhoneFragment::class.java).start()
        }
    }


    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在登录……")
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("登录失败……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when (requestID) {
            0x0 -> {
                val check = checkResultCode(result)
                if (check != null) {
                    if (check.code == SUCCESS_CODE) {
                        onLogin(check)
                    } else {
                        toast((check.obj as JSONObject).optString(MSG_FLAG))
                    }
                }
            }
            0x1 -> {
                val check = checkResultCode(result)
                if (check != null) {
                    when (check.code) {
                        SUCCESS_CODE -> {
                            onLogin(check)
                        }
                        "202" -> { //需要完善资料
                            FragmentContainerActivity.from(mContext)
                                    .setNeedNetWorking(true)
                                    .setTitle("绑定手机号")
                                    .setExtraBundle(bundleOf(Pair("info", (check.obj as JSONObject).optJSONObject("data").optString("user_id"))))
                                    .setClazz(BindPhoneFragment::class.java)
                                    .start()
                        }
                    }
                }
            }
        }
    }

    private fun onLogin(check: BaseNetResult) {
        val auth = (check.obj as JSONObject).optJSONObject("data").optString("user_is_rank")
        if (auth == "1") {
            //认证司机
            UserInfo.resetLoginSuccessDoActionToken()
            MinePersonAuthFragment.startAuthFragment(mContext,MinePersonAuthFragment.SHOW_JUMP)
        }
        userPresent.onLogin((check.obj as JSONObject).optJSONObject("data"))
        finish()
    }


    //   ==============================QQ登录回调==============================
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN || requestCode == com.tencent.connect.common.Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onComplete(response: Any?) {
        if (response == null) {
            toast("QQ登录失败")
            return
        }
        val jsonResponse = response as JSONObject
        if (jsonResponse.length() == 0) {
            toast("QQ登录失败")
            return
        }
        qqLoginInfo = ThirdLoginManager.getQQLoginInfo(jsonResponse)
        mPresent.getDataByPost(0x1,
                RequestParamsHelper.LOGIN_MODEL,
                RequestParamsHelper.ACT_QQLOGIN,
                RequestParamsHelper.getQQloginParam("",
                        qqLoginInfo.openid,
                        qqLoginInfo.access_token))
    }

    override fun onCancel() {
        toast("登录取消")
    }

    override fun onError(p0: UiError?) {
        toast("QQ登录失败")
    }
    //   ==============================QQ登录回调==============================


    override fun onLoginSuccess(userInfo: UserInfo?) {
        super.onLoginSuccess(userInfo)
        finish()
    }

}