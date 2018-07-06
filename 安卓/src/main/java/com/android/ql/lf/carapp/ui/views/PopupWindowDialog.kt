package com.android.ql.lf.carapp.ui.views

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow

/**
 * Created by lf on 2017/11/17 0017.
 * @author lf on 2017/11/17 0017
 */
class PopupWindowDialog {

    companion object {
        fun showReplyDialog(context: Context, contentView: View): PopupWindow {
            val popupWindow = PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popupWindow.isTouchable = true
            popupWindow.setTouchInterceptor { v, event -> false }
            popupWindow.isFocusable = true
            popupWindow.isOutsideTouchable = true
            popupWindow.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            popupWindow.inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
            popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0)
            val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            return popupWindow
        }
    }

}