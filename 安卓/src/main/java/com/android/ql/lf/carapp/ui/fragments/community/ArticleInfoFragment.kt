package com.android.ql.lf.carapp.ui.fragments.community

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ArticleAnswerBean
import com.android.ql.lf.carapp.data.ArticleBean
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.adapter.ArticleCommentListAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.views.ImageContainerLinearLayout
import com.android.ql.lf.carapp.ui.views.PopupWindowDialog
import com.android.ql.lf.carapp.ui.views.PraiseView
import com.android.ql.lf.carapp.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_article_info_layout.*
import org.json.JSONObject

/**
 * Created by lf on 18.2.5.
 * @author lf on 18.2.5
 */
class ArticleInfoFragment : BaseRecyclerViewFragment<ArticleAnswerBean>() {

    companion object {
        val ARTICLE_BEAN_FLAG = "article_bean_flag"
    }

    private var articleBean: ArticleBean? = null

    private val topView by lazy {
        View.inflate(mContext, R.layout.layout_article_info_top_layout, null)
    }

    override fun getLayoutId() = R.layout.fragment_article_info_layout

    override fun createAdapter(): BaseQuickAdapter<ArticleAnswerBean, BaseViewHolder> =
            ArticleCommentListAdapter(R.layout.adapter_article_comment_item_layout, mArrayList)

    override fun initView(view: View?) {
        super.initView(view)
        arguments.classLoader = this.javaClass.classLoader
        articleBean = arguments.getParcelable(ARTICLE_BEAN_FLAG)
        mBaseAdapter.addHeaderView(topView)
        mBaseAdapter.setHeaderAndEmpty(true)
        mBottomPraiseView.setOnClickListener {
            mBottomPraiseView.toggle()
            mPresent.getDataByPost(0x4,
                    RequestParamsHelper.QAA_MODEL,
                    RequestParamsHelper.ACT_ARTICLE_PRAISE,
                    RequestParamsHelper.getArticlePraiseParam(articleBean!!.quiz_id))
        }
    }

    private fun setArticleInfo() {
        topView.findViewById<TextView>(R.id.mTvAnswerTopViewName).text = articleBean!!.member_name ?: "暂无"
        if (articleBean!!.member_pic != null) {
            GlideManager.loadFaceCircleImage(mContext, articleBean!!.member_pic, topView.findViewById(R.id.mIvAnswerTopViewFace))
        }
        mBottomPraiseView.setPraiseText(articleBean!!.quiz_click)
        topView.findViewById<TextView>(R.id.mTvAnswerTopViewTitle).text = articleBean!!.quiz_title
        topView.findViewById<TextView>(R.id.mTvAnswerTopViewTime).text = articleBean!!.quiz_time
        topView.findViewById<TextView>(R.id.mTvAnswerTopViewContent).text = articleBean!!.quiz_content
        topView.findViewById<TextView>(R.id.mTvAnswerTopViewSeeCount).text = articleBean!!.quiz_look
        topView.findViewById<TextView>(R.id.mTvAnswerTopViewCommentCount).text = articleBean!!.quiz_replies
        topView.findViewById<TextView>(R.id.mTvPraiseText).text = articleBean!!.quiz_click
        topView.findViewById<ImageContainerLinearLayout>(R.id.mLlAnswerTopViewPics).setImages(articleBean!!.quiz_pic)
        val praiseView = topView.findViewById<PraiseView>(R.id.mAnswerTopViewPraiseView)
        praiseView.setOnClickListener {
            praiseView.toggle()
            mPresent.getDataByPost(0x4,
                    RequestParamsHelper.QAA_MODEL,
                    RequestParamsHelper.ACT_ARTICLE_PRAISE,
                    RequestParamsHelper.getArticlePraiseParam(articleBean!!.quiz_id))
        }
        mTvAnswerInfoSeeCount.text = articleBean!!.quiz_look
        mTvAnswerInfoCommentCount.text = articleBean!!.quiz_replies
    }

    override fun getEmptyMessage() = "暂没有评价哦~~"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (mContext as FragmentContainerActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        mTvArticleInfoReply.setOnClickListener {
            showReplyDialog()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.QAA_MODEL,
                RequestParamsHelper.ACT_QUIZ_DETAIL,
                RequestParamsHelper.getQuizDetailParams(articleBean!!.quiz_id, currentPage))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x2) {
            getFastProgressDialog("正在提交……")
        } else if (requestID == 0x3) {
            getFastProgressDialog("正在删除……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            val check = checkResultCode(result)
            if (check != null) {
                if (SUCCESS_CODE == check.code) {
                    processList(result as String, ArticleAnswerBean::class.java)
                    val json = check.obj as JSONObject
                    articleBean = Gson().fromJson(json.optJSONObject("arr").toString(), ArticleBean::class.java)
                    setArticleInfo()
                    mPresent.getDataByPost(0x1, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_QUIZ_LOOK, RequestParamsHelper.getQuizLookParams(articleBean!!.quiz_id))
                } else if (check.code == "400") {
                    mTvArticleInfoDelete.visibility = View.VISIBLE
                    id_srl_base_recycler_view.visibility = View.GONE
                    mLlArticleInfoBottomContainer.visibility = View.GONE
                    RxBus.getDefault().post(WriteArticleFragment.ARTICLE_SEND_SUCCESS_FLAG)
                }
            }
        } else if (requestID == 0x2) {
            onPostRefresh()
        } else if (requestID == 0x3) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                toast("删除成功")
                onPostRefresh()
            } else {
                toast("删除失败")
            }
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemChildClick(adapter, view, position)
        when (view!!.id) {
            R.id.mTvAnswerInfoItemDelete -> {
                val builder = AlertDialog.Builder(mContext)
                builder.setMessage("是否要删除此回帖？")
                builder.setPositiveButton("删除") { _, _ ->
                    mPresent.getDataByPost(0x3, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_DEL_QAA, RequestParamsHelper.getDelQaaParam(aid = mArrayList[position].answer_id))
                }
                builder.setNegativeButton("否", null)
                builder.create().show()
            }
            R.id.mPraiseView -> {
                (view as PraiseView).toggle()
                mPresent.getDataByPost(0x4,
                        RequestParamsHelper.QAA_MODEL,
                        RequestParamsHelper.ACT_PRAISE,
                        RequestParamsHelper.getPraiseParams(mArrayList[position].answer_id))
            }
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.QAA_MODEL,
                RequestParamsHelper.ACT_QUIZ_DETAIL,
                RequestParamsHelper.getQuizDetailParams(articleBean!!.quiz_id, currentPage))
    }

    private fun showReplyDialog() {
        if (articleBean != null) {
            val contentView = LayoutInflater.from(context).inflate(R.layout.layout_repay_layout, null)
            val popupWindow = PopupWindowDialog.showReplyDialog(mContext, contentView)
            val et_content = contentView.findViewById<EditText>(R.id.mEtReplyContent)
            et_content.hint = "写评论"
            contentView.findViewById<Button>(R.id.mBtReplaySend).setOnClickListener {
                if (et_content.isEmpty()) {
                    toast("请输入评论内容")
                    return@setOnClickListener
                }
                popupWindow.dismiss()
                mPresent.getDataByPost(0x2, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_ADD_ANSWER,
                        RequestParamsHelper.getAddAnswerParams(articleBean!!.quiz_id, et_content.getTextString()))
            }
        }
    }
}