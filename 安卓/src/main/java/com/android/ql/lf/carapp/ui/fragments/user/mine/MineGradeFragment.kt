package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.os.Bundle
import android.text.Html
import android.view.View
import android.webkit.WebSettings
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import kotlinx.android.synthetic.main.fragment_mine_grade_layout.*
import org.json.JSONObject

/**
 * Created by liufeng on 2018/2/1.
 */
class MineGradeFragment : BaseNetWorkingFragment() {

    private val gradePair by lazy {
        mapOf(Pair("0", "兵")
                , Pair("1", "班")
                , Pair("2", "排")
                , Pair("3", "连")
                , Pair("4", "营")
                , Pair("5", "团")
                , Pair("6", "旅")
                , Pair("7", "师")
                , Pair("8", "军")
        )
    }

    override fun getLayoutId() = R.layout.fragment_mine_grade_layout

    override fun initView(view: View?) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.MEMBER_MODEL,
                RequestParamsHelper.ACT_MY_GRADES,
                RequestParamsHelper.getMyGradesParam())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在加载……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (check != null && SUCCESS_CODE == check.code) {
            val json = check.obj as JSONObject
            val resultJson = json.optJSONObject("result")
            val rank = resultJson.optString("member_rank")
            mTvMineGradeCurrentGrade.text = "会员当前等级：${gradePair[rank]}"
            mTvMineGradeGrade.text = gradePair[rank]
            mTvMineGradeComment.text = resultJson.optString("member_grade")
            mTvMineGradeOrderNum.text = resultJson.optString("member_order_num")
            mTvMineGradeGradeIntroduce.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            mTvMineGradeGradeIntroduce.settings.javaScriptEnabled = true
            mTvMineGradeGradeIntroduce.loadData(json.optJSONObject("arr").optString("ptgg_content"), "text/html; charset=UTF-8", null)
        }
    }
}