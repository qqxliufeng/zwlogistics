package com.android.ql.lf.carapp.ui.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import com.android.ql.lf.carapp.R

/**
 * Created by lf on 2017/11/17 0017.
 * @author lf on 2017/11/17 0017
 */
class MyProgressDialog(context: Context) : Dialog(context) {

    var message: String = "正在加载……"

    constructor(context: Context, message: String) : this(context) {
        this.message = message
    }

    private val contentView: View = View.inflate(context, R.layout.dialog_my_progress_layout, null)

    override fun show() {
        setContentView(contentView)
        val tvMessage = contentView.findViewById<TextView>(R.id.mTvProgressDialogInfo)
        tvMessage.text = message
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        super.show()
    }

}