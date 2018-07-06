package com.android.ql.lf.carapp.ui.fragments.mall.order

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ImageBean
import com.android.ql.lf.carapp.present.MallOrderPresent
import com.android.ql.lf.carapp.ui.adapter.OrderImageUpLoadAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_order_comment_submit_layout.*
import okhttp3.MultipartBody
import java.util.ArrayList

/**
 * Created by lf on 18.3.28.
 * @author lf on 18.3.28
 */
class OrderCommentSubmitFragment : BaseNetWorkingFragment() {

    companion object {
        val ORDER_ID_FLAG = "order_id_flag"
        val ORDER_SN_FLAG = "order_sn_flag"
        val PRODUCT_ID_FLAG = "product_id_flag"
    }

    private val imageList = arrayListOf<ImageBean>()

    private val MAX_SELECTED_ITEMS = 3


    private val mBaseAdapter: BaseQuickAdapter<ImageBean, BaseViewHolder> by lazy {
        OrderImageUpLoadAdapter(R.layout.adapter_write_article_image_up_load_item_layout, imageList)
    }

    override fun getLayoutId(): Int = R.layout.fragment_order_comment_submit_layout

    override fun initView(view: View?) {
        val bmp = BitmapFactory.decodeResource(context.resources, R.drawable.img_icon_star_n)
        val layoutParams = mRbGoodsCommentStart.layoutParams
        layoutParams.height = bmp.height
        layoutParams.width = -2
        mRbGoodsCommentStart.layoutParams = layoutParams
        imageList.add(ImageBean(R.drawable.img_icon_add_photo, null))
        mRvOrderComment.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvOrderComment.adapter = mBaseAdapter
        mRvOrderComment.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (mBaseAdapter.itemCount - 1 >= MAX_SELECTED_ITEMS) {
                    toast("最多只能选择三张图片")
                    return
                }
                val item = mBaseAdapter.getItem(position)
                if (item != null) {
                    if (item.resId != null) {
                        openImageChoose(MimeType.ofImage(), MAX_SELECTED_ITEMS - (mBaseAdapter.itemCount - 1))
                    }
                }
            }
        })
        mBtGoodsCommentSubmit.setOnClickListener {
            if (mEtGoodsCommentContent.isEmpty()) {
                toast("请输入商品评价")
                return@setOnClickListener
            }
            if (mEtGoodsCommentContent.getTextString().length < 10) {
                toast("请输入至少10个字")
                return@setOnClickListener
            }
            if (imageList.size > 1) {
                ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                    override fun onActionEnd(builder: MultipartBody.Builder?) {
                        builder!!.addFormDataPart("oid", arguments.getString(ORDER_ID_FLAG, ""))
                        builder.addFormDataPart("gid", arguments.getString(PRODUCT_ID_FLAG, ""))
                        builder.addFormDataPart("content", mEtGoodsCommentContent.getTextString())
                        builder.addFormDataPart("f", mRbGoodsCommentStart.rating.toString())
                        builder.addFormDataPart("sn", arguments.getString(ORDER_SN_FLAG,""))
                        mPresent.uploadFile(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_EVALUATE, builder.build().parts())
                    }

                    override fun onActionFailed() {
                        toast("上传失败")
                    }

                    override fun onActionStart() {
                        getFastProgressDialog("正在上传……")
                    }
                }).upload(imageList.filter { it.uriPath != null } as ArrayList<ImageBean>)
            } else {
                mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_EVALUATE,
                        RequestParamsHelper.getEvaluateParam(arguments.getString(ORDER_ID_FLAG, ""),
                                arguments.getString(PRODUCT_ID_FLAG, ""),
                                mEtGoodsCommentContent.text.toString(),
                                mRbGoodsCommentStart.rating.toString(),
                                arguments.getString(ORDER_SN_FLAG,"")
                        ))
            }
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在上传……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null) {
            MallOrderPresent.notifyRefreshOrderList()
            MallOrderPresent.notifyRefreshOrderNum()
            toast("评价成功！")
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val uris = Matisse.obtainPathResult(data)
            uris.forEach {
                imageList.add(0, ImageBean(null, it))
            }
            mBaseAdapter.notifyDataSetChanged()
        }
    }
}