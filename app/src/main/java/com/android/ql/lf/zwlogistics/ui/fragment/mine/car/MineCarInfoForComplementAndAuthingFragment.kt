package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.view.View
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.CarBean
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.other.BrowserImageFragment
import com.android.ql.lf.zwlogistics.utils.GlideManager
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_mine_car_info_for_complement_and_authing_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class MineCarInfoForComplementAndAuthingFragment :BaseNetWorkingFragment(){

    private var carInfoBean:CarBean? = null

    override fun getLayoutId() = R.layout.fragment_mine_car_info_for_complement_and_authing_layout


    override fun initView(view: View?) {
        mPresent.getDataByPost(0x0,RequestParamsHelper.getCarInfoParam(arguments!!.getString("cid")))
    }


    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在加载车辆信息……")
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("加载车辆信息失败……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        handleSuccess(requestID,result)
    }

    override fun onHandleSuccess(requestID: Int, jsonObject: JSONObject?) {
        super.onHandleSuccess(requestID, jsonObject)
        if (jsonObject!=null){
            carInfoBean = Gson().fromJson(jsonObject.toString(),CarBean::class.java)
            if (carInfoBean!=null){
                when(carInfoBean?.vehicle_is_state){
                    1->{
                        mTvMineCarComplementInfoTitle.text = "审核中，请耐心等待审核！"
                    }
                    2->{
                        mTvMineCarComplementInfoTitle.text = "审核成功：您已成功完成审核！"
                    }
                }
                GlideManager.loadRoundImage(mContext,carInfoBean!!.vehicle_driving,mIvMineCarComplementInfoDriverCard,20)
                GlideManager.loadRoundImage(mContext,carInfoBean!!.vehicle_run,mIvMineCarComplementInfoYYZ,20)
                GlideManager.loadRoundImage(mContext,carInfoBean!!.vehicle_number,mIvMineCarComplementInfoCarNum,20)
                mTvMineCarComplementInfoCarType.text = carInfoBean?.vehicle_type
                mTvMineCarComplementInfoCarLength.text = carInfoBean?.vehicle_length
                mTvMineCarComplementInfoCarWeight.text = "${carInfoBean?.vehicle_weight}吨"
                mTvMineCarComplementInfoCarDate.text = carInfoBean?.vehicle_year
                mTvMineCarComplementInfoCarName.text = carInfoBean?.vehicle_name

                mIvMineCarComplementInfoDriverCard.setOnClickListener {
                    startPhoto(0)
                }
                mIvMineCarComplementInfoYYZ.setOnClickListener {
                    startPhoto(1)
                }
                mIvMineCarComplementInfoCarNum.setOnClickListener{
                    startPhoto(2)
                }
            }
        }
    }

    private fun startPhoto(index:Int) {
        val list = carInfoBean?.imageList
        if (list != null) {
            BrowserImageFragment.startBrowserImage(mContext, list, index)
        }
    }

}