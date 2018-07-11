package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.CarParamBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.dialog_select_date_layout.*

class SelectDateFragment : BottomSheetDialogFragment() {


    private var dataList: ArrayList<CarParamBean>? = null


    private var listener: ((date: CarParamBean?) -> Unit)? = null

    private var currentSelectBean: CarParamBean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_select_date_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mTvSelectDateCancel.setOnClickListener {
            dismiss()
        }
        mRvSelectDate.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRvSelectDate.adapter = object : BaseQuickAdapter<CarParamBean, BaseViewHolder>(android.R.layout.simple_list_item_1, dataList) {
            override fun convert(helper: BaseViewHolder?, item: CarParamBean?) {
                val tv_name = helper!!.getView<TextView>(android.R.id.text1)
                tv_name.gravity = Gravity.CENTER
                tv_name.setTextColor(ContextCompat.getColor(mContext, R.color.normalTextColor))
                helper.setText(android.R.id.text1, item?.name)
            }
        }
        mRvSelectDate.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                currentSelectBean = dataList?.get(position)
                listener?.invoke(dataList?.get(position))
                dismiss()
            }
        })
    }


    fun setDataSource(list: ArrayList<CarParamBean>) {
        dataList = list
    }

    fun myShow(manager: FragmentManager?, tag: String?, listener: (date: CarParamBean?) -> Unit) {
        this.listener = listener
        super.show(manager, tag)
    }


}