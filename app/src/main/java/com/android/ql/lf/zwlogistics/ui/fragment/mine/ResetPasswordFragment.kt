package com.android.ql.lf.zwlogistics.ui.fragment.mine

import android.view.View
import com.android.ql.lf.carapp.utils.getTextString
import com.android.ql.lf.carapp.utils.isEmpty
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import kotlinx.android.synthetic.main.fragment_reset_password_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class ResetPasswordFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_reset_password_layout

    override fun initView(view: View?) {
        mBtResetPasswordSave.setOnClickListener {
            if (mEtResetOldPassword.isEmpty()) {
                toast("请输入原密码")
                return@setOnClickListener
            }
            if (mEtResetNewPassword.isEmpty()) {
                toast("请输入新密码")
                return@setOnClickListener
            }
            if (mEtResetNewPassword.getTextString().length < 6) {
                toast("新密码长度不能少于6位")
                return@setOnClickListener
            }
            if (mEtResetConfirmNewPassword.isEmpty()) {
                toast("请再次输入新密码")
                return@setOnClickListener
            }
            if (mEtResetNewPassword.getTextString() != mEtResetConfirmNewPassword.getTextString()) {
                toast("两次密码不一致")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0, RequestParamsHelper.getResetPasswordParam(mEtResetOldPassword.getTextString(), mEtResetNewPassword.getTextString(), mEtResetConfirmNewPassword.getTextString()))
        }
    }


    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            getFastProgressDialog("正在修改密码……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            val check = checkResultCode(result)
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    toast("密码修改成功，请牢记！")
                    finish()
                } else {
                    toast((check.obj as JSONObject).optString(MSG_FLAG))
                }
            } else {
                toast("密码修改失败")
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x0) {
            toast("密码修改失败")
        }
    }
}