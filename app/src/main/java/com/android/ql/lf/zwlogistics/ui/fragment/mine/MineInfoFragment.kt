package com.android.ql.lf.zwlogistics.ui.fragment.mine

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import com.android.ql.lf.carapp.utils.getTextString
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.utils.Constants
import com.android.ql.lf.zwlogistics.utils.GlideManager
import com.soundcloud.android.crop.Crop
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_mine_info_layout.*
import java.io.File

class MineInfoFragment :BaseNetWorkingFragment(){

    override fun getLayoutId() = R.layout.fragment_mine_info_layout

    override fun initView(view: View?) {
        mRlMineInfoFaceContainer.setOnClickListener {
            openImageChoose(MimeType.ofImage(),1)
        }
        mTvMineInfoResetPassword.setOnClickListener {
            FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setTitle("修改密码").setClazz(ResetPasswordFragment::class.java).start()
        }
        mRlMineInfoNickNameContainer.setOnClickListener {
            showEditInfoDialog("请输入昵称",mTvMineInfoNickName.text.toString())
        }
        mBtMineInfoLogout.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setMessage("是否要退出登录？")
                    .setNegativeButton("取消",null)
                    .setPositiveButton("退出"){dialog, which ->

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
            mTvMineInfoNickName.text = content.getTextString()
//            mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_EDIT_PERSONAL,
//                    RequestParamsHelper.getEditPersonalParam(account = nickName, idcard = idCard))
        }
        builder.setView(contentView)
        builder.create().show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK){
            if (data!=null) {
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
        }else if (requestCode == Crop.REQUEST_CROP && resultCode == Activity.RESULT_OK){
            if (data!=null){
                val uri = Crop.getOutput(data)
                GlideManager.loadFaceCircleImage(mContext,uri.path,mIvMineInfoFace)
            }
        }
    }
}