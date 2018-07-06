package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ImageBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.present.UserPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.ui.fragments.user.ResetPasswordFragment
import com.android.ql.lf.carapp.ui.fragments.user.ResetWalletPasswordFragment
import com.android.ql.lf.carapp.utils.*
import com.soundcloud.android.crop.Crop
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_mine_personal_info_layout.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import java.io.File
import java.util.ArrayList

/**
 * Created by lf on 18.2.3.
 * @author lf on 18.2.3
 */
class MinePersonalInfoFragment : BaseNetWorkingFragment() {

    private val userPresent by lazy {
        UserPresent()
    }

    //0  用户名   1 身份证号    2 头像
    private var currentToken = 0

    override fun getLayoutId() = R.layout.fragment_mine_personal_info_layout

    override fun initView(view: View?) {
        GlideManager.loadFaceCircleImage(mContext, UserInfo.getInstance().memberPic, mTvPersonalInfoFace)
        mTvPersonalInfoNickName.text = UserInfo.getInstance().memberName
        if (UserInfo.getInstance().memberPhone!=null && UserInfo.getInstance().memberPhone.length >= 11) {
            mTvPersonalInfoPhone.text = UserInfo.getInstance().memberPhone.let { "${it.substring(0, 3)}****${it.substring(7, it.length)}" }
        }else{
            mTvPersonalInfoPhone.text = "暂无"
        }
        mTvPersonalInfoIdCard.text = if (!TextUtils.isEmpty(UserInfo.getInstance().memberIdCard)) {
            UserInfo.getInstance().memberIdCard
        } else {
            "暂无"
        }
        mFaceContainer.setOnClickListener {
            currentToken = 2
            openImageChoose(MimeType.ofImage(), 1)
        }
        mNickNameContainer.setOnClickListener {
            currentToken = 0
            showEditInfoDialog("修改昵称", UserInfo.getInstance().memberName)
        }
        mIdCardContainer.setOnClickListener {
            currentToken = 1
            showEditInfoDialog("修改身份证号", UserInfo.getInstance().memberIdCard ?: "")
        }
        mTvPersonalInfoResetPassword.setOnClickListener {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "修改密码", ResetPasswordFragment::class.java)
        }
        if (TextUtils.isEmpty(UserInfo.getInstance().memberSecondPw)) {
            mTvPersonalInfoResetWalletPassword.text = "设置钱包密码"
            mTvPersonalInfoResetWalletPassword.setOnClickListener {
                FragmentContainerActivity.from(mContext).setTitle("设置钱包密码")
                        .setExtraBundle(bundleOf(Pair(ResetWalletPasswordFragment.PASSWORD_TYPE_FLAG, ResetWalletPasswordFragment.SET_PASSWORD_FLAG)))
                        .setNeedNetWorking(true)
                        .setClazz(ResetWalletPasswordFragment::class.java).start()
            }
        } else {
            mTvPersonalInfoResetWalletPassword.text = "修改钱包密码"
            mTvPersonalInfoResetWalletPassword.setOnClickListener {
                FragmentContainerActivity.from(mContext)
                        .setExtraBundle(bundleOf(Pair(ResetWalletPasswordFragment.PASSWORD_TYPE_FLAG, ResetWalletPasswordFragment.RESET_PASSWORD_FLAG)))
                        .setTitle("修改钱包密码").setNeedNetWorking(true).setClazz(ResetWalletPasswordFragment::class.java).start()
            }
        }
    }

    private fun showEditInfoDialog(title: String, oldInfo: String) {
        val builder = AlertDialog.Builder(mContext)
        builder.setTitle(title)
        val contentView = View.inflate(mContext, R.layout.layout_edit_personal_content_layout, null)
        val content = contentView.findViewById<EditText>(R.id.mEtEditPersonalInfo)
        content.setText(oldInfo)
        content.setSelection(oldInfo.length)
        builder.setNegativeButton("取消", null)
        builder.setPositiveButton("确定") { _, _ ->
            if (oldInfo == content.text.toString().trim()) {
                return@setPositiveButton
            }
            var nickName = ""
            var idCard = ""
            when (currentToken) {
                0 -> {
                    nickName = content.getTextString()
                }
                1 -> {
                    if (!content.isIdCard()) {
                        toast("请输入正确的身份证号")
                        return@setPositiveButton
                    }
                    idCard = content.getTextString()
                }
            }
            mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_EDIT_PERSONAL,
                    RequestParamsHelper.getEditPersonalParam(account = nickName, idcard = idCard))
        }
        builder.setView(contentView)
        builder.create().show()
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            getFastProgressDialog("正在修改……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (check != null) {
            if (SUCCESS_CODE == check.code) {
                when (currentToken) {
                    0 -> {
                        val nameResult = (check.obj as JSONObject).optJSONObject("result").optString("member_name")
                        mTvPersonalInfoNickName.text = nameResult
                        userPresent.modifyInfoForName(nameResult)
                    }
                    1 -> {
                        val idCardResult = (check.obj as JSONObject).optJSONObject("result").optString("member_idcard")
                        mTvPersonalInfoIdCard.text = idCardResult
                    }
                    2 -> {
                        val picResult = (check.obj as JSONObject).optJSONObject("result").optString("member_pic")
                        GlideManager.loadFaceCircleImage(mContext, picResult, mTvPersonalInfoFace)
                        userPresent.modifyInfoForPic(picResult)
                    }
                }
            } else {
                toast((check.obj as JSONObject).optString("msg"))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val uris = Matisse.obtainResult(data)
            uris[0].let {
                val dir = File("${Constants.IMAGE_PATH}face/")
                if (!dir.exists()) {
                    dir.mkdirs()
                }
                val desUri = Uri.fromFile(File(dir, "${System.currentTimeMillis()}.jpg"))
                Crop.of(it, desUri).start(mContext, this@MinePersonalInfoFragment)
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            if (data != null) {
                val uri = Crop.getOutput(data)
                ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                    override fun onActionFailed() {
                        toast("头像上传失败，请稍后重试！")
                    }

                    override fun onActionStart() {
                        getFastProgressDialog("正在上传头像……")
                    }

                    override fun onActionEnd(builder: MultipartBody.Builder) {
                        mPresent.uploadFile(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_EDIT_PERSONAL, builder.build().parts())
                    }
                }).upload(arrayListOf(ImageBean(null, uri.path)))
            }
        }
    }
}