package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.QRCodeUtil
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.toast
import kotlinx.android.synthetic.main.fragment_mine_q_code_layout.*
import org.json.JSONObject

/**
 * Created by lf on 18.2.2.
 * @author lf on 18.2.2
 */
class MineQCodeFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_mine_q_code_layout

    override fun initView(view: View?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_QRCODE, RequestParamsHelper.getMyQrcodeParam())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在加载……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (check != null) {
            if (check.code == SUCCESS_CODE) {
                val resultJson = check.obj as JSONObject
                val url = resultJson.optString("arr")
                if (!TextUtils.isEmpty(url)) {
                    mIvMineQCode.setImageBitmap(QRCodeUtil.createQRCodeBitmap(url, 500, 500))
                }
                val arr1 = resultJson.optJSONObject("arr1")
                if (arr1 != null) {
                    mTvMineQCodeRuleContent.text = Html.fromHtml(arr1.optString("ptgg_content"))
                }
            } else {
                toast((check.obj as JSONObject).optString(MSG_FLAG))
            }
        }
    }

}