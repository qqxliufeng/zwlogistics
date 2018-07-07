package com.android.ql.lf.zwlogistics.ui.fragment.bottom

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

import android.view.ViewGroup
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.activity.MainActivity
import com.android.ql.lf.zwlogistics.ui.activity.SelectAddressActivity
import com.android.ql.lf.zwlogistics.ui.adapter.OrderItemAdapter
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseRecyclerViewFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.car.SelectMultiTypeFragment
import com.android.ql.lf.zwlogistics.ui.fragment.order.OrderInfoFragment
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_index_layout.*


class IndexFragment : BaseRecyclerViewFragment<String>() {


    private var sourceAddress: SelectAddressActivity.SelectAddressItemBean? = null

    private var desAddress: SelectAddressActivity.SelectAddressItemBean? = null

    override fun getLayoutId() = R.layout.fragment_index_layout


    override fun createAdapter() = OrderItemAdapter(R.layout.adapter_index_order_item_layout, mArrayList)

    private val selectMultiTypeFragment by lazy {
        SelectMultiTypeFragment()
    }

    override fun initView(view: View?) {
        super.initView(view)
        val param = mTvMainIndexTitle.layoutParams as ViewGroup.MarginLayoutParams
        param.topMargin = (mContext as MainActivity).statusHeight

        mLlIndexOrderSourceAddressContainer.setOnClickListener {
            mCtvIndexOrderSourceAddress.isChecked = true
            SelectAddressActivity.startAddressActivityForResult(this, 1)
        }

        mLlIndexOrderDesAddressContainer.setOnClickListener {
            mCtvIndexOrderDesAddress.isChecked = true
            SelectAddressActivity.startAddressActivityForResult(this, 1)
        }

        mLlIndexOrderCarTypeContainer.setOnClickListener {
            mCtvIndexOrderCarType.isChecked = true
            selectMultiTypeFragment.myShow(childFragmentManager, "select_multi_type_dialog", {
                mCtvIndexOrderCarType.isChecked = false
                if (it?.size == 1 && it.contains("")){
                    mCtvIndexOrderCarType.text = "车型车长"
                    return@myShow
                }
                val stringBuilder = StringBuilder()
                it?.filter { "" != it }?.forEach {
                    stringBuilder.append(it).append(",")
                }
                stringBuilder.deleteCharAt(stringBuilder.length - 1)
                Log.e("TAG",stringBuilder.toString())
                mCtvIndexOrderCarType.text = stringBuilder.toString()
            }) {
                mCtvIndexOrderCarType.isChecked = false
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        testAdd("")
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.TRANSPARENT))
        return itemDecoration
    }


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity.from(mContext).setTitle("我要竞标").setClazz(OrderInfoFragment::class.java).setNeedNetWorking(true).start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            mCtvIndexOrderSourceAddress.isChecked = false
            mCtvIndexOrderDesAddress.isChecked = false
            if (data != null) {
                val addressBean = data.getParcelableExtra<SelectAddressActivity.SelectAddressItemBean>(SelectAddressActivity.REQUEST_DATA_FALG)
                if (mCtvIndexOrderSourceAddress.isChecked) {
                    sourceAddress = addressBean
                    mCtvIndexOrderSourceAddress.text = sourceAddress?.name
                }
                if (mCtvIndexOrderDesAddress.isChecked) {
                    desAddress = addressBean
                    mCtvIndexOrderDesAddress.text = sourceAddress?.name
                }
            }
        }
    }

}