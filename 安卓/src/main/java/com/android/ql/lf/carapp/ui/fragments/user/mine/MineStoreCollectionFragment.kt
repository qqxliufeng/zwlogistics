package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.StoreInfoBean
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.mall.normal.StoreInfoFragment
import com.android.ql.lf.carapp.utils.GlideManager
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

/**
 * Created by liufeng on 2018/1/30.
 */
class MineStoreCollectionFragment : BaseRecyclerViewFragment<StoreInfoBean>() {

    override fun createAdapter(): BaseQuickAdapter<StoreInfoBean, BaseViewHolder> =
            MineStoreCollectionAdapter(R.layout.adapter_mine_store_collection_item_layout, mArrayList)

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_CONCERM_SHOP, RequestParamsHelper.getMyConcermShopParams(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_CONCERM_SHOP, RequestParamsHelper.getMyConcermShopParams(currentPage))
    }

    override fun getEmptyMessage() = "暂无收藏店铺哦~~~"

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(result as String, StoreInfoBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity
                .from(mContext)
                .setNeedNetWorking(true)
                .setHiddenToolBar(true)
                .setClazz(StoreInfoFragment::class.java)
                .setExtraBundle(bundleOf(Pair(StoreInfoFragment.STORE_ID_FLAG, mArrayList[position])))
                .start()
    }

    class MineStoreCollectionAdapter(layout: Int, list: ArrayList<StoreInfoBean>)
        : BaseQuickAdapter<StoreInfoBean, BaseViewHolder>(layout, list) {
        override fun convert(helper: BaseViewHolder?, item: StoreInfoBean?) {
            if (item!!.wholesale_shop_pic != null && !item.wholesale_shop_pic.isEmpty()) {
                GlideManager.loadImage(mContext, item.wholesale_shop_pic[0], helper!!.getView(R.id.mIvMallCollectionStorePic))
            }
            helper!!.setText(R.id.mTvMallCollectionStoreName, item.wholesale_shop_name)
        }
    }

}