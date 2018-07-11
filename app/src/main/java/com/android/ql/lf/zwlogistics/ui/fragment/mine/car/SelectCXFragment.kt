package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.CarParamBean
import com.android.ql.lf.zwlogistics.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.dialog_select_cx_layout.*
import org.jetbrains.anko.collections.forEachWithIndex

class SelectCXFragment : BottomSheetDialogFragment() {


    private var dataList: ArrayList<CarParamBean>? = null

    private var listener: ((carType: CarParamBean) -> Unit)? = null

    private var currentSelectBean: CarParamBean? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_select_cx_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mRvSelectCX.layoutManager = GridLayoutManager(context, 4)
        mRvSelectCX.adapter = object : BaseQuickAdapter<CarParamBean, BaseViewHolder>(R.layout.adapter_cx_select_item_layout, dataList) {
            override fun convert(helper: BaseViewHolder?, item: CarParamBean?) {
                helper!!.setText(R.id.mTvSelectCXItem, item!!.name)
                helper.setChecked(R.id.mTvSelectCXItem, item.isSelect)
            }
        }
        mRvSelectCX.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                currentSelectBean = dataList!![position]
                currentSelectBean?.isSelect = true
                listener?.invoke(currentSelectBean!!)
                dismiss()
            }
        })
    }


    fun setDataSource(list: ArrayList<CarParamBean>) {
        dataList = list
    }

    fun myShow(manager: FragmentManager?, tag: String?, listener: ((carType: CarParamBean) -> Unit)?) {
        this.listener = listener
        super.show(manager, tag)
    }
}