package com.android.ql.lf.zwlogistics.ui.fragment.mine

import android.app.Activity
import android.support.design.widget.BottomSheetDialog
import android.view.View
import android.widget.TextView
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.utils.QRCodeUtil
import kotlinx.android.synthetic.main.fragment_mine_invita_layout.*
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.utils.Constants
import com.android.ql.lf.zwlogistics.utils.ThirdShareManager
import com.tencent.connect.share.QQShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.jetbrains.anko.support.v4.toast


class MineInviteFragment : BaseNetWorkingFragment(), IUiListener {


    //分享对话框
    private val bottomSheetDialog by lazy {
        BottomSheetDialog(mContext)
    }

    //分享布局
    private val shareContentView by lazy {
        val content = View.inflate(mContext, R.layout.dialog_invite_share_layout, null)
        content.findViewById<TextView>(R.id.mTvShareWX).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        content.findViewById<TextView>(R.id.mTvShareWXCircle).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        content.findViewById<TextView>(R.id.mTvShareQQ).setOnClickListener {
            bottomSheetDialog.dismiss()
            ThirdShareManager.qqShare(
                    (mContext as Activity),
                    Tencent.createInstance(Constants.TENCENT_ID,
                            mContext.applicationContext),
                    this)
        }

        content.findViewById<TextView>(R.id.mTvShareQQZone).setOnClickListener {
            bottomSheetDialog.dismiss()
            ThirdShareManager.zoneShare(
                    (mContext as Activity),
                    Tencent.createInstance(Constants.TENCENT_ID,
                            mContext.applicationContext),
                    this)
        }

        content.findViewById<TextView>(R.id.mTvShareCancel).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        content
    }


    override fun getLayoutId() = R.layout.fragment_mine_invita_layout

    override fun initView(view: View?) {
        mTvInviteCodeText.text = UserInfo.getInstance().user_code
        mBtInviteCodeShare.setOnClickListener {
            if (shareContentView.parent == null) {
                bottomSheetDialog.setContentView(shareContentView)
            }
            bottomSheetDialog.show()
        }
        mBtInviteCodeCopy.setOnClickListener {
            val cm = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val mClipData = ClipData.newPlainText("Label", mTvInviteCodeText.text)
            cm.primaryClip = mClipData
            toast("复制成功")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mIvInviteCode.postDelayed({ mIvInviteCode.setImageBitmap(QRCodeUtil.createQRCodeBitmap(UserInfo.getInstance().user_code, 500, 500)) },100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Tencent.onActivityResultData(requestCode, resultCode, data, this)
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_QQ_SHARE || requestCode == com.tencent.connect.common.Constants.REQUEST_QZONE_SHARE) {
            Tencent.handleResultData(data, this)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onComplete(p0: Any?) {
        toast("分享成功")
    }

    override fun onCancel() {
        toast("分享取消")
    }

    override fun onError(p0: UiError?) {
        toast("分享失败")
    }

}