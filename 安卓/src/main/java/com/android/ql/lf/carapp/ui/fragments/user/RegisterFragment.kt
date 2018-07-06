package com.android.ql.lf.carapp.ui.fragments.user

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.CountDownTimer
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ProtocolBean
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.ui.fragments.WebViewContentFragment
import com.android.ql.lf.carapp.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_register_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject

/**
 * Created by lf on 18.1.24.
 * @author lf on 18.1.24
 */
class RegisterFragment : BaseNetWorkingFragment() {

    private var code: String? = null
    private val timeCount: CountDownTimer by lazy {
        object : CountDownTimer(1000 * 60, 1000) {
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
        mIvRegisterClearPhone.setOnClickListener {
            mEtRegisterPhone.setText("")
        }
        mIvRegisterClearPassword.setOnClickListener {
            mEtRegisterPassword.setText("")
        }
        mTvRegisterGetCode.setOnClickListener {
            mContext.hiddenKeyBoard(mEtRegisterPhone.windowToken)
            if (mEtRegisterPhone.isEmpty()) {
                mEtRegisterPhone.showSnackBar("手机号不能为空")
                return@setOnClickListener
            }
            if (!mEtRegisterPhone.isPhone()) {
                mEtRegisterPhone.showSnackBar("请输入正确的手机号")
                return@setOnClickListener
            }
            timeCount.start()
            mPresent.getDataByPost(0,
                    RequestParamsHelper.LOGIN_MODEL,
                    RequestParamsHelper.ACT_CODE,
                    RequestParamsHelper.getCodeParams(mEtRegisterPhone.text.toString()))
        }
        mBtRegister.setOnClickListener {
            if (mEtRegisterPhone.isEmpty()) {
                mEtRegisterPhone.showSnackBar("手机号不能为空")
                return@setOnClickListener
            }
            if (!mEtRegisterPhone.isPhone()) {
                mEtRegisterPhone.showSnackBar("请输入正确的手机号")
                return@setOnClickListener
            }
            if (mEtRegisterCode.isEmpty()) {
                mEtRegisterCode.showSnackBar("请输入验证码")
                return@setOnClickListener
            }
            if (code != mEtRegisterCode.text.toString()) {
                mEtRegisterCode.showSnackBar("请输入正确的验证码")
                return@setOnClickListener
            }
            if (mEtRegisterPassword.isEmpty()) {
                mEtRegisterPassword.showSnackBar("请输入密码")
                return@setOnClickListener
            }
            if (!mCbRegisterProtocol.isChecked) {
                mEtRegisterPassword.showSnackBar("请先同意用户服务协议")
                return@setOnClickListener
            }
            mPresent.getDataByPost(
                    0x1,
                    RequestParamsHelper.LOGIN_MODEL,
                    RequestParamsHelper.ACT_REGISTER,
                    RequestParamsHelper.getRegisterParams(mEtRegisterPhone.text.toString(), mEtRegisterPassword.text.toString()))
        }
        mTvRegisterProtocol.setOnClickListener {
            mPresent.getDataByPost(0x2, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PTGG, RequestParamsHelper.getPtggParam("11"))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在注册……")
        } else if (requestID == 0x2) {
            getFastProgressDialog("正在加载……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (result != null) {
            if (requestID == 0x0) {
                val messageResultJson = JSONObject(result.toString())
                if (SUCCESS_CODE == messageResultJson.optString("status")) {
                    code = messageResultJson.optString("code")
                    toast("短信已经发送，请注意查收")
                }
            } else if (requestID == 0x1) {
                val baseNetResult = checkResultCode(result)
                if (SUCCESS_CODE == baseNetResult.code) {
                    toast("注册成功，请登录")
                    finish()
                } else {
                    toast((baseNetResult.obj as JSONObject).optString("msg"))
                }
            } else if (requestID == 0x2) {
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    val protocolBean = Gson().fromJson((check.obj as JSONObject).optJSONObject("result").toString(), ProtocolBean::class.java)
                    FragmentContainerActivity.from(mContext)
                            .setNeedNetWorking(true)
                            .setTitle(protocolBean.ptgg_title)
                            .setExtraBundle(bundleOf(Pair(WebViewContentFragment.PATH_FLAG, protocolBean.ptgg_content)))
                            .setClazz(WebViewContentFragment::class.java)
                            .start()
                } else {
                    toast("获取服务协议失败")
                }
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x0) {
            toast("获取验证码失败，请稍后重试")
        } else if (requestID == 0x1) {
            toast("注册失败，请稍后重试")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timeCount.cancel()
    }

}