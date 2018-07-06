package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.EventIsMasterAndMoneyBean
import com.android.ql.lf.carapp.data.ImageBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.activities.SelectAddressActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.*
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_mine_apply_master_info_submit_layout.*
import okhttp3.MultipartBody
import org.json.JSONArray

/**
 * Created by lf on 18.2.12.
 * @author lf on 18.2.12
 */
class MineApplySallerInfoSubmitFragment : BaseNetWorkingFragment() {

    private val storeImagesList by lazy {
        arrayListOf<ImageBean>()
    }

    private val idCardImageList by lazy {
        arrayListOf<ImageBean>()
    }

    private val licenceImageList by lazy {
        arrayListOf<ImageBean>()
    }

    private val storeImagesAdapter by lazy {
        ImageAdapter(R.layout.adapter_apply_master_info_image_item_layout, storeImagesList)
    }

    private val idCardImagesAdapter by lazy {
        ImageAdapter(R.layout.adapter_apply_master_info_image_item_layout, idCardImageList)
    }

    private val licenceImagesAdapter by lazy {
        ImageAdapter(R.layout.adapter_apply_master_info_image_item_layout, licenceImageList)
    }

    private val formStoreImagesList by lazy {
        arrayListOf<String>()
    }

    private val formIdCardImagesList by lazy {
        arrayListOf<String>()
    }

    private val formLicenceImagesList by lazy {
        arrayListOf<String>()
    }

    private val addressSubscription = RxBus.getDefault().toObservable(SelectAddressActivity.SelectAddressItemBean::class.java).subscribe {
        selectAddress = it.name
        mEtMasterInfoAddress.text = selectAddress!!
    }

    private var currentImageFlag = 0

    private var statueCode = true

    private var selectAddress: String? = null

    override fun getLayoutId() = R.layout.fragment_mine_apply_master_info_submit_layout

    override fun initView(view: View?) {
        addressSubscription
        val storeHeaderView = View.inflate(mContext, R.layout.layout_apply_master_add_image_layout, null)
        val idCardHeaderView = View.inflate(mContext, R.layout.layout_apply_master_add_image_layout, null)
        val licenceHeaderView = View.inflate(mContext, R.layout.layout_apply_master_add_image_layout, null)

        mRvApplyMasterInfoStoreImages.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvApplyMasterInfoStoreImages.adapter = storeImagesAdapter
        storeImagesAdapter.addHeaderView(storeHeaderView, -1, LinearLayoutManager.HORIZONTAL)
        storeImagesAdapter.headerLayout.setOnClickListener {
            if (storeImagesList.size >= 3) {
                toast("最多只能上传三张")
                return@setOnClickListener
            }
            currentImageFlag = 0
            openImageChoose(MimeType.ofImage(), 3 - storeImagesList.size)
        }

        mRvApplyMasterInfoIdCardImages.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvApplyMasterInfoIdCardImages.adapter = idCardImagesAdapter
        idCardImagesAdapter.addHeaderView(idCardHeaderView, -1, LinearLayoutManager.HORIZONTAL)
        idCardImagesAdapter.headerLayout.setOnClickListener {
            if (idCardImageList.size >= 2) {
                toast("最多只能上传两张")
                return@setOnClickListener
            }
            currentImageFlag = 1
            openImageChoose(MimeType.ofImage(), 2 - idCardImageList.size)
        }

        mRvApplyMasterInfoLicenceImages.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvApplyMasterInfoLicenceImages.adapter = licenceImagesAdapter
        licenceImagesAdapter.addHeaderView(licenceHeaderView, -1, LinearLayoutManager.HORIZONTAL)
        licenceImagesAdapter.headerLayout.setOnClickListener {
            if (licenceImageList.size >= 1) {
                toast("最多只能上传一张")
                return@setOnClickListener
            }
            currentImageFlag = 2
            openImageChoose(MimeType.ofImage(), 1 - licenceImageList.size)
        }

        mEtMasterInfoAddress.setOnClickListener {
            startActivity(Intent(mContext, SelectAddressActivity::class.java))
            (mContext as FragmentContainerActivity).overridePendingTransition(R.anim.activity_open, 0)
        }

        mBtMasterInfoSubmit.setOnClickListener {
            if (storeImagesList.size <= 0) {
                toast("请上传至少一张门店照片")
                return@setOnClickListener
            }
            if (idCardImageList.size != 2) {
                toast("请上传正反面身份证照片")
                return@setOnClickListener
            }
            if (licenceImageList.isEmpty()) {
                toast("请上传营业执照")
                return@setOnClickListener
            }
            if (mEtMasterInfoStoreName.isEmpty()) {
                toast("请输入店铺名称")
                return@setOnClickListener
            }
            if (mEtMasterInfoPhone.isEmpty()) {
                toast("请输入店铺名称")
                return@setOnClickListener
            }
            if (!mEtMasterInfoPhone.isPhone()) {
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            if (selectAddress == null) {
                toast("请选择店铺地址")
                return@setOnClickListener
            }
            if (mEtMinePersonalServiceDetailAddress.isEmpty()) {
                toast("请输入详情的店铺地址")
                return@setOnClickListener
            }
            if (!TextUtils.isDigitsOnly(mEtMasterInfoMasterNum.text.toString())) {
                toast("请输入合法的店铺师傅数量")
                return@setOnClickListener
            }
            if (mEtMasterInfoMasterNum.isEmpty()) {
                toast("请输入店铺师傅数量")
                return@setOnClickListener
            }
            if (mEtMasterInfoMasterNum.text.toString().toInt() <= 0) {
                toast("输入店铺师傅数量必须大于0")
                return@setOnClickListener
            }
            uploadImage(0x0, storeImagesList, "正在上传门店图片……")
        }
    }

    private fun uploadImage(requestID: Int, tempList: ArrayList<ImageBean>, message: String) {
        ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
            override fun onActionStart() {
                getFastProgressDialog(message)
            }

            override fun onActionEnd(builder: MultipartBody.Builder) {
                mPresent.uploadFile(requestID, "t", "pictime", builder.build().parts())
            }

            override fun onActionFailed() {
                statueCode = false
            }
        }).upload(tempList)
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when (requestID) {
            0x0 -> {
                if (result != null && statueCode) {
                    val imageJsonArray = JSONArray(result as String)
                    (0 until imageJsonArray.length()).forEach {
                        formStoreImagesList.add(imageJsonArray[it] as String)
                    }
                    mBtMasterInfoSubmit.post {
                        uploadImage(0x1, idCardImageList, "正在上传身份证图片……")
                    }
                }
            }
            0x1 -> {
                if (result != null && statueCode) {
                    val imageJsonArray = JSONArray(result as String)
                    (0 until imageJsonArray.length()).forEach {
                        formIdCardImagesList.add(imageJsonArray[it] as String)
                    }
                    mBtMasterInfoSubmit.post {
                        uploadImage(0x2, licenceImageList, "正在上传营业执照图片……")
                    }
                }
            }
            0x2 -> {
                if (result != null && statueCode) {
                    val imageJsonArray = JSONArray(result as String)
                    (0 until imageJsonArray.length()).forEach {
                        formLicenceImagesList.add(imageJsonArray[it] as String)
                    }
                    formIdCardImagesList.addAll(formLicenceImagesList)
                    mBtMasterInfoSubmit.post {
                        mPresent.getDataByPost(0x3,
                                RequestParamsHelper.MEMBER_MODEL,
                                RequestParamsHelper.ACT_APPLY,
                                RequestParamsHelper.getApplyParam("2",
                                        mEtMasterInfoStoreName.getTextString(),
                                        formStoreImagesList,
                                        formIdCardImagesList,
                                        mEtMasterInfoPhone.getTextString(),
                                        selectAddress!!,
                                        mEtMinePersonalServiceDetailAddress.getTextString(),
                                        mEtMasterInfoMasterNum.getTextString(),
                                        mEtMasterInfoStoreIntroduce.getTextString()))
                    }
                }
            }
            0x3 -> {
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    toast("上传成功，等待后台审核")
                    UserInfo.getInstance().setMemberAuthenticationSeller("0")
                    RxBus.getDefault().post(EventIsMasterAndMoneyBean.getInstance())
                    finish()
                } else {
                    toast("上传失败")
                }
            }
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x3) {
            getFastProgressDialog("正在上传信息……")
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        statueCode = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            when (currentImageFlag) {
                0 -> {
                    Matisse.obtainPathResult(data).forEach {
                        storeImagesList.add(ImageBean(null, it))
                    }
                    storeImagesAdapter.notifyDataSetChanged()
                }
                1 -> {
                    Matisse.obtainPathResult(data).forEach {
                        idCardImageList.add(ImageBean(null, it))
                    }
                    idCardImagesAdapter.notifyDataSetChanged()
                }
                2 -> {
                    Matisse.obtainPathResult(data).forEach {
                        licenceImageList.add(ImageBean(null, it))
                    }
                    licenceImagesAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    class ImageAdapter(resId: Int, list: ArrayList<ImageBean>) : BaseQuickAdapter<ImageBean, BaseViewHolder>(resId, list) {
        override fun convert(helper: BaseViewHolder?, item: ImageBean?) {
            val imageView = helper!!.getView<ImageView>(R.id.mIvApplyMasterImageInfoItem)
            Glide.with(mContext).load(item!!.uriPath).into(imageView)
        }
    }

    override fun onDestroyView() {
        unsubscribe(addressSubscription)
        super.onDestroyView()
    }
}