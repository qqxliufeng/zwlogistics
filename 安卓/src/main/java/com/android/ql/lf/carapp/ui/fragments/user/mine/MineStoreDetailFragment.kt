package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.RefreshData
import com.android.ql.lf.carapp.data.ShopInfoBean
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.*
import kotlinx.android.synthetic.main.fragment_mine_store_detail_layout.*
import org.json.JSONObject
import java.sql.Ref

/**
 * Created by lf on 18.4.13.
 * @author lf on 18.4.13
 */
class MineStoreDetailFragment : BaseNetWorkingFragment() {

    companion object {
        val SHOP_INFO_FLAG = "shop_info_flag"
    }

    private val shopInfo by lazy {
        arguments.classLoader = this.javaClass.classLoader
        arguments.getParcelable<ShopInfoBean>(SHOP_INFO_FLAG)
    }

    private var address: String? = null

    override fun getLayoutId() = R.layout.fragment_mine_store_detail_layout

    override fun initView(view: View?) {
        mEtStoreInfoDetailStoreName.setText(shopInfo.shop_name)
        mEtStoreInfoDetailPhone.setText(shopInfo.shop_phone)
        mEtStoreInfoAddress.text = shopInfo.shop_address
        address = shopInfo.shop_address
        mEtStoreInfoDetailAddress.setText(shopInfo.shop_d)
        mEtStoreInfoDetailIntroduce.setText(shopInfo.shop_content)
        mBtStoreInfoDetailSubmit.setOnClickListener {
            if (mEtStoreInfoDetailStoreName.isEmpty()) {
                toast(mEtStoreInfoDetailStoreName.hint.toString())
                return@setOnClickListener
            }
            if (mEtStoreInfoDetailPhone.isEmpty()) {
                toast(mEtStoreInfoDetailPhone.hint.toString())
                return@setOnClickListener
            }
            if (!mEtStoreInfoDetailPhone.isPhone()) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (address == null) {
                toast("请先选择地址")
                return@setOnClickListener
            }
            if (mEtStoreInfoDetailAddress.isEmpty()) {
                toast(mEtStoreInfoDetailAddress.hint.toString())
                return@setOnClickListener
            }
            if (mEtStoreInfoDetailIntroduce.isEmpty()) {
                toast(mEtStoreInfoDetailIntroduce.hint.toString())
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_EDIT_MY_SHOP_HEADER,
                    RequestParamsHelper.getEditMyShopHeaderParam(
                            shopInfo.shop_id,
                            mEtStoreInfoDetailStoreName.text.toString(),
                            mEtStoreInfoDetailPhone.text.toString(),
                            address!!,
                            mEtStoreInfoDetailAddress.text.toString(),
                            mEtStoreInfoDetailIntroduce.text.toString()))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在上传……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (check != null) {
            if (check.code == SUCCESS_CODE) {
                toast("上传成功")
                RefreshData.isRefresh = true
                RefreshData.any = MineStoreInfoFragment.REFRESH_STORE_INFO_FLAG
                RxBus.getDefault().post(RefreshData)
                finish()
            } else {
                toast((check.obj as JSONObject).optString(MSG_FLAG))
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("上传失败")
    }

}