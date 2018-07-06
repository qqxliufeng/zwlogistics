package com.android.ql.lf.carapp.ui.adapter

import android.text.TextUtils
import android.view.View
import android.widget.Button
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.MallSaleOrderBean
import com.android.ql.lf.carapp.present.MallOrderPresent
import com.android.ql.lf.carapp.utils.GlideManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 18.3.27.
 * @author lf on 18.3.27
 */
class MainMallOrderItemAdapter(layoutId: Int, list: ArrayList<MallSaleOrderBean>) : BaseQuickAdapter<MallSaleOrderBean, BaseViewHolder>(layoutId, list) {

    override fun convert(helper: BaseViewHolder?, item: MallSaleOrderBean?) {
        GlideManager.loadImage(mContext, item!!.product_shoppic, helper!!.getView(R.id.mIvMallOrderListItemStorePic))
        GlideManager.loadImage(mContext, item.order_sku_pic, helper.getView(R.id.mIvOrderListItemPic))
        helper.setText(R.id.mTvOrderListItemTitle, item.product_name)
        helper.setText(R.id.mTvOrderListItemSpecification, item.order_specification)
        helper.setText(R.id.mIvOrderListItemNum, "X${item.order_num}")
        helper.setText(R.id.mTvOrderListItemPrice, "￥${item.order_price}")
        helper.setText(R.id.mTvMallOrderListItemStoreName, item.product_shopname)
        val bt_action1 = helper.getView<Button>(R.id.mBtOrderListItemAction1)
        val bt_action2 = helper.getView<Button>(R.id.mBtOrderListItemAction2)
        val bt_action0 = helper.getView<Button>(R.id.mBtOrderListItemAction0)
        if (!TextUtils.isEmpty(item.order_token)) {
            when (item.order_token) {
                MallOrderPresent.MallOrderStatus.WAITING_FOR_MONEY.index -> {
                    bt_action1.visibility = View.VISIBLE
                    bt_action2.visibility = View.VISIBLE
                    bt_action0.visibility = View.GONE
                    bt_action1.text = "取消订单"
                    bt_action2.text = "去付款"
                    helper.setText(R.id.mTvShoppingCarItemEditMode, MallOrderPresent.MallOrderStatus.WAITING_FOR_MONEY.description)
                    helper.addOnClickListener(R.id.mBtOrderListItemAction1)
                    helper.addOnClickListener(R.id.mBtOrderListItemAction2)
                }
                MallOrderPresent.MallOrderStatus.WAITING_FOR_SEND.index -> {
                    bt_action1.visibility = View.VISIBLE
                    bt_action2.visibility = View.GONE
                    bt_action0.visibility = View.GONE
                    bt_action1.text = "申请退款"
                    helper.setText(R.id.mTvShoppingCarItemEditMode, MallOrderPresent.MallOrderStatus.WAITING_FOR_SEND.description)
                    helper.addOnClickListener(R.id.mBtOrderListItemAction1)
                }
                MallOrderPresent.MallOrderStatus.WAITING_FOR_RECEIVER.index -> {
                    bt_action1.visibility = View.VISIBLE
                    bt_action2.visibility = View.VISIBLE
                    bt_action0.visibility = View.VISIBLE
                    bt_action1.text = "查看物流"
                    bt_action2.text = "确认收货"
                    bt_action0.text = "申请退款"
                    helper.setText(R.id.mTvShoppingCarItemEditMode, MallOrderPresent.MallOrderStatus.WAITING_FOR_RECEIVER.description)
                    helper.addOnClickListener(R.id.mBtOrderListItemAction1)
                    helper.addOnClickListener(R.id.mBtOrderListItemAction2)
                    helper.addOnClickListener(R.id.mBtOrderListItemAction0)
                }
                MallOrderPresent.MallOrderStatus.WAITING_FOR_EVALUATE.index -> {
                    bt_action1.visibility = View.GONE
                    bt_action2.visibility = View.VISIBLE
                    bt_action0.visibility = View.GONE
                    bt_action2.text = "去评价"
                    helper.setText(R.id.mTvShoppingCarItemEditMode, MallOrderPresent.MallOrderStatus.WAITING_FOR_EVALUATE.description)
                    helper.addOnClickListener(R.id.mBtOrderListItemAction2)
                }
                MallOrderPresent.MallOrderStatus.MALL_ORDER_COMPLEMENT.index -> {
                    bt_action1.visibility = View.VISIBLE
                    bt_action2.visibility = View.GONE
                    bt_action0.visibility = View.GONE
                    bt_action1.text = "查看订单"
                    helper.setText(R.id.mTvShoppingCarItemEditMode, MallOrderPresent.MallOrderStatus.MALL_ORDER_COMPLEMENT.description)
                    helper.addOnClickListener(R.id.mBtOrderListItemAction1)
                }
                MallOrderPresent.MallOrderStatus.MALL_ORDER_CANCEL.index -> {
                    helper.setText(R.id.mTvShoppingCarItemEditMode, MallOrderPresent.MallOrderStatus.MALL_ORDER_CANCEL.description)
                    bt_action1.visibility = View.GONE
                    bt_action2.visibility = View.GONE
                    bt_action0.visibility = View.GONE
                }
                MallOrderPresent.MallOrderStatus.MALL_ORDER_HAS_BACK.index -> {
                    helper.setText(R.id.mTvShoppingCarItemEditMode, MallOrderPresent.MallOrderStatus.MALL_ORDER_HAS_BACK.description)
                    bt_action1.visibility = View.GONE
                    bt_action2.visibility = View.GONE
                    bt_action0.visibility = View.GONE
                }
                MallOrderPresent.MallOrderStatus.MALL_ORDER_APPLY_BACK.index -> {
                    helper.setText(R.id.mTvShoppingCarItemEditMode, MallOrderPresent.MallOrderStatus.MALL_ORDER_APPLY_BACK.description)
                    bt_action1.visibility = View.GONE
                    bt_action2.visibility = View.GONE
                    bt_action0.visibility = View.GONE
                }
            }
        }
    }
}