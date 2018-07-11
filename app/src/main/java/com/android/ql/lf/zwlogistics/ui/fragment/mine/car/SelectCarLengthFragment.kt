package com.android.ql.lf.zwlogistics.ui.fragment.mine.car

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import com.android.ql.lf.carapp.utils.getTextString
import com.android.ql.lf.carapp.utils.isEmpty
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.CarParamBean
import com.android.ql.lf.zwlogistics.ui.widgets.DJEditText
import com.android.ql.lf.zwlogistics.utils.getScreenSize
import com.android.ql.lf.zwlogistics.utils.hiddenKeyBoard
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import org.jetbrains.anko.support.v4.toast

class SelectCarLengthFragment :BottomSheetDialogFragment(){

    private var screenHeight = 0

    private var arrayList:ArrayList<CarParamBean>? = null

    private var bottomSheetBehavior:BottomSheetBehavior<View>? = null

    private var listener:((data:CarParamBean)->Unit)? = null

    private var currentSelectBean:CarParamBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenHeight = context!!.getScreenSize().height
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
        val adapter= object : BaseQuickAdapter<CarParamBean,BaseViewHolder>(R.layout.adapter_car_type_item_layout,arrayList) {
            override fun convert(helper: BaseViewHolder?, item: CarParamBean?) {
                helper!!.setText(R.id.mCtvCarTypeItemName,item!!.name)
                helper.setChecked(R.id.mCtvCarTypeItemName,item.isSelect)
            }
        }
        mRvSelectLength.adapter = adapter
        val footView = View.inflate(context,R.layout.layout_select_car_length_custom_length_layout,null)
        val et_legnth = footView.findViewById<DJEditText>(R.id.mEtCarLengthCustom)
        et_legnth.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (currentSelectBean!=null){
                    currentSelectBean?.isSelect = false
                    adapter.notifyItemChanged(arrayList!!.indexOf(currentSelectBean!!))
                    currentSelectBean = null
                }
                if ("." == s?.toString()){
                    et_legnth.setText("0.")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
        adapter.addFooterView(footView)
        contentView.findViewById<TextView>(R.id.mTvSelectCarLengthSubmit).setOnClickListener {
            if (currentSelectBean!=null) {
                this.listener?.invoke(currentSelectBean!!)
                dismiss()
            }else{
                if (et_legnth.isEmpty()) {
                    toast("请选择或输入车长")
                }else{
                    arrayList?.forEach { it.isSelect = false }
                    adapter.notifyDataSetChanged()
                    val customBean = CarParamBean()
                    customBean.name = et_legnth.getTextString()
                    customBean.id = "-1"
                    this.listener?.invoke(customBean)
                    dismiss()
                }
            }
        }
        mRvSelectLength.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                et_legnth.setText("")
                context?.hiddenKeyBoard(et_legnth.windowToken)
                currentSelectBean = arrayList?.get(position)
                currentSelectBean?.isSelect = !currentSelectBean!!.isSelect
                arrayList?.filter { it != currentSelectBean }?.forEach {
                    it.isSelect = false
                }
                adapter?.notifyDataSetChanged()
            }
        })
        bottomSheetBehavior = BottomSheetBehavior.from(contentView.parent as View)
        return bottomDialog
    }


    override fun onStart() {
        super.onStart()
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN){
                    dismiss()
                }
            }
        })
    }

    fun setDataSource(list:ArrayList<CarParamBean>){
        arrayList = list
    }

    fun myShow(manager: FragmentManager?, tag: String?,listener:(data:CarParamBean)->Unit) {
        this.listener = listener
        super.show(manager, tag)
    }

}