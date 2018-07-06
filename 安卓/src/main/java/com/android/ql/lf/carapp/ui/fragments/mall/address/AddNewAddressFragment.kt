package com.android.ql.lf.carapp.ui.fragments.mall.address

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.AddressBean
import com.android.ql.lf.carapp.data.RefreshData
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.activities.SelectAddressActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.*
import kotlinx.android.synthetic.main.fragment_add_new_address_layout.*
import org.json.JSONObject

/**
 * Created by lf on 2017/11/9 0009.
 * @author lf on 2017/11/9 0009
 */
class AddNewAddressFragment : BaseNetWorkingFragment() {

    companion object {
        val ADDRESS_BEAN_FLAG = "address_bean_flag"
    }

    private var addressInfo: String? = null

    private var addressBean: AddressBean? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId(): Int = R.layout.fragment_add_new_address_layout

    override fun initView(view: View?) {
        subscription = RxBus.getDefault().toObservable(SelectAddressActivity.SelectAddressItemBean::class.java).subscribe {
            addressInfo = it.name
            mEtAddNewAddressAddress.text = addressInfo!!
        }
        mEtAddNewAddressAddress.setOnClickListener {
            startActivity(Intent(mContext, SelectAddressActivity::class.java))
            (mContext as FragmentContainerActivity).overridePendingTransition(R.anim.activity_open, 0)
        }

        if (arguments != null) {
            arguments.classLoader = this.javaClass.classLoader
            addressBean = arguments.getParcelable(ADDRESS_BEAN_FLAG)
            mEtAddNewAddressName.setText(addressBean!!.address_name)
            mEtAddNewAddressPhone.setText(addressBean!!.address_phone)
            addressInfo = addressBean!!.address_addres
            mEtAddNewAddressAddress.text = addressBean!!.address_addres
            mEtAddNewAddressCode.setText(addressBean!!.address_postcode)
            mEtAddNewAddressAddressDetail.setText(addressBean!!.address_detail)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.save_new_address_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mMenuAddNewAddressSave) {
            if (mEtAddNewAddressName.isEmpty()) {
                toast("收货人姓名不能为空")
                return false
            }
            if (mEtAddNewAddressPhone.isEmpty()) {
                toast("收货人手机号不能为空")
                return false
            }
            if (!mEtAddNewAddressPhone.isPhone()) {
                toast("请输入合法的手机号吗")
                return false
            }
            if (addressInfo == null) {
                toast("请选择省市区")
                return false
            }
            if (TextUtils.isEmpty(mEtAddNewAddressCode.text.toString())) {
                toast("请输入邮政编码")
                return false
            }
            if (TextUtils.isEmpty(mEtAddNewAddressAddressDetail.text.toString())) {
                toast("请输入详细地址")
                return false
            }
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_ADD_ADDRESS,
                    RequestParamsHelper.getAddAddressListParams(
                            addressBean?.address_id ?: "",
                            name = mEtAddNewAddressName.getTextString(),
                            phone = mEtAddNewAddressPhone.getTextString(),
                            addressInfo = addressInfo!!,
                            code = mEtAddNewAddressCode.getTextString(),
                            detail = mEtAddNewAddressAddressDetail.getTextString()))
        }
        return true
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在提交……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (check != null) {
            toast((check.obj as JSONObject).optString("msg"))
            if (check.code == SUCCESS_CODE) {
                RefreshData.isRefresh = true
                RefreshData.any = "添加地址"
                RxBus.getDefault().post(RefreshData)
                finish()
            }
        }
    }
}