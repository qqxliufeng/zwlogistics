package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.text.TextUtils
import android.view.View
import android.widget.CheckedTextView
import android.widget.TextView
import com.android.ql.lf.carapp.utils.*
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.CarParamBean
import com.android.ql.lf.zwlogistics.ui.widgets.DJEditText
import com.google.android.flexbox.FlexboxLayout
import org.jetbrains.anko.collections.forEachWithIndex

class SelectMultiTypeFragment : BottomSheetDialogFragment() {


    private var bottomSheetBehavior: BottomSheetBehavior<View>? = null

    private var lengthList: ArrayList<CarParamBean>? = null

    private var carTypeList: ArrayList<CarParamBean>? = null

    private val selectLengthList by lazy {
        arrayListOf<CarParamBean>()
    }

    private val selectCarTypeList by lazy {
        arrayListOf<CarParamBean>()
    }


    private var listener: ((ArrayList<CarParamBean>?, ArrayList<CarParamBean>?) -> Unit)? = null

    private var dismissListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (lengthList!=null && !lengthList!!.isEmpty() && lengthList!!.none { it.isSelect }){
            lengthList!![0].isSelect = true
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val contentView = View.inflate(context, R.layout.dialog_select_muilt_type_layout, null)
        val lengthContainer = contentView.findViewById<FlexboxLayout>(R.id.mFlSelectMultiTypeLengthContainer)
        val carTypeContainer = contentView.findViewById<FlexboxLayout>(R.id.mFlSelectMultiTypeCarTypeContainer)


        val clearView = contentView.findViewById<TextView>(R.id.mTvSelectMultiTypeClear)

        addItem(lengthList!!, lengthContainer, clearView, "米")
        addItem(carTypeList!!, carTypeContainer, clearView)

        View.inflate(context, R.layout.layout_select_car_length_custom_length_layout, lengthContainer)
        val et_legnth = lengthContainer.findViewById<DJEditText>(R.id.mEtCarLengthCustom)
        et_legnth.setTextChangedListener {
            et_legnth.setFirstPoint()
            lengthList!![0].isSelect = TextUtils.isEmpty(et_legnth.getTextString()) && lengthList!!.none { it.isSelect }
            (lengthContainer.getChildAt(0) as CheckedTextView).isChecked = lengthList!![0].isSelect
        }

        clearView.setOnClickListener {
            et_legnth.setText("")
            if (lengthList?.size == 0 && carTypeList?.size == 0) {
                return@setOnClickListener
            }

            if (lengthList!![0].isSelect && carTypeList!![0].isSelect) {
                return@setOnClickListener
            }
            lengthList?.forEachWithIndex { index, item ->
                item.isSelect = index == 0
                (lengthContainer.getChildAt(index) as CheckedTextView).isChecked = item.isSelect
            }
            carTypeList?.forEachWithIndex { index, item ->
                item.isSelect = index == 0
                (carTypeContainer.getChildAt(index) as CheckedTextView).isChecked = item.isSelect
            }
        }

        contentView.findViewById<TextView>(R.id.mTvSelectMultiTypeSubmit).setOnClickListener {
            selectLengthList.clear()
            selectCarTypeList.clear()
            val tempLengthList = lengthList!!.filter { it.isSelect }
            if (tempLengthList.size == 1 && tempLengthList[0].type == 0) {
                selectLengthList.addAll(tempLengthList)
            } else {
                tempLengthList.forEach {
                    selectLengthList.add(it)
                }
            }
            val tempTypeList = carTypeList!!.filter { it.isSelect }
            if (tempTypeList.size == 1 && tempTypeList[0].type == 0) {
                selectCarTypeList.addAll(tempTypeList)
            } else {
                tempTypeList.forEach {
                    selectCarTypeList.add(it)
                }
            }
            val formatData = et_legnth.getFormateFloat()
            if (formatData != null) {
                val formatParamBean = CarParamBean()
                formatParamBean.name = formatData
                formatParamBean.type = -2
                selectLengthList.add(formatParamBean)
            }
            listener?.invoke(selectLengthList, selectCarTypeList)
            dismiss()
        }
        dialog.setContentView(contentView)
        bottomSheetBehavior = BottomSheetBehavior.from(contentView.parent as View)
        return dialog
    }


    private fun addItem(items: ArrayList<CarParamBean>, container: FlexboxLayout, clearView: View, endWith: String = "") {
        items.forEachWithIndex { index, carParamBean ->
            val itemView = View.inflate(context, R.layout.adapter_car_type_item_layout, container) as FlexboxLayout
            val checkedTextView = itemView.getChildAt(index) as CheckedTextView
            checkedTextView.text = if (carParamBean.name == "不限车长") {
                carParamBean.name
            } else {
                "${carParamBean.name}$endWith"
            }
            checkedTextView.isChecked = carParamBean.isSelect
            checkedTextView.setOnClickListener {
                if (index == 0) { // 不限车长item
                    items.forEachWithIndex { index, item ->
                        item.isSelect = index == 0
                        (container.getChildAt(index) as CheckedTextView).isChecked = item.isSelect
                    }
                    return@setOnClickListener
                }
                items[0].isSelect = false
                (container.getChildAt(0) as CheckedTextView).isChecked = items[0].isSelect
                (it as CheckedTextView).isChecked = !it.isChecked
                carParamBean.isSelect = it.isChecked
                if (items.none { it.isSelect }) {
                    items[index].isSelect = true
                    it.isChecked = items[index].isSelect
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }


    fun setLengthDataSource(dataSource: ArrayList<CarParamBean>) {
        this.lengthList = dataSource
        val carParamBean = CarParamBean()
        carParamBean.name = "不限车长"
        carParamBean.id = "-1"
        carParamBean.isSelect = true
        carParamBean.type = 0
        this.lengthList?.add(0, carParamBean)
    }

    fun setCarTypeDataSource(dataSource: ArrayList<CarParamBean>) {
        this.carTypeList = dataSource
        val carParamBean = CarParamBean()
        carParamBean.name = "不限车型"
        carParamBean.id = "-1"
        carParamBean.isSelect = true
        carParamBean.type = 0
        this.carTypeList?.add(0, carParamBean)
    }


    fun myShow(manager: FragmentManager?, tag: String?, listener: (ArrayList<CarParamBean>?, ArrayList<CarParamBean>?) -> Unit, dismissListener: () -> Unit) {
        this.listener = listener
        this.dismissListener = dismissListener
        super.show(manager, tag)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        this.dismissListener?.invoke()
    }

}