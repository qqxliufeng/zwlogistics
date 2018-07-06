package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.GoodsBean
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.mall.normal.NewGoodsInfoFragment
import com.android.ql.lf.carapp.utils.GlideManager
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf

/**
 * Created by liufeng on 2018/1/30.
 */
class MineGoodsCollectionFragment : BaseRecyclerViewFragment<GoodsBean>() {

    override fun createAdapter(): BaseQuickAdapter<GoodsBean, BaseViewHolder> =
            MineGoodsCollectionAdapter(R.layout.adapter_mine_goods_collection_item_layout, mArrayList)

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_SHOP_COLLECT, RequestParamsHelper.getMyShopCollectParams(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_SHOP_COLLECT, RequestParamsHelper.getMyShopCollectParams(currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(result as String, GoodsBean::class.java)
    }

    override fun getEmptyMessage() = "暂无收藏商品哦~~~"

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity.from(mContext)
                .setNeedNetWorking(true)
                .setTitle("商品详情")
                .setExtraBundle(bundleOf(Pair(NewGoodsInfoFragment.GOODS_ID_FLAG, mArrayList[position].product_id)))
                .setClazz(NewGoodsInfoFragment::class.java)
                .start()
    }

    class MineGoodsCollectionAdapter(layout: Int, list: ArrayList<GoodsBean>) : BaseQuickAdapter<GoodsBean, BaseViewHolder>(layout, list) {
        override fun convert(helper: BaseViewHolder?, item: GoodsBean?) {
            GlideManager.loadImage(mContext, item!!.product_pic[0], helper!!.getView(R.id.mIvMallCollectionGoodsPic))
            helper.setText(R.id.mTvMallCollectionGoodsName, item.product_name)
            helper.setText(R.id.mTvMallCollectionGoodsPrice, "￥${item.product_price}")
        }
    }

}