package com.android.ql.lf.carapp.ui.fragments.mall.normal

import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.present.MallOrderPresent
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_refund_layout.*

/**
 * Created by lf on 2017/12/9 0009.
 * @author lf on 2017/12/9 0009
 */
class RefundFragment : BaseNetWorkingFragment() {

    companion object {
        val OID_FLAG = "oid_flag"
    }

    private var bottomDialog: BottomSheetDialog? = null

    override fun getLayoutId() = R.layout.fragment_refund_layout

    private var refundCase: String? = null

    override fun initView(view: View?) {
        mEtRefundSelectPhone.setText(UserInfo.getInstance().memberPhone)
        mTvRefundSelectCase.setOnClickListener {
            if (bottomDialog == null) {
                bottomDialog = BottomSheetDialog(mContext)
                val contentView = View.inflate(mContext, R.layout.dialog_refund_layout, null)
                val rv_content = contentView.findViewById<RecyclerView>(R.id.mRvRefundContent)
                val iv_close = contentView.findViewById<ImageView>(R.id.mIvRefundClose)
                iv_close.setOnClickListener {
                    bottomDialog!!.dismiss()
                }
                rv_content.layoutManager = LinearLayoutManager(mContext)
                val tempList = arrayListOf<String>()
                tempList.add("订单不能按预计时间送达")
                tempList.add("操作有误（商品、地址等选错）")
                tempList.add("重复下单/误下单")
                tempList.add("其它渠道价格更低")
                tempList.add("该商品降价了")
                tempList.add("不想买了")
                tempList.add("其它原因")
                rv_content.adapter = object : BaseQuickAdapter<String, BaseViewHolder>(android.R.layout.simple_list_item_1, tempList) {
                    override fun convert(helper: BaseViewHolder?, item: String?) {
                        helper!!.setText(android.R.id.text1, item)
                    }
                }
                rv_content.addOnItemTouchListener(object : OnItemClickListener() {
                    override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                        val item = tempList[position]
                        mTvRefundSelectCase.text = item
                        refundCase = item
                        bottomDialog!!.dismiss()
                    }
                })
                bottomDialog!!.setContentView(contentView)
                val parent = contentView.parent as View
                val behavior = BottomSheetBehavior.from(parent)
                contentView.measure(0, 0)
                behavior.peekHeight = contentView.measuredHeight
            }
            bottomDialog!!.show()
        }
        mBtRefundRefund.setOnClickListener {
            if (TextUtils.isEmpty(refundCase)) {
                toast("请先选择退款原因")
                return@setOnClickListener
            }
            if (mEtRefundSelectName.isEmpty()) {
                toast("请输入退款联系人姓名")
                return@setOnClickListener
            }
            if (mEtRefundSelectPhone.isEmpty()) {
                toast("请输入联系人手机号")
                return@setOnClickListener
            }
            if (!mEtRefundSelectPhone.isPhone()) {
                toast("请输入合法的手机号")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.ORDER_MODEL,
                    RequestParamsHelper.ACT_REFUND,
                    RequestParamsHelper.getRefundParam(
                            mEtRefundSelectName.getTextString(),
                            mEtRefundSelectPhone.getTextString(),
                            arguments.getString(OID_FLAG, ""),
                            refundCase!!))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在申请退款……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val json = checkResultCode(result)
        if (json != null && json.code == SUCCESS_CODE) {
            toast("退款申请成功")
            MallOrderPresent.notifyRefreshOrderNum()
            MallOrderPresent.notifyRefreshOrderList()
            finish()
        } else {
            toast("退款申请失败")
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("退款申请失败")
    }


}