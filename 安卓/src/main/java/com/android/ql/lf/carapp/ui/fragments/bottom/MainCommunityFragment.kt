package com.android.ql.lf.carapp.ui.fragments.bottom

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.*
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.activities.MainActivity
import com.android.ql.lf.carapp.ui.adapter.ArticleListAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.DetailContentFragment
import com.android.ql.lf.carapp.ui.fragments.community.ArticleInfoFragment
import com.android.ql.lf.carapp.ui.fragments.community.ArticleListFragment
import com.android.ql.lf.carapp.ui.fragments.community.ArticleSearchFragment
import com.android.ql.lf.carapp.ui.fragments.community.WriteArticleFragment
import com.android.ql.lf.carapp.ui.fragments.user.LoginFragment
import com.android.ql.lf.carapp.ui.fragments.user.mine.MineArticleFragment
import com.android.ql.lf.carapp.utils.*
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.gson.Gson
import com.sunfusheng.marqueeview.MarqueeView
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.fragment_main_community_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject

/**
 * Created by lf on 18.1.24.
 * @author lf on 18.1.24
 */
class MainCommunityFragment : BaseRecyclerViewFragment<ArticleBean>() {

    companion object {
        val COMMUNITY_SEND_ARTICLE_FLAG = "community_send_article_flag"
        val COMMUNITY_MY_ARTICLE_FLAG = "community_my_article_flag"
        val ARTICLE_DETAIL_FLAG = "article_detail_flag"

        fun newInstance(): MainCommunityFragment {
            return MainCommunityFragment()
        }
    }

    private val topList = arrayListOf<CommunityTagBean>()
    private lateinit var topMarqueeView: MarqueeView
    private var mBannerMainCommunity: Banner? = null
    private var currentArticle: ArticleBean? = null
    private val topRecycleViewAdapter by lazy {
        TopRecyclerViewAdapter(R.layout.layout_main_community_top_item_layout, topList,(mContext.getScreenSize().first -
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40.0f,mContext.resources.displayMetrics).toInt())/4)
    }

    private val articleSubscription by lazy {
        RxBus.getDefault().toObservable(String::class.java).subscribe {
            if (it == WriteArticleFragment.ARTICLE_SEND_SUCCESS_FLAG) {
                onPostRefresh()
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_main_community_layout

    override fun initView(view: View?) {
        super.initView(view)
        val height = (mContext as MainActivity).statusHeight
        val param = mRlCommunityTitleContainer.layoutParams as ViewGroup.MarginLayoutParams
        param.topMargin = height
        mRlCommunityTitleContainer.layoutParams = param
        registerLoginSuccessBus()
        articleSubscription
        mFabWriteNote.doClickWithUserStatusStart(COMMUNITY_SEND_ARTICLE_FLAG) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "发布帖子", WriteArticleFragment::class.java)
        }
        mIvMainCommunityMine.doClickWithUserStatusStart(COMMUNITY_MY_ARTICLE_FLAG) {
            FragmentContainerActivity.from(mContext).setClazz(MineArticleFragment::class.java).setTitle("我的帖子").start()
        }
        mIvMainCommunitySearch.setOnClickListener {
            FragmentContainerActivity.from(mContext).setClazz(ArticleSearchFragment::class.java).setNeedNetWorking(true).setHiddenToolBar(true).start()
        }
        val topView = View.inflate(mContext, R.layout.layout_main_community_top_layout, null)
        val topRecyclerView = topView.findViewById<RecyclerView>(R.id.mRvMainCommunityTopContainer)
        mBannerMainCommunity = topView.findViewById(R.id.mBannerMainCommunity)
        topMarqueeView = topView.findViewById(R.id.mMvMainCommunityTopContainer)
        topRecyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        topRecyclerView.adapter = topRecycleViewAdapter
        topRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                FragmentContainerActivity.from(mContext)
                        .setClazz(ArticleListFragment::class.java)
                        .setTitle(topList[position].tag_title)
                        .setNeedNetWorking(true)
                        .setExtraBundle(bundleOf(Pair("type", topList[position].tag_title)))
                        .start()
            }
        })
        topView.findViewById<TextView>(R.id.mTvMainCommunityTopMoreArticle).setOnClickListener {
            FragmentContainerActivity.from(mContext)
                    .setClazz(ArticleListFragment::class.java)
                    .setTitle("全部")
                    .setNeedNetWorking(true)
                    .setExtraBundle(bundleOf(Pair("type", "")))
                    .start()
        }
        mBannerMainCommunity!!.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                imageView!!.scaleType = ImageView.ScaleType.FIT_XY
                GlideManager.loadImage(mContext, (path as BannerImageBean).lunbo_pic, imageView)
            }
        })
        mBaseAdapter.addHeaderView(topView)
        mBaseAdapter.setHeaderAndEmpty(true)
    }

    override fun createAdapter(): BaseQuickAdapter<ArticleBean, BaseViewHolder> =
            ArticleListAdapter(R.layout.adapter_article_item_layout, mArrayList)

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_QUIZ, RequestParamsHelper.getQuizParam(1, currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (requestID == 0x0) {
            if (check != null) {
                if (SUCCESS_CODE == check.code) {
                    val communityContainerBean = Gson().fromJson(result.toString(), CommunityContainerBean::class.java)
                    val firstNewsList = ArrayList<String>()
                    communityContainerBean.arr1.forEach {
                        firstNewsList.add(it.quiz_title)
                    }
                    topList.clear()
                    topList.addAll(communityContainerBean.arr)
                    topRecycleViewAdapter.notifyDataSetChanged()
                    mBannerMainCommunity!!.setImages(communityContainerBean.arr2)
                            .setDelayTime(3000)
                            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                            .start()
                    mBannerMainCommunity?.setOnBannerListener {
                        FragmentContainerActivity.from(mContext)
                                .setTitle("详情")
                                .setNeedNetWorking(true)
                                .setClazz(DetailContentFragment::class.java)
                                .setExtraBundle(bundleOf(
                                        Pair(DetailContentFragment.MODEL_NAME_FLAG, RequestParamsHelper.QAA_MODEL),
                                        Pair(DetailContentFragment.ACT_NAME_FLAG, RequestParamsHelper.ACT_COMMUNITY_LUNBO_DETAIL),
                                        Pair(DetailContentFragment.PARAM_FLAG, mapOf(Pair("lid", communityContainerBean.arr2[it].lunbo_id)))
                                ))
                                .start()
                    }
                    topMarqueeView.startWithList(firstNewsList)
                    topMarqueeView.setOnItemClickListener { position, textView ->
                        val firstBean = communityContainerBean.arr1[position]
                        val articleBean = ArticleBean()
                        articleBean.quiz_title = firstBean.quiz_title
                        articleBean.quiz_id = firstBean.quiz_id
                        articleBean.quiz_token = firstBean.quiz_token
                        FragmentContainerActivity
                                .from(mContext)
                                .setTitle("详情")
                                .setClazz(ArticleInfoFragment::class.java)
                                .setNeedNetWorking(true)
                                .setExtraBundle(bundleOf(Pair(ArticleInfoFragment.ARTICLE_BEAN_FLAG, articleBean)))
                                .start()
                    }
                    processList(check.obj as JSONObject, ArticleBean::class.java)
                }
            }
        } else {
            if (check != null) {
                processList(check.obj as JSONObject, ArticleBean::class.java)
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        toast("加载失败……")
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x1, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_QUIZ, RequestParamsHelper.getQuizParam(1, currentPage))
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider))
        return itemDecoration
    }

    override fun onStart() {
        super.onStart()
        mBannerMainCommunity?.startAutoPlay()
        topMarqueeView.startFlipping()
    }

    override fun onStop() {
        super.onStop()
        mBannerMainCommunity?.stopAutoPlay()
        topMarqueeView.stopFlipping()
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        currentArticle = mArrayList[position]
        if (UserInfo.getInstance().isLogin) {
            interArticleInfo()
        } else {
            UserInfo.loginToken = ARTICLE_DETAIL_FLAG
            LoginFragment.startLogin(mContext)
        }
    }

    private fun interArticleInfo() {
        FragmentContainerActivity
                .from(mContext)
                .setTitle("详情")
                .setClazz(ArticleInfoFragment::class.java)
                .setNeedNetWorking(true)
                .setExtraBundle(bundleOf(Pair(ArticleInfoFragment.ARTICLE_BEAN_FLAG, currentArticle!!)))
                .start()
    }

    override fun onLoginSuccess(userInfo: UserInfo?) {
        super.onLoginSuccess(userInfo)
        when (UserInfo.loginToken) {
            COMMUNITY_SEND_ARTICLE_FLAG -> {
                mFabWriteNote.doClickWithUseStatusEnd()
            }
            COMMUNITY_MY_ARTICLE_FLAG -> {
                mIvMainCommunityMine.doClickWithUseStatusEnd()
            }
            ARTICLE_DETAIL_FLAG -> {
                interArticleInfo()
            }
        }
    }

    override fun onDestroyView() {
        mBannerMainCommunity?.releaseBanner()
        unsubscribe(articleSubscription)
        super.onDestroyView()
    }

    class TopRecyclerViewAdapter(layoutId: Int, list: ArrayList<CommunityTagBean>,var width:Int) : BaseQuickAdapter<CommunityTagBean, BaseViewHolder>(layoutId, list) {

        override fun convert(helper: BaseViewHolder?, item: CommunityTagBean?) {
            val imageView = helper!!.getView<ImageView>(R.id.mIvMainCommunityTopItemImage)
            val layoutParams = imageView.layoutParams
            layoutParams.width = this.width
            layoutParams.height = this.width / 5 * 4
            imageView.layoutParams = layoutParams
            Glide.with(mContext)
                    .load(Constants.BASE_IP + item!!.tag_pic)
                    .bitmapTransform(RoundedCornersTransformation(mContext, 8, 0))
                    .into(imageView)
            helper.setText(R.id.mTvMainCommunityTopItemTitle, item.tag_title)
        }
    }

    class CommunityContainerBean {
        lateinit var result: ArrayList<ArticleBean>

        lateinit var arr: ArrayList<CommunityTagBean>

        lateinit var arr1: ArrayList<FirstNewsBean>

        lateinit var arr2: ArrayList<BannerImageBean>
    }

}