package com.android.ql.lf.zwlogistics.ui.fragment.mine

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.CountDownTimer
import android.view.View
import com.android.ql.lf.carapp.utils.getTextString
import com.android.ql.lf.carapp.utils.isEmpty
import com.android.ql.lf.carapp.utils.isPhone
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.other.DetailContentFragment
import com.android.ql.lf.zwlogistics.utils.Constants
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import kotlinx.android.synthetic.main.fragment_register_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class RegisterFragment : BaseNetWorkingFragment() {

    private var code: String? = null
    private val timeCount: CountDownTimer by lazy {
        object : CountDownTimer(Constants.MAX_COUNT_DOWN, 1000) {
            override fun onFinish() {
                mTvRegisterGetCode.text = "没有收到验证码？"
                mTvRegisterGetCode.isEnabled = true
            }

            override fun onTick(millisUntilFinished: Long) {
                mTvRegisterGetCode.text = "${millisUntilFinished / 1000}秒"
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_register_layout

    override fun initView(view: View?) {
        (mContext as FragmentContainerActivity).setToolBarBackgroundColor(Color.WHITE)
        (mContext as FragmentContainerActivity).setStatusBarLightColor(false)
        val toolbar = (mContext as FragmentContainerActivity).toolbar
        toolbar.setTitleTextColor(Color.DKGRAY)
        toolbar.navigationIcon!!.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP)
        mCbRegisterProtocol.isChecked = true
        mTvRegisterGetCode.setOnClickListener {
            if (mEtRegisterUserName.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!mEtRegisterUserName.isPhone()) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            timeCount.start()
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.getPhoneParam(mEtRegisterUserName.getTextString()))
        }

        mTvRegisterProtocol.setOnClickListener {
            FragmentContainerActivity
                    .from(mContext)
                    .setTitle("《用户服务协议》")
                    .setClazz(DetailContentFragment::class.java)
                    .setExtraBundle(bundleOf(Pair(DetailContentFragment.PARAM_FLAG, RequestParamsHelper.getUserProtocolParam("0"))))
                    .start()
        }

        mBtRegister.setOnClickListener {
            if (mEtRegisterUserName.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!mEtRegisterUserName.isPhone()) {
                toast("请输入正确的手机号")
                mEtRegisterUserName.setText("")
                return@setOnClickListener
            }
            if (mEtRegisterPassword.isEmpty() || mEtRegisterPassword.getTextString().length < 6) {
                toast("请输入至少6位数密码")
                return@setOnClickListener
            }
            if (mEtRegisterCode.isEmpty()) {
                toast("请输入验证码")
                return@setOnClickListener
            }
            if (mEtRegisterCode.getTextString() != code) {
                toast("输入验证码错误，请重新输入")
                mEtRegisterCode.setText("")
                return@setOnClickListener
            }
            if (!mCbRegisterProtocol.isChecked){
                toast("请先同意《用户服务协议》")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x1,
                    RequestParamsHelper.getRegisterParams(
                            mEtRegisterUserName.getTextString(),
                            mEtRegisterPassword.getTextString(),
                            if (mEtRegisterInviteCode.isEmpty()) {
                                ""
                            } else {
                                mEtRegisterInviteCode.getTextString()
                            }))
        }

    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        when (requestID) {
            0x0 -> getFastProgressDialog("正在获取验证码……")
            0x1 -> getFastProgressDialog("正在注册……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        when (requestID) {
            0x0 -> {
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    toast("获取验证码成功，请注意查收！")
                    code = (check.obj as JSONObject).optString(RESULT_OBJECT)
                } else {
                    onRequestFail(requestID,NullPointerException())
                }
            }
            0x1 -> {
                val check = checkResultCode(result)
                if (check!=null){
                    if (check.code == SUCCESS_CODE){
                        toast("恭喜，注册成功，请登录")
                        finish()
                    }else{
                        toast((check.obj as JSONObject).optString(MSG_FLAG))
                    }
                }
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        when (requestID) {
            0x0 -> {
                toast("获取验证码失败！")
                timeCount.onFinish()
            }
            0x1 -> toast("用户注册失败……")
        }
    }


    override fun onDestroyView() {
        timeCount.cancel()
        super.onDestroyView()
    }

}