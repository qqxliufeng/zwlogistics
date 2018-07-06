package com.android.ql.lf.carapp.ui.fragments.user

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.present.UserPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.*
import com.google.gson.Gson
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.android.synthetic.main.fragment_login_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject


/**
 * Created by lf on 18.1.24.
 * @author lf on 18.1.24
 */
class LoginFragment : BaseNetWorkingFragment(), IUiListener {

    companion object {
        fun startLogin(context: Context) {
            FragmentContainerActivity.from(context).setClazz(LoginFragment::class.java).setNeedNetWorking(true).setTitle("登录").start()
        }
    }

    private val userPresent: UserPresent by lazy {
        UserPresent()
    }

    private val wxLoginSubscription by lazy {
        RxBus.getDefault().toObservable(BaseResp::class.java).subscribe {
            if (it is SendAuth.Resp) {
                val param = ApiParams()
                param.addParam("code", it.code)
                mPresent.getDataByGet(0x2, "t", "wxlogin", param)
            }
        }
    }

    private lateinit var qqLoginInfo: ThirdLoginManager.QQLoginInfoBean
    private lateinit var wxLoginInfo: ThirdLoginManager.WXUserInfo

    private lateinit var iwxApi: IWXAPI

    override fun getLayoutId() = R.layout.fragment_login_layout

    override fun initView(view: View?) {
        registerLoginSuccessBus()
        wxLoginSubscription
        (mContext as FragmentContainerActivity).setToolBarBackgroundColor(Color.WHITE)
        (mContext as FragmentContainerActivity).setStatusBarLightColor(false)
        val toolbar = (mContext as FragmentContainerActivity).toolbar
        toolbar.setTitleTextColor(Color.DKGRAY)
        toolbar.navigationIcon!!.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP)
        mTvLoginRegister.setOnClickListener {
            FragmentContainerActivity.from(mContext)
                    .setTitle("注册")
                    .setNeedNetWorking(true)
                    .setClazz(RegisterFragment::class.java)
                    .start()
        }
        mTvLoginForgetPassword.setOnClickListener {
            FragmentContainerActivity.from(mContext)
                    .setTitle("忘记密码")
                    .setNeedNetWorking(true)
                    .setClazz(ForgetPasswordFragment::class.java)
                    .start()
        }
        mIvLoginClearName.setOnClickListener {
            mEtLoginName.setText("")
        }
        mIvLoginClearPassword.setOnClickListener {
            mEtLoginPassword.setText("")
        }
        mIvQQLogin.setOnClickListener {
            ThirdLoginManager.qqLogin(Tencent.createInstance(Constants.TENCENT_ID, mContext.applicationContext), this@LoginFragment, this@LoginFragment)
        }
        mIvWXLogin.setOnClickListener {
            iwxApi = WXAPIFactory.createWXAPI(mContext, Constants.WX_APP_ID, true)
            ThirdLoginManager.wxLogin(iwxApi)
        }
        mBtLogin.setOnClickListener {
            if (mEtLoginName.isEmpty()) {
                mEtLoginName.showSnackBar("手机号不能为空")
                return@setOnClickListener
            }
            if (!mEtLoginName.isPhone()) {
                mEtLoginName.showSnackBar("请输入正确的手机号")
                return@setOnClickListener
            }
            if (mEtLoginPassword.isEmpty()) {
                mEtLoginPassword.showSnackBar("密码不能为空")
                return@setOnClickListener
            }
            mPresent.getDataByPost(
                    0x0,
                    RequestParamsHelper.LOGIN_MODEL, RequestParamsHelper.ACT_LOGIN,
                    RequestParamsHelper.getLoginParams(mEtLoginName.text.toString(), mEtLoginPassword.text.toString()))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在登录……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            val checkResult = checkResultCode(result)
            if (checkResult != null) {
                val jsonObject = checkResult.obj as JSONObject
                if (SUCCESS_CODE == checkResult.code) {
                    toast("登录成功")
                    userPresent.onLogin(jsonObject.optJSONObject("result"), jsonObject.optJSONObject("arr"))
                } else {
                    toast(jsonObject.optString("msg"))
                }
            } else {
                toast("登录失败，请稍后重试……")
            }
        } else if (requestID == 0x1) { //qq 登录
            val check = checkResultCode(result)
            if (check != null) {
                when (check.code) {
                    SUCCESS_CODE -> {
                        userPresent.onLogin((check.obj as JSONObject).optJSONObject("result"), (check.obj as JSONObject).optJSONObject("arr"))
                    }
                    "202" -> { //需要完善资料
                        FragmentContainerActivity.from(mContext)
                                .setNeedNetWorking(true)
                                .setTitle("完善资料")
                                .setExtraBundle(bundleOf(Pair("info", qqLoginInfo)))
                                .setClazz(ThirdLoginCompleteInfoFragment::class.java)
                                .start()
                    }
                }
            }
        } else if (requestID == 0x2) { //  微信登录
            val check = checkResultCode(result)
            if (check != null) {
                when (check.code) {
                    SUCCESS_CODE -> {
                        userPresent.onLogin((check.obj as JSONObject).optJSONObject("result"), (check.obj as JSONObject).optJSONObject("arr"))
                    }
                    "202" -> { // 授权成功 需要完善资料
                        wxLoginInfo = Gson().fromJson((check.obj as JSONObject).optJSONObject("result").toString(), ThirdLoginManager.WXUserInfo::class.java)
                        FragmentContainerActivity.from(mContext)
                                .setNeedNetWorking(true)
                                .setTitle("完善资料")
                                .setExtraBundle(bundleOf(Pair("info", wxLoginInfo)))
                                .setClazz(ThirdLoginCompleteInfoFragment::class.java)
                                .start()
                    }
                }
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("登录失败，请稍后重试……")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN || requestCode == com.tencent.connect.common.Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onLoginSuccess(userInfo: UserInfo?) {
        super.onLoginSuccess(userInfo)
        JPushInterface.setDebugMode(true)
        JPushInterface.setAlias(mContext, 0, UserInfo.getInstance().memberAlias)
        JPushInterface.init(mContext.applicationContext)
        finish()
    }

    //                QQ登录回调                              //
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

    override fun onDestroyView() {
        unsubscribe(wxLoginSubscription)
        super.onDestroyView()
    }

}