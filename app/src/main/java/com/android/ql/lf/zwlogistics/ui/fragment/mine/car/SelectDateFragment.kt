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
import com.android.ql.lf.zwlogistics.utils.RxBus
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.android.synthetic.main.dialog_select_date_layout.*

class SelectDateFragment : BottomSheetDialogFragment() {


    private val dataList by lazy {
        arrayListOf<String>()
    }


    private var listener:((date: String?)->Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_select_date_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        dataList.clear()
        (0..14).forEach {
            var tempDate = "${it + 4}"
            if (tempDate.length == 1) {
                tempDate = "0$tempDate"
            }
            dataList.add("20${tempDate}年")
        }
        mTvSelectDateCancel.setOnClickListener {
            dismiss()
        }
        mRvSelectDate.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRvSelectDate.adapter = object : BaseQuickAdapter<String, BaseViewHolder>(android.R.layout.simple_list_item_1, dataList) {
            override fun convert(helper: BaseViewHolder?, item: String?) {
                val tv_name = helper!!.getView<TextView>(android.R.id.text1)
                tv_name.gravity = Gravity.CENTER
                tv_name.setTextColor(ContextCompat.getColor(mContext, R.color.normalTextColor))
                helper.setText(android.R.id.text1, item!!)
            }
        }
        mRvSelectDate.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                listener?.invoke(dataList[position])
                dismiss()
            }
        })
    }


    fun myShow(manager: FragmentManager?, tag: String?,listener:(date: String?)->Unit) {
        this.listener = listener
        super.show(manager, tag)
    }


}