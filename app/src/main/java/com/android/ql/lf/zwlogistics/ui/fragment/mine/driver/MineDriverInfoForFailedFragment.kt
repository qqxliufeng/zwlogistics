package com.android.ql.lf.zwlogistics.ui.fragment.mine.driver

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.PostDriverAuthBean
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.present.AuthManager
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.android.ql.lf.zwlogistics.utils.showInfoDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.zhihu.matisse.MimeType
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_mine_driver_info_for_failed_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class MineDriverInfoForFailedFragment : BaseNetWorkingFragment(), FragmentContainerActivity.OnBackPressListener {


    private var tempSelectFlag = MinePersonAuthFragment.FACE_FLAG

    private val postDriverAuthBean by lazy {
        PostDriverAuthBean()
    }

    override fun getLayoutId() = R.layout.fragment_mine_driver_info_for_failed_layout

    override fun initView(view: View?) {
        mPresent.getDataByPost(0x1, RequestParamsHelper.getAuthInfoParams())
    }

    private fun uploadInfo() {
        AuthManager.authDriver(mPresent, postDriverAuthBean,
                actionStart = {
                    getFastProgressDialog("正在处理图片……")
                },
                actionEnd = {
                    onRequestEnd(-1)
                },
                actionFailed = {
                    toast("提交申请失败……")
                    onRequestEnd(-1)
                })
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            getFastProgressDialog("正在提交申请……")
        } else if (requestID == 0x1) {
            getFastProgressDialog("正在加载信息……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        handleSuccess(requestID, result)
    }

    override fun onHandleSuccess(requestID: Int, jsonObject: JSONObject) {
        if (requestID == 0x0) {
            toast("提交申请成功！敬请等待后台审核")
            UserInfo.getInstance().user_is_rank = "${UserInfo.UserInfoAuthStatus.SHENG_HE_ZHONG.statusCode}"
            finish()
        } else if (requestID == 0x1) {
            if (jsonObject.optString("user_is_rank") == UserInfo.UserInfoAuthStatus.SHENG_HE_SHI_BAI.statusCode.toString()) {
                mTvDriverInfoForFailTitle.text = "审核失败：${jsonObject.optString("user_rank_content")}"
            }
            mFlDriverInfoForFailFaceContainer.setOnClickListener {
                tempSelectFlag = MinePersonAuthFragment.FACE_FLAG
                openImageChoose(MimeType.ofImage(), 1)
            }
            mFlDriverInfoForFailIdCardFrontContainer.setOnClickListener {
                tempSelectFlag = MinePersonAuthFragment.IDCARD_FRONT_FLAG
                openImageChoose(MimeType.ofImage(), 1)
            }
            mFlDriverInfoForFailIdCardBackgroundContainer.setOnClickListener {
                tempSelectFlag = MinePersonAuthFragment.IDCARD_BACK_GROUND_FLAG
                openImageChoose(MimeType.ofImage(), 1)
            }
            mFlDriverInfoForFailDriverCardContainer.setOnClickListener {
                tempSelectFlag = MinePersonAuthFragment.DRIVER_FLAG
                openImageChoose(MimeType.ofImage(), 1)
            }
            mFlDriverInfoForFailCYZGContainer.setOnClickListener {
                tempSelectFlag = MinePersonAuthFragment.CYZG_FLAG
                openImageChoose(MimeType.ofImage(), 1)
            }
            mTvDriverInfoForFailSubmit.setOnClickListener {
                postDriverAuthBean.driverName = mEtDriverInfoForFailName.text.toString()
                postDriverAuthBean.driverIdCardNum = mEtDriverInfoForFailIdCardNum.text.toString()
                postDriverAuthBean.driverPhone = mEtDriverInfoForFailPhone.text.toString()
                val isEmpty = postDriverAuthBean.isFeildEmpty
                if ("" != isEmpty) {
                    toast(isEmpty)
                    return@setOnClickListener
                }
                uploadInfo()
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x0) {
            onRequestEnd(requestID)
            toast("提交申请失败……")
        } else if (requestID == 0x1) {
            toast("信息加载失败")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            AuthManager.handleResultData(data, tempSelectFlag, postDriverAuthBean) {
                when (tempSelectFlag) {
                    MinePersonAuthFragment.FACE_FLAG -> {
                        setImageUri(mIvDriverInfoForFailFace, mTvDriverInfoForFailFace, it)
                    }
                    MinePersonAuthFragment.IDCARD_FRONT_FLAG -> {
                        setImageUri(mIvDriverInfoForFailIdCardFront, mTvDriverInfoForFailIdCardFront, it)
                    }
                    MinePersonAuthFragment.IDCARD_BACK_GROUND_FLAG -> {
                        setImageUri(mIvDriverInfoForFailIdCardBack, mTvDriverInfoForFailIdCardBack, it)
                    }
                    MinePersonAuthFragment.DRIVER_FLAG -> {
                        setImageUri(mIvDriverInfoForFailDriverCard, mTvDriverInfoForFailDriverCard, it)
                    }
                    MinePersonAuthFragment.CYZG_FLAG -> {
                        setImageUri(mIvDriverInfoForFailCYZG, mTvDriverInfoForFailCYZG, it)
                    }
                }
            }
        }
    }

    private fun setImageUri(imageView: ImageView, textView: TextView, uri: String) {
        textView.visibility = View.GONE
        Glide.with(this).load(uri).bitmapTransform(CenterCrop(mContext), RoundedCornersTransformation(mContext, 20, 0)).into(imageView)
    }

    override fun onBackPress(): Boolean {
        showInfoDialog("身份认证通过才能参与竞标，建议继续完善资料~", "放弃", "继续完善", {
            finish()
        }, null)
        return true
    }

}