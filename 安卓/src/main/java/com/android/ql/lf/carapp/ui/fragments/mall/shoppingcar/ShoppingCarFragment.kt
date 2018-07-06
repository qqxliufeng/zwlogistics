package com.android.ql.lf.carapp.ui.fragments.mall.shoppingcar

import android.annotation.SuppressLint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.RefreshData
import com.android.ql.lf.carapp.data.ShoppingCarItemBean
import com.android.ql.lf.carapp.present.ShoppingCarPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.adapter.ShoppingCarItemAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.mall.order.OrderSubmitFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.RxBus
import com.android.ql.lf.carapp.utils.alert
import com.android.ql.lf.carapp.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_shopping_car_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject
import java.text.DecimalFormat

/**
 * Created by lf on 2017/11/8 0008.
 * @author lf on 2017/11/8 0008
 */
@SuppressLint("RestrictedApi")
class ShoppingCarFragment : BaseRecyclerViewFragment<ShoppingCarItemBean>() {

    companion object {
        val REFRESH_SHOPPING_CAR_FLAG = "refresh shopping car"
    }

    private lateinit var currentItem: ShoppingCarItemBean

    private var currentEditMode = 1// 1 减  0  加

    private var isActioning = false

    private val selectedList = ArrayList<ShoppingCarItemBean>()

    private val shoppingCarPresent by lazy {
        ShoppingCarPresent(mArrayList)
    }

    private val shoppingCarSubscription by lazy {
        RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (RefreshData.isRefresh && RefreshData.any == REFRESH_SHOPPING_CAR_FLAG) {
//                mCalculate.isEnabled = false
//                mCivShoppingCarAllSelect.isChecked = false
//                onPostRefresh()
                finish()
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_shopping_car_layout

    override fun createAdapter(): BaseQuickAdapter<ShoppingCarItemBean, BaseViewHolder> =
            ShoppingCarItemAdapter(R.layout.adapter_shopping_car_item_layout, mArrayList)

    override fun initView(view: View?) {
        super.initView(view)
        shoppingCarSubscription
        mShoppingCarContainer.visibility = View.VISIBLE
        mCalculate.isEnabled = false
        setRefreshEnable(false)
        mLlShoppingCarAllSelectContainer.setOnClickListener {
            mCivShoppingCarAllSelect.isChecked = !mCivShoppingCarAllSelect.isChecked
            val results = if (mCivShoppingCarAllSelect.isChecked) {
                shoppingCarPresent.allItemSelects()
            } else {
                shoppingCarPresent.cancelItemsSelects()
            }
            mTvShoppingCarAllSelectMoney.text = shoppingCarPresent.formatPrice(results.second)
            mCalculate.isEnabled = !shoppingCarPresent.isNoneSelected()
            mBaseAdapter.notifyDataSetChanged()
        }
        mCalculate.setOnClickListener {
            selectedList.clear()
            selectedList.addAll(mArrayList.filter { it.isSelector } as ArrayList<ShoppingCarItemBean>)
            FragmentContainerActivity
                    .from(mContext)
                    .setTitle("确认订单")
                    .setNeedNetWorking(true)
                    .setExtraBundle(bundleOf(Pair(OrderSubmitFragment.Companion.GOODS_ID_FLAG, selectedList)))
                    .setClazz(OrderSubmitFragment::class.java)
                    .start()
        }
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration: DividerItemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.Companion.MEMBER_MODEL,
                RequestParamsHelper.ACT_SHOPCART, RequestParamsHelper.getShopcartParam(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        setLoadEnable(false)
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        when (requestID) {
            0x1 -> getFastProgressDialog("正在删除……")
            0x2 -> {
                isActioning = true
                getFastProgressDialog("")
            }
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            val check = checkResultCode(result)
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    if ((check.obj as JSONObject).optJSONArray("result").length() != 0) {
                        processList(result as String, ShoppingCarItemBean::class.java)
                    } else {
                        emptyShoppingCar()
                    }
                }
            }
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                toast("删除成功")
                mArrayList.remove(currentItem)
                mBaseAdapter.notifyDataSetChanged()
            }
        } else if (requestID == 0x2) {
            isActioning = false
            val check = checkResultCode(result)
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    if (currentEditMode == 0) {
                        currentItem.shopcart_num = (currentItem.shopcart_num.toInt() + 1).toString()
                    } else {
                        currentItem.shopcart_num = (currentItem.shopcart_num.toInt() - 1).toString()
                    }
                    mTvShoppingCarAllSelectMoney.text = "￥${DecimalFormat("0.00").format(shoppingCarPresent.calculateAllPrice())}"
                    mBaseAdapter.notifyItemChanged(mArrayList.indexOf(currentItem))
                }
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        if (requestID == 0x0) {
            emptyShoppingCar()
        }
        if (requestID == 0x2){
            isActioning = false
        }
    }

    private fun emptyShoppingCar() {
        mShoppingCarContainer.visibility = View.GONE
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemChildClick(adapter, view, position)
        currentItem = mArrayList[position]
        when (view?.id) {
            R.id.mIvShoppingCarItemSelector -> {
                currentItem.isSelector = !currentItem.isSelector
                mBaseAdapter.notifyItemChanged(position)
                mTvShoppingCarAllSelectMoney.text = "￥${DecimalFormat("0.00").format(shoppingCarPresent.calculateAllPrice())}"
                mCalculate.isEnabled = !shoppingCarPresent.isNoneSelected()
                mCivShoppingCarAllSelect.isChecked = shoppingCarPresent.isAllItemsSelected()
            }
            R.id.mIvShoppingCarDeleteNum -> {
                if (isActioning){ // 正在操作中，不能继续操作
                    return
                }
                if (currentItem.shopcart_num.toInt() <= 1) {
                    return
                }
                currentEditMode = 1
                mPresent.getDataByPost(0x2,
                        RequestParamsHelper.Companion.MEMBER_MODEL,
                        RequestParamsHelper.Companion.ACT_UPDATE_SHOPCART,
                        RequestParamsHelper.Companion.getUpdateShopcart(currentItem.shopcart_id, (currentItem.shopcart_num.toInt() - 1).toString()))
            }
            R.id.mIvShoppingCarAddNum -> {
                if (isActioning){ // 正在操作中，不能继续操作
                    return
                }
                currentEditMode = 0
                mPresent.getDataByPost(0x2,
                        RequestParamsHelper.Companion.MEMBER_MODEL,
                        RequestParamsHelper.Companion.ACT_UPDATE_SHOPCART,
                        RequestParamsHelper.Companion.getUpdateShopcart(currentItem.shopcart_id, (currentItem.shopcart_num.toInt() + 1).toString()))
            }
        }
    }

    override fun onMyItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        currentItem = mArrayList[position]
        alert("是否要删除当前商品？", "删除", "取消") { dialog, which ->
            mPresent.getDataByPost(0x1,
                    RequestParamsHelper.Companion.MEMBER_MODEL,
                    RequestParamsHelper.Companion.ACT_DEL_SHOPCART,
                    RequestParamsHelper.Companion.getDelShopcartParam(currentItem.shopcart_id))
        }
    }

    override fun onDestroyView() {
        unsubscribe(shoppingCarSubscription)
        super.onDestroyView()
    }
}