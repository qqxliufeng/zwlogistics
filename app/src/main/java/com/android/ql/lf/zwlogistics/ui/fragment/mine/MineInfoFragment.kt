package com.android.ql.lf.zwlogistics.ui.fragment.mine

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.android.ql.lf.carapp.data.ImageBean
import com.android.ql.lf.carapp.utils.getTextString
import com.android.ql.lf.carapp.utils.isEmpty
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.present.UserPresent
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.utils.Constants
import com.android.ql.lf.zwlogistics.utils.GlideManager
import com.android.ql.lf.zwlogistics.utils.ImageUploadHelper
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.soundcloud.android.crop.Crop
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_mine_info_layout.*
import okhttp3.MultipartBody
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import java.io.File

class MineInfoFragment : BaseNetWorkingFragment() {


    private val userPresent by lazy {
        UserPresent()
    }


    override fun getLayoutId() = R.layout.fragment_mine_info_layout

    override fun initView(view: View?) {
        if (TextUtils.isEmpty(UserInfo.getInstance().user_pic)) {
            mIvMineInfoFace.setImageResource(R.drawable.icon_default_face)
        } else {
            GlideManager.loadFaceCircleImage(mContext, UserInfo.getInstance().user_pic, mIvMineInfoFace)
        }
        mTvMineInfoNickName.text = UserInfo.getInstance().user_nickname

        mRlMineInfoFaceContainer.setOnClickListener {
            openImageChoose(MimeType.ofImage(), 1)
        }

        mTvMineInfoResetPassword.setOnClickListener {
            FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setTitle("修改密码").setClazz(ResetPasswordFragment::class.java).start()
        }

        mRlMineInfoNickNameContainer.setOnClickListener {
            showEditInfoDialog("请输入昵称", mTvMineInfoNickName.text.toString())
        }

        mBtMineInfoLogout.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setMessage("是否要退出登录？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("退出") { dialog, which ->
                        userPresent.onLogout()
                        finish()
                    }
            builder.create().show()
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
            if (content.isEmpty()){
                toast("请输入昵称")
                return@setPositiveButton
            }
            mPresent.getDataByPost(0x0, RequestParamsHelper.getUpdateNickNameParams(content.getTextString()))
        }
        builder.setView(contentView)
        builder.create().show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val resultData = Matisse.obtainResult(data)
                resultData[0].let {
                    val dir = File("${Constants.IMAGE_PATH}face/")
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    val desUri = Uri.fromFile(File(dir, "${System.currentTimeMillis()}.jpg"))
                    Crop.of(it, desUri).start(mContext, this@MineInfoFragment)
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
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
                        builder.addFormDataPart("uid",UserInfo.getInstance().user_id)
                        mPresent.uploadFile(0x1, RequestParamsHelper.USER_MODEL, RequestParamsHelper.ACT_UPDATE_FACE, builder.build().parts())
                    }
                }).upload(arrayListOf(ImageBean(null, uri.path)))
            }
        }
    }


    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0){
            getFastProgressDialog("正在修改昵称……")
        }
    }


    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when(requestID){
            0x0->{
                val check = checkResultCode(result)
                if (check!=null){
                    if (check.code == SUCCESS_CODE){
                        toast("昵称修改成功")
                        val data = (check.obj as JSONObject).optString(RESULT_OBJECT)
                        userPresent.modifyInfoForName(data)
                        mTvMineInfoNickName.text = data
                    }else{
                        toast("昵称修改失败……")
                    }
                }else{
                    toast("昵称修改失败……")
                }
            }
            0x1->{
                val check = checkResultCode(result)
                if (check!=null){
                    if (check.code == SUCCESS_CODE){
                        toast("头像修改成功")
                        val data = (check.obj as JSONObject).optString(RESULT_OBJECT)
                        userPresent.modifyInfoForPic(data)
                        GlideManager.loadFaceCircleImage(mContext, data, mIvMineInfoFace)
                    }else{
                        toast("头像修改失败……")
                    }
                }else{
                    toast("头像修改失败……")
                }
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x1){
            toast("头像修改失败……")
        }else{
            toast("昵称修改失败……")
        }
    }

}