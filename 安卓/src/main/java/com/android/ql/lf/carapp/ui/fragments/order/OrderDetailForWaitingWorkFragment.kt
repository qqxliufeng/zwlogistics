package com.android.ql.lf.carapp.ui.fragments.order

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.OrderBean
import com.android.ql.lf.carapp.present.ServiceOrderPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_order_detail_for_waiting_work_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject
import java.util.*

/**
 * Created by lf on 18.1.27.
 * @author lf on 18.1.27
 */
class OrderDetailForWaitingWorkFragment : BaseNetWorkingFragment() {

    companion object {
        val ORDER_BEAN_FLAG = "order_bean_flag"
    }

    private val serviceOrderPresent by lazy {
        ServiceOrderPresent()
    }

    private var orderBean: OrderBean? = null
    private var bespokeTime: String? = null

    override fun getLayoutId() =
            R.layout.fragment_order_detail_for_waiting_work_layout

    override fun initView(view: View?) {
        mTvOrderDetailLoadFail.setOnClickListener {
            mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_QORDER_DETAIL, RequestParamsHelper.getOrderDetailParam(arguments.getString(ORDER_BEAN_FLAG)))
        }
        mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_QORDER_DETAIL, RequestParamsHelper.getOrderDetailParam(arguments.getString(ORDER_BEAN_FLAG)))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        when (requestID) {
            0x1 -> getFastProgressDialog("正在提交……")
            0x0 -> getFastProgressDialog("正在加载……")
            0x2 -> getFastProgressDialog("正在提交时间……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                mTvOrderDetailLoadFail.visibility = View.GONE
                mSvOrderDetailInfo.visibility = View.VISIBLE
                val json = check.obj as JSONObject
                orderBean = Gson().fromJson(json.optJSONObject("result").toString(), OrderBean::class.java)
                setText(mTvOrderDetailForWaitingName, orderBean?.qorder_name)
                setText(mTvOrderDetailForWaitingStatus, ServiceOrderPresent.OrderStatus.getDescriptionByIndex(orderBean?.qorder_token))
                setText(mTvOrderDetailForWaitingPhone, "手机号码：${orderBean?.qorder_phone}")
                setText(mTvOrderDetailForWaitingYTime, orderBean?.qorder_appointment_time)
                mTvOrderDetailForWaitingYTime.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val datePicker = DatePickerDialog(mContext,
                            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                                val tempTime = "$year-${month + 1}-$dayOfMonth"
                                val timePicker = TimePickerDialog(mContext, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                    alert("是否要保存预约时间？", positiveAction = { _, _ ->
                                        bespokeTime = "$tempTime $hourOfDay:$minute"
                                        mPresent.getDataByPost(0x2, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_ORDER_TIME,
                                                RequestParamsHelper.getOrderTimeParam(orderBean!!.qorder_id, bespokeTime!!))
                                    })
                                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                                timePicker.setTitle("请选择结束时间")
                                timePicker.show()
                            },
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                    datePicker.setTitle("请选择时间")
                    datePicker.show()
                }
                mTvOrderDetailForWaitingContent.text = Html.fromHtml("<font color='${ContextCompat.getColor(mContext, R.color.colorPrimary)}'>备注：</font>${orderBean?.qorder_content}")
                setText(mTvOrderDetailForWaitingWorkPrice, "￥${orderBean?.qorder_price}")
                setText(mTvOrderDetailForWaitingOrderName, orderBean?.qorder_name)
                setText(mTvOrderDetailForWaitingOrderSN, orderBean?.qorder_sn)
                setText(mTvOrderDetailForWaitingOrderTime, orderBean?.qorder_time)
                setText(mTvOrderDetailForWaitingOrderAllCount, "总价：￥${orderBean?.qorder_price}")
                mBtOrderDetailForWaitingWorkTakePhoto.setOnClickListener {
                    if (orderBean != null) {
                        FragmentContainerActivity
                                .from(mContext)
                                .setTitle("拍照")
                                .setNeedNetWorking(true)
                                .setClazz(OrderImageUpLoadFragment::class.java)
                                .setExtraBundle(bundleOf(Pair("oid", orderBean!!.qorder_id)))
                                .start()
                    }
                }
                mBtOrderDetailForWaitingWorkSubmit.setOnClickListener {
                    if (orderBean != null) {
                        if (mTvOrderDetailForWaitingCode.isEmpty()) {
                            toast("请输入验证码")
                            return@setOnClickListener
                        }
                        mPresent.getDataByPost(0x1,
                                RequestParamsHelper.ORDER_MODEL,
                                RequestParamsHelper.ACT_EDIT_QORDER_STATUS,
                                RequestParamsHelper.getEditQorderStatusParam(
                                        orderBean!!.qorder_id,
                                        ServiceOrderPresent.OrderStatus.WAITING_CONFIRM.index,
                                        mTvOrderDetailForWaitingCode.getTextString(),
                                        if (mTvOrderDetailForWaitingHXCode.isEmpty()) {
                                            ""
                                        } else {
                                            mTvOrderDetailForWaitingHXCode.getTextString()
                                        }))
                    }
                }
                if (orderBean != null && !orderBean!!.qorder_pic.isEmpty()) {
                    mICllOrderDetailImageContainer.visibility = View.VISIBLE
                    mTvOrderDetailImageTitle.visibility = View.VISIBLE
                    mViewOrderDetailImageLine.visibility = View.VISIBLE
                    mICllOrderDetailImageContainer.setImages(orderBean!!.qorder_pic)
                } else {
                    mTvOrderDetailImageTitle.visibility = View.GONE
                    mICllOrderDetailImageContainer.visibility = View.GONE
                    mViewOrderDetailImageLine.visibility = View.GONE
                }
            } else {
                mTvOrderDetailLoadFail.visibility = View.VISIBLE
                mSvOrderDetailInfo.visibility = View.GONE
            }
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null) {
                when {
                    check.code == SUCCESS_CODE -> {
                        toast("确认完成")
                        serviceOrderPresent.updateOrderStatus(ServiceOrderPresent.OrderStatus.WAITING_WORK.index.toInt())
                        serviceOrderPresent.updateOrderStatus(ServiceOrderPresent.OrderStatus.HAVING_WORK.index.toInt())
                        finish()
                    }
                    check.code == "400" -> toast((check.obj as JSONObject).optString("msg"))
                    check.code == "403" -> toast((check.obj as JSONObject).optString("msg"))//验证码错误
                    else -> toast("确认失败，请稍后重试……")
                }
            }
        } else if (requestID == 0x2) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                mTvOrderDetailForWaitingYTime.text = bespokeTime
                serviceOrderPresent.updateOrderStatus(ServiceOrderPresent.OrderStatus.WAITING_WORK.index.toInt())
            } else {
                mTvOrderDetailForWaitingYTime.text = "暂无"
            }
        }
    }

    fun setText(textView: TextView, text: String?) {
        textView.text = if (TextUtils.isEmpty(text)) {
            "暂无"
        } else {
            text
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x0) {
            mTvOrderDetailLoadFail.visibility = View.VISIBLE
            mSvOrderDetailInfo.visibility = View.GONE
        } else if (requestID == 0x1) {
            toast("提交失败，请稍后重试……")
        }
    }

}