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
import com.android.ql.lf.zwlogistics.utils.Constants
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import kotlinx.android.synthetic.main.fragment_forget_password_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

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
            mPresent.getDataByPost(0x1,
                    RequestParamsHelper.getPhoneParam(mEtForgetPasswordPhone.getTextString()))
        }
        mBtForgetPassword.setOnClickListener {
            if (mEtForgetPasswordPhone.isEmpty()){
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!mEtForgetPasswordPhone.isPhone()){
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (mEtForgetPasswordNew.isEmpty()){
                toast("请输入新密码")
                return@setOnClickListener
            }
            if (mEtForgetPasswordNew.getTextString().length < 6){
                toast("请输入至少6位数新密码")
                return@setOnClickListener
            }
            if (mEtForgetPasswordNewConfirm.isEmpty()){
                toast("请再次输入新密码")
                return@setOnClickListener
            }
            if (mEtForgetPasswordNew.getTextString() != mEtForgetPasswordNewConfirm.getTextString()){
                toast("两次密码不一致")
                return@setOnClickListener
            }
            if (mEtForgetPasswordCode.isEmpty()){
                toast("请输入验证码")
                return@setOnClickListener
            }
            if (mEtForgetPasswordCode.getTextString() != code){
                toast("请输入正确的验证码")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0,RequestParamsHelper.getForgetPWParams(mEtForgetPasswordPhone.getTextString(),mEtForgetPasswordNew.getTextString(),mEtForgetPasswordNewConfirm.getTextString()))

        }
    }


    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        when(requestID){
            0x0->{
                getFastProgressDialog("正在修改密码……")
            }
            0x1->{
                getFastProgressDialog("正在发送短信……")
            }
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when(requestID){
            0x0->{
                val check = checkResultCode(result)
                if (check!=null){
                    if (check.code == SUCCESS_CODE){
                        toast("密码修改成功，请登录……")
                        finish()
                    }else{
                        toast((check.obj as JSONObject).optString(MSG_FLAG))
                    }
                }else{
                    toast("修改密码失败")
                }
            }
            0x1->{
                val check = checkResultCode(result)
                if (check!=null){
                    if (check.code == SUCCESS_CODE){
                        toast("获取验证码成功，请注意查收！")
                        code = (check.obj as JSONObject).optString(RESULT_OBJECT)
                    }else{
                        onRequestFail(requestID,NullPointerException())
                    }
                }else{
                    onRequestFail(requestID,NullPointerException())
                }
            }
        }
    }


    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        when(requestID){
            0x0->{
                toast("修改密码失败")
            }
            0x1->{
                toast("短信发送失败")
                timeCount.onFinish()
            }
        }
    }




    override fun onDestroyView() {
        timeCount.cancel()
        super.onDestroyView()
    }

}