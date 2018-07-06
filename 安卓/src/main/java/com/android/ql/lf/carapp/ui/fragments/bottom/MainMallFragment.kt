package com.android.ql.lf.carapp.ui.fragments.bottom

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.*
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.activities.MainActivity
import com.android.ql.lf.carapp.ui.adapter.GoodsMallItemAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.DetailContentFragment
import com.android.ql.lf.carapp.ui.fragments.mall.normal.*
import com.android.ql.lf.carapp.ui.fragments.mall.shoppingcar.ShoppingCarFragment
import com.android.ql.lf.carapp.ui.fragments.user.LoginFragment
import com.android.ql.lf.carapp.ui.views.DividerGridItemDecoration
import com.android.ql.lf.carapp.ui.views.HotView
import com.android.ql.lf.carapp.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.gson.Gson
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.activity_fragment_container_layout.*
import kotlinx.android.synthetic.main.fragment_main_mall_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.collections.forEachWithIndex
import org.json.JSONObject

/**
 * Created by lf on 18.1.24.
 * @author lf on 18.1.24
 */
class MainMallFragment : BaseRecyclerViewFragment<GoodsBean>() {

    companion object {

        val MAIN_MALL_COLLECTION_FLAG = "main_mall_collection_flag"
        val MAIN_MALL_SHOPPING_CAR_FLAG = "main_mall_shopping_car_flag"
        val MAIN_MALL_MY_SHOPPING_CAR_FLAG = "main_mall_my_shopping_car_flag"
        val MAIN_MALL_ENTER_GOODS_INFO_FLAG = "main_mall_enter_goods_info_flag"

        val REFRESH_COLLECTION_STATUS_FLAG = "refresh_collection_status_flag"


        fun newInstance(): MainMallFragment {
            return MainMallFragment()
        }
    }

    private var productContainer: ProductContainerBean? = null

    private val topView by lazy { View.inflate(mContext, R.layout.layout_main_mall_top_header_layout, null) }

    private val hotViewContainer by lazy {
        arrayListOf<HotView>(
                topView.findViewById(R.id.mHvMainMall1),
                topView.findViewById(R.id.mHvMainMall2),
                topView.findViewById(R.id.mHvMainMall3),
                topView.findViewById(R.id.mHvMainMall4)
        )
    }

    private val bannerView by lazy {
        topView.findViewById<Banner>(R.id.mBannerMainMall)
    }

    private val classifyView by lazy {
        topView.findViewById<RecyclerView>(R.id.mRvMainMallClassify)
    }

    private val mClassifyList by lazy {
        arrayListOf<ClassifyBean>()
    }

    private val classifyAdapter by lazy {
        object : BaseQuickAdapter<ClassifyBean, BaseViewHolder>(R.layout.adapter_main_mall_classify_item_layout, mClassifyList) {
            override fun convert(helper: BaseViewHolder?, item: ClassifyBean?) {
                val icon = helper!!.getView<ImageView>(R.id.mIvMainMallClassifyItemIcon)
                if (item!!.imageRes == 0) {
                    GlideManager.loadCircleImage(mContext, item.classify_pic, icon)
                } else {
                    icon.setImageResource(item.imageRes)
                }
                helper.setText(R.id.mTvMainMallClassifyItemName, item.classify_title)
            }
        }
    }

    private var tempGoodsBean: GoodsBean? = null

    private val collectionSubscription by lazy {
        RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (it.isRefresh && it.any == REFRESH_COLLECTION_STATUS_FLAG) {
                refreshCollectionStatus()
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_main_mall_layout

    override fun createAdapter(): BaseQuickAdapter<GoodsBean, BaseViewHolder> = GoodsMallItemAdapter(R.layout.adapter_main_mall_item_layout, mArrayList)

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT, RequestParamsHelper.getProductParams(currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.PRODUCT_MODEL, RequestParamsHelper.ACT_PRODUCT, RequestParamsHelper.getProductParams(currentPage))
    }

    override fun initView(view: View?) {
        super.initView(view)
        registerLoginSuccessBus()
        collectionSubscription
        val height = (mContext as MainActivity).statusHeight
        val param = mTvMainMallTitle.layoutParams as ViewGroup.MarginLayoutParams
        param.topMargin = height
        mTvMainMallTitle.layoutParams = param
        classifyView.layoutManager = GridLayoutManager(mContext, 4)
        classifyView.adapter = classifyAdapter
        classifyView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val classifyItem = mClassifyList[position]
                if (classifyItem.imageRes == 0) {
                    val searchParam = SearchParamBean()
                    val params = HashMap<String, String>()
                    params.put("type_id", classifyItem.classify_id)
                    params.put("stype_id", "")
                    searchParam.params = params
                    FragmentContainerActivity
                            .from(mContext)
                            .setNeedNetWorking(true)
                            .setClazz(SearchResultListFragment::class.java)
                            .setHiddenToolBar(true)
                            .setExtraBundle(bundleOf(Pair(SearchResultListFragment.SEARCH_PARAM_FLAG, searchParam)))
                            .start()
                } else {
                    FragmentContainerActivity
                            .from(mContext)
                            .setNeedNetWorking(true)
                            .setClazz(GoodsClassifyFragment::class.java)
                            .setTitle("商品分类")
                            .start()
                }
            }
        })
        mLlMainMallSearchContainer.setOnClickListener {
            val searchParam = SearchParamBean()
            val params = HashMap<String, String>()
            searchParam.params = params
            FragmentContainerActivity
                    .from(mContext)
                    .setNeedNetWorking(true)
                    .setClazz(SearchResultListFragment::class.java)
                    .setHiddenToolBar(true)
                    .setExtraBundle(bundleOf(Pair(SearchResultListFragment.SEARCH_PARAM_FLAG, searchParam)))
                    .start()
        }
        mFabShoppingCar.setImageResource(R.drawable.img_icon_shoppingcart_white_null)
        mFabShoppingCar.doClickWithUserStatusStart(MAIN_MALL_MY_SHOPPING_CAR_FLAG) {
            FragmentContainerActivity.from(mContext).setClazz(ShoppingCarFragment::class.java).setTitle("购物车").setNeedNetWorking(true).start()
        }
        bannerView!!.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                imageView!!.scaleType = ImageView.ScaleType.FIT_XY
                GlideManager.loadImage(mContext, (path as BannerImageBean).lunbo_pic, imageView)
            }
        })
        mBaseAdapter.addHeaderView(topView)
        mBaseAdapter.setHeaderAndEmpty(true)
    }

    override fun onStart() {
        super.onStart()
        bannerView.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        bannerView.startAutoPlay()
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        when (requestID) {
            0x1 -> getFastProgressDialog("正在收藏……")
            0x2 -> getFastProgressDialog("正在搬家……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when (requestID) {
            0x0 -> {
                val check = checkResultCode(result)
                if (check != null) {
                    if (check.code == SUCCESS_CODE) {
                        processList(check.obj as JSONObject, GoodsBean::class.java)
                        if (currentPage == 0) {
                            productContainer = Gson().fromJson(check.obj.toString(), ProductContainerBean::class.java)
                            mClassifyList.clear()
                            mClassifyList.addAll(productContainer!!.arr)
                            val lastClassifyItem = ClassifyBean()
                            lastClassifyItem.imageRes = R.drawable.img_icon_main_mall_icon8
                            lastClassifyItem.classify_title = "更多"
                            mClassifyList.add(lastClassifyItem)
                            classifyView.isFocusableInTouchMode = false
                            classifyAdapter.notifyDataSetChanged()
                            bannerView!!.setImages(productContainer!!.arr2).setDelayTime(3000).setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setOnBannerListener {
                                val bannerImageBean = productContainer!!.arr2[it]
                                if (!TextUtils.isEmpty(bannerImageBean.lunbo_isdiscount) && TextUtils.isDigitsOnly(bannerImageBean.lunbo_isdiscount) && bannerImageBean.lunbo_isdiscount.toInt() > 0) {
                                    FragmentContainerActivity.from(mContext).setTitle("优惠券").setNeedNetWorking(true).setClazz(PlatformCouponFragment::class.java).start()
                                } else {
                                    FragmentContainerActivity.from(mContext)
                                            .setTitle(if(TextUtils.isEmpty(bannerImageBean.lunbo_title))"详情" else bannerImageBean.lunbo_title)
                                            .setNeedNetWorking(true)
                                            .setClazz(DetailContentFragment::class.java)
                                            .setExtraBundle(bundleOf(
                                                    Pair(DetailContentFragment.MODEL_NAME_FLAG, RequestParamsHelper.QAA_MODEL),
                                                    Pair(DetailContentFragment.ACT_NAME_FLAG, RequestParamsHelper.ACT_COMMUNITY_LUNBO_DETAIL),
                                                    Pair(DetailContentFragment.PARAM_FLAG, mapOf(Pair("lid", bannerImageBean.lunbo_id)))
                                            ))
                                            .start()
                                }
                            }.start()
                            productContainer!!.arr1.forEachWithIndex { index, item ->
                                hotViewContainer[index].bindData(item.faddish_title, item.faddish_description, item.faddish_pic) {
                                    val searchParam = SearchParamBean()
                                    val params = HashMap<String, String>()
                                    params.put("type", item.faddish_id!!)
                                    searchParam.params = params
                                    FragmentContainerActivity
                                            .from(mContext)
                                            .setClazz(SearchResultListFragment::class.java)
                                            .setHiddenToolBar(true)
                                            .setNeedNetWorking(true)
                                            .setExtraBundle(bundleOf(Pair(SearchResultListFragment.SEARCH_PARAM_FLAG, searchParam)))
                                            .start()
                                }
                            }
                            if (!TextUtils.isEmpty(productContainer!!.arr3)) {
                                if (productContainer!!.arr3!!.toInt() > 0) {
                                    mFabShoppingCar.setImageResource(R.drawable.img_icon_shoppingcart_white_full)
                                } else {
                                    mFabShoppingCar.setImageResource(R.drawable.img_icon_shoppingcart_white_null)
                                }
                            } else {
                                mFabShoppingCar.setImageResource(R.drawable.img_icon_shoppingcart_white_null)
                            }
                        }
                    }
                }
            }
            0x1 -> {
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    toast((check.obj as JSONObject).optString(MSG_FLAG))
                    refreshCollectionStatus()
                }
            }
        }
    }

    private fun refreshCollectionStatus() {
        if (tempGoodsBean != null) {
            if (tempGoodsBean!!.product_collect == "0") {
                tempGoodsBean!!.product_collect = "1"
            } else {
                tempGoodsBean!!.product_collect = "0"
            }
            mBaseAdapter.notifyItemChanged(mArrayList.indexOf(tempGoodsBean) + 1)
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        val manager = GridLayoutManager(mContext, 2)
        mManager = manager
        return manager
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        return DividerGridItemDecoration(mContext)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        tempGoodsBean = mArrayList[position]
        if (UserInfo.getInstance().isLogin) {
            enterGoodsInfo(tempGoodsBean!!)
        } else {
            UserInfo.loginToken = MAIN_MALL_ENTER_GOODS_INFO_FLAG
            LoginFragment.startLogin(mContext)
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemChildClick(adapter, view, position)
        tempGoodsBean = mArrayList[position]
        when (view!!.id) {
            R.id.mIvGoodsInfoItemCollection -> {
                if (UserInfo.getInstance().isLogin) {
                    //收藏
                    collectionGoods(tempGoodsBean!!)
                } else {
                    UserInfo.loginToken = MAIN_MALL_COLLECTION_FLAG
                    LoginFragment.startLogin(mContext)
                }
            }
        }
    }

    /**
     * 添加 收藏
     */
    private fun collectionGoods(goodsBean: GoodsBean) {
        mPresent.getDataByPost(0x1,
                RequestParamsHelper.PRODUCT_MODEL,
                RequestParamsHelper.ACT_COLLECT_PRODUCT,
                RequestParamsHelper.getCollectProductParam(goodsBean.product_id))
    }

    /**
     * 进入商品详情
     */
    private fun enterGoodsInfo(goodsBean: GoodsBean) {
        FragmentContainerActivity.from(mContext).setTitle("详情").setExtraBundle(bundleOf(Pair(NewGoodsInfoFragment.GOODS_ID_FLAG, goodsBean.product_id))).setClazz(NewGoodsInfoFragment::class.java).setNeedNetWorking(true).start()
    }

    override fun onLoginSuccess(userInfo: UserInfo?) {
        super.onLoginSuccess(userInfo)
        if (tempGoodsBean!=null) {
            when (UserInfo.loginToken) {
                MAIN_MALL_COLLECTION_FLAG -> {
                    //收藏
                    collectionGoods(tempGoodsBean!!)
                    UserInfo.resetLoginSuccessDoActionToken()
                }
                MAIN_MALL_ENTER_GOODS_INFO_FLAG -> {
                    //进入商品详情
                    enterGoodsInfo(tempGoodsBean!!)
                    UserInfo.resetLoginSuccessDoActionToken()
                }
                MAIN_MALL_MY_SHOPPING_CAR_FLAG -> {
                    mFabShoppingCar.doClickWithUseStatusEnd()
                }
                else -> {
                    if (mArrayList.isEmpty()) {
                        onPostRefresh()
                    }
                }
            }
        }
//        UserInfo.resetLoginSuccessDoActionToken()
    }

    override fun onDestroyView() {
        bannerView.releaseBanner()
        unsubscribe(collectionSubscription)
        super.onDestroyView()
    }

    class ProductContainerBean {
        lateinit var result: ArrayList<GoodsBean>
        lateinit var arr: ArrayList<ClassifyBean>
        lateinit var arr1: ArrayList<HotGoodsBean>
        lateinit var arr2: ArrayList<BannerImageBean>
        var arr3: String? = null
    }

    class HotGoodsBean {
        var faddish_id: String? = null
        var faddish_title: String? = null
        var faddish_description: String? = null
        var faddish_pic: String? = null
        var faddish_time: String? = null
    }

}