package com.android.ql.lf.carapp.ui.fragments

import android.os.Bundle
import android.text.Html
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.utils.ApiParams
import kotlinx.android.synthetic.main.fragment_detail_content_layout.*
import org.json.JSONObject

/**
 * Created by liufeng on 2018/2/22.
 */
class DetailContentFragment : BaseNetWorkingFragment() {

    companion object {
        val MODEL_NAME_FLAG = "model_name"
        val ACT_NAME_FLAG = "act_name"
        val PARAM_FLAG = "params"
    }

    override fun getLayoutId() = R.layout.fragment_detail_content_layout

    override fun initView(view: View?) {
        mWbDetailContent.settings.javaScriptEnabled = true
        mWbDetailContent.settings.domStorageEnabled = true
        mWbDetailContent.settings.defaultTextEncodingName = "utf-8"
        mWbDetailContent.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        mWbDetailContent.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                imgReset()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }

    private fun imgReset() {
        mWbDetailContent.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                " img.style.maxWidth = '100%';" +
                "}" +
                "})()")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mapParams = arguments.getSerializable(PARAM_FLAG) as HashMap<String, String>
        val apiParam = ApiParams()
        mapParams.forEach {
            apiParam.addParam(it.key, it.value)
        }
        mPresent.getDataByPost(0x0,
                arguments.getString(MODEL_NAME_FLAG),
                arguments.getString(ACT_NAME_FLAG),
                apiParam)
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在加载……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (check != null) {
            if (SUCCESS_CODE == check.code) {
                mWbDetailContent.loadData((check.obj as JSONObject).optJSONObject("result").optString("lunbo_content"), "text/html; charset=UTF-8", null)
            }
        }
    }
}