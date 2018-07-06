package com.android.ql.lf.carapp.ui.fragments.user

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.CountDownTimer
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.*
import kotlinx.android.synthetic.main.fragment_forget_password_layout.*
import org.json.JSONObject

/**
 * Created by lf on 18.1.24.
 * @author lf on 18.1.24
 */
class ForgetPasswordFragment : BaseNetWorkingFragment() {

    private var code: String? = null
    private val timeCount: CountDownTimer by lazy {
        object : CountDownTimer(1000 * 60, 1000) {
            override fun onFinish() {
                mTvForgetPasswordGetCode.text = "没有收到验证码？"
                mTvForgetPasswordGetCode.isEnabled = true
            }

            override fun onTick(millisUntilFinished: Long) {
                mTvForgetPasswordGetCode.text = "${millisUntilFinished / 1000}秒"
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
        mIvForgetPasswordClearPhone.setOnClickListener {
            mEtForgetPasswordPhone.setText("")
        }
        mIvForgetPasswordClearPW.setOnClickListener {
            mEtForgetPasswordPW.setText("")
        }
        mIvForgetPasswordClearConfirmPW.setOnClickListener {
            mEtForgetPasswordConfirmPW.setText("")
        }
        mTvForgetPasswordGetCode.setOnClickListener {
            mContext.hiddenKeyBoard(mEtForgetPasswordPhone.windowToken)
            if (mEtForgetPasswordPhone.isEmpty()) {
                mEtForgetPasswordPhone.showSnackBar("手机号不能为空")
                return@setOnClickListener
            }
            if (!mEtForgetPasswordPhone.isPhone()) {
                mEtForgetPasswordPhone.showSnackBar("请输入正确的手机号")
                return@setOnClickListener
            }
            mTvForgetPasswordGetCode.isEnabled = false
            timeCount.start()
            mPresent.getDataByPost(0,
                    RequestParamsHelper.LOGIN_MODEL,
                    RequestParamsHelper.ACT_CODE,
                    RequestParamsHelper.getCodeParams(mEtForgetPasswordPhone.text.toString()))
        }
        mBtForgetPassword.setOnClickListener {
            if (mEtForgetPasswordPhone.isEmpty()) {
                mEtForgetPasswordPhone.showSnackBar("手机号不能为空")
                return@setOnClickListener
            }
            if (!mEtForgetPasswordPhone.isPhone()) {
                mEtForgetPasswordPhone.showSnackBar("请输入正确的手机号")
                return@setOnClickListener
            }
            if (mEtForgetPasswordCode.isEmpty()) {
                mEtForgetPasswordCode.showSnackBar("请输入验证码")
                return@setOnClickListener
            }
            if (code != mEtForgetPasswordCode.getTextString()) {
                mEtForgetPasswordCode.showSnackBar("请输入正确的验证码")
                return@setOnClickListener
            }
            if (mEtForgetPasswordPW.isEmpty()) {
                mEtForgetPasswordCode.showSnackBar("请输入密码")
                return@setOnClickListener
            }
            if (mEtForgetPasswordConfirmPW.isEmpty()) {
                mEtForgetPasswordCode.showSnackBar("请再次输入密码")
                return@setOnClickListener
            }
            if (mEtForgetPasswordPW.getTextString() != mEtForgetPasswordConfirmPW.getTextString()) {
                mEtForgetPasswordCode.showSnackBar("两次密码不一致")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x1, RequestParamsHelper.LOGIN_MODEL, RequestParamsHelper.ACT_FORGETPW, RequestParamsHelper.getForgetPWParams(mEtForgetPasswordPhone.getTextString(), mEtForgetPasswordPW.getTextString()))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在修改密码……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null) {
                if (SUCCESS_CODE == check.code) {
                    toast("密码修改成功，请登录")
                    finish()
                } else {
                    toast((check.obj as JSONObject).optString("msg"))
                }
            }
        } else {
            val messageResultJson = JSONObject(result.toString())
            if (SUCCESS_CODE == messageResultJson.optString("status")) {
                code = messageResultJson.optString("code")
                toast("短信已经发送，请注意查收")
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x1) {
            toast("修改失败，请稍后重试……")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timeCount.cancel()
    }
}