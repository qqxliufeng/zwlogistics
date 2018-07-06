package com.android.ql.lf.carapp.ui.fragments.user

import android.os.CountDownTimer
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.*
import kotlinx.android.synthetic.main.fragment_reset_wallet_password_layout.*
import org.json.JSONObject

/**
 * Created by lf on 18.4.11.
 * @author lf on 18.4.11
 */
class ResetWalletPasswordFragment : BaseNetWorkingFragment() {

    companion object {
        val PASSWORD_TYPE_FLAG = "password_type_flag"

        val RESET_PASSWORD_FLAG = 1
        val SET_PASSWORD_FLAG = 2
        val FORGET_PASSWORD_FLAG = 3
    }

    private var tempAct = ""
    private var apiParams: ApiParams? = null

    private var passwordType = RESET_PASSWORD_FLAG

    private var code: String? = null
    private val timeCount: CountDownTimer by lazy {
        object : CountDownTimer(1000 * 60, 1000) {
            override fun onFinish() {
                mTvResetWalletPasswordGetCode.text = "没有收到验证码？"
                mTvResetWalletPasswordGetCode.isEnabled = true
            }

            override fun onTick(millisUntilFinished: Long) {
                mTvResetWalletPasswordGetCode.text = "${millisUntilFinished / 1000}秒"
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_reset_wallet_password_layout

    override fun initView(view: View?) {
        passwordType = arguments.getInt(PASSWORD_TYPE_FLAG, RESET_PASSWORD_FLAG)
        when (passwordType) {
            RESET_PASSWORD_FLAG -> {
                tempAct = RequestParamsHelper.ACT_EDIT_SECOND_PW
                mEtResetWalletPasswordPW.hint = "请输入6位纯数字原始密码"
                mEtResetWalletPasswordConfirmPW.hint = "请输入6位纯数字新密码"
            }
            SET_PASSWORD_FLAG -> {
                tempAct = RequestParamsHelper.ACT_SET_SECOND_PW
                mEtResetWalletPasswordPW.hint = "请输入6位纯数字密码"
                mEtResetWalletPasswordConfirmPW.hint = "请再次输入6位纯数字密码"
            }
            FORGET_PASSWORD_FLAG -> {
                tempAct = RequestParamsHelper.ACT_FORGET_SECOND_PW
                mEtResetWalletPasswordPW.hint = "请输入6位纯数字密码"
                mEtResetWalletPasswordConfirmPW.hint = "请再次输入6位纯数字密码"
            }
        }
        mEtResetWalletPasswordPhone.setText(UserInfo.getInstance().memberPhone)
        mIvResetWalletPasswordClearPW.setOnClickListener {
            mEtResetWalletPasswordPW.setText("")
        }
        mIvResetWalletPasswordClearConfirmPW.setOnClickListener {
            mEtResetWalletPasswordConfirmPW.setText("")
        }

        mTvResetWalletPasswordGetCode.setOnClickListener {
            mTvResetWalletPasswordGetCode.isEnabled = false
            timeCount.start()
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.LOGIN_MODEL,
                    RequestParamsHelper.ACT_CODE,
                    RequestParamsHelper.getCodeParams(mEtResetWalletPasswordPhone.text.toString()))
        }

        mBtResetWalletPassword.setOnClickListener {
            if (mEtResetWalletPasswordPhone.isEmpty()) {
                mEtResetWalletPasswordPhone.showSnackBar("手机号不能为空")
                return@setOnClickListener
            }
            if (!mEtResetWalletPasswordPhone.isPhone()) {
                mEtResetWalletPasswordPhone.showSnackBar("请输入正确的手机号")
                return@setOnClickListener
            }
            if (mEtResetWalletPasswordCode.isEmpty()) {
                mEtResetWalletPasswordCode.showSnackBar("请输入验证码")
                return@setOnClickListener
            }
            if (code != mEtResetWalletPasswordCode.getTextString()) {
                mEtResetWalletPasswordCode.showSnackBar("请输入正确的验证码")
                return@setOnClickListener
            }
            if (mEtResetWalletPasswordPW.isEmpty()) {
                mEtResetWalletPasswordCode.showSnackBar(mEtResetWalletPasswordPW.hint.toString())
                return@setOnClickListener
            }
            if (mEtResetWalletPasswordPW.getTextString().length != 6) {
                mEtResetWalletPasswordCode.showSnackBar("请输入6位密码")
                return@setOnClickListener
            }
            if (mEtResetWalletPasswordConfirmPW.isEmpty()) {
                mEtResetWalletPasswordCode.showSnackBar(mEtResetWalletPasswordConfirmPW.hint.toString())
                return@setOnClickListener
            }
            if (mEtResetWalletPasswordConfirmPW.getTextString().length != 6) {
                mEtResetWalletPasswordCode.showSnackBar("请输入6位密码")
                return@setOnClickListener
            }
            if (passwordType == SET_PASSWORD_FLAG) {
                if (mEtResetWalletPasswordPW.getTextString() != mEtResetWalletPasswordConfirmPW.getTextString()) {
                    mEtResetWalletPasswordCode.showSnackBar("两次密码不一致")
                    return@setOnClickListener
                }
            }
            when (arguments.getInt(PASSWORD_TYPE_FLAG, RESET_PASSWORD_FLAG)) {
                RESET_PASSWORD_FLAG -> {
                    apiParams = RequestParamsHelper.getEditSecondPW(mEtResetWalletPasswordPhone.getTextString(),
                            mEtResetWalletPasswordPW.getTextString(), mEtResetWalletPasswordConfirmPW.getTextString())
                }
                SET_PASSWORD_FLAG -> {
                    apiParams = RequestParamsHelper.getSetSecondPW(mEtResetWalletPasswordPhone.getTextString(), mEtResetWalletPasswordConfirmPW.getTextString())
                }
                FORGET_PASSWORD_FLAG -> {
                    apiParams = RequestParamsHelper.getForgetSecondPW(mEtResetWalletPasswordPhone.getTextString(), mEtResetWalletPasswordConfirmPW.getTextString())
                }
            }
            mPresent.getDataByPost(0x1,
                    RequestParamsHelper.MEMBER_MODEL,
                    tempAct,
                    apiParams)
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when (requestID) {
            0x0 -> {
                val messageResultJson = JSONObject(result.toString())
                if (SUCCESS_CODE == messageResultJson.optString("status")) {
                    code = messageResultJson.optString("code")
                    toast("短信已经发送，请注意查收")
                }
            }
            0x1 -> {
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    toast("密码修改成功")
                    UserInfo.getInstance().memberSecondPw = mEtResetWalletPasswordConfirmPW.getTextString()
                    finish()
                } else {
                    toast((check.obj as JSONObject).optString("msg"))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timeCount.cancel()
    }
}