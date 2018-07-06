package com.android.ql.lf.carapp.ui.fragments.mall.normal

import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ClassifyBean
import com.android.ql.lf.carapp.data.ClassifyItemEntity
import com.android.ql.lf.carapp.data.SearchParamBean
import com.android.ql.lf.carapp.data.lists.ListParseHelper
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.bundleOf
import org.json.JSONObject

/**
 * Created by lf on 18.3.20.
 * @author lf on 18.3.20
 */
class StoreClassifyFragment : BaseRecyclerViewFragment<ClassifyItemEntity>() {

    companion object {
        val SID_FLAG = "sid_flag"
    }

    override fun createAdapter(): BaseQuickAdapter<ClassifyItemEntity, BaseViewHolder> {
        return object : BaseSectionQuickAdapter<ClassifyItemEntity, BaseViewHolder>(
                R.layout.adapter_store_classify_content_layout,
                R.layout.adapter_store_classify_header_layout,
                mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: ClassifyItemEntity?) {
                helper!!.setText(R.id.mTvStoreClassifyContent, item!!.t.classify_title)
            }

            override fun convertHead(helper: BaseViewHolder?, item: ClassifyItemEntity?) {
                helper!!.setText(R.id.mTvStoreClassifyTitle, item!!.header)
            }
        }
    }

    override fun initView(view: View?) {
        super.initView(view)
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager = GridLayoutManager(mContext, 2)

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.PRODUCT_MODEL,
                RequestParamsHelper.ACT_PRODUCT_TYPE,
                RequestParamsHelper.getProductTypeParams(""))
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val decoration = super.getItemDecoration() as DividerItemDecoration
        decoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.recycler_view_height_divider2))
        return decoration
    }

    override fun onLoadMore() {
        super.onLoadMore()
        setLoadEnable(false)
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        try {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                val tempList = arrayListOf<ClassifyBean>()
                tempList.addAll(ListParseHelper<ClassifyBean>().fromJson((check.obj as JSONObject).toString(), ClassifyBean::class.java))
                if (!tempList.isEmpty()) {
                    tempList.forEach {
                        val contentEntity = ClassifyItemEntity(true, it.classify_title)
                        mArrayList.add(contentEntity)
                        it.sub.forEach {
                            val item = ClassifyItemEntity(it)
                            mArrayList.add(item)
                        }
                    }
                }
                mBaseAdapter.notifyDataSetChanged()
            } else {
                toast("加载失败")
            }
        } catch (e: Exception) {
            toast("加载失败")
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        val contentItem = mArrayList[position]
        if (!contentItem.isHeader) {
            val searchParam = SearchParamBean()
            val params = HashMap<String, String>()
            params.put("type_id", contentItem.t.classify_pid)
            params.put("stype_id", contentItem.t.classify_id)
            params.put("sid", arguments.getString(SID_FLAG))
            searchParam.params = params
            FragmentContainerActivity
                    .from(mContext)
                    .setNeedNetWorking(true)
                    .setClazz(SearchResultListFragment::class.java)
                    .setHiddenToolBar(true)
                    .setExtraBundle(bundleOf(Pair(SearchResultListFragment.SEARCH_PARAM_FLAG, searchParam)))
                    .start()
        }
    }
}