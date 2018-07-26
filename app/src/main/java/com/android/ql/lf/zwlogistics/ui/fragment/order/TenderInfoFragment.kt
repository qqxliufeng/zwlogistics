package com.android.ql.lf.zwlogistics.ui.fragment.order

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.android.ql.lf.carapp.utils.getTextString
import com.android.ql.lf.carapp.utils.isEmpty
import com.android.ql.lf.carapp.utils.isPhone
import com.android.ql.lf.carapp.utils.setFirstPoint
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.CarBean
import com.android.ql.lf.zwlogistics.data.CarParamBean
import com.android.ql.lf.zwlogistics.present.AuthManager
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.car.MineCarListFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.car.SelectCXFragment
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.android.ql.lf.zwlogistics.utils.RxBus
import com.tencent.mm.opensdk.utils.Log
import kotlinx.android.synthetic.main.fragment_tender_info_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class TenderInfoFragment : BaseNetWorkingFragment() {

    private var carType: String? = null

    private val carTypeList by lazy {
        arrayListOf<CarParamBean>()
    }

    private val selectCXFragment by lazy {
        SelectCXFragment()
    }

    private val carSubscribe by lazy {
        RxBus.getDefault().toObservable(CarBean::class.java).subscribe {
            carType = "${it.vehicle_id}"
            mTvTenderInfoCarType.text = it.vehicle_name
        }
    }

    override fun getLayoutId() = R.layout.fragment_tender_info_layout

    override fun initView(view: View?) {
        carSubscribe
        mEtTenderInfoPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mEtTenderInfoPrice.setFirstPoint()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        mLlTenderInfoCarType.setOnClickListener {
            FragmentContainerActivity
                    .from(mContext)
                    .setTitle("请选择车辆")
                    .setClazz(MineCarListFragment::class.java)
                    .setExtraBundle(bundleOf(Pair(MineCarListFragment.IS_SELECT_CAR_FLAG, true)))
                    .setNeedNetWorking(true)
                    .start()
        }
        mBtTenderInfoSubmit.setOnClickListener {
            try {
                if (mEtTenderInfoPhone.isEmpty()) {
                    toast("请输入手机号")
                    return@setOnClickListener
                }
                if (!mEtTenderInfoPhone.isPhone()) {
                    toast("请输入正确的手机号")
                    return@setOnClickListener
                }
                if (mEtTenderInfoPrice.isEmpty()) {
                    toast("请输入价格")
                    return@setOnClickListener
                }
                val price = java.lang.Float.parseFloat(mEtTenderInfoPrice.getTextString())
                if (carType == null) {
                    toast("请选择车型")
                    return@setOnClickListener
                }
                mPresent.getDataByPost(0x1, RequestParamsHelper.getTenderInfoParams(
                        arguments!!.getString("pid"),
                        mEtTenderInfoPhone.getTextString(),
                        "$price",
                        carType!!,
                        mEtTenderInfoRemark.getTextString()))
            } catch (e: NumberFormatException) {
                toast("请输入合法金额")
                return@setOnClickListener
            }
        }
        mPresent.getDataByPost(0x0, RequestParamsHelper.getCarParamsParams())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        when (requestID) {
            0x0 -> {
                getFastProgressDialog("正在加载车型……")
            }
            0x1 -> {
                getFastProgressDialog("正在提交信息……")
            }
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        handleSuccess(requestID, result)
    }

    override fun onHandleSuccess(requestID: Int, jsonObject: Any?) {
        super.onHandleSuccess(requestID, jsonObject)
        when (requestID) {
            0x0 -> {
                if (jsonObject != null && jsonObject is JSONObject) {
                    val tempTypeList = AuthManager.parseCarParams("model", jsonObject)
                    if (tempTypeList != null) {
                        carTypeList.addAll(tempTypeList)
                        selectCXFragment.setDataSource(carTypeList)
                    }
                }
            }
            0x1 -> {
                toast("恭喜，竞标成功~")
                FragmentContainerActivity
                        .from(mContext)
                        .setExtraBundle(bundleOf(Pair("oid", arguments!!.getString("pid"))))
                        .setClazz(TenderSuccessFragment::class.java)
                        .setTitle("竞标成功")
                        .start()
                finish()
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        when (requestID) {
            0x0 -> toast("车型加载失败……")
            0x1 -> {
                if (TextUtils.isEmpty(e.message)) {
                    toast("信息提交失败……")
                } else {
                    toast(e.message!!)
                }
            }
        }
    }

    override fun onDestroyView() {
        unsubscribe(carSubscribe)
        super.onDestroyView()
    }

}