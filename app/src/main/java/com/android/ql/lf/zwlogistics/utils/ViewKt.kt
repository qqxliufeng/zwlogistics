package com.android.ql.lf.carapp.utils

import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.android.ql.lf.zwlogistics.R
import java.util.regex.Pattern

/**
 * Created by lf on 18.2.10.
 * @author lf on 18.2.10
 */

val PHONE_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}\$"
val IDCARD_REG = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)\$"

/**
 * 显示SnackBar
 */
fun View.showSnackBar(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackBar.view.setBackgroundResource(R.color.colorPrimary)
    snackBar.show()
}

/**
 * 校验输入框是否为空
 */
fun EditText.isEmpty(): Boolean {
    return TextUtils.isEmpty(this.text.toString().trim())
}

fun EditText.isPhone(): Boolean {
    return Pattern.compile(PHONE_REG).matcher(this.text).matches()
}

fun EditText.isIdCard(): Boolean {
    return Pattern.compile(IDCARD_REG).matcher(this.text).matches()
}

fun EditText.getTextString(): String {
    return this.text.toString().trim()
}


fun TextView.setDiffColorText(source1: String, source2: String, color1: String = "#c8c9ca", color2: String = "#545557") {
    text = Html.fromHtml(source1.fromHtml(color1)+source2.fromHtml(color2))
}


fun String.fromHtml(color: String = "#c8c9ca"): String {
    return "<font color='$color'>$this</font>"
}

fun View.doClickWithUserStatusStart(token: String, action: (view: View) -> Unit) {
    setOnClickListener {
        //        if (UserInfo.getInstance().isLogin) {
//            action(this)
//        } else {
//            UserInfo.loginToken = token
//            FragmentContainerActivity.from(this.context).setClazz(LoginFragment::class.java).setTitle("登录").setNeedNetWorking(true).start()
//        }
    }
}

fun View.doClickWithUseStatusEnd() {
    performClick()
//    UserInfo.resetLoginSuccessDoActionToken()
}

