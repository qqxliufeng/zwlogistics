package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.app.Activity
import android.content.Intent
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.CarParamBean
import com.android.ql.lf.zwlogistics.data.PostCarAuthBean
import com.android.ql.lf.zwlogistics.present.AuthManager
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.zhihu.matisse.MimeType
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_mine_car_info_for_failed_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class MineCarInfoForFailedFragment : BaseNetWorkingFragment() {

    private var tempSelectFlag = NewCarAuthFragment.DRIVER_CARD_FLAG

    private var carTypeBean: CarParamBean? = null
    private var carLengthBean:CarParamBean? = null
    private var carDateBean:CarParamBean? = null

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

    private val postCarAuthBean by lazy {
        PostCarAuthBean()
    }


    override fun getLayoutId() = R.layout.fragment_mine_car_info_for_failed_layout

    override fun initView(view: View?) {
        val failContent = arguments!!.getString("content")
        postCarAuthBean.driver_car_id = arguments!!.getString("cid")
        if (!TextUtils.isEmpty(failContent)) {
            mTvMineCarFailTitle.text = "审核失败原因：$failContent"
        } else {
            mTvMineCarFailTitle.text = "审核失败原因：暂无"
        }

        mTvMineCarFailDriverCardTitle.text = Html.fromHtml("上传车辆行驶证<font color='#c8c9ca'>（请勿遮挡证件）</font>")
        mTvMineCarFailYYZTitle.text = Html.fromHtml("上传车辆营运证照片<font color='#c8c9ca'>（请勿遮挡证件）</font>")
        mTvMineCarFailNumTitle.text = Html.fromHtml("上传车牌号照片<font color='#c8c9ca'>（请勿遮挡车牌号）</font>")

        mFlMineCarFailDriverCardContainer.setOnClickListener {
            tempSelectFlag = NewCarAuthFragment.DRIVER_CARD_FLAG
            openImageChoose(MimeType.ofImage(),1)
        }
        mFlMineCarFailYYZContainer.setOnClickListener {
            tempSelectFlag = NewCarAuthFragment.YYZ_CARD_FLAG
            openImageChoose(MimeType.ofImage(),1)
        }
        mFlMineCarFailNumContainer.setOnClickListener {
            tempSelectFlag = NewCarAuthFragment.CAR_NUM_FLAG
            openImageChoose(MimeType.ofImage(),1)
        }


        mLlMineCarFailCarTypeContainer.setOnClickListener {
            selectCXFragment.myShow(childFragmentManager, "cx_select_dialog") {
                carTypeBean = it
                mTvMineCarFailCarType.text = it.name
            }
        }
        mLlMineCarFailCarLengthContainer.setOnClickListener {
            selectLengthFragment.myShow(childFragmentManager, "length_select_dialog") {
                carLengthBean = it
                mTvMineCarFailCarLength.text = it.name
            }
        }
        mLlMineCarFailCarDateContainer.setOnClickListener {
            selectDateFragment.myShow(childFragmentManager, "date_select_dialog") {
                carDateBean = it
                mTvMineCarFailCarDate.text = it?.name
            }
        }

        mTvMineCarFailSubmit.setOnClickListener {
            postCarAuthBean.driver_car_type = if (carTypeBean!=null){carTypeBean!!.name}else{""}
            postCarAuthBean.driver_car_length = if (carLengthBean!=null){carLengthBean!!.name}else{""}
            postCarAuthBean.driver_car_weight = mEtMineCarFailCarWeight.text.toString()
            postCarAuthBean.driver_car_name = mEtMineCarFailCarName.text.toString()
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
        mPresent.getDataByPost(0x1, RequestParamsHelper.getCarParamsParams())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            AuthManager.handleResultData(data, tempSelectFlag, postCarAuthBean) {
                when (tempSelectFlag) {
                    NewCarAuthFragment.DRIVER_CARD_FLAG -> {
                        setImageUri(mIvMineCarFailDriverCard,mTvMineCarFailDriverCard,it)
                    }
                    NewCarAuthFragment.YYZ_CARD_FLAG -> {
                        setImageUri(mIvMineCarFailYYZ,mTvMineCarFailYYZ,it)
                    }
                    NewCarAuthFragment.CAR_NUM_FLAG -> {
                        setImageUri(mIvMineCarFailNum,mTvMineCarFailNum,it)
                    }
                }
            }
        }
    }

    private fun setImageUri(imageView: ImageView, textView: TextView,uri:String){
        textView.visibility = View.GONE
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
                AuthManager.rxBusPostCarApply(NewCarAuthFragment.POST_AUTH_APPLY_FLAG)
                finish()
            }
        }
    }

}