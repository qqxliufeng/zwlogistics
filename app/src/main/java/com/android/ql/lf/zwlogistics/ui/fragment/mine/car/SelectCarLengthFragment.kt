package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.utils.getScreenSize
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.dialog_select_car_length_layout.*

class SelectCarLengthFragment :BottomSheetDialogFragment(){

    private var screenHeight = 0

    private val arrayList:ArrayList<String> = arrayListOf()

    private var bottomSheetBehavior:BottomSheetBehavior<View>? = null

    private var listener:((data:String)->Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenHeight = context!!.getScreenSize().height
        arrayList.clear()
        (0 .. 5).forEach {
            arrayList.add("item")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomDialog = super.onCreateDialog(savedInstanceState)
        val contentView = View.inflate(context,R.layout.dialog_select_car_length_layout,null)
        val toolbar = contentView.findViewById<Toolbar>(R.id.mTlSelectCarLength)
        toolbar.title = "车长"
        toolbar.setNavigationIcon(R.drawable.icon_back)
        toolbar.setNavigationOnClickListener {
            dismiss()
        }
        bottomDialog.setContentView(contentView)
        val mRvSelectLength = contentView.findViewById<RecyclerView>(R.id.mRvSelectLength)
        val gridLayoutManager = GridLayoutManager(context,4)
        mRvSelectLength.layoutManager = gridLayoutManager
        val adapter= object : BaseQuickAdapter<String,BaseViewHolder>(R.layout.adapter_car_type_item_layout,arrayList) {
            override fun convert(helper: BaseViewHolder?, item: String?) {
            }
        }
        mRvSelectLength.adapter = adapter
        val footView = View.inflate(context,R.layout.layout_select_car_length_custom_length_layout,null)
        adapter.addFooterView(footView)
        bottomSheetBehavior = BottomSheetBehavior.from(contentView.parent as View)
        return bottomDialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mClCarLengthContainer.layoutParams = CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,screenHeight)
        bottomSheetBehavior?.peekHeight = screenHeight
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun myShow(manager: FragmentManager?, tag: String?,listener:(data:String)->Unit) {
        this.listener = listener
        super.show(manager, tag)
    }

}