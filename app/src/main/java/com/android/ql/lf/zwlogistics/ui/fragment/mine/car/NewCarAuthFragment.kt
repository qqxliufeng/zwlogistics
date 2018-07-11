package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.View
import android.widget.ImageView
import com.android.ql.lf.carapp.data.ImageBean
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.CarParamBean
import com.android.ql.lf.zwlogistics.data.PostCarAuthBean
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.present.AuthManager
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MinePersonAuthFragment
import com.android.ql.lf.zwlogistics.ui.fragment.other.DetailContentFragment
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.android.ql.lf.zwlogistics.utils.RxBus
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.zhihu.matisse.MimeType
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_new_car_auth_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class NewCarAuthFragment : BaseNetWorkingFragment() {

    companion object {

        const val POST_AUTH_APPLY_FLAG = "post_auth_apply_flag"

        const val DRIVER_CARD_FLAG = 0
        const val YYZ_CARD_FLAG = 1
        const val CAR_NUM_FLAG = 2

        fun startCarAuthFragment(mContext: Context, showJump: Int) {
            FragmentContainerActivity
                    .from(mContext)
                    .setNeedNetWorking(true)
                    .setTitle("新车认证")
                    .setExtraBundle(bundleOf(Pair(MinePersonAuthFragment.IS_SHOW_JUMP, showJump)))
                    .setClazz(NewCarAuthFragment::class.java)
                    .start()
        }
    }

    private val isShowJump by lazy {
        arguments!!.getInt(MinePersonAuthFragment.IS_SHOW_JUMP) == MinePersonAuthFragment.SHOW_JUMP
    }


    private var carTypeBean: CarParamBean? = null
    private var carLengthBean:CarParamBean? = null
    private var carDateBean:CarParamBean? = null

    private var tempSelectFlag = DRIVER_CARD_FLAG

    private val postCarAuthBean by lazy {
        PostCarAuthBean()
    }

    private val carTypeDataList by lazy {
        arrayListOf<CarParamBean>()
    }

    private val carLengthDataList by lazy {
        arrayListOf<CarParamBean>()
    }

    private val carDateDataList by lazy {
        arrayListOf<CarParamBean>()
    }

    private val selectCXFragment by lazy {
        SelectCXFragment()
    }


    private val selectLengthFragment by lazy {
        SelectCarLengthFragment()
    }


    private val selectDateFragment by lazy {
        SelectDateFragment()
    }


    override fun getLayoutId() = R.layout.fragment_new_car_auth_layout

    override fun initView(view: View?) {
        initTitle()
        initClick()
        initData()
    }

    private fun initData() {
        mPresent.getDataByPost(0x1,RequestParamsHelper.getCarParamsParams())
    }

    private fun initClick() {
        mFlAddNewCarXSZContainer.setOnClickListener {
            tempSelectFlag = DRIVER_CARD_FLAG
            openImageChoose(MimeType.ofImage(), 1)
        }
        mFlAddNewCarYXZContainer.setOnClickListener {
            tempSelectFlag = YYZ_CARD_FLAG
            openImageChoose(MimeType.ofImage(), 1)
        }
        mFlAddNewCarCPHContainer.setOnClickListener {
            tempSelectFlag = CAR_NUM_FLAG
            openImageChoose(MimeType.ofImage(), 1)
        }

        mLlAddNewCarCXContainer.setOnClickListener {
            selectCXFragment.myShow(childFragmentManager, "cx_select_dialog") {
                carTypeBean = it
                mTvAddNewCarCX.text = it.name
            }
        }

        mLlAddNewCarCCContainer.setOnClickListener {
            selectLengthFragment.myShow(childFragmentManager, "length_select_dialog") {
                carLengthBean = it
                mTvAddNewCarCC.text = it.name
            }
        }

        mLlAddNewCarCCNFContainer.setOnClickListener {
            selectDateFragment.myShow(childFragmentManager, "date_select_dialog") {
                carDateBean = it
                mTvAddNewCarCCNF.text = it?.name
            }
        }

        mTvAddNewCarSubmit.setOnClickListener {
            if (!mCbAddNewCarProtocol.isChecked){
                toast("请先同意众维物流协议")
                return@setOnClickListener
            }
            postCarAuthBean.driver_car_type = if (carTypeBean!=null){carTypeBean!!.name}else{""}
            postCarAuthBean.driver_car_length = if (carLengthBean!=null){carLengthBean!!.name}else{""}
            postCarAuthBean.driver_car_weight = mEtAddNewCarWeight.text.toString()
            postCarAuthBean.driver_car_name = mEtAddNewCarName.text.toString()
            postCarAuthBean.driver_car_date = if (carDateBean!=null){carDateBean!!.name}else{""}
            val isEmpty = postCarAuthBean.isEmpty()
            if (""!=isEmpty){
                toast(isEmpty)
                return@setOnClickListener
            }

            AuthManager.authCar(mPresent,postCarAuthBean,actionStart = {
                getFastProgressDialog("正在处理图片……")
            },actionEnd = {
                onRequestEnd(-1)
            },actionFailed = {
                toast("提交申请失败……")
                onRequestEnd(-1)
            })
        }
    }

    private fun initTitle() {
        if (isShowJump){
            mAsvCarStep.visibility = View.VISIBLE
        }else{
            mAsvCarStep.visibility = View.GONE
        }
        mTvAddNewCarProtocol.setOnClickListener {
            FragmentContainerActivity
                    .from(mContext)
                    .setTitle("用户服务协议")
                    .setClazz(DetailContentFragment::class.java)
                    .setExtraBundle(bundleOf(Pair(DetailContentFragment.PARAM_FLAG, RequestParamsHelper.getUserProtocolParam("1"))))
                    .start()
        }
        mCbAddNewCarProtocol.isChecked = true
        mTvAddNewCarXSZTitle.text = Html.fromHtml("上传车辆行驶证<font color='#c8c9ca'>（请勿遮挡证件）</font>")
        mTvAddNewCarYXZTitle.text = Html.fromHtml("上传车辆营运证照片<font color='#c8c9ca'>（请勿遮挡证件）</font>")
        mTvAddNewCarCPHTitle.text = Html.fromHtml("上传车牌号照片<font color='#c8c9ca'>（请勿遮挡车牌号）</font>")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            AuthManager.handleResultData(data, tempSelectFlag, postCarAuthBean) {
                when (tempSelectFlag) {
                    0 -> {
                        setImageUri(mIvAddNewCarXSZ,it)
                    }
                    1 -> {
                        setImageUri(mIvAddNewCarYXZ,it)
                    }
                    2 -> {
                        setImageUri(mIvAddNewCarCPH,it)
                    }
                }
            }
        }
    }

    private fun setImageUri(imageView: ImageView,uri:String){
        imageView.visibility = View.VISIBLE
        Glide.with(this).load(uri).bitmapTransform(CenterCrop(mContext), RoundedCornersTransformation(mContext,20,0)).into(imageView)
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1){
            getFastProgressDialog("正在加载车辆信息")
        }else if (requestID == 0x0){
            getFastProgressDialog("正在提交申请……")
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        onRequestEnd(-1)
        if (requestID == 0x0){
            toast("提交申请失败……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        handleSuccess(requestID,result)
    }

    override fun onHandleSuccess(requestID: Int, jsonObject: JSONObject?) {
        super.onHandleSuccess(requestID, jsonObject)
        when(requestID){
            0x1->{
                if (jsonObject!=null) {
                    val tempTypeList = AuthManager.parseCarParams("model", jsonObject)
                    if (tempTypeList!=null){
                        carTypeDataList.addAll(tempTypeList)
                        selectCXFragment.setDataSource(carTypeDataList)
                    }
                    val tempLengthList = AuthManager.parseCarParams("length", jsonObject)
                    if (tempLengthList!=null){
                        carLengthDataList.addAll(tempLengthList)
                        selectLengthFragment.setDataSource(carLengthDataList)
                    }
                    val tempDateList = AuthManager.parseCarParams("year", jsonObject)
                    if (tempDateList!=null){
                        carDateDataList.addAll(tempDateList)
                        selectDateFragment.setDataSource(carDateDataList)
                    }
                }
            }
            0x0->{
                toast("提交申请成功！敬请等待后台审核")
                AuthManager.rxBusPostCarApply(POST_AUTH_APPLY_FLAG)
                finish()
            }
        }
    }


}