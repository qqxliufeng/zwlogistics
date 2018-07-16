package com.android.ql.lf.zwlogistics.ui.fragment.mine.driver

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.ImageView
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.PostDriverAuthBean
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.present.AuthManager
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.activity.MainActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.car.NewCarAuthFragment
import com.android.ql.lf.zwlogistics.utils.showInfoDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.zhihu.matisse.MimeType
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_mine_person_auth_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast

class MinePersonAuthFragment : BaseNetWorkingFragment(), FragmentContainerActivity.OnBackPressListener {

    companion object {
        const val IS_SHOW_JUMP = "is_show_jump"
        const val SHOW_JUMP = 0
        const val NO_SHOW_JUMP = 1

        const val FACE_FLAG = 0
        const val IDCARD_FRONT_FLAG = 1
        const val IDCARD_BACK_GROUND_FLAG = 2
        const val DRIVER_FLAG = 3
        const val CYZG_FLAG = 4


        fun startAuthFragment(mContext: Context, showJump: Int) {
            FragmentContainerActivity
                    .from(mContext)
                    .setClazz(MinePersonAuthFragment::class.java)
                    .setTitle("司机身份认证")
                    .setExtraBundle(bundleOf(Pair(IS_SHOW_JUMP, showJump)))
                    .setNeedNetWorking(true)
                    .start()
        }
    }

    private val isShowJump by lazy {
        arguments!!.getInt(IS_SHOW_JUMP) == SHOW_JUMP
    }

    private val postDriverAuthBean by lazy {
        PostDriverAuthBean()
    }

    private var tempSelectFlag = FACE_FLAG

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }


    override fun getLayoutId() = R.layout.fragment_mine_person_auth_layout

    override fun initView(view: View?) {
        (mContext as FragmentContainerActivity).setOnBackPressListener(this)
        if (isShowJump) {
            mAsvStep.visibility = View.VISIBLE
            mTvPersonAuthNext.text = "下一步"
        } else {
            mTvPersonAuthNext.text = "提交申请"
            mAsvStep.visibility = View.GONE
        }

        mTvPersonAuthNext.setOnClickListener {
            postDriverAuthBean.driverName = mEtAuthDriverName.text.toString()
            postDriverAuthBean.driverIdCardNum = mEtAuthDriverIdCard.text.toString()
            postDriverAuthBean.driverPhone = mEtAuthDriverPhone.text.toString()
            val isEmpty = postDriverAuthBean.isFeildEmpty
            if ("" != isEmpty) {
                toast(isEmpty)
                return@setOnClickListener
            }
            uploadInfo()
        }

        mFlAuthDriverFaceContainer.setOnClickListener {
            tempSelectFlag = FACE_FLAG
            openImageChoose(MimeType.ofImage(), 1)
        }
        mFlmFlAuthDriverIdCardFrontContainer.setOnClickListener {
            tempSelectFlag = IDCARD_FRONT_FLAG
            openImageChoose(MimeType.ofImage(), 1)
        }
        mFlAuthDriverIdCardBackContainer.setOnClickListener {
            tempSelectFlag = IDCARD_BACK_GROUND_FLAG
            openImageChoose(MimeType.ofImage(), 1)
        }
        mFlAuthDriverCardContainer.setOnClickListener {
            tempSelectFlag = DRIVER_FLAG
            openImageChoose(MimeType.ofImage(), 1)
        }
        mFlAuthDriverCYZGCardContainer.setOnClickListener {
            tempSelectFlag = CYZG_FLAG
            openImageChoose(MimeType.ofImage(), 1)
        }
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
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            val check = checkResultCode(result)
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    toast("提交申请成功！敬请等待后台审核")
                    UserInfo.getInstance().user_is_rank = "${UserInfo.UserInfoAuthStatus.SHENG_HE_ZHONG.statusCode}"
                    if (isShowJump) {
                        NewCarAuthFragment.startCarAuthFragment(mContext, SHOW_JUMP)
                    }
                    finish()
                } else {
                    onRequestFail(requestID, NullPointerException())
                }
            } else {
                onRequestFail(requestID, NullPointerException())
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x0) {
            onRequestEnd(requestID)
            toast("提交申请失败……")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            AuthManager.handleResultData(data, tempSelectFlag, postDriverAuthBean) {
                when (tempSelectFlag) {
                    MinePersonAuthFragment.FACE_FLAG -> {
                        setImageUri(mIvAuthDriverFace, it)
                    }
                    MinePersonAuthFragment.IDCARD_FRONT_FLAG -> {
                        setImageUri(mIvmIvAuthDriverIdCardFront, it)
                    }
                    MinePersonAuthFragment.IDCARD_BACK_GROUND_FLAG -> {
                        setImageUri(mIvmIvAuthDriverIdCardBackGround, it)
                    }
                    MinePersonAuthFragment.DRIVER_FLAG -> {
                        setImageUri(mIvAuthDriverCard, it)
                    }
                    MinePersonAuthFragment.CYZG_FLAG -> {
                        setImageUri(mIvAuthDriverCYZG, it)
                    }
                }
            }
        }
    }

    private fun setImageUri(imageView: ImageView, uri: String) {
        imageView.visibility = View.VISIBLE
        Glide.with(this).load(uri).bitmapTransform(CenterCrop(mContext), RoundedCornersTransformation(mContext, 20, 0)).into(imageView)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (isShowJump) {
            inflater!!.inflate(R.menu.auth_person, menu)
        } else {
            super.onCreateOptionsMenu(menu, inflater)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.mMenuJump) {
            showInfoDialog("身份认证通过才能参与竞标，建议继续完善资料~", "放弃", "继续完善", {
                startActivity(Intent(mContext, MainActivity::class.java))
                finish()
            }, null)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPress(): Boolean {
        showInfoDialog("身份认证通过才能参与竞标，建议继续完善资料~", "放弃", "继续完善", {
            finish()
        }, null)
        return true
    }
}