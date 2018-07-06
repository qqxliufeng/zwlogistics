package com.android.ql.lf.carapp.ui.fragments.order

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ImageBean
import com.android.ql.lf.carapp.ui.adapter.OrderImageUpLoadAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.Constants
import com.android.ql.lf.carapp.utils.ImageUploadHelper
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.fragment_order_image_upload_layout.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.ArrayList

/**
 * Created by lf on 18.1.29.
 * @author lf on 18.1.29
 */
class OrderImageUpLoadFragment : BaseNetWorkingFragment() {

    private val mArrayList = arrayListOf<ImageBean>()
    private val mBaseAdapter: BaseQuickAdapter<ImageBean, BaseViewHolder> by lazy {
        OrderImageUpLoadAdapter(R.layout.adapter_order_image_up_load_item_layout, mArrayList)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId() = R.layout.fragment_order_image_upload_layout

    override fun initView(view: View?) {
        mArrayList.add(ImageBean(R.drawable.img_icon_add_photo, null))
        mRecyclerView.adapter = mBaseAdapter
        mRecyclerView.layoutManager = GridLayoutManager(mContext, 2)
        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (mBaseAdapter.itemCount - 1 >= 3) {
                    toast("最多只能选择三张图片")
                    return
                }
                val item = mBaseAdapter.getItem(position)
                if (item != null) {
                    if (item.resId != null) {
                        Matisse.from(this@OrderImageUpLoadFragment)
                                .choose(MimeType.ofImage())
                                .imageEngine(GlideEngine())
                                .capture(true)
                                .maxSelectable(3 - (mBaseAdapter.itemCount - 1))
                                .captureStrategy(CaptureStrategy(true, Constants.FILE_PROVIDE_PATH))
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                .thumbnailScale(0.85f)
                                .theme(R.style.my_matisse_style)
                                .forResult(0)
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.upload_image_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.upload) {
            if (mArrayList.size == 1) {
                return true
            }
            ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                override fun onActionStart() {
                    getFastProgressDialog("正在上传照片……")
                }

                override fun onActionEnd(builder: MultipartBody.Builder) {
                    builder.addFormDataPart("oid", arguments.getString("oid"))
                    mPresent.uploadFile(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_PLAY_PIC, builder.build().parts())
                }

                override fun onActionFailed() {
                    toast("上传失败……")
                }
            }).upload(mArrayList.filter { it.uriPath != null } as ArrayList<ImageBean>)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val uris = Matisse.obtainPathResult(data)
            uris.forEach {
                mArrayList.add(0, ImageBean(null, it))
            }
            mBaseAdapter.notifyDataSetChanged()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (check != null && check.code == SUCCESS_CODE) {
            toast("上传成功")
            finish()
        } else {
            toast("上传失败")
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("上传失败")
    }

}