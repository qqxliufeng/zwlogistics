package com.android.ql.lf.carapp.ui.fragments.bottom

import android.app.Dialog
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.os.EnvironmentCompat
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.UpdateNotifyBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.data.VersionInfo
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.activities.MainActivity
import com.android.ql.lf.carapp.ui.fragments.BaseFragment
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.ui.fragments.message.MineMessageListFragment
import com.android.ql.lf.carapp.ui.fragments.order.OrderAllListQDFragment
import com.android.ql.lf.carapp.ui.fragments.order.OrderListForAfterSaleFragment
import com.android.ql.lf.carapp.ui.fragments.order.OrderListForMineFragment
import com.android.ql.lf.carapp.ui.fragments.order.OrderListForQDFragment
import com.android.ql.lf.carapp.utils.*
import kotlinx.android.synthetic.main.fragment_main_order_house_layout.*
import org.json.JSONObject
import q.rorbin.badgeview.QBadgeView

/**
 * Created by lf on 18.1.24.
 * @author lf on 18.1.24
 */
class MainOrderHouseFragment : BaseNetWorkingFragment() {

    companion object {

        val MY_ALL_ORDER_LIST_FLAG = "MY_ALL_ORDER_LIST_FLAG"

        val MY_MESSAGE_FLAG = "my_message_flag"

        fun newInstance(): MainOrderHouseFragment {
            return MainOrderHouseFragment()
        }

        val TITLES = listOf("抢单", "我的订单", "售后订单")

    }

    private val loginSubscription by lazy {
        RxBus.getDefault().toObservable(UserInfo::class.java).subscribe {
            if (it.isLogin) {
                if (UserInfo.loginToken == MY_MESSAGE_FLAG) {
                    mFlMainMainOrderHouseNotifyContainer.doClickWithUseStatusEnd()
                    updateNotifyRed(View.GONE)
                } else if (UserInfo.loginToken == MY_ALL_ORDER_LIST_FLAG) {
                    mIvMainOrderHouseCount.doClickWithUseStatusEnd()
                }
            }
        }
    }

    private val userLogoutSubscription by lazy {
        RxBus.getDefault().toObservable(String::class.java).subscribe {
            if (it == UserInfo.LOGOUT_FLAG) {
                mTvMainOrderHouseAddress.text = "暂无"
            }
        }
    }

    private val orderCountBadgeView by lazy {
        QBadgeView(mContext)
    }

    private val updateMessageNotifySubscription by lazy {
        RxBus.getDefault().toObservable(UpdateNotifyBean::class.java).subscribe {
            mViewMainOrderHouseMessageNotify.visibility = it.status
        }
    }

    override fun getLayoutId() = R.layout.fragment_main_order_house_layout

    override fun initView(view: View?) {
        val statusHeight = (mContext as MainActivity).statusHeight
        mAlOrderHouse.setPadding(0, statusHeight, 0, 0)
        mVpOrderHouse.adapter = OrderHouseViewPagerAdapter(childFragmentManager)
        mVpOrderHouse.offscreenPageLimit = TITLES.size
        mTlOrderHouse.setupWithViewPager(mVpOrderHouse)
        orderCountBadgeView.badgeNumber = 0
        val orderCountBadge = orderCountBadgeView.bindTarget(mIvMainOrderHouseCount)
        orderCountBadge.badgeBackgroundColor = Color.WHITE
        orderCountBadge.badgeTextColor = ContextCompat.getColor(mContext, R.color.main_color)
        orderCountBadge.setBadgeTextSize(8.0f, true)
        mFlMainMainOrderHouseNotifyContainer.doClickWithUserStatusStart(MY_MESSAGE_FLAG) {
            updateNotifyRed(View.GONE)
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的消息", true, false, MineMessageListFragment::class.java)
        }
        mIvMainOrderHouseCount.doClickWithUserStatusStart(MY_ALL_ORDER_LIST_FLAG) {
            FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setClazz(OrderAllListQDFragment::class.java).setTitle("抢单列表").start()
        }
        mAlOrderHouse.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            mTlOrderHouseTitleContainer.alpha = 1 - Math.abs(verticalOffset).toFloat() / appBarLayout.totalScrollRange.toFloat()
        }
        loginSubscription
        userLogoutSubscription
        updateMessageNotifySubscription
        mPresent.getDataByPost(0x0, "t", "app_update")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (check != null) {
            if (check.code == SUCCESS_CODE) {
                val json = check.obj as JSONObject
                val resultJson = json.optJSONObject("result")
                if (resultJson.optString("version_code").toInt() > VersionHelp.currentVersionCode(mContext)) {
                    VersionInfo.getInstance().versionCode = resultJson.optString("version_code").toInt()
                    VersionInfo.getInstance().content = resultJson.optString("ssw")
                    VersionInfo.getInstance().downUrl = resultJson.optString("apk")
                    val updateDialog = Dialog(mContext)
                    val contentView = View.inflate(mContext, R.layout.layout_update_layout, null)
                    val tv_content = contentView.findViewById<TextView>(R.id.mTvUpdateVersionContent)
                    contentView.findViewById<Button>(R.id.mBtUpdateVersionCancel).setOnClickListener {
                        updateDialog.dismiss()
                    }
                    contentView.findViewById<Button>(R.id.mBtUpdateVersionUpdate).setOnClickListener {
                        updateDialog.dismiss()
                        toast("正在下载……")
                        VersionHelp.downNewVersion(mContext, Uri.parse(resultJson.optString("apk")),"${System.currentTimeMillis()}")
                    }
                    updateDialog.setCanceledOnTouchOutside(false)
                    tv_content.text = resultJson.optString("ssw")
                    updateDialog.setContentView(contentView)
                    updateDialog.show()
                }
            }
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
    }


    fun updateAddress(address: String) {
        mTvMainOrderHouseAddress.text = address
    }

    fun updateOrderNum(count: Int) {
        orderCountBadgeView.badgeNumber = count
    }

    fun updateNotifyRed(visibility: Int) {
        mViewMainOrderHouseMessageNotify.visibility = visibility
        RxBus.getDefault().post(UpdateNotifyBean(visibility))
    }

    class OrderHouseViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int) = when (position) {
            0 -> {
                OrderListForQDFragment.newInstance()
            }
            1 -> {
                OrderListForMineFragment.newInstance()
            }
            else -> {
                OrderListForAfterSaleFragment.newInstance()
            }
        }

        override fun getCount() = MainOrderHouseFragment.TITLES.size

        override fun getPageTitle(position: Int) = MainOrderHouseFragment.TITLES[position]

    }

    override fun onDestroyView() {
        unsubscribe(loginSubscription)
        unsubscribe(userLogoutSubscription)
        unsubscribe(updateMessageNotifySubscription)
        super.onDestroyView()
    }

}