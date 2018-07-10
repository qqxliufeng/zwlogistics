package com.android.ql.lf.zwlogistics.ui.fragment.mine.driver

import android.view.View
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.DriverAuthBean
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.utils.GlideManager
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_mine_driver_info_for_complement_and_authing_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class MineDriverInfoForComplementAndAuthingFragment : BaseNetWorkingFragment() {

    private var driverAuthBean: DriverAuthBean? = null

    override fun getLayoutId() = R.layout.fragment_mine_driver_info_for_complement_and_authing_layout

    override fun initView(view: View?) {
        mPresent.getDataByPost(0x0, RequestParamsHelper.getAuthInfoParams())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在加载信息……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (check != null) {
            if (check.code == SUCCESS_CODE) {
                bindData(check.obj as JSONObject)
            } else {
                onRequestFail(requestID, NullPointerException())
            }
        } else {
            onRequestFail(requestID, NullPointerException())
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("信息加载失败")
    }

    private fun bindData(jsonObject: JSONObject) {
        driverAuthBean = Gson().fromJson(jsonObject.optJSONObject(RESULT_OBJECT).toString(), DriverAuthBean::class.java)
        UserInfo.getInstance().user_is_rank = driverAuthBean?.user_is_rank
        if (UserInfo.getInstance().user_is_rank == UserInfo.UserInfoAuthStatus.SHENG_HE_ZHONG.statusCode.toString()){
            mTvDriverInfoForComplementTitle.text = "审核中，请耐心等待审核！"
        }else{
            mTvDriverInfoForComplementTitle.text = "审核成功，您已成功完成审核！"
        }
        mTvDriverInfoForComplementName.text = driverAuthBean?.user_rank_nickname
        mTvDriverInfoForComplementIdCard.text = driverAuthBean?.user_rank_number
        mTvDriverInfoForComplementPhone.text = driverAuthBean?.user_rank_phone

        GlideManager.loadRoundImage(mContext,driverAuthBean?.user_rank_pic,mIvDriverInfoForComplementFace,20)
        GlideManager.loadRoundImage(mContext,driverAuthBean?.user_rank_idcard_front,mIvDriverInfoForComplementIdCardFront,20)
        GlideManager.loadRoundImage(mContext,driverAuthBean?.user_rank_idcard_back,mIvDriverInfoForComplementIdCardBackGround,20)
        GlideManager.loadRoundImage(mContext,driverAuthBean?.user_rank_driving,mIvDriverInfoForComplementDriverCard,20)
        GlideManager.loadRoundImage(mContext,driverAuthBean?.user_rank_appraisal,mIvDriverInfoForComplementCYZG,20)
    }

}