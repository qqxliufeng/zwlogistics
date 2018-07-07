package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.FragmentManager
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.CarLengthBean
import com.android.ql.lf.zwlogistics.utils.getScreenSize
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.dialog_select_muilt_type_layout.*
import org.jetbrains.anko.collections.forEachWithIndex

class SelectMultiTypeFragment : BottomSheetDialogFragment() {

    private var screenHeight = 0


    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private val lengthList by lazy {
        arrayListOf<CarLengthBean>()
    }

    private val selectList by lazy {
        arrayListOf<String>()
    }


    private var listener: ((ArrayList<String>?) -> Unit)? = null
    private var dismissListener: (() -> Unit)? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        lengthList.clear()
        (0..20).forEach {
            val carLengthBean = CarLengthBean()
            if (it == 0) {
                carLengthBean.name = "不限车长"
                carLengthBean.type = 0
                carLengthBean.isSelect = true
                lengthList.add(carLengthBean)
            } else {
                carLengthBean.type = 1
                carLengthBean.name = "10.0米"
                lengthList.add(carLengthBean)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenHeight = context!!.getScreenSize().height
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val contentView = View.inflate(context, R.layout.dialog_select_muilt_type_layout, null)
        val lengthContainer = contentView.findViewById<FlexboxLayout>(R.id.mFlSelectMultiTypeLengthContainer)

        val clearView = contentView.findViewById<TextView>(R.id.mTvSelectMultiTypeClear)
        clearView.setOnClickListener {
            if (lengthList.size == 0) {
                return@setOnClickListener
            }

            if (lengthList[0].isSelect){
                return@setOnClickListener
            }

            lengthList.forEachWithIndex { index, item ->
                item.isSelect = index == 0
                (lengthContainer.getChildAt(index) as CheckedTextView).isChecked = item.isSelect
            }
        }

        contentView.findViewById<TextView>(R.id.mTvSelectMultiTypeSubmit).setOnClickListener {
            val tempLengthList = lengthList.filter { it.isSelect }
            if (tempLengthList.size == 1 && tempLengthList[0].type == 0){
                selectList.add("")
            }else{
                tempLengthList.forEach {
                    selectList.add(it.name)
                }
            }
            listener?.invoke(selectList)
            dismiss()
        }

        lengthList.forEachWithIndex { index, carLengthBean ->
            val itemView = View.inflate(context, R.layout.adapter_car_type_item_layout, lengthContainer) as FlexboxLayout
            val checkedTextView = itemView.getChildAt(index) as CheckedTextView
            checkedTextView.text = carLengthBean.name
            checkedTextView.isChecked = carLengthBean.isSelect
            checkedTextView.setOnClickListener {
                if (index == 0){ // 不限车长item
                    clearView.performClick()
                    return@setOnClickListener
                }
                lengthList[0].isSelect = false
                (lengthContainer.getChildAt(0) as CheckedTextView).isChecked = lengthList[0].isSelect
                (it as CheckedTextView).isChecked = !it.isChecked
                carLengthBean.isSelect = it.isChecked
                if (lengthList.none { it.isSelect }){
                    lengthList[index].isSelect = true
                    it.isChecked = lengthList[index].isSelect
                }
            }
        }
        View.inflate(context, R.layout.layout_select_car_length_custom_length_layout, lengthContainer)
        dialog.setContentView(contentView)
        bottomSheetBehavior = BottomSheetBehavior.from(contentView.parent as View)
        return dialog
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mClCarLengthContainer.layoutParams = CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight)
        bottomSheetBehavior?.peekHeight = screenHeight
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }


    fun myShow(manager: FragmentManager?, tag: String?, listener: (list: ArrayList<String>?) -> Unit, dismissListener: () -> Unit) {
        this.listener = listener
        this.dismissListener = dismissListener
        super.show(manager, tag)
    }


    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        this.dismissListener?.invoke()
    }

}