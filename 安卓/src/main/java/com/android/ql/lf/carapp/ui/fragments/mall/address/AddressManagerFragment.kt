package com.android.ql.lf.carapp.ui.fragments.mall.address

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.AddressBean
import com.android.ql.lf.carapp.data.RefreshData
import com.android.ql.lf.carapp.data.lists.ListParseHelper
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.adapter.AddressManagerListAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.RxBus
import com.android.ql.lf.carapp.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_fragment_container_layout.*
import kotlinx.android.synthetic.main.fragment_address_manage_list_layout.*
import org.json.JSONObject
import java.util.*

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class AddressManagerFragment : BaseRecyclerViewFragment<AddressBean>() {


    private var currentItem: AddressBean? = null
    private var topItem: AddressBean? = null

    private val addressSubscription by lazy {
        RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (it.isRefresh && it.any is String && "添加地址" == it.any) {
                onPostRefresh()
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_address_manage_list_layout

    override fun createAdapter(): BaseQuickAdapter<AddressBean, BaseViewHolder> =
            AddressManagerListAdapter(R.layout.adapter_address_manager_list_item_layout, mArrayList)

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun initView(view: View?) {
        super.initView(view)
        addressSubscription
        mTvAddNewAddress.setOnClickListener {
            FragmentContainerActivity
                    .from(mContext)
                    .setTitle("新增收货地址")
                    .setNeedNetWorking(true)
                    .setClazz(AddNewAddressFragment::class.java)
                    .start()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        setRefreshEnable(false)
        setLoadEnable(false)
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.MEMBER_MODEL,
                RequestParamsHelper.ACT_ADDRESS_LIST,
                RequestParamsHelper.getAddressListParams())
    }

    override fun getEmptyMessage() = "暂没有收货地址~~"

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID != 0x0) {
            getFastProgressDialog("正在操作……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) { //加载列表
            try {
                val json = checkResultCode(result.toString())
                if (json != null) {
                    mArrayList.addAll(ListParseHelper<AddressBean>().fromJson((json.obj as JSONObject).toString(), AddressBean::class.java))
                    if (mArrayList.size == 1) {
                        mArrayList[0].address_token = "1"
                    }
                    Collections.sort(mArrayList) { o1, o2 ->
                        o2.address_token.toInt().compareTo(o1.address_token.toInt())
                    }
                    mBaseAdapter.notifyDataSetChanged()
                } else {
                    onRequestFail(requestID, Exception())
                }
            } catch (e: Exception) {
                setEmptyView()
            }
        } else if (requestID == 0x1) { //设置默认地址
            val json = checkResultCode(result.toString())
            if (json != null) {
                if (json.code == SUCCESS_CODE) {
                    onPostRefresh()
                }else{
                    toast((json.obj as JSONObject).optString(MSG_FLAG))
                }
            }
        } else if (requestID == 0x2) { //删除当前地址
            val json = checkResultCode(result.toString())
            if (json != null) {
                if (json.code == SUCCESS_CODE) {
                    onPostRefresh()
                }
            }
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        currentItem = mArrayList[position]
        when (view!!.id) {
            R.id.mCIbAddressItemDefault -> {
                setDefaultAddress()
            }
            R.id.mTvAddressItemDel -> {
                deleteAddress()
            }
            R.id.mTvAddressItemEdit -> {
                val bundle = Bundle()
                bundle.putParcelable(AddNewAddressFragment.ADDRESS_BEAN_FLAG, mArrayList[position])
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "新增收货地址", true, false, bundle, AddNewAddressFragment::class.java)
            }
        }
    }

    /**
     * 设置默认地址
     */
    private fun setDefaultAddress() {
        if ("1" != currentItem!!.address_token) {
            for (item in mArrayList) {
                if ("1" == item.address_token) {
                    topItem = item
                    break
                }
            }
            if (topItem == null) {
                topItem = mArrayList[0]
            }
            mPresent.getDataByPost(0x1,
                    RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_DEFAULT_ADDRESS,
                    RequestParamsHelper.getDefaultAddressParams(topItem!!.address_id, currentItem!!.address_id))
        }
    }

    /**
     * 删除当前地址
     */
    private fun deleteAddress() {
        AlertDialog.Builder(mContext).setMessage("是否要删除当前地址？").setPositiveButton("删除") { _, _ ->
            mPresent.getDataByPost(0x2, RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_DEL_ADDRESS, RequestParamsHelper.getDelAddressParams(currentItem!!.address_id))
        }.setNegativeButton("取消", null).create().show()

    }

    override fun onDestroyView() {
        unsubscribe(addressSubscription)
        super.onDestroyView()
    }

}