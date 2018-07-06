package com.android.ql.lf.carapp.ui.fragments.mall.normal

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.StoreInfoBean
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.views.DividerGridItemDecoration
import com.android.ql.lf.carapp.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_my_store_info_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject

/**
 * Created by lf on 18.3.20.
 * @author lf on 18.3.20
 */
class MyStoreInfoFragment : BaseRecyclerViewFragment<MyStoreInfoFragment.MyGoodsBean>() {

    companion object {
        val STORE_ID_FLAG = "store_id_flag"
    }

    private var collectStatus = 0

    private var sort = ""
    private var sortFlag = true
    private var saleFlag = true

    private var keyword = ""

    private val storeInfoBean by lazy {
        arguments.classLoader = this@MyStoreInfoFragment.javaClass.classLoader
        arguments.getParcelable<StoreInfoBean>(STORE_ID_FLAG)
    }
    override fun createAdapter(): BaseQuickAdapter<MyGoodsBean, BaseViewHolder> = object : BaseQuickAdapter<MyGoodsBean,BaseViewHolder>(R.layout.adapter_main_mall_item_layout, mArrayList) {

        override fun convert(helper: BaseViewHolder?, item: MyGoodsBean?) {
            val imageView = helper!!.getView<ImageView>(R.id.mTvGoodsInfoItemPic)
            GlideManager.loadImage(mContext, item!!.merchant_product_main_pic, imageView)
            helper.setText(R.id.mTvGoodsInfoItemName, item.merchant_product_name)
            helper.setText(R.id.mTvGoodsInfoItemPrice, "￥${item.merchant_product_price}")
            val imageCollect = helper.getView<ImageView>(R.id.mIvGoodsInfoItemCollection)
            imageCollect.visibility = View.GONE
        }
    }

    override fun getLayoutId() = R.layout.fragment_my_store_info_layout

    override fun initView(view: View?) {
        super.initView(view)
        val statusHeight = (mContext as FragmentContainerActivity).statusHeight
        mAblStoreInfoContainer.setPadding(0, statusHeight, 0, 0)
        mAblStoreInfoContainer.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            mTlStoreInfoContainer.alpha = 1 - Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
        }
        mIvSearchGoodsBack.setOnClickListener {
            finish()
        }
        if (storeInfoBean.wholesale_shop_pic != null && !storeInfoBean.wholesale_shop_pic.isEmpty()) {
            GlideManager.loadImage(mContext, storeInfoBean.wholesale_shop_pic[0], mIvStoreInfoPic)
        }
        mTvStoreInfoName.text = storeInfoBean.wholesale_shop_name
        mTvStoreInfoFansCount.text = storeInfoBean.wholesale_shop_attention
        mTvStoreInfoTopProductClassify.setOnClickListener {
           mContext.hiddenKeyBoard(mEtSearchContent.windowToken)
                keyword = if (mEtSearchContent.isEmpty()) {
                    ""
                } else {
                    mEtSearchContent.getTextString()
                }
                onPostRefresh()
        }
        mEtSearchContent.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mContext.hiddenKeyBoard(mEtSearchContent.windowToken)
                keyword = if (mEtSearchContent.isEmpty()) {
                    ""
                } else {
                    mEtSearchContent.getTextString()
                }
                onPostRefresh()
            }
            false
        }
        mRbStoreInfoSort1.isChecked = true
        mRbStoreInfoSort1.setOnClickListener {
            if (sort == "") {
                return@setOnClickListener
            }
            sort = ""
            onPostRefresh()
        }
        mRbStoreInfoSort2.setOnClickListener {
            sort = if (saleFlag) {
                "sv1"
            } else {
                "sv2"
            }
            saleFlag = !saleFlag
            onPostRefresh()
        }
        mRbStoreInfoSort3.setOnClickListener {
            sort = if (sortFlag) {
                mRbStoreInfoSort3.text = "价格从高到低"
                "p1"
            } else {
                mRbStoreInfoSort3.text = "价格从低到高"
                "p2"
            }
            sortFlag = !sortFlag
            onPostRefresh()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_M_PRODUCT_SEARCH,
                RequestParamsHelper.getWithPageParams(currentPage)
                        .addParam("sid", storeInfoBean!!.wholesale_shop_id)
                        .addParam("sort", sort)
                        .addParam("keyword", keyword))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_M_PRODUCT_SEARCH,
                RequestParamsHelper.getWithPageParams(currentPage)
                        .addParam("sid", storeInfoBean!!.wholesale_shop_id)
                        .addParam("sort", sort)
                        .addParam("keyword", keyword))
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        val manager = GridLayoutManager(mContext, 2)
        mManager = manager
        return manager
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return DividerGridItemDecoration(mContext)
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when (requestID) {
            0x0 -> {
                processList(result as String, MyGoodsBean::class.java)
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    collectStatus = (check.obj as JSONObject).optInt("arr")
                }
            }
        }
    }

    class MyGoodsBean {
        var merchant_product_id:String? = null
        var merchant_product_pic:String? = null
        var merchant_product_price:String? = null
        var merchant_product_time:String? = null
        var merchant_product_sv:String? = null
        var merchant_product_name:String? = null
        var merchant_product_main_pic:String? = null
    }


}