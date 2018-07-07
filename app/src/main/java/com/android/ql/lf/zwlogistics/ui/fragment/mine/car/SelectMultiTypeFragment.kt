package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.utils.getScreenSize
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.dialog_select_muilt_type_layout.*

class SelectMultiTypeFragment :BottomSheetDialogFragment(){

    private var screenHeight = 0


    private var bottomSheetBehavior:BottomSheetBehavior<View>? = null

    private val lengthList by lazy {
        arrayListOf<String>()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        lengthList.clear()
        (0 .. 20).forEach {
            lengthList.add("15.8米")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenHeight = context!!.getScreenSize().height
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)
        val contentView = View.inflate(context, R.layout.dialog_select_muilt_type_layout,null)
        val lengthContainer = contentView.findViewById<FlexboxLayout>(R.id.mFlSelectMultiTypeLengthContainer)
        lengthList.forEach {
            val itemView = View.inflate(context,R.layout.adapter_car_type_item_layout,null)
            itemView.findViewById<CheckedTextView>(R.id.mCtvCarTypeItemName).text = it
            lengthContainer.addView(itemView)
        }
        dialog.setContentView(contentView)
        bottomSheetBehavior = BottomSheetBehavior.from(contentView.parent as View)
        return dialog
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

}