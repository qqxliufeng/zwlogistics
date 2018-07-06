package com.android.ql.lf.carapp.ui.fragments.user

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.CountDownTimer
import android.os.Parcelable
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.present.UserPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.*
import kotlinx.android.synthetic.main.fragment_third_login_complete_info_layout.*
import org.json.JSONObject

/**
 * Created by lf on 18.2.24.
 * @author lf on 18.2.24
 */
class ThirdLoginCompleteInfoFragment : BaseNetWorkingFragment() {

    private var info: Parcelable? = null

    private val userPresent: UserPresent by lazy {
        UserPresent()
    }

    private var code: String? = null
    private val timeCount: CountDownTimer by lazy {
        object : CountDownTimer(1000 * 60, 1000) {
            override fun onFinish() {
                mTvCompleteGetCode.text = "没有收到验证码？"
                mTvCompleteGetCode.isEnabled = true
            }

            override fun onTick(millisUntilFinished: Long) {
                mTvCompleteGetCode.text = "${millisUntilFinished / 1000}秒"
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_third_login_complete_info_layout

    override fun initView(view: View?) {
        (mContext as FragmentContainerActivity).setToolBarBackgroundColor(Color.WHITE)
        (mContext as FragmentContainerActivity).setStatusBarLightColor(false)
        val toolbar = (mContext as FragmentContainerActivity).toolbar
        toolbar.setTitleTextColor(Color.DKGRAY)
        toolbar.navigationIcon!!.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP)
        arguments.classLoader = this.javaClass.classLoader
        info = arguments.getParcelable("info")

        mTvCompleteGetCode.setOnClickListener {
            mContext.hiddenKeyBoard(mEtCompleteName.windowToken)
            if (mEtCompleteName.isEmpty()) {
                mEtCompleteName.showSnackBar("手机号不能为空")
                return@setOnClickListener
            }
            if (!mEtCompleteName.isPhone()) {
                mEtCompleteName.showSnackBar("请输入正确的手机号")
                return@setOnClickListener
            }
            timeCount.start()
            mPresent.getDataByPost(0,
                    RequestParamsHelper.LOGIN_MODEL,
                    RequestParamsHelper.ACT_CODE,
                    RequestParamsHelper.getCodeParams(mEtCompleteName.text.toString()))
        }
        mIvCompleteName.setOnClickListener {
            mEtCompleteName.setText("")
        }
        mBtCompleteLogin.setOnClickListener {
            if (mEtCompleteName.isEmpty()) {
                mEtCompleteName.showSnackBar("手机号不能为空")
                return@setOnClickListener
            }
            if (!mEtCompleteName.isPhone()) {
                mEtCompleteName.showSnackBar("请输入正确的手机号")
                return@setOnClickListener
            }
            if (code != mEtCompleteCode.text.toString()) {
                mEtCompleteCode.showSnackBar("请输入正确的验证码")
                return@setOnClickListener
            }
            if (info is ThirdLoginManager.QQLoginInfoBean) {
                mPresent.getDataByPost(0x1,
                        RequestParamsHelper.LOGIN_MODEL,
                        RequestParamsHelper.ACT_QQLOGIN,
                        RequestParamsHelper.getQQloginParam(mEtCompleteName.getTextString(),
                                (info as ThirdLoginManager.QQLoginInfoBean).openid,
                                (info as ThirdLoginManager.QQLoginInfoBean).access_token))
            } else if (info is ThirdLoginManager.WXUserInfo) {
                mPresent.getDataByPost(0x1,
                        RequestParamsHelper.LOGIN_MODEL,
                        RequestParamsHelper.ACT_WX_PERFECT,
                        RequestParamsHelper.getWXCompleteDataParam(
                                mEtCompleteName.getTextString(),
                                (info as ThirdLoginManager.WXUserInfo).headimgurl,
                                (info as ThirdLoginManager.WXUserInfo).openid,
                                (info as ThirdLoginManager.WXUserInfo).nickname))
            }
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在完善信息……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            val messageResultJson = JSONObject(result.toString())
            if (SUCCESS_CODE == messageResultJson.optString("status")) {
                code = messageResultJson.optString("code")
                toast("短信已经发送，请注意查收")
            }
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null) {
                if (check.code != SUCCESS_CODE) {
                    toast((check.obj as JSONObject).optString("msg"))
                } else {
                    userPresent.onLogin((check.obj as JSONObject).optJSONObject("result"), (check.obj as JSONObject).optJSONObject("arr"))
                    finish()
                }
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x0) {
            toast("获取验证码失败，请稍后重试")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timeCount.cancel()
    }
}