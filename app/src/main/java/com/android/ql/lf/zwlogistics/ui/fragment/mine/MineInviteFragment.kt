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
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.utils.Constants
import com.android.ql.lf.zwlogistics.utils.ThirdShareManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tencent.connect.share.QQShare
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.jetbrains.anko.support.v4.toast
import java.lang.Exception


class MineInviteFragment : BaseNetWorkingFragment(), IUiListener {


    //分享对话框
    private val bottomSheetDialog by lazy {
        BottomSheetDialog(mContext)
    }

    private val iwxapi by lazy {
        WXAPIFactory.createWXAPI(mContext,Constants.WX_APP_ID,true)
    }


    //分享布局
    private val shareContentView by lazy {
        val content = View.inflate(mContext, R.layout.dialog_invite_share_layout, null)
        content.findViewById<TextView>(R.id.mTvShareWX).setOnClickListener {
            bottomSheetDialog.dismiss()
            Glide.with(this)
                    .load(UserInfo.getInstance().sharePic)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(object : SimpleTarget<Bitmap>(150,150) {
                        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                            ThirdShareManager.wxShare(iwxapi,resource,SendMessageToWX.Req.WXSceneSession)
                        }

                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            toast("分享失败")
                        }
                    })
        }

        content.findViewById<TextView>(R.id.mTvShareWXCircle).setOnClickListener {
            bottomSheetDialog.dismiss()
            Glide.with(this)
                    .load(UserInfo.getInstance().sharePic)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(object : SimpleTarget<Bitmap>(150,150) {
                        override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                            ThirdShareManager.wxShare(iwxapi,resource,SendMessageToWX.Req.WXSceneTimeline)
                        }

                        override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                            super.onLoadFailed(e, errorDrawable)
                            toast("分享失败")
                        }
                    })
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
        mIvInviteCode.postDelayed({ mIvInviteCode.setImageBitmap(QRCodeUtil.createQRCodeBitmap(UserInfo.getInstance().shareUrl, 500, 500)) },100)
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