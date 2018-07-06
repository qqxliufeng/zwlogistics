package com.android.ql.lf.zwlogistics.ui.fragment.mine

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
import org.jetbrains.anko.support.v4.toast


class MineInviteFragment : BaseNetWorkingFragment() {


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
        }
        content.findViewById<TextView>(R.id.mTvShareQQZone).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        content.findViewById<TextView>(R.id.mTvShareCancel).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        content
    }


    override fun getLayoutId() = R.layout.fragment_mine_invita_layout

    override fun initView(view: View?) {
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
        mIvInviteCode.setImageBitmap(QRCodeUtil.createQRCodeBitmap("123456",500,500))
    }
}