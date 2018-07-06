package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.PayResult
import com.android.ql.lf.carapp.data.WXPayBean
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.order.PayResultFragment
import com.android.ql.lf.carapp.ui.views.SelectPayTypeView
import com.android.ql.lf.carapp.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import org.json.JSONObject
import java.util.regex.Pattern

/**
 * Created by liufeng on 2018/2/4.
 */
class MineEnsureMoneyFragment : BaseRecyclerViewFragment<MineEnsureMoneyFragment.EnsureMoneyProduct>() {

    private lateinit var mTvEnsureMoneyIntroduce: TextView

    private var payType: String = SelectPayTypeView.WX_PAY


    private val handle = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                PayManager.SDK_PAY_FLAG -> {
                    val payResult = PayResult(msg.obj as Map<String, String>)
                    val resultInfo = payResult.result// 同步返回需要验证的信息
                    val resultStatus = payResult.resultStatus
                    val bundle = Bundle()
                    if (TextUtils.equals(resultStatus, "9000")) {
                        //支付成功
                        bundle.putInt(PayResultFragment.PAY_CODE_FLAG, PayResultFragment.PAY_SUCCESS_CODE)
                    } else {
                        //支付失败
                        bundle.putInt(PayResultFragment.PAY_CODE_FLAG, PayResultFragment.PAY_FAIL_CODE)
                    }
                    FragmentContainerActivity.startFragmentContainerActivity(mContext, "支付结果", true, false, bundle, PayResultFragment::class.java)
                }
            }
        }
    }

    private val wxPaySuccessSubscription by lazy {
        RxBus.getDefault().toObservable(WxPaySuccessBean::class.java).subscribe {
            if (it.payResult){
                onPostRefresh()
            }
        }
    }

    override fun createAdapter(): BaseQuickAdapter<EnsureMoneyProduct, BaseViewHolder> =
            EnsureMoneyProductAdapter(R.layout.adapter_ensure_money_item_layout, mArrayList)

    override fun initView(view: View?) {
        super.initView(view)
        wxPaySuccessSubscription
        val footView = View.inflate(mContext, R.layout.fragment_mine_ensure_money_layout, null)
        mTvEnsureMoneyIntroduce = footView.findViewById(R.id.mTvEnsureMoneyIntroduce)
        mBaseAdapter.addFooterView(footView)
//        mLlMineEnsureMoneyContainer.minimumHeight = screenSize.height - statusBarHeight - actionBarHeight
//        mBtMineEnsureMoneyBack.setOnClickListener {
//            val dialog = Dialog(mContext)
//            dialog.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(mContext, android.R.color.transparent)))
//            val contentView = View.inflate(mContext,R.layout.dialog_ensure_money_back_layout,null)
//            val iv_close = contentView.findViewById<ImageView>(R.id.mIvEnsureMoneyBackClose)
//            iv_close.setColorFilter(Color.parseColor("#555555"))
//            iv_close.setOnClickListener{
//                dialog.dismiss()
//            }
//            dialog.setContentView(contentView)
//            dialog.show()
//        }
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_M_P, RequestParamsHelper.getEnsureMoneyProductParam())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在支付……")
        }
        if (requestID == 0x2) {
            getFastProgressDialog("正在申请……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            processList(result as String, EnsureMoneyProduct::class.java)
            setLoadEnable(false)
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                val arrJson = (check.obj as JSONObject).optJSONObject("arr")
                mTvEnsureMoneyIntroduce.text = Html.fromHtml(arrJson.optString("ptgg_content"))
            }
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    if (payType == SelectPayTypeView.WX_PAY) {
                        PreferenceUtils.setPrefBoolean(mContext, "is_mall_order", false)
                        val wxBean = Gson().fromJson((check.obj as JSONObject).optJSONObject("result").toString(), WXPayBean::class.java)
                        PayManager.wxPay(mContext, wxBean)
                    } else {
                        PayManager.aliPay(mContext, handle, (check.obj as JSONObject).optString("result"))
                    }
                } else {
                    toast((check.obj as JSONObject).optString("msg"))
                }
            }
        } else if (requestID == 0x2) {
            val check = checkResultCode(result)
            if (check != null) {
                toast((check.obj as JSONObject).optString("msg"))
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x2) {
            toast("申请退款失败，请稍后重试……")
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemChildClick(adapter, view, position)
        val item = mArrayList[position]
        if (view!!.id == R.id.mBtEnsureMoneyProductAction) {
            when (item.m_p_type) {
                "1" -> {
                    if (item.member_ismaster_ensure_money == "0") {
                        showPayMoneyDialog(item)
                    } else {
                        //退师傅保证金
                        refund(item)
                    }
                }
                "2" -> {
                    if (item.member_ismerchant_ensure_money == "0") {
                        showPayMoneyDialog(item)
                    } else {
                        //退店铺保证金
                        refund(item)
                    }
                }
            }
        }
    }

    private fun showPayMoneyDialog(item: EnsureMoneyProduct) {
        val builder = AlertDialog.Builder(mContext)
        val content = View.inflate(mContext, R.layout.dialog_ensure_money_pay_layout, null)
        val et_count = content.findViewById<EditText>(R.id.mEtEnsureMoneyPayCount)
        et_count.setText(item.m_p_price)
        builder.setView(content)
        builder.setNegativeButton("取消", null)
        builder.setPositiveButton("确定") { _, _ ->
            if (et_count.isEmpty()) {
                toast("请输入金额")
                return@setPositiveButton
            }
            if (!isDouble(et_count.getTextString())) {
                toast("请输入合法的金额")
                return@setPositiveButton
            }
            if (et_count.getTextString().toFloat() < item.m_p_price!!.toFloat()) {
                toast("输入金额必须大于指定最低金额")
                return@setPositiveButton
            }
            pay(item,et_count.getTextString())
        }
        builder.setTitle("请输入保证金金额")
        builder.create().show()
    }

    private fun isDouble(str: String?): Boolean {
        if (null == str || "" == str) {
            return false
        }
        val pattern = Pattern.compile("^[-\\+]?[.\\d]*$")
        return pattern.matcher(str).matches()
    }

    private fun pay(item: EnsureMoneyProduct,price:String) {
        payType = SelectPayTypeView.WX_PAY
        val bottomPayDialog = BottomSheetDialog(mContext)
        val contentView = SelectPayTypeView(mContext)
        contentView.setShowConfirmView(View.VISIBLE)
        contentView.setOnConfirmClickListener {
            bottomPayDialog.dismiss()
            payType = contentView.payType
            mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PAYMENT_DEPOSIT,
                    RequestParamsHelper.getPaymentDepositParam(item.m_p_type!!, item.m_p_id!!, payType,price))
        }
        bottomPayDialog.setContentView(contentView)
        bottomPayDialog.show()
    }

    private fun refund(item: EnsureMoneyProduct) {
        alert("是否要退款？", "退款", "否") { _, _ ->
            mPresent.getDataByPost(0x2,
                    RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_REFUND_DEPOSIT,
                    RequestParamsHelper.getRefundDepositParam(item.m_p_type!!))
        }
    }

    override fun onDestroyView() {
        unsubscribe(wxPaySuccessSubscription)
        super.onDestroyView()
    }

    class EnsureMoneyProductAdapter(layout: Int, list: ArrayList<EnsureMoneyProduct>) : BaseQuickAdapter<EnsureMoneyProduct, BaseViewHolder>(layout, list) {
        override fun convert(helper: BaseViewHolder?, item: EnsureMoneyProduct?) {
            helper!!.setText(R.id.mTvEnsureMoneyProductTitle, item!!.m_p_name)
            helper.setText(R.id.mTvEnsureMoneyProductCount, "￥${item.m_p_price}")
            val bt_action = helper.getView<Button>(R.id.mBtEnsureMoneyProductAction)
            helper.addOnClickListener(R.id.mBtEnsureMoneyProductAction)
            when (item.m_p_type) {
                "1" -> {
                    if (item.member_ismaster_ensure_money == "0") {
                        bt_action.text = "去缴纳"
                        bt_action.setTextColor(Color.WHITE)
                        bt_action.setBackgroundResource(R.drawable.shape_bt_bg1)
                    } else {
                        bt_action.text = "去退款"
                        bt_action.setTextColor(ContextCompat.getColor(mContext, R.color.text_dark_color))
                        bt_action.setBackgroundResource(R.drawable.shape_bt_bg5)
                    }
                }
                "2" -> {
                    if (item.member_ismerchant_ensure_money == "0") {
                        bt_action.text = "去缴纳"
                        bt_action.setTextColor(Color.WHITE)
                        bt_action.setBackgroundResource(R.drawable.shape_bt_bg1)
                    } else {
                        bt_action.text = "去退款"
                        bt_action.setTextColor(ContextCompat.getColor(mContext, R.color.text_dark_color))
                        bt_action.setBackgroundResource(R.drawable.shape_bt_bg5)
                    }
                }
            }
        }
    }

    class EnsureMoneyProduct {
        var m_p_id: String? = null
        var m_p_name: String? = null
        var m_p_content: String? = null
        var m_p_yprice: String? = null
        var m_p_price: String? = null
        var m_p_num: String? = null
        var m_p_type: String? = null
        var member_ismaster_ensure_money: String? = null
        var member_ismerchant_ensure_money: String? = null
    }

    class WxPaySuccessBean(var payResult: Boolean)


}