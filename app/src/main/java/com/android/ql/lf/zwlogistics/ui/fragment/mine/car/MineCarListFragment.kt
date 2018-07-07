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
import com.android.ql.lf.zwlogistics.utils.alert
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_mine_car_list_layout.*
import org.jetbrains.anko.support.v4.alert

class MineCarListFragment :BaseRecyclerViewFragment<CarBean>(){

    companion object {
        const val ACTION_ADD = "添加"
        const val ACTION_MANAGER = "管理"
        const val ACTION_CANCEL = "取消"
        const val NO_CAR_NOTIFY = "暂无车辆"
    }

    private var menuItem:MenuItem? = null

    private val tempList by lazy {
        arrayListOf<CarBean>()
    }

    override fun getLayoutId() = R.layout.fragment_mine_car_list_layout


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun createAdapter(): BaseQuickAdapter<CarBean, BaseViewHolder>  =
            object : BaseQuickAdapter<CarBean,BaseViewHolder>(R.layout.adapter_mine_car_item_layout,mArrayList) {
        override fun convert(helper: BaseViewHolder?, item: CarBean?) {
            val iv_delete = helper!!.getView<ImageView>(R.id.mIvCarItemDeleteMode)
            if (item!!.isManagerMode){
                iv_delete.visibility = View.VISIBLE
                if (item.isSelect){
                    iv_delete.setImageResource(R.drawable.icon_car_manager_select)
                }else{
                    iv_delete.setImageResource(R.drawable.icon_car_manager_unselect)
                }
            }else{
                iv_delete.visibility = View.GONE
            }
        }
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val decoration = super.getItemDecoration() as DividerItemDecoration
        decoration.setDrawable(ContextCompat.getDrawable(mContext,R.drawable.shape_car_decoration)!!)
        return decoration
    }


    override fun getEmptyLayoutId() = R.layout.layout_car_list_empty_layout

    override fun getEmptyMessage() = NO_CAR_NOTIFY

    override fun onRefresh() {
//        setEmptyView()
//        setRefreshEnable(false)
//        menuItem?.title = ACTION_ADD
        setRefreshEnable(false)
        setLoadEnable(false)
        (0 .. 10).forEach {
            mArrayList.add(CarBean())
        }
        mBaseAdapter.setNewData(mArrayList)
        onRequestEnd(-1)
        mBaseAdapter.loadMoreEnd()
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.car_menu,menu)
        menuItem = menu!!.getItem(0)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.mMenuManagerCar){
            //如果没有车辆，进行添加
            if (mArrayList.isEmpty() && item.title == ACTION_ADD){
                FragmentContainerActivity.from(mContext).setClazz(NewCarAuthFragment::class.java).setTitle("新车认证").setNeedNetWorking(true).start()
                return super.onOptionsItemSelected(item)
            }
            //如果当前模式是管理，则显示取消，否则显示管理
            if (item.title == ACTION_MANAGER){
                item.title = ACTION_CANCEL
                mTvCarListAction.text = "删除"
                mTvCarListAction.setOnClickListener {
                    if (tempList.isEmpty()){
                        return@setOnClickListener
                    }
                    alert("是否要删除所选车辆？") { _, _ ->
                        tempList.forEach {
                            mArrayList.remove(it)
                        }
                        mBaseAdapter.notifyDataSetChanged()
                    }
                }
            }else{
                item.title = ACTION_MANAGER
                mTvCarListAction.text = "添加新车辆"
                mTvCarListAction.setOnClickListener {
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
        if (currentItem.isManagerMode){
            if (currentItem.isSelect){
                tempList.remove(currentItem)
                currentItem.isSelect = false
            }else{
                tempList.add(currentItem)
                currentItem.isSelect = true
            }
            mBaseAdapter.notifyItemChanged(position)
        }
    }
}