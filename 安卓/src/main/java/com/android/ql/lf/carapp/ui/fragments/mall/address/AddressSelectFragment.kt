package com.android.ql.lf.carapp.ui.fragments.mall.address

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.AddressBean
import com.android.ql.lf.carapp.data.RefreshData
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.adapter.AddressSelectListItemAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class AddressSelectFragment : BaseRecyclerViewFragment<AddressBean>() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun createAdapter(): BaseQuickAdapter<AddressBean, BaseViewHolder> =
            AddressSelectListItemAdapter(R.layout.adapter_select_list_item_layout, mArrayList)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.add_new_address_menu, menu)
    }

    override fun initView(view: View?) {
        super.initView(view)
        subscription = RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (RefreshData.isRefresh && RefreshData.any is String && "添加地址" == RefreshData.any) {
                onPostRefresh()
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.Companion.MEMBER_MODEL,
                RequestParamsHelper.Companion.ACT_ADDRESS_LIST,
                RequestParamsHelper.Companion.getAddressListParams())
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mMenuAddNewAddress) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "新增地址", true, false, AddNewAddressFragment::class.java)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(result as String, AddressBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        RxBus.getDefault().post(mArrayList[position])
        finish()
    }
}