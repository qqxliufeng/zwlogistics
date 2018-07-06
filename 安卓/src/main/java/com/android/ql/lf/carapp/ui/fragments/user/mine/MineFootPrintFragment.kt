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
 * Created by lf on 18.2.3.
 * @author lf on 18.2.3
 */
class MineFootPrintFragment : BaseRecyclerViewFragment<GoodsBean>() {

    override fun createAdapter(): BaseQuickAdapter<GoodsBean, BaseViewHolder> {
        return object : BaseQuickAdapter<GoodsBean, BaseViewHolder>(R.layout.adapter_mine_foot_print_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: GoodsBean?) {
                if (item!!.product_pic != null && !item.product_pic.isEmpty()) {
                    GlideManager.loadImage(mContext, item.product_pic[0], helper!!.getView(R.id.mIvMallFootGoodsPic))
                }
                helper!!.setText(R.id.mTvMallFootGoodsName, item.product_name)
                helper.setText(R.id.mTvMallFootGoodsPrice, "￥${item.product_price}")
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_SPOOR, RequestParamsHelper.getMySpoorParmas(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_MY_SPOOR, RequestParamsHelper.getMySpoorParmas(currentPage))
    }

    override fun getEmptyMessage() = "暂无足迹~~~"

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(result as String, GoodsBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        FragmentContainerActivity.from(mContext)
                .setNeedNetWorking(true)
                .setTitle("商品详情")
                .setExtraBundle(bundleOf(Pair(NewGoodsInfoFragment.GOODS_ID_FLAG, mArrayList[position].product_id)))
                .setClazz(NewGoodsInfoFragment::class.java)
                .start()
    }

//    class MineFootPrintAdapter(resId: Int, headerResId: Int, list: ArrayList<MyFootPrintBean>) : BaseSectionQuickAdapter<MyFootPrintBean, BaseViewHolder>(resId, headerResId, list) {
//
//        override fun convert(helper: BaseViewHolder?, item: MyFootPrintBean?) {
//        }
//
//        override fun convertHead(helper: BaseViewHolder?, item: MyFootPrintBean?) {
//            helper!!.setText(android.R.id.text1, item!!.header)
//        }
//    }
//
//    class MyFootPrintBean : SectionEntity<String> {
//        constructor(isHeader: Boolean, header: String?) : super(isHeader, header)
//        constructor(t: String?) : super(t)
//    }
}