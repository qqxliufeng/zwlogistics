package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.RefreshData
import com.android.ql.lf.carapp.data.ShopInfoBean
import com.android.ql.lf.carapp.data.StoreInfoBean
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.ui.fragments.mall.normal.MyStoreInfoFragment
import com.android.ql.lf.carapp.ui.fragments.mall.normal.StoreInfoFragment
import com.android.ql.lf.carapp.utils.GlideManager
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.RxBus
import com.android.ql.lf.carapp.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_mine_store_info_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject

/**
 * Created by lf on 18.2.2.
 * @author lf on 18.2.2
 */
class MineStoreInfoFragment : BaseNetWorkingFragment() {

    companion object {
        val REFRESH_STORE_INFO_FLAG = "refresh_store_info_flag"
    }

    private var shopInfoBean: ShopInfoBean? = null

    private val mArrayList = arrayListOf<MineStoreIndexInfoBean>()

    private val headerView by lazy {
        View.inflate(mContext, R.layout.layout_mine_store_info_top_layout, null)
    }

    private val editStoreInfoSubscription by lazy {
        RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (it.isRefresh && it.any == REFRESH_STORE_INFO_FLAG) {
                mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_SHOP_HEADER, RequestParamsHelper.getMyShopHeaderParam())
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_mine_store_info_layout

    override fun initView(view: View?) {
        editStoreInfoSubscription
        mArrayList.add(MineStoreIndexInfoBean(0, R.drawable.icon_mine_store_info_index, "店铺首页"))
        mArrayList.add(MineStoreIndexInfoBean(1, R.drawable.icon_mine_store_info_info, "店铺信息"))
//        mArrayList.add(MineStoreIndexInfoBean(2, R.drawable.icon_mine_store_info_upload, "商品上传"))
        val gridLayoutManager = GridLayoutManager(mContext, 2)
        mRvMineStoreInfoContainer.layoutManager = gridLayoutManager
        val adapter: BaseQuickAdapter<MineStoreIndexInfoBean, BaseViewHolder> = object : BaseQuickAdapter<MineStoreIndexInfoBean, BaseViewHolder>(R.layout.adapter_mine_store_info_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: MineStoreIndexInfoBean?) {
                val textView = helper!!.getView<TextView>(R.id.mTvMineStoreInfoItem)
                textView.text = item!!.name
                textView.setCompoundDrawablesWithIntrinsicBounds(0, item!!.resId, 0, 0)
            }
        }
        mRvMineStoreInfoContainer.adapter = adapter
        adapter.addHeaderView(headerView)
        mRvMineStoreInfoContainer.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                when (mArrayList[position].index) {
                    0 -> {
                        if (shopInfoBean != null) {
                            val storeInfo = StoreInfoBean()
                            storeInfo.wholesale_shop_name = shopInfoBean!!.shop_name
                            storeInfo.wholesale_shop_num = shopInfoBean!!.shop_num
                            storeInfo.wholesale_shop_id = shopInfoBean!!.shop_id
                            storeInfo.wholesale_shop_pic = arrayListOf(shopInfoBean!!.shop_mpic)
                            storeInfo.wholesale_shop_attention = shopInfoBean!!.shop_attention
                            FragmentContainerActivity.from(mContext)
                                    .setTitle("店铺首页")
                                    .setClazz(MyStoreInfoFragment::class.java)
                                    .setNeedNetWorking(true)
                                    .setHiddenToolBar(true)
                                    .setExtraBundle(bundleOf(Pair(MyStoreInfoFragment.STORE_ID_FLAG, storeInfo)))
                                    .start()
                        }
                    }
                    1 ->
                        if (shopInfoBean != null) {
                            FragmentContainerActivity.from(mContext)
                                    .setTitle("店铺信息")
                                    .setNeedNetWorking(true)
                                    .setClazz(MineStoreDetailFragment::class.java)
                                    .setExtraBundle(bundleOf(Pair(MineStoreDetailFragment.SHOP_INFO_FLAG, shopInfoBean!!)))
                                    .start()
                        }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_SHOP_HEADER, RequestParamsHelper.getMyShopHeaderParam())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在加载信息……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (check != null) {
            if (check.code == SUCCESS_CODE) {
                shopInfoBean = Gson().fromJson((check.obj as JSONObject).optJSONObject("result").toString(), ShopInfoBean::class.java)
                GlideManager.loadImage(mContext, shopInfoBean!!.shop_mpic, headerView.findViewById<ImageView>(R.id.mIvMineStoreHeaderInfoPic))
                headerView.findViewById<TextView>(R.id.mTvMineStoreHeaderInfoTitle).text = shopInfoBean!!.shop_name
            } else {
                toast((check.obj as JSONObject).optString(MSG_FLAG))
            }
        }
    }

    override fun onDestroyView() {
        unsubscribe(editStoreInfoSubscription)
        super.onDestroyView()
    }

    class MineStoreIndexInfoBean(var index: Int, var resId: Int, var name: String)

}