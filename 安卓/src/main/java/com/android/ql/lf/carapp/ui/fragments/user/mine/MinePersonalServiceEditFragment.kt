package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.EventIsMasterAndMoneyBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.activities.SelectAddressActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.gson.Gson
import com.tencent.mm.opensdk.utils.Log
import kotlinx.android.synthetic.main.fragment_mine_personal_service_layout.*
import org.jetbrains.anko.collections.forEachWithIndex
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by liufeng on 2018/2/1.
 */
class MinePersonalServiceEditFragment : BaseNetWorkingFragment() {

    private var startTime: String? = null
    private var endTime: String? = null

    private var shopInfo: UserInfo.ShopInfo? = null

    private var bottomDialog: BottomSheetDialog? = null

    private val workRangeList by lazy {
        arrayListOf<WorkRangeBean>()
    }

    private var currentWorkRange: WorkRangeBean? = null

    private var selectAddress: String? = null

    private val addressSubscription = RxBus.getDefault().toObservable(SelectAddressActivity.SelectAddressItemBean::class.java).subscribe {
        selectAddress = it.name
        mEtMinePersonalServiceAddress.text = selectAddress!!
    }

    override fun getLayoutId() = R.layout.fragment_mine_personal_service_layout

    override fun initView(view: View?) {
        addressSubscription
        mRlServiceEditWorkRangeContainer.setOnClickListener {
            if (!workRangeList.isEmpty()) {
                val titleList = arrayListOf<String>()
                val checkList = arrayListOf<Boolean>()
                val titleArray = arrayOf<String>()
                val selectList = arrayListOf<Int>()
                workRangeList.forEach {
                    titleList.add(it.product_tag_title!!)
                    checkList.add(false)
                }
                val builder = AlertDialog.Builder(mContext)
                builder.setMultiChoiceItems(titleList.toArray(titleArray), checkList.toBooleanArray()) { dialog, which, isChecked ->
                    if (selectList.contains(which)) {
                        selectList.remove(which)
                    } else {
                        selectList.add(which)
                    }
                }
                builder.setNegativeButton("取消", null)
                builder.setPositiveButton("确定") { _, _ ->
                    if (!selectList.isEmpty()) {
                        val sb = StringBuilder()
                        selectList.forEach {
                            val item = workRangeList[it]
                            sb.append(item.product_tag_title).append(",")
                        }
                        mTvServiceEditWorkRange.text = sb.deleteCharAt(sb.length - 1).toString()
                        currentWorkRange = WorkRangeBean()
                        currentWorkRange!!.product_tag_title = mTvServiceEditWorkRange.text.toString()
                    }
                }
                builder.setTitle("选择服务类型")
                builder.create().show()
                /*if (bottomDialog == null) {
                    bottomDialog = BottomSheetDialog(mContext)
                    val contentView = View.inflate(mContext, R.layout.dialog_work_range_layout, null)
                    val rv_content = contentView.findViewById<RecyclerView>(R.id.mRvRefundContent)
                    val iv_close = contentView.findViewById<ImageView>(R.id.mIvRefundClose)
                    iv_close.setOnClickListener {
                        bottomDialog!!.dismiss()
                    }
                    rv_content.layoutManager = LinearLayoutManager(mContext)
                    rv_content.adapter = object : BaseQuickAdapter<WorkRangeBean, BaseViewHolder>(android.R.layout.simple_list_item_1, workRangeList) {
                        override fun convert(helper: BaseViewHolder?, item: WorkRangeBean?) {
                            helper!!.setText(android.R.id.text1, item!!.tag_title)
                        }
                    }
                    rv_content.addOnItemTouchListener(object : OnItemClickListener() {
                        override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                            currentWorkRange = workRangeList[position]
                            mTvServiceEditWorkRange.text = currentWorkRange!!.tag_title
                            bottomDialog!!.dismiss()
                        }
                    })
                    bottomDialog!!.setContentView(contentView)
                    val parent = contentView.parent as View
                    val behavior = BottomSheetBehavior.from(parent)
                    contentView.measure(0, 0)
                    behavior.peekHeight = contentView.measuredHeight
                }
                bottomDialog!!.show()*/
            }
        }
        mTvServiceEditWorkStartTime.setOnClickListener {
            val timePicker = TimePickerDialog(mContext, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                if (!TextUtils.isEmpty(endTime)) {
                    val df = SimpleDateFormat("HH:mm")
                    val tmpEndTime = df.parse(endTime)
                    val tmpStartTime = df.parse("$hourOfDay:$minute")
                    if (tmpStartTime.time >= tmpEndTime.time) {
                        toast("结束时间必须大于开始时间")
                    } else {
                        startTime = "$hourOfDay:$minute"
                        mTvServiceEditWorkStartTime.text = startTime
                    }
                } else {
                    startTime = "$hourOfDay:$minute"
                    mTvServiceEditWorkStartTime.text = startTime
                }
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true)
            timePicker.setTitle("请选择开始时间")
            timePicker.show()
        }
        mTvServiceEditWorkEndTime.setOnClickListener {
            val timePicker = TimePickerDialog(mContext, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                if (!TextUtils.isEmpty(startTime)) {
                    val df = SimpleDateFormat("HH:mm")
                    val tmpEndTime = df.parse("$hourOfDay:$minute")
                    val tmpStartTime = df.parse(startTime)
                    if (tmpStartTime.time >= tmpEndTime.time) {
                        toast("结束时间必须大于开始时间")
                    } else {
                        endTime = "$hourOfDay:$minute"
                        mTvServiceEditWorkEndTime.text = endTime
                    }
                } else {
                    endTime = "$hourOfDay:$minute"
                    mTvServiceEditWorkEndTime.text = endTime
                }
            }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true)
            timePicker.setTitle("请选择结束时间")
            timePicker.show()
        }
        mBtMinePersonalServiceEditSubmit.setOnClickListener {
            if (mEtMinePersonalServicePhone.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (!mEtMinePersonalServicePhone.isPhone()) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (selectAddress == null) {
                toast("请选择店铺地址")
                return@setOnClickListener
            }
            if (mEtMinePersonalServiceDetailAddress.isEmpty()) {
                toast("请输入详情的店铺地址")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(startTime)) {
                toast("请选择营业开始时间")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(endTime)) {
                toast("请选择营业结束时间")
                return@setOnClickListener
            }
            if (mEtMinePersonalServiceContent.isEmpty()) {
                toast("请输入服务介绍")
                return@setOnClickListener
            }
            if (currentWorkRange == null) {
                toast("请选择服务类型")
                return@setOnClickListener
            }
            if (shopInfo != null) {
                mPresent.getDataByPost(0x1,
                        RequestParamsHelper.MEMBER_MODEL,
                        RequestParamsHelper.ACT_EDIT_PERSONAL_SERVICE,
                        RequestParamsHelper.getEditePersonalServiceParam(
                                shopInfo!!.shop_id,
                                selectAddress!!,
                                mEtMinePersonalServiceDetailAddress.getTextString(),
                                mTvServiceEditWorkRange.text.toString(),
                                startTime!!,
                                endTime!!,
                                mEtMinePersonalServiceContent.getTextString()))
            }
        }
        mEtMinePersonalServiceAddress.setOnClickListener {
            startActivity(Intent(mContext, SelectAddressActivity::class.java))
            (mContext as FragmentContainerActivity).overridePendingTransition(R.anim.activity_open, 0)
        }
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PERSONAL_SERVICE, RequestParamsHelper.getPersonalServiceParam())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            getFastProgressDialog("正在加载……")
        } else if (requestID == 0x1) {
            getFastProgressDialog("正在提交……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                val jsonObject = check.obj as JSONObject
                shopInfo = Gson().fromJson(jsonObject.optJSONObject("result").toString(), UserInfo.ShopInfo::class.java)
                setText(mEtMinePersonalServicePhone, shopInfo!!.shop_phone)
                setText(mEtMinePersonalServiceAddress, shopInfo!!.shop_address)
                selectAddress = shopInfo!!.shop_address
                setText(mTvServiceEditWorkStartTime, shopInfo!!.shop_start_time)
                setText(mTvServiceEditWorkEndTime, shopInfo!!.shop_end_time)
                setText(mEtMinePersonalServiceContent, shopInfo!!.shop_content)
                setText(mEtMinePersonalServiceDetailAddress, shopInfo!!.shop_d)
                setText(mTvServiceEditWorkRange, shopInfo!!.shop_ppa)
                if (!TextUtils.isEmpty(shopInfo!!.shop_ppa)) {
                    currentWorkRange = WorkRangeBean()
                    currentWorkRange!!.product_tag_title = shopInfo!!.shop_ppa
                }
                startTime = shopInfo!!.shop_start_time
                endTime = shopInfo!!.shop_end_time
                mBtMinePersonalServiceEditSubmit.isEnabled = true
                val jsonArray = jsonObject.optJSONArray("arr")
                (0 until jsonArray.length()).forEach {
                    workRangeList.add(Gson().fromJson(jsonArray.optJSONObject(it).toString(), WorkRangeBean::class.java))
                }
            }
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    toast("修改成功！")
                    UserInfo.getInstance().shopInfo.shop_address = selectAddress + mEtMinePersonalServiceDetailAddress.getTextString()
                    UserInfo.getInstance().shopInfo.shop_d = mEtMinePersonalServiceDetailAddress.getTextString()
                    UserInfo.getInstance().shopInfo.shop_content = mEtMinePersonalServiceContent.getTextString()
                    UserInfo.getInstance().shopInfo.shop_start_time = mTvServiceEditWorkStartTime.text.toString()
                    UserInfo.getInstance().shopInfo.shop_end_time = mTvServiceEditWorkEndTime.text.toString()
                    UserInfo.getInstance().shopInfo.shop_phone = mEtMinePersonalServicePhone.getTextString()
                    RxBus.getDefault().post(EventIsMasterAndMoneyBean.getInstance())
                    finish()
                } else {
                    toast((check.obj as JSONObject).optString("msg"))
                }
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x0) {
            toast("加载失败……")
        }
    }

    fun setText(textView: TextView, text: String) {
        textView.text = if (TextUtils.isEmpty(text)) {
            "暂无"
        } else {
            text
        }
    }

    override fun onDestroyView() {
        unsubscribe(addressSubscription)
        super.onDestroyView()
    }


    class WorkRangeBean {
        var product_tag_title: String? = null
        var product_tag_id: String? = null
        var product_tag_sort: String? = null
    }

}