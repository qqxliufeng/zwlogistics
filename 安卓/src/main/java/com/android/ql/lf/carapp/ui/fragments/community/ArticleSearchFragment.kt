package com.android.ql.lf.carapp.ui.fragments.community

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ArticleBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.user.LoginFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.getTextString
import com.android.ql.lf.carapp.utils.isEmpty
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_article_search_layout.*
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 18.2.13.
 * @author lf on 18.2.13
 */
class ArticleSearchFragment : BaseRecyclerViewFragment<ArticleBean>() {

    companion object {
        val ARTICLE_DETAIL_FLAG_FOR_SEARCH = "article_detail_flag_for_search"
    }

    private var currentArticle: ArticleBean? = null

    override fun getLayoutId() = R.layout.fragment_article_search_layout

    override fun createAdapter(): BaseQuickAdapter<ArticleBean, BaseViewHolder> = ArticleSearchAdapter(android.R.layout.simple_list_item_1, mArrayList)

    override fun initView(view: View?) {
        mTlArticleSearch.setPadding(0, (mContext as FragmentContainerActivity).statusHeight, 0, 0)
        (mContext as FragmentContainerActivity).setSupportActionBar(mTlArticleSearch)
        (mContext as FragmentContainerActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mTlArticleSearch.setNavigationOnClickListener {
            finish()
        }
        isFirstRefresh = false
        super.initView(view)
        registerLoginSuccessBus()
        setRefreshEnable(false)
        mTvSearchSubmit.setOnClickListener {
            if (mEtSearchContent.isEmpty()) {
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_QUIZ_SEARCH, RequestParamsHelper.getQuizSearchParam(mEtSearchContent.getTextString()))
            val inputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(mEtSearchContent.windowToken, 0)
        }
        mEtSearchContent.postDelayed({
            val inputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(mEtSearchContent, 0)
        }, 100)
    }

    override fun getEmptyMessage() = "没有相关帖子"

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        mSwipeRefreshLayout.post {
            mSwipeRefreshLayout.isRefreshing = true
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        processList(result as String, ArticleBean::class.java)
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        currentArticle = mArrayList[position]
        if (UserInfo.getInstance().isLogin) {
            interArticleInfo()
        } else {
            UserInfo.loginToken = ARTICLE_DETAIL_FLAG_FOR_SEARCH
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
        when(UserInfo.loginToken){
            ARTICLE_DETAIL_FLAG_FOR_SEARCH -> {
                interArticleInfo()
            }
        }
    }

    class ArticleSearchAdapter(resId: Int, list: ArrayList<ArticleBean>) : BaseQuickAdapter<ArticleBean, BaseViewHolder>(resId, list) {
        override fun convert(helper: BaseViewHolder?, item: ArticleBean?) {
            helper!!.setText(android.R.id.text1, item!!.quiz_title)
        }
    }
}