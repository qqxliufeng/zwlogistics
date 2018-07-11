package com.android.ql.lf.zwlogistics.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.net.Uri
import android.os.IBinder
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseFragment
import org.jetbrains.anko.windowManager

fun Context.checkGpsIsOpen():Boolean{
    return (getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun Activity.openGpsPage(requestCode:Int = -1){
    startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),requestCode)
}

fun Context.hiddenKeyBoard(token: IBinder) {
    val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Context.showKeyBoard(token: View) {
    val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(token, InputMethodManager.SHOW_FORCED)
}

fun Fragment.startPhone(phone: String) {
    val builder = AlertDialog.Builder(this.context!!)
    builder.setMessage("是否拨打电话？")
    builder.setNegativeButton("否", null)
    builder.setPositiveButton("是") { _, _ ->
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
    builder.create().show()
}

fun Context.getScreenSize():BaseFragment.ScreenSize{
    val outMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(outMetrics)
    val screenSize = BaseFragment.ScreenSize()
    screenSize.height = outMetrics.heightPixels
    screenSize.width = outMetrics.widthPixels
    return screenSize
}

fun Fragment.showInfoDialog(title:String = "",leftText:String,rightText:String,leftListener:(()->Unit)?,rightListener:(()->Unit)?){
    val dialog = Dialog(context)
    val contentView = View.inflate(context, R.layout.layout_back_dialog_content_layout, null)
    val tv_title = contentView.findViewById<TextView>(R.id.mTvBackDialogTitle)
    val bt_left = contentView.findViewById<Button>(R.id.mBtBackDialogCancel)
    val bt_right = contentView.findViewById<Button>(R.id.mBtBackDialogContinue)
    tv_title.text = title
    bt_left.text = leftText
    bt_right.text = rightText
    bt_left.setOnClickListener {
        dialog.dismiss()
        leftListener?.invoke()
    }
    bt_right.setOnClickListener {
        dialog.dismiss()
        rightListener?.invoke()
    }
    dialog.setContentView(contentView)
    dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.show()
    val param = dialog.window.attributes
    param.width = context!!.getScreenSize().width / 5 * 4
    param.height = context!!.getScreenSize().height / 2
    dialog.window.attributes = param
}



fun Fragment.alert(title: String? = "title",
                   message: String = "message",
                   positiveButton: String = "是",
                   negativeButton: String = "否",
                   positiveAction: ((dialog: DialogInterface, which: Int) -> Unit)? = null,
                   negativeAction: ((dialog: DialogInterface, which: Int) -> Unit)? = null) =
        this.context?.alert(title, message, positiveButton, negativeButton, positiveAction, negativeAction)

fun Fragment.alert(message: String = "message",
                   positiveButton: String = "是",
                   negativeButton: String = "否", positiveAction: ((dialog: DialogInterface, which: Int) -> Unit)? = null) =
        this.alert(null, message, positiveButton, negativeButton, positiveAction, null)

fun Context.alert(title: String? = "title",
                  message: String = "message",
                  positiveButton: String = "是",
                  negativeButton: String = "否",
                  positiveAction: ((dialog: DialogInterface, which: Int) -> Unit)? = null,
                  negativeAction: ((dialog: DialogInterface, which: Int) -> Unit)? = null) {
    val builder = AlertDialog.Builder(this)
    builder.setMessage(message)
    builder.setTitle(title)
    builder.setNegativeButton(negativeButton, negativeAction)
    builder.setPositiveButton(positiveButton, positiveAction)
    builder.create().show()
}