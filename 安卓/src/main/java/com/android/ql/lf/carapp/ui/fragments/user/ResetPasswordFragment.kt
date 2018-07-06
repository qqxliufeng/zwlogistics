package com.android.ql.lf.carapp.ui.fragments.user

import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.getTextString
import com.android.ql.lf.carapp.utils.isEmpty
import com.android.ql.lf.carapp.utils.toast
import kotlinx.android.synthetic.main.fragment_reset_password_layout.*
import org.json.JSONObject

/**
 * Created by lf on 18.2.5.
 * @author lf on 18.2.5
 */
class ResetPasswordFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_reset_password_layout

    override fun initView(view: View?) {
        mBtEtResetPasswordSave.setOnClickListener {
            if (mEtResetPasswordOldPW.isEmpty()) {
                toast("请输入原密码")
                return@setOnClickListener
            }
            if (mEtResetPasswordNewOne.isEmpty()) {
                toast("请输入新密码")
                return@setOnClickListener
            }
            if (mEtResetPasswordNewTwo.isEmpty()) {
                toast("请再次输入新密码")
                return@setOnClickListener
            }
            if (mEtResetPasswordNewOne.text.toString().trim() != (mEtResetPasswordNewTwo.text.toString().trim())) {
                toast("两次密码不一致")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_EDIT_PW, RequestParamsHelper.getEditPWParams(mEtResetPasswordOldPW.getTextString(), mEtResetPasswordNewOne.getTextString()))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在修改密码……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        val check = checkResultCode(result)
        if (check != null) {
            if (SUCCESS_CODE == check.code) {
                toast("修改成功，请牢记密码")
            } else {
                toast((check.obj as JSONObject).optString("msg"))
            }
        } else {
            toast("修改失败，请稍后重试……")
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("修改失败，请稍后重试……")
    }

}