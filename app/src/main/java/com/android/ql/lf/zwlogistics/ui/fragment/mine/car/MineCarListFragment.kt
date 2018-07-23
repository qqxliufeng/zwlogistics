package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.CarBean
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseRecyclerViewFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MinePersonAuthFragment
import com.android.ql.lf.zwlogistics.utils.GlideManager
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.android.ql.lf.zwlogistics.utils.RxBus
import com.android.ql.lf.zwlogistics.utils.alert
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_mine_car_list_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class MineCarListFragment : BaseRecyclerViewFragment<CarBean>() {

    companion object {
        const val ACTION_ADD = "添加"
        const val ACTION_MANAGER = "管理"
        const val ACTION_CANCEL = "取消"
        const val NO_CAR_NOTIFY = "暂无车辆"
    }

    private var menuItem: MenuItem? = null

    private val tempList by lazy {
        arrayListOf<CarBean>()
    }


    private val subscriptionCarAuthApply by lazy {
        RxBus.getDefault().toObservable(String::class.java).subscribe {
            onPostRefresh()
        }
    }

    override fun getLayoutId() = R.layout.fragment_mine_car_list_layout


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun initView(view: View?) {
        super.initView(view)
        subscriptionCarAuthApply
        setLoadEnable(false)
    }

    override fun createAdapter(): BaseQuickAdapter<CarBean, BaseViewHolder> =
            object : BaseQuickAdapter<CarBean, BaseViewHolder>(R.layout.adapter_mine_car_item_layout, mArrayList) {
                override fun convert(helper: BaseViewHolder?, item: CarBean?) {
                    val iv_delete = helper!!.getView<ImageView>(R.id.mIvCarItemDeleteMode)
                    if (item!!.isManagerMode) {
                        iv_delete.visibility = View.VISIBLE
                        if (item.isSelect) {
                            iv_delete.setImageResource(R.drawable.icon_car_manager_select)
                        } else {
                            iv_delete.setImageResource(R.drawable.icon_car_manager_unselect)
                        }
                    } else {
                        iv_delete.visibility = View.GONE
                    }
                    helper.setText(R.id.mTvMineCarListItemStatus, when (item.vehicle_is_state) {
                        1 -> {
                            "审核中"
                        }
                        2 -> {
                            "审核通过"
                        }
                        3 -> {
                            "审核失败"
                        }
                        else -> {
                            "${item.vehicle_is_state}"
                        }
                    })
                    helper.setText(R.id.mTvMineCarListItemName, "品牌：${item.vehicle_name}")
                    helper.setText(R.id.mTvMineCarListItemLength, "车长：${item.vehicle_length}")
                    helper.setText(R.id.mTvMineCarListItemType, "车型：${item.vehicle_type}")
                    GlideManager.loadImage(mContext, item.vehicle_number, helper.getView(R.id.mIvMineCarListItemPic))
                }
            }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val decoration = super.getItemDecoration() as DividerItemDecoration
        decoration.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.shape_car_decoration)!!)
        return decoration
    }


    override fun getEmptyLayoutId() = R.layout.layout_car_list_empty_layout

    override fun getEmptyMessage() = NO_CAR_NOTIFY

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.getCarListParam())
    }


    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在删除车辆……")
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        if (requestID == 0x1) {
            toast("删除车辆失败……")
        } else if (requestID == 0x0) {
            super.onRequestFail(requestID, e)
            menuItem?.title = ACTION_ADD
            mTvCarListAction.setOnClickListener {
                NewCarAuthFragment.startCarAuthFragment(mContext, MinePersonAuthFragment.NO_SHOW_JUMP)
            }
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        when (requestID) {
            0x0 -> {
                processList(result as String, CarBean::class.java)
                if (mArrayList.isEmpty()) {
                    menuItem?.title = ACTION_ADD
                } else {
                    menuItem?.title = ACTION_MANAGER
                }
                mTvCarListAction.text = "添加新车辆"
                mTvCarListAction.setOnClickListener {
                    NewCarAuthFragment.startCarAuthFragment(mContext, MinePersonAuthFragment.NO_SHOW_JUMP)
                }
            }
            0x1 -> {
                handleSuccess(requestID, result)
            }
        }
    }

    override fun onHandleSuccess(requestID: Int, obj: Any?) {
        super.onHandleSuccess(requestID, obj)
        if (requestID == 0x1) {
            toast("删除成功")
            onPostRefresh()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.car_menu, menu)
        menuItem = menu!!.getItem(0)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.mMenuManagerCar) {
            //如果没有车辆，进行添加
            if (mArrayList.isEmpty() && item.title == ACTION_ADD) {
                NewCarAuthFragment.startCarAuthFragment(mContext, MinePersonAuthFragment.NO_SHOW_JUMP)
                return super.onOptionsItemSelected(item)
            }
            //如果当前模式是管理，则显示取消，否则显示管理
            if (item.title == ACTION_MANAGER) {
                item.title = ACTION_CANCEL
                mTvCarListAction.text = "删除"
                mTvCarListAction.setOnClickListener {
                    if (tempList.isEmpty()) {
                        toast("请选择要删除的车辆")
                        return@setOnClickListener
                    }
                    alert("是否要删除所选车辆？") { _, _ ->
                        val stringBuilder = StringBuilder()
                        tempList.forEach {
                            stringBuilder.append(it.vehicle_id).append(",")
                        }
                        stringBuilder.deleteCharAt(stringBuilder.lastIndex)
                        mPresent.getDataByPost(0x1, RequestParamsHelper.getCarDeleteParam(stringBuilder.toString()))
                    }
                }
            } else {
                item.title = ACTION_MANAGER
                mTvCarListAction.text = "添加新车辆"
                mTvCarListAction.setOnClickListener {
                    NewCarAuthFragment.startCarAuthFragment(mContext, MinePersonAuthFragment.NO_SHOW_JUMP)
                }
            }
            mArrayList.forEach {
                it.isManagerMode = item.title != ACTION_MANAGER
            }
            mBaseAdapter.notifyDataSetChanged()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        val currentItem = mArrayList[position]
        if (currentItem.isManagerMode) {
            if (currentItem.isSelect) {
                tempList.remove(currentItem)
                currentItem.isSelect = false
            } else {
                tempList.add(currentItem)
                currentItem.isSelect = true
            }
            mBaseAdapter.notifyItemChanged(position)
        } else {
            when (currentItem.vehicle_is_state) {
                1, 2 -> {
                    FragmentContainerActivity
                            .from(mContext)
                            .setNeedNetWorking(true)
                            .setTitle("车辆信息")
                            .setExtraBundle(bundleOf(Pair("cid", currentItem.vehicle_id)))
                            .setClazz(MineCarInfoForComplementAndAuthingFragment::class.java)
                            .start()
                }
                3 -> {
                    FragmentContainerActivity
                            .from(mContext)
                            .setNeedNetWorking(true)
                            .setTitle("车辆信息")
                            .setExtraBundle(bundleOf(Pair("cid", currentItem.vehicle_id), Pair("content", currentItem.vehicle_content)))
                            .setClazz(MineCarInfoForFailedFragment::class.java)
                            .start()
                }
            }
        }
    }

    override fun onDestroyView() {
        unsubscribe(subscriptionCarAuthApply)
        super.onDestroyView()
    }
}