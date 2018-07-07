package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.app.Activity
import android.content.Intent
import android.text.Html
import android.view.View
import com.android.ql.lf.carapp.data.ImageBean
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.utils.GlideManager
import com.android.ql.lf.zwlogistics.utils.RxBus
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_new_car_auth_layout.*

class NewCarAuthFragment : BaseNetWorkingFragment() {

    private var currentSelectImage = -1

    private var xszImageBean: ImageBean? = null
    private var yxzImageBean: ImageBean? = null
    private var cphImageBean: ImageBean? = null

    private var currentCarTypeBean: SelectCXFragment.CarTypeBean? = null


    //------------车型---------------------------

    private val carTypeDataList by lazy {
        arrayListOf<SelectCXFragment.CarTypeBean>()
    }

    private val selectCXFragment by lazy {
        SelectCXFragment()
    }

    //------------车型---------------------------


    //------------车长---------------------------

    private val selectLengthFragment by lazy {
        SelectCarLengthFragment()
    }

    //------------车长---------------------------



    //------------出厂年份---------------------------

    private val selectDateFragment by lazy {
        SelectDateFragment()
    }

    //------------出厂年份---------------------------


    override fun getLayoutId() = R.layout.fragment_new_car_auth_layout

    override fun initView(view: View?) {
        initTitle()
        initClick()
        initData()
    }

    private fun initData() {
        (0..20).forEach {
            carTypeDataList.add(SelectCXFragment.CarTypeBean(null, "平板$it"))
        }
        selectCXFragment.setDataSource(carTypeDataList)
    }


    private fun initClick() {
        mFlAddNewCarXSZContainer.setOnClickListener {
            currentSelectImage = 0
            openImageChoose(MimeType.ofImage(), 1)
        }
        mFlAddNewCarYXZContainer.setOnClickListener {
            currentSelectImage = 1
            openImageChoose(MimeType.ofImage(), 1)
        }
        mFlAddNewCarCPHContainer.setOnClickListener {
            currentSelectImage = 2
            openImageChoose(MimeType.ofImage(), 1)
        }

        mLlAddNewCarCXContainer.setOnClickListener {
            if (currentCarTypeBean != null) {
                selectCXFragment.getCurrentPosition(carTypeDataList.indexOf(currentCarTypeBean!!))
            }
            selectCXFragment.myShow(childFragmentManager, "cx_select_dialog"){
                currentCarTypeBean = it
                mTvAddNewCarCX.text = it.name
            }
        }

        mLlAddNewCarCCContainer.setOnClickListener {
            selectLengthFragment.myShow(childFragmentManager,"length_select_dialog"){

            }
        }

        mLlAddNewCarCCNFContainer.setOnClickListener {
            selectDateFragment.myShow(childFragmentManager, "date_select_dialog"){
                mTvAddNewCarCCNF.text = it
            }
        }

        mTvAddNewCarSubmit.setOnClickListener {
            FragmentContainerActivity.from(mContext).setClazz(MineAuthSuccessFragment::class.java).setTitle("提交成功").start()
        }

    }

    private fun initTitle() {
        mTvAddNewCarXSZTitle.text = Html.fromHtml("上传车辆行驶证<font color='#c8c9ca'>（请勿遮挡证件）</font>")
        mTvAddNewCarYXZTitle.text = Html.fromHtml("上传车辆营运证照片<font color='#c8c9ca'>（请勿遮挡证件）</font>")
        mTvAddNewCarCPHTitle.text = Html.fromHtml("上传车牌号照片<font color='#c8c9ca'>（请勿遮挡车牌号）</font>")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            when (currentSelectImage) {
                0 -> {
                    mIvAddNewCarXSZ.visibility = View.VISIBLE
                    xszImageBean = ImageBean(null, Matisse.obtainPathResult(data)[0])
                    GlideManager.loadRoundImage(mContext, xszImageBean, mIvAddNewCarXSZ, 20)
                }
                1 -> {
                    mIvAddNewCarYXZ.visibility = View.VISIBLE
                    yxzImageBean = ImageBean(null, Matisse.obtainPathResult(data)[0])
                    GlideManager.loadRoundImage(mContext, yxzImageBean, mIvAddNewCarYXZ, 20)
                }
                2 -> {
                    mIvAddNewCarCPH.visibility = View.VISIBLE
                    cphImageBean = ImageBean(null, Matisse.obtainPathResult(data)[0])
                    GlideManager.loadRoundImage(mContext, cphImageBean, mIvAddNewCarCPH, 20)
                }
            }
            currentSelectImage = -1
        }
    }


}