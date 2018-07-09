package com.android.ql.lf.zwlogistics.ui.fragment.mine

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.CountDownTimer
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
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MinePersonAuthFragment
import com.android.ql.lf.zwlogistics.ui.fragment.other.DetailContentFragment
import com.android.ql.lf.zwlogistics.utils.Constants
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import kotlinx.android.synthetic.main.fragment_bind_phone_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class BindPhoneFragment :BaseNetWorkingFragment(){


    private var code: String? = null
    private val timeCount: CountDownTimer by lazy {
        object : CountDownTimer(Constants.MAX_COUNT_DOWN, 1000) {
            override fun onFinish() {
                mTvBindPhoneGetCode.text = "没有收到验证码？"
                mTvBindPhoneGetCode.isEnabled = true
            }

            override fun onTick(millisUntilFinished: Long) {
                mTvBindPhoneGetCode.text = "${millisUntilFinished / 1000}秒"
            }
        }
    }


    private val userPresent by lazy {
        UserPresent()
    }

    override fun getLayoutId() = R.layout.fragment_bind_phone_layout

    override fun initView(view: View?) {
        (mContext as FragmentContainerActivity).setToolBarBackgroundColor(Color.WHITE)
        (mContext as FragmentContainerActivity).setStatusBarLightColor(false)
        val toolbar = (mContext as FragmentContainerActivity).toolbar
        toolbar.setTitleTextColor(Color.DKGRAY)
        toolbar.navigationIcon!!.setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP)
        mCbBindPhoneProtocol.isChecked = true
        mTvBindPhoneGetCode.setOnClickListener {
            if (mEtBindPhone.isEmpty()){
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!mEtBindPhone.isPhone()){
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            timeCount.start()
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.getPhoneParam(mEtBindPhone.getTextString()))
        }
        mTvBindPhoneProtocol.setOnClickListener {
            FragmentContainerActivity
                    .from(mContext)
                    .setTitle("用户服务协议")
                    .setClazz(DetailContentFragment::class.java)
                    .setExtraBundle(bundleOf(Pair(DetailContentFragment.PARAM_FLAG, RequestParamsHelper.getUserProtocolParam())))
                    .start()
        }
        mBtBindPhoneLogin.setOnClickListener {
            if (mEtBindPhone.isEmpty()){
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!mEtBindPhone.isPhone()){
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (mEtBindPhoneCode.isEmpty()){
                toast("请输入验证码")
                return@setOnClickListener
            }
            if (mEtBindPhoneCode.getTextString() != code){
                toast("请输入正确的验证码")
                return@setOnClickListener
            }
            if (!mCbBindPhoneProtocol.isChecked){
                toast("请先同意《用户服务协议》")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x1,RequestParamsHelper.getBindPhoneParams(arguments!!.getString("info"),mEtBindPhone.getTextString()))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        when (requestID) {
            0x0 -> getFastProgressDialog("正在获取验证码……")
            0x1 -> getFastProgressDialog("正在绑定手机号……")
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
                        toast("绑定成功！")
                        onLogin(check)
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
            0x1 -> toast("绑定手机号失败……")
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

    override fun onDestroyView() {
        timeCount.cancel()
        super.onDestroyView()
    }

}