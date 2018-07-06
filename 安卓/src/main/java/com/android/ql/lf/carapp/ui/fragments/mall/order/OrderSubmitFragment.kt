package com.android.ql.lf.carapp.ui.fragments.mall.order

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.*
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.*
import com.android.ql.lf.carapp.present.MallOrderPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.mall.address.AddressSelectFragment
import com.android.ql.lf.carapp.ui.fragments.mall.normal.NewGoodsInfoFragment
import com.android.ql.lf.carapp.ui.views.PopupWindowDialog
import com.android.ql.lf.carapp.ui.views.SelectPayTypeView
import com.android.ql.lf.carapp.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_order_submit_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject
import java.text.DecimalFormat

/**
 * Created by lf on 18.3.22.
 * @author lf on 18.3.22
 */
class OrderSubmitFragment : BaseRecyclerViewFragment<ShoppingCarItemBean>() {

    companion object {
        val GOODS_ID_FLAG = "goods_id_flag"
        val PAY_MALL_ORDER_FLAG = "pay_mall_order_flag"
    }

    private var tempList: ArrayList<ShoppingCarItemBean>? = null

    /**
     * 头部View
     */
    private val headerView by lazy {
        View.inflate(mContext, R.layout.layout_submit_new_order_header_layout, null)
    }

    /**
     * 空地址View
     */
    private val emptyAddressButton by lazy {
        headerView.findViewById<Button>(R.id.mBtSubmitOrderHeaderNoAddress)
    }

    /**
     * 地址包含View
     */
    private val selectAddressContainerView by lazy {
        headerView.findViewById<LinearLayout>(R.id.mLlSubmitOrderAddress)
    }

    /**
     * 地址名
     */
    private val addressName by lazy {
        headerView.findViewById<TextView>(R.id.mIvSubmitOrderAddressName)
    }

    /**
     * 地址手机号
     */
    private val addressPhone by lazy {
        headerView.findViewById<TextView>(R.id.mIvSubmitOrderAddressPhone)
    }

    /**
     * 地址详细信息
     */
    private val addressDetail by lazy {
        headerView.findViewById<TextView>(R.id.mIvSubmitOrderAddressDetail)
    }

    /**
     * 底部View
     */
    private val footerView by lazy { View.inflate(mContext, R.layout.layout_submit_new_order_footer_layout, null) }

    private val selectTypeView by lazy {
        footerView.findViewById<SelectPayTypeView>(R.id.mStvPay)
    }

    private val invoiceContainer by lazy {
        footerView.findViewById<RelativeLayout>(R.id.mRlInvoiceContainer)
    }

    private val couponContainer by lazy {
        footerView.findViewById<RelativeLayout>(R.id.mRlCouponContainer)
    }

    private val selectInvoice by lazy {
        footerView.findViewById<CheckBox>(R.id.mCbInvoice)
    }

    private val couponName by lazy {
        footerView.findViewById<TextView>(R.id.mTvCouponName)
    }

    private val contentView: View by lazy {
        View.inflate(mContext, R.layout.dialog_bbs_layout, null)
    }

    private val addressSubscription by lazy {
        RxBus.getDefault().toObservable(AddressBean::class.java).subscribe {
            if (it != null) {
                addressBean = it
                setAddressInfo(addressBean!!)
                //加载运费模板
                mPresent.getDataByPost(0x3, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_ADDRESS,
                        RequestParamsHelper.getAddressParams(addressBean!!.address_id, freightId.toString()))
            }
        }
    }

    private val paySubscription by lazy {
        RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (it.isRefresh && it.any == PAY_MALL_ORDER_FLAG) {
                finish()
            }
        }
    }

    private val couponBottomDialog by lazy {
        BottomSheetDialog(mContext)
    }

    private var couponContentView: View? = null

    private var addressBean: AddressBean? = null

    private val orderList = arrayListOf<MallSubmitOrderBean>()

    private var payType: String = SelectPayTypeView.WX_PAY

    private val couponList = arrayListOf<OrderCouponBean>()

    private val expressList = arrayListOf<String>()

    private var couponBean: OrderCouponBean? = null

    private var money = 0.00f

    private val shopId by lazy {
        StringBuilder()
    }

    private val freightId by lazy {
        StringBuilder()
    }

    private val handle by lazy {
        @SuppressLint("HandlerLeak")
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
                            bundle.putInt(OrderPayResultFragment.PAY_CODE_FLAG, OrderPayResultFragment.PAY_SUCCESS_CODE)
                        } else {
                            //支付失败
                            bundle.putInt(OrderPayResultFragment.PAY_CODE_FLAG, OrderPayResultFragment.PAY_FAIL_CODE)
                        }
//                    OrderPresent.notifyRefreshOrderNum()
                        FragmentContainerActivity
                                .from(mContext)
                                .setTitle("支付结果")
                                .setNeedNetWorking(true)
                                .setExtraBundle(bundle)
                                .setClazz(OrderPayResultFragment::class.java)
                                .start()
                        finish()
                    }
                }
            }
        }
    }

    override fun createAdapter(): BaseQuickAdapter<ShoppingCarItemBean, BaseViewHolder> {
        return object : BaseQuickAdapter<ShoppingCarItemBean, BaseViewHolder>(R.layout.adapter_submit_order_info_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: ShoppingCarItemBean?) {
                val iv_pic = helper!!.getView<ImageView>(R.id.mIvSubmitOrderGoodsPic)
                GlideManager.loadImage(iv_pic.context, item!!.sku_pic, iv_pic)
                helper.setText(R.id.mIvSubmitOrderGoodsName, item.shopcart_name)
                helper.setText(R.id.mTvSubmitOrderItemStoreName, item.shop_shopname)
                helper.setText(R.id.mTvSubmitOrderGoodsExpressPrice, "商家配送 ￥${item.shopcart_mdprice}")
                GlideManager.loadImage(mContext, item.shop_shoppic, helper.getView(R.id.mIvSubmitOrderItemStorePic))
                helper.setText(R.id.mIvSubmitOrderGoodsSpe, item.shopcart_specification)
                helper.setText(R.id.mIvSubmitOrderGoodsPrice, "￥${item.shopcart_price}")
                helper.setText(R.id.mIvSubmitOrderGoodsNum, "X${item.shopcart_num}")
                helper.setText(R.id.mTvSubmitOrderGoodsTotal, Html.fromHtml("共${item.shopcart_num}件商品  小计:<span style='color:#E1332C'>￥${(item.shopcart_price.toFloat() * item.shopcart_num.toInt()) + item.shopcart_mdprice.toFloat()}元</span>"))
                helper.setText(R.id.mTvSubmitOrderGoodsBBSContent, if (TextUtils.isEmpty(item.bbs)) "选填" else item.bbs)
                helper.addOnClickListener(R.id.mRlSubmitOrderGoodsBBS)
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_order_submit_layout

    override fun initView(view: View?) {
        arguments.classLoader = this.javaClass.classLoader
        tempList = arguments.getParcelableArrayList(GOODS_ID_FLAG)
        super.initView(view)
        addressSubscription
        paySubscription
        mSwipeRefreshLayout.isEnabled = false
        mBaseAdapter.addHeaderView(headerView)
        mBaseAdapter.addFooterView(footerView)
        emptyAddressButton.setOnClickListener {
            FragmentContainerActivity.from(mContext).setTitle("选择地址").setNeedNetWorking(true).setClazz(AddressSelectFragment::class.java).start()
        }
        selectAddressContainerView.setOnClickListener {
            FragmentContainerActivity.from(mContext).setTitle("选择地址").setNeedNetWorking(true).setClazz(AddressSelectFragment::class.java).start()
        }
        invoiceContainer.setOnClickListener {
            //是否开具发票
            selectInvoice.isChecked = !selectInvoice.isChecked
        }
        couponContainer.setOnClickListener {
            //选择优惠券
            if (couponList.isEmpty()) {
                mPresent.getDataByPost(0x2, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_MY_DISCOUNT, RequestParamsHelper.getMyDiscountParam(shopid = shopId.toString()))
            } else {
                showCouponList()
            }
        }
        mTvSubmitOrder.setOnClickListener {
            if (addressBean == null) {
                toast("请选择收货地址")
                return@setOnClickListener
            }
            mArrayList.forEach {
                val orderBean = MallSubmitOrderBean()
                orderBean.address = addressBean!!.address_id
                orderBean.gid = it.shopcart_gid
                orderBean.cid = it.shopcart_id
                orderBean.mliuyan = it.bbs
                orderBean.num = it.shopcart_num
                orderBean.specification = it.shopcart_specification
                orderBean.price = it.shopcart_price
                orderBean.mdprice = it.shopcart_mdprice
                orderBean.bbs = it.bbs
                orderBean.key = it.shopcart_key
                orderBean.pic = it.sku_pic
                orderList.add(orderBean)
            }
            val json = Gson().toJson(orderList)
            payType = selectTypeView.payType
            mPresent.getDataByPost(0x1, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_ADD_ORDER,
                    RequestParamsHelper.getAddOrderParams(
                            invoice = if (selectInvoice.isChecked) "1" else "0",
                            paytype = payType,
                            post_data = json,
                            discount = if (couponBean == null) "" else if(couponBean!!.discount_id == null) "" else couponBean!!.discount_id!!))
            orderList.clear()
        }
    }

    private fun showCouponList() {
        if (couponContentView == null) {
            val orderCouponBean = OrderCouponBean()
            orderCouponBean.discount_title = "暂不使用优惠券"
            orderCouponBean.discount_id = null
            couponList.add(orderCouponBean)
            couponContentView = View.inflate(mContext, R.layout.dialog_bottom_coupon_layout, null)
            val mCouponList = couponContentView!!.findViewById<RecyclerView>(R.id.mRvBottomCouponList)
            couponContentView!!.findViewById<ImageView>(R.id.mIvBottomCouponClose).setOnClickListener { couponBottomDialog.dismiss() }
            mCouponList.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            mCouponList.adapter = object : BaseQuickAdapter<OrderCouponBean, BaseViewHolder>(android.R.layout.simple_list_item_1, couponList) {
                override fun convert(helper: BaseViewHolder?, item: OrderCouponBean?) {
                    helper!!.setText(android.R.id.text1, item!!.discount_title)
                }
            }
            mCouponList.addOnItemTouchListener(object : OnItemClickListener() {
                override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    couponBean = couponList[position]
                    if (couponBean!!.discount_id == null) {
                        couponName.text = "暂不使用"
                        mTvSubmitOrderGoodsPrice.text = "￥ " + DecimalFormat("0.00").format(money)
                    } else {
                        if (couponBean!!.discount_fr!!.toFloat() > money) {
                            toast("当前订单金额不支持此优惠券")
                            couponName.text = "暂不使用"
                            couponBean!!.discount_id = null
                            mTvSubmitOrderGoodsPrice.text = "￥ " + DecimalFormat("0.00").format(money)
                        } else {
                            couponName.text = couponBean!!.discount_title
                            var tempMoney = money
                            tempMoney -= couponBean!!.discount_price!!.toFloat()
                            mTvSubmitOrderGoodsPrice.text = "￥ " + DecimalFormat("0.00").format(tempMoney)
                        }
                    }
                    couponBottomDialog.dismiss()
                }
            })
            couponBottomDialog.setContentView(couponContentView)
            val height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400.0f, context.resources.displayMetrics)
            couponContentView!!.layoutParams.height = height.toInt()
            val behavior = BottomSheetBehavior.from(couponContentView!!.parent as View)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = height.toInt()
        }
        couponBottomDialog.show()
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    private fun setAddressInfo(addressBean: AddressBean) {
        addressName.text = addressBean.address_name
        addressPhone.text = addressBean.address_phone
        addressDetail.text = "${addressBean.address_addres} ${addressBean.address_detail}"
        emptyAddressButton.visibility = View.GONE
        selectAddressContainerView.visibility = View.VISIBLE
        //根据距离重新计算运费价格
        if (!expressList.isEmpty() && !mArrayList.isEmpty() && expressList.size == mArrayList.size) {
            expressList.forEachIndexed { index, s ->
                mArrayList[index].shopcart_mdprice = s
            }
            money = 0.00f
            mArrayList.forEach {
                money += ((it.shopcart_price.toFloat() * it.shopcart_num.toInt()) + it.shopcart_mdprice.toFloat())
            }
            mTvSubmitOrderGoodsPrice.text = "￥ " + DecimalFormat("0.00").format(money)
            mBaseAdapter.notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        setRefreshEnable(false)
        setLoadEnable(false)
        initList()
        //加载地址
        mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_DEFAULT_ADDRESS, RequestParamsHelper.getDefaultAddress(freightId.toString()))
    }

    private fun initList() {
        if (tempList != null && !tempList!!.isEmpty()) {
            money = 0.00f
            var num = 0
            tempList!!.forEach {
                money += ((it.shopcart_price.toFloat() * it.shopcart_num.toInt()) + it.shopcart_mdprice.toFloat())
                num += it.shopcart_num.toInt()
                it.sku_pic = it.shopcart_pic
                shopId.append(it.shop_id).append(",")
                freightId.append(it.shopcart_freight).append(",")
            }
            shopId.deleteCharAt(shopId.lastIndex)
            freightId.deleteCharAt(freightId.lastIndex)
            mTvSubmitOrderGoodsCount.text = Html.fromHtml("共<span style='color:#78BFFF'> $num </span>件")
            mTvSubmitOrderGoodsPrice.text = "￥ " + DecimalFormat("0.00").format(money)
            mArrayList.addAll(tempList!!)
            mBaseAdapter.notifyDataSetChanged()
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        when (requestID) {
            0x0 -> getFastProgressDialog("正在加载地址……")
            0x1 -> getFastProgressDialog("正在提交订单……")
            0x2 -> getFastProgressDialog("正在加载优惠券……")
            0x3 -> getFastProgressDialog("")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0 || requestID == 0x3) {
            //加载地址
            try {
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    val resultJson = check.obj as JSONObject
                    addressBean = Gson().fromJson(resultJson.optJSONObject("result").toString(), AddressBean::class.java)
                } else {
                    emptyAddressButton.visibility = View.VISIBLE
                    selectAddressContainerView.visibility = View.GONE
                }
                if (check != null) {
                    expressList.clear()
                    val jsonArray = (check.obj as JSONObject).optJSONArray("arr")
                    if (jsonArray != null) {
                        (0 until jsonArray.length()).forEach {
                            expressList.add(jsonArray.optString(it))
                        }
                    }
                    if (addressBean != null) {
                        setAddressInfo(addressBean!!)
                    }
                }
            } catch (e: Exception) {
                emptyAddressButton.visibility = View.VISIBLE
                selectAddressContainerView.visibility = View.GONE
            }
        } else if (requestID == 0x1) {
            //提交订单
            val check = checkResultCode(result)
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    MallOrderPresent.notifyRefreshShoppingCarList()
                    when (payType) {
                        SelectPayTypeView.WX_PAY -> {
                            PreferenceUtils.setPrefBoolean(mContext, "is_mall_order", true)
                            val wxBean = Gson().fromJson((check.obj as JSONObject).optJSONObject("result").toString(), WXPayBean::class.java)
                            PayManager.wxPay(mContext, wxBean)
                        }
                        SelectPayTypeView.ALI_PAY -> PayManager.aliPay(mContext, handle, (check.obj as JSONObject).optString("result"))
                        SelectPayTypeView.ACCOUNT_PAY -> {
                            FragmentContainerActivity
                                    .from(mContext)
                                    .setTitle("支付结果")
                                    .setNeedNetWorking(true)
                                    .setExtraBundle(bundleOf(Pair(OrderPayResultFragment.PAY_CODE_FLAG, OrderPayResultFragment.PAY_SUCCESS_CODE)))
                                    .setClazz(OrderPayResultFragment::class.java)
                                    .start()
                            finish()
                        }
                    }
                } else {
                    toast((check.obj as JSONObject).optString(MSG_FLAG))
                }
            }
        } else if (requestID == 0x2) {
            try {
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    val jsonArray = (check.obj as JSONObject).optJSONArray("result")
                    if (jsonArray != null && jsonArray.length() > 0) {
                        (0 until jsonArray.length()).forEach {
                            couponList.add(Gson().fromJson(jsonArray.optJSONObject(it).toString(), OrderCouponBean::class.java))
                        }
                        showCouponList()
                    } else {
                        toast("暂无优惠券")
                    }
                } else {
                    toast("暂无优惠券")
                }
            } catch (e: Exception) {
                toast("暂无优惠券")
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x0) {
            emptyAddressButton.visibility = View.VISIBLE
            selectAddressContainerView.visibility = View.GONE
        } else if (requestID == 0x2) {
            toast("暂无优惠券")
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val currentItem = mArrayList[position]
        when (view!!.id) {
            R.id.mRlSubmitOrderGoodsBBS -> {
                val popupDialog = PopupWindowDialog.showReplyDialog(mContext, contentView)
                val et_content = contentView.findViewById<EditText>(R.id.mEtDialogBbsContent)
                val tv_finish = contentView.findViewById<TextView>(R.id.mTvDialogBbsFinish)
                et_content.setText("")
                tv_finish.setOnClickListener {
                    if (et_content.isEmpty()) {
                        toast("请输入留言内容")
                        return@setOnClickListener
                    }
                    et_content.clearFocus()
                    currentItem.bbs = et_content.getTextString()
                    mBaseAdapter.notifyItemChanged(position + 1)
                    popupDialog.dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        unsubscribe(addressSubscription)
        unsubscribe(paySubscription)
        super.onDestroyView()
    }

    class OrderCouponBean {
        var getdiscount_id: String? = null
        var getdiscount_uid: String? = null
        var getdiscount_theme: String? = null
        var getdiscount_type: String? = null
        var getdiscount_status: String? = null
        var discount_id: String? = null
        var discount_price: String? = null
        var discount_num: String? = null
        var discount_validity: String? = null
        var discount_time: String? = null
        var discount_shopping: String? = null
        var discount_type: String? = null
        var discount_status: String? = null
        var discount_token: String? = null
        var discount_fr: String? = null
        var discount_uid: String? = null
        var discount_already: String? = null
        var discount_title: String? = null
    }

}