package com.android.ql.lf.carapp.ui.fragments.community

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.View
import android.widget.CheckedTextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ImageBean
import com.android.ql.lf.carapp.data.ProtocolBean
import com.android.ql.lf.carapp.present.CommunityPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.adapter.OrderImageUpLoadAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.ui.fragments.WebViewContentFragment
import com.android.ql.lf.carapp.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.android.flexbox.FlexboxLayout
import com.google.gson.Gson
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_write_article_layout.*
import okhttp3.MultipartBody
import org.jetbrains.anko.bundleOf
import org.json.JSONObject
import java.util.*

/**
 * Created by lf on 18.2.6.
 * @author lf on 18.2.6
 */
class WriteArticleFragment : BaseNetWorkingFragment() {

    companion object {
        val ARTICLE_SEND_SUCCESS_FLAG = "article send success"
    }

    private val images = arrayListOf<ImageBean>()
    private var currentTag: String? = null
    private val tags by lazy {
        arrayListOf<ArticleTagBean>()
    }

    private val mBaseAdapter: BaseQuickAdapter<ImageBean, BaseViewHolder> by lazy {
        OrderImageUpLoadAdapter(R.layout.adapter_write_article_image_up_load_item_layout, images)
    }
    private val communityPresent by lazy {
        CommunityPresent()
    }

    override fun getLayoutId() = R.layout.fragment_write_article_layout

    override fun initView(view: View?) {
        images.add(ImageBean(R.drawable.img_icon_add_photo, null))
        val linearLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvWriteArticleImages.layoutManager = linearLayoutManager
        mRvWriteArticleImages.adapter = mBaseAdapter
        mRvWriteArticleImages.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (mBaseAdapter.itemCount - 1 >= 3) {
                    toast("最多只能选择三张图片")
                    return
                }
                val item = mBaseAdapter.getItem(position)
                if (item != null) {
                    if (item.resId != null) {
                        openImageChoose(MimeType.ofImage(), 3 - (mBaseAdapter.itemCount - 1))
                    }
                }
            }
        })
        mCbWriteArticleProtocol.isChecked = true
        mBtWriteArticleSubmit.setOnClickListener {
            if (mEtWriteArticleTitle.isEmpty()) {
                toast("请输入帖子标题")
                return@setOnClickListener
            }
            if (mEtWriteArticleContent.isEmpty()) {
                toast("请输入帖子内容")
                return@setOnClickListener
            }
            if (currentTag == null) {
                toast("至少选择一个标签")
                return@setOnClickListener
            }
            if (!mCbWriteArticleProtocol.isChecked) {
                toast("请先确认同意${mTvWriteArticleProtocol.text}")
                return@setOnClickListener
            }
            //images size == 1 说明没有选择任何图片
            if (images.size == 1) {
                mPresent.getDataByPost(0x1,
                        RequestParamsHelper.QAA_MODEL,
                        RequestParamsHelper.ACT_ADD_QUIZ,
                        RequestParamsHelper.getAddQuizParam(mEtWriteArticleTitle.getTextString(), mEtWriteArticleContent.getTextString(), currentTag!!))
            } else {
                communityPresent.onUploadArticle(
                        images.filter { it.uriPath != null } as ArrayList<ImageBean>,
                        150,
                        object : ImageUploadHelper.OnImageUploadListener {
                            override fun onActionStart() {
                                getFastProgressDialog("正在发布……")
                            }

                            override fun onActionEnd(builder: MultipartBody.Builder) {
                                builder.addFormDataPart("title", mEtWriteArticleTitle.getTextString())
                                builder.addFormDataPart("content", mEtWriteArticleContent.getTextString())
                                builder.addFormDataPart("type", currentTag!!)
                                mPresent.uploadFile(0x2, RequestParamsHelper.QAA_MODEL,
                                        RequestParamsHelper.ACT_ADD_QUIZ, builder.build().parts())
                            }
                            override fun onActionFailed() {
                                toast("发布失败……")
                                onRequestEnd(-1)
                            }
                        })
            }
        }
        mTvWriteArticleProtocol.setOnClickListener {
            mPresent.getDataByPost(0x3, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PTGG, RequestParamsHelper.getPtggParam("2"))
        }
        mPresent.getDataByPost(0x0, RequestParamsHelper.QAA_MODEL, RequestParamsHelper.ACT_TAG, RequestParamsHelper.getTagParam())
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            getFastProgressDialog("正在获取标签……")
        } else if (requestID == 0x1) {
            getFastProgressDialog("正在发布……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when(requestID){
            0x0->{
                val check = checkResultCode(result)
                if (check != null && SUCCESS_CODE == check.code) {
                    val jsonArray = (check.obj as JSONObject).optJSONArray("result")
                    (0 until jsonArray.length()).forEach {
                        tags.add(Gson().fromJson(jsonArray.optJSONObject(it).toString(), ArticleTagBean::class.java))
                        val textView = CheckedTextView(mContext)
                        val layoutParams = FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT)
                        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, resources.displayMetrics).toInt()
                        layoutParams.setMargins(margin, margin, margin, margin)
                        textView.setBackgroundResource(R.drawable.selector_ctv_bg2)
                        textView.setTextColor(resources.getColorStateList(R.color.select_ctv_1))
                        textView.layoutParams = layoutParams
                        textView.setOnClickListener { view ->
                            currentTag = textView.text.toString()
                            (0 until mFlWriteArticleTag.childCount)
                                    .map { mFlWriteArticleTag.getChildAt(it) as CheckedTextView }
                                    .filter { it != view }
                                    .forEach { it.isChecked = false }
                            (view as CheckedTextView).isChecked = !view.isChecked
                        }
                        textView.text = tags[it].tag_title
                        mFlWriteArticleTag.addView(textView)
                    }
                    mBtWriteArticleSubmit.isEnabled = true
                }
            }
            0x1,0x2->{
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    toast("发布成功")
                    RxBus.getDefault().post(ARTICLE_SEND_SUCCESS_FLAG)
                    finish()
                } else {
                    toast("发布失败")
                }
            }
            0x3->{
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    val protocolBean = Gson().fromJson((check.obj as JSONObject).optJSONObject("result").toString(), ProtocolBean::class.java)
                    FragmentContainerActivity.from(mContext)
                            .setNeedNetWorking(true)
                            .setTitle(protocolBean.ptgg_title)
                            .setExtraBundle(bundleOf(Pair(WebViewContentFragment.PATH_FLAG, protocolBean.ptgg_content)))
                            .setClazz(WebViewContentFragment::class.java)
                            .start()
                } else {
                    toast("获取服务协议失败")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            val uris = Matisse.obtainPathResult(data)
            uris.forEach {
                images.add(0, ImageBean(null, it))
            }
            mBaseAdapter.notifyDataSetChanged()
        }
    }

    class ArticleTagBean {
        var tag_id: String? = null
        var tag_title: String? = null
        var tag_content: String? = null
        var tag_pid: String? = null
        var tag_path: String? = null
        var tag_token: String? = null
        var tag_url: String? = null
        var tag_pic: String? = null
        var tag_sort: String? = null
        var tag_url1: String? = null
        var tag_time: String? = null
        var tag_sign: String? = null
    }

}