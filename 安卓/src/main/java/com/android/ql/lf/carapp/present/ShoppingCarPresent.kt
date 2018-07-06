package com.android.ql.lf.carapp.present

import com.android.ql.lf.carapp.data.ShoppingCarItemBean
import java.text.DecimalFormat

/**
 * Created by lf on 18.3.15.
 * @author lf on 18.3.15
 */
class ShoppingCarPresent(var shopItems: ArrayList<ShoppingCarItemBean>) {

    private var allMoney = 0.00f

    /**
     * 取消全选
     */
    fun cancelItemsSelects(): Pair<Boolean, Float> {
        if (shopItems.isEmpty()) return Pair(false, 0.00f)
        shopItems.forEach {
            it.isSelector = false
        }
        allMoney = 0.00f
        return Pair(true, allMoney)
    }

    /**
     * 全选
     */
    fun allItemSelects(): Pair<Boolean, Float> {
        if (shopItems.isEmpty()) return Pair(false, 0.0f)
        shopItems.forEach {
            it.isSelector = true
        }
        return Pair(true, calculateAllPrice())
    }

    /**
     * 计算总价
     */
    fun calculateAllPrice(): Float {
        allMoney = 0.00f
        shopItems.forEach {
            if (it.isSelector) {
                allMoney += (it.shopcart_price.toFloat() * it.shopcart_num.toInt())
            }
        }
        return allMoney
    }

    /**
     * 返回格式后的价格
     */
    fun getPrice(): String = "￥${DecimalFormat("0.00").format(allMoney)}"

    /**
     * 格式价格
     */
    fun formatPrice(price: Float): String = "￥${DecimalFormat("0.00").format(price)}"

    /**
     * 判断是否没有一个条目选中
     */
    fun isNoneSelected() = allMoney == 0.00f

    /**
     * 选中某一个条目，判断是否都已经选中了
     */
    fun isAllItemsSelected(): Boolean = shopItems.filter { it.isSelector }.size == shopItems.size

}