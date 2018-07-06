package com.android.ql.lf.carapp.ui.fragments.mall.normal

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.json.JSONObject

/**
 * Created by lf on 18.5.29.
 * @author lf on 18.5.29
 */
class PlatformCouponFragment : BaseRecyclerViewFragment<NewGoodsInfoFragment.CouponBean>() {

    override fun createAdapter(): BaseQuickAdapter<NewGoodsInfoFragment.CouponBean, BaseViewHolder> =
            object : BaseQuickAdapter<NewGoodsInfoFragment.CouponBean, BaseViewHolder>(R.layout.adapter_platform_coupon_item_layout, mArrayList) {
        override fun convert(helper: BaseViewHolder?, item: NewGoodsInfoFragment.CouponBean?) {
            helper!!.setText(R.id.mTvPlatformCouponItemMoney, item!!.discount_price)
            helper.setText(R.id.mTvPlatformCouponItemDes, "购满${item.discount_fr}元使用")
            helper.setText(R.id.mTvPlatformCouponItemTime, "使用时间：${item.discount_time}-${item.discount_validity}")
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, "t", RequestParamsHelper.ACT_PLATFORM_DISCOUNT, RequestParamsHelper.getPlatformDiscountParam())
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val decoration = super.getItemDecoration() as DividerItemDecoration
        decoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_line_bg))
        return decoration
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在领取……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when (requestID) {
            0x0 -> {
                processList(result as String, NewGoodsInfoFragment.CouponBean::class.java)
                setLoadEnable(false)
            }
            0x1 -> {
                val check = checkResultCode(result)
                if (check != null) {
                    toast((check.obj as JSONObject).optString(MSG_FLAG))
                }
            }
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        mPresent.getDataByPost(0x1, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_GET_DISCOUNT, RequestParamsHelper.getDiscountParam(mArrayList[position].discount_id!!))
    }

}