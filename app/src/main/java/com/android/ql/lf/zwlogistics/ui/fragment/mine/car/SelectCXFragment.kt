package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.dialog_select_cx_layout.*
import org.jetbrains.anko.collections.forEachWithIndex

class SelectCXFragment : BottomSheetDialogFragment() {


    private var dataList :ArrayList<CarTypeBean>? = null

    private var listener:((carType:CarTypeBean)->Unit)? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_select_cx_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mRvSelectCX.layoutManager = GridLayoutManager(context, 4)
        mRvSelectCX.adapter = object : BaseQuickAdapter<CarTypeBean, BaseViewHolder>(R.layout.adapter_cx_select_item_layout, dataList) {
            override fun convert(helper: BaseViewHolder?, item: CarTypeBean?) {
                helper!!.setText(R.id.mTvSelectCXItem, item!!.name)
                helper.setChecked(R.id.mTvSelectCXItem,item.isSelect)
            }
        }
        mRvSelectCX.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                listener?.invoke(dataList!![position])
                dismiss()
            }
        })
    }


    fun getCurrentPosition(position:Int = 0){
        dataList?.forEachWithIndex{ index,carType->
            carType.isSelect = index == position
        }
    }

    fun setDataSource(list:ArrayList<CarTypeBean>){
        dataList = list
    }

    fun myShow(manager: FragmentManager?, tag: String?,listener:((carType:CarTypeBean)->Unit)?) {
        this.listener = listener
        super.show(manager, tag)
    }


    class CarTypeBean(var id: String?, var name: String,var isSelect:Boolean = false)

}