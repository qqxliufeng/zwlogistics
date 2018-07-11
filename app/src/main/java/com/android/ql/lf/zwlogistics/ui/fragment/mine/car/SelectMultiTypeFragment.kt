package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.widget.CheckedTextView
import android.widget.TextView
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.CarParamBean
import com.android.ql.lf.zwlogistics.utils.getScreenSize
import com.google.android.flexbox.FlexboxLayout
import org.jetbrains.anko.collections.forEachWithIndex

class SelectMultiTypeFragment : BottomSheetDialogFragment() {

    private var screenHeight = 0


    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private var lengthList:ArrayList<CarParamBean>? = null

    private var carTypeList:ArrayList<CarParamBean>? = null

    private val selectLengthList by lazy {
        arrayListOf<String>()
    }

    private val selectCarTypeList by lazy {
        arrayListOf<String>()
    }


    private var listener: ((ArrayList<String>?) -> Unit)? = null
    private var dismissListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenHeight = context!!.getScreenSize().height

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val contentView = View.inflate(context, R.layout.dialog_select_muilt_type_layout, null)
        val lengthContainer = contentView.findViewById<FlexboxLayout>(R.id.mFlSelectMultiTypeLengthContainer)
        val carTypeContainer = contentView.findViewById<FlexboxLayout>(R.id.mFlSelectMultiTypeCarTypeContainer)


        val clearView = contentView.findViewById<TextView>(R.id.mTvSelectMultiTypeClear)
        clearView.setOnClickListener {
            if (lengthList?.size == 0 && carTypeList?.size == 0) {
                return@setOnClickListener
            }

            if (lengthList!![0].isSelect && carTypeList!![0].isSelect){
                return@setOnClickListener
            }

            lengthList?.forEachWithIndex { index, item ->
                item.isSelect = index == 0
                (lengthContainer.getChildAt(index) as CheckedTextView).isChecked = item.isSelect
            }
            carTypeList?.forEachWithIndex{index,item->
                item.isSelect = index == 0
                (carTypeContainer.getChildAt(index) as CheckedTextView).isChecked = item.isSelect
            }
        }
        contentView.findViewById<TextView>(R.id.mTvSelectMultiTypeSubmit).setOnClickListener {
            val tempLengthList = lengthList!!.filter { it.isSelect }
            if (tempLengthList.size == 1 && tempLengthList[0].type == 0){
                selectLengthList.add("")
            }else{
                tempLengthList.forEach {
                    selectLengthList.add(it.name)
                }
            }
            listener?.invoke(selectLengthList)
            dismiss()
        }

        addItem(lengthList!!,lengthContainer,clearView)
        addItem(carTypeList!!,carTypeContainer,clearView)

        View.inflate(context, R.layout.layout_select_car_length_custom_length_layout, lengthContainer)
        dialog.setContentView(contentView)
        bottomSheetBehavior = BottomSheetBehavior.from(contentView.parent as View)
        return dialog
    }


    private fun addItem(items:ArrayList<CarParamBean>,container:FlexboxLayout,clearView:View){
        items.forEachWithIndex { index, carParamBean ->
            val itemView = View.inflate(context, R.layout.adapter_car_type_item_layout, container) as FlexboxLayout
            val checkedTextView = itemView.getChildAt(index) as CheckedTextView
            checkedTextView.text = carParamBean.name
            checkedTextView.isChecked = carParamBean.isSelect
            checkedTextView.setOnClickListener {
                if (index == 0){ // 不限车长item
                    clearView.performClick()
                    return@setOnClickListener
                }
                items[0].isSelect = false
                (container.getChildAt(0) as CheckedTextView).isChecked = items[0].isSelect
                (it as CheckedTextView).isChecked = !it.isChecked
                carParamBean.isSelect = it.isChecked
                if (items.none { it.isSelect }){
                    items[index].isSelect = true
                    it.isChecked = items[index].isSelect
                }
            }
        }
    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        mClCarLengthContainer.layoutParams = CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight)
//        bottomSheetBehavior?.peekHeight = screenHeight
//    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }


    fun setLengthDataSource(dataSource: ArrayList<CarParamBean>){
        this.lengthList = dataSource
    }

    fun setCarTypeDataSource(dataSource: ArrayList<CarParamBean>){
        this.carTypeList = dataSource
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