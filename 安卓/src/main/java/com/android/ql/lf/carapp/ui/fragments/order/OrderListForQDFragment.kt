package com.android.ql.lf.carapp.ui.fragments.order

import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.EventIsMasterAndMoneyBean
import com.android.ql.lf.carapp.data.NewOrderMessageBean
import com.android.ql.lf.carapp.data.OrderBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.present.ServiceOrderPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.activities.MainActivity
import com.android.ql.lf.carapp.ui.activities.SplashActivity
import com.android.ql.lf.carapp.ui.adapter.OrderListForQDAdapter
import com.android.ql.lf.carapp.ui.fragments.BaseRecyclerViewFragment
import com.android.ql.lf.carapp.ui.fragments.bottom.MainOrderHouseFragment
import com.android.ql.lf.carapp.ui.fragments.user.LoginFragment
import com.android.ql.lf.carapp.ui.fragments.user.mine.MineApplyMasterInfoSubmitFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.android.ql.lf.carapp.utils.RxBus
import com.android.ql.lf.carapp.utils.toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_order_for_qd_layout.*
import org.json.JSONObject

/**
 * Created by lf on 18.1.24.
 * @author lf on 18.1.24
 */
class OrderListForQDFragment : BaseRecyclerViewFragment<OrderBean>() {

    companion object {

        val RECEIVER_ORDER_FLAG = "receiver order"

        fun newInstance(): OrderListForQDFragment {
            return OrderListForQDFragment()
        }
    }

    private val serviceOrderPresent by lazy {
        ServiceOrderPresent()
    }

    private var currentOrderBean: OrderBean? = null

    private var isShowing = true

    private val newOrderNotifyDialog by lazy {
        Dialog(mContext)
    }

    //接收是否谁为师傅和是否交纳保证金的事件
    private val masterAndMoneySubscription by lazy {
        RxBus.getDefault().toObservable(EventIsMasterAndMoneyBean::class.java).subscribe {
            showNotify()
            onPostRefresh()
        }
    }

    //接收新订单的通知
    private val newOrderMessageSubscription by lazy {
        RxBus.getDefault().toObservable(NewOrderMessageBean::class.java).subscribe {
            if (!TextUtils.isEmpty(it.orderMessage) && UserInfo.getInstance().isLogin) {
                if (isShowing) {
//                    sendNotifyOnlySound()
                    showNewOrderDialog()
                } else {
                    showNewOrderDialog()
                    sendNotify()
                }
            }
        }
    }

    private fun showNewOrderDialog() {
        if (!newOrderNotifyDialog.isShowing) {
            newOrderNotifyDialog.setCancelable(true)
            newOrderNotifyDialog.setCanceledOnTouchOutside(false)
            newOrderNotifyDialog.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(mContext, android.R.color.transparent)))
            val contentView = View.inflate(mContext, R.layout.dialog_order_notify_layout, null)
            contentView.findViewById<Button>(R.id.mBtNewOrderNotify).setOnClickListener {
                newOrderNotifyDialog.dismiss()
            }
            val timeCount = contentView.findViewById<TextView>(R.id.mTvOrderNotifyDialogTimeCount)
            timeCount.text = "5s"
            newOrderNotifyDialog.setContentView(contentView)
            val countDownTime = object : CountDownTimer(1000 * 5, 1000) {
                override fun onFinish() {
                    newOrderNotifyDialog.dismiss()
                }

                override fun onTick(millisUntilFinished: Long) {
                    timeCount.text = "${millisUntilFinished / 1000}秒"
                }
            }
            countDownTime.start()
            newOrderNotifyDialog.setOnDismissListener {
                onPostRefresh()// 刷新订单，接受新订单
                countDownTime.cancel()
            }
            newOrderNotifyDialog.show()
        }
    }

    //接收退出登录事件
    private val userLogoutSubscription by lazy {
        RxBus.getDefault().toObservable(String::class.java).subscribe {
            if (it == UserInfo.LOGOUT_FLAG) {
                onLogoutSuccess()
            }
        }
    }

    //在其它页面中 来了信息了 要跳到首页中
    private fun sendNotify() {
        val forIntent = Intent(context, MainActivity::class.java)
        startActivity(forIntent)
//        val builder = NotificationCompat.Builder(mContext)
//        builder.setAutoCancel(true)
//        val forIntent = Intent(context, MainActivity::class.java)
//        val intentPend = PendingIntent.getActivity(context, 0, forIntent, PendingIntent.FLAG_CANCEL_CURRENT)
//        builder.setContentIntent(intentPend)
//        builder.setSmallIcon(R.mipmap.ic_launcher)
//        builder.setDefaults(NotificationCompat.DEFAULT_SOUND)
//        builder.setTicker("新消息")
//        builder.setContentText("您有新的订单，请注意查收！")
//        builder.setContentTitle("新消息提醒")
//        val manager = NotificationManagerCompat.from(mContext)
//        manager.notify(0, builder.build())
    }

    private fun sendNotifyOnlySound() {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) ?: return
        val r = RingtoneManager.getRingtone(context, notification)
        r.play()
        val vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(1000)
    }

    override fun getLayoutId() = R.layout.fragment_order_for_qd_layout

    override fun createAdapter(): BaseQuickAdapter<OrderBean, BaseViewHolder>
            = OrderListForQDAdapter(mContext, R.layout.adapter_order_list_for_qd_item_layout, mArrayList)

    override fun initView(view: View?) {
        super.initView(view)
        registerLoginSuccessBus()
        masterAndMoneySubscription
        userLogoutSubscription
        newOrderMessageSubscription
        showNotify()
//        sendNotify()
    }

    override fun onResume() {
        super.onResume()
        isShowing = true
    }

    override fun onPause() {
        super.onPause()
        isShowing = false
    }

    override fun onLoginSuccess(userInfo: UserInfo?) {
        showNotify()
        onPostRefresh()
    }

    private fun onLogoutSuccess() {
        showNotify()
        super.onRefresh()
    }

    private fun showNotify() {
        if (UserInfo.getInstance().isLogin) {
            //更新地址
            if (UserInfo.getInstance().shopInfo != null) {
                (parentFragment as MainOrderHouseFragment).updateAddress(UserInfo.getInstance().shopInfo.shop_address+UserInfo.getInstance().shopInfo.shop_d)
            } else {
                (parentFragment as MainOrderHouseFragment).updateAddress("暂无")
            }
            if (UserInfo.getInstance().isMaster) {
                mTvOrderQDNotify.visibility = View.GONE
                mBaseAdapter.notifyDataSetChanged()
                return
            }
            mTvOrderQDNotify.visibility = View.VISIBLE
            when (UserInfo.getInstance().authenticationStatus) {
                0 -> {
                    mTvOrderQDNotify.text = "当前帐号正在认证中……"
                    mTvOrderQDNotify.isEnabled = false
                }
                2 -> {
                    mTvOrderQDNotify.text = "审核失败，请重新提交资料……"
                    mTvOrderQDNotify.isEnabled = true
                    mTvOrderQDNotify.setOnClickListener {
                        FragmentContainerActivity.from(mContext).setTitle("申请成为师傅").setNeedNetWorking(true).setClazz(MineApplyMasterInfoSubmitFragment::class.java).start()
                    }
                }
                3 -> {
                    mTvOrderQDNotify.text = "当前帐号未认证，暂无法接单，请立即认证"
                    mTvOrderQDNotify.isEnabled = true
                    mTvOrderQDNotify.setOnClickListener {
                        FragmentContainerActivity.from(mContext).setTitle("申请成为师傅").setNeedNetWorking(true).setClazz(MineApplyMasterInfoSubmitFragment::class.java).start()
                    }
                }
            }
            mBaseAdapter.notifyDataSetChanged()
        } else {
            (parentFragment as MainOrderHouseFragment).updateAddress("暂无")
            (parentFragment as MainOrderHouseFragment).updateOrderNum(0)
            mTvOrderQDNotify.visibility = View.VISIBLE
            mTvOrderQDNotify.text = "登录后显示订单"
            mTvOrderQDNotify.setOnClickListener {
                UserInfo.loginToken = RECEIVER_ORDER_FLAG
                LoginFragment.startLogin(mContext)
            }
        }
    }

    override fun getEmptyMessage(): String {
        return "这里还没有订单呢！"
    }

    override fun getEmptyLayoutId() = R.layout.layout_order_list_empty

    override fun onRefresh() {
        super.onRefresh()
        if (UserInfo.getInstance().isLogin) {
            mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_QORDER, RequestParamsHelper.getQorderParam(page = currentPage, location = if (UserInfo.getInstance().shopInfo != null) {
                UserInfo.getInstance().shopInfo.shop_address
            } else {
                ""
            }))
        } else {
            showNotify()
            onRequestFail(-1, IllegalStateException("状态异常"))
            onRequestEnd(-1)
        }
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_QORDER, RequestParamsHelper.getQorderParam(page = currentPage, location = if (UserInfo.getInstance().shopInfo != null) {
            UserInfo.getInstance().shopInfo.shop_address
        } else {
            ""
        }))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在抢单……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            processList(result as String, OrderBean::class.java)
            val check = checkResultCode(result)
            if (check != null && currentPage == 0) {
                (parentFragment as MainOrderHouseFragment).updateOrderNum((check.obj as JSONObject).optInt("arr1"))
                (parentFragment as MainOrderHouseFragment).updateNotifyRed(if (((check.obj as JSONObject).optString("arr2").toInt() > 0)) {
                    View.VISIBLE
                } else {
                    View.GONE
                })
            }
            if (!mArrayList.isEmpty()) {
                //校对时间
                val currentTime = System.currentTimeMillis()
                mArrayList.forEach {
                    it.endTime = currentTime + (it.qorder_remaining_time * 1000)
                }
            }
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                toast("恭喜，抢单成功，祝您工作愉快！")
                serviceOrderPresent.updateOrderStatus(ServiceOrderPresent.OrderStatus.WAITING_WORK.index.toInt())
            } else {
                toast("该订单已被抢了~~")
            }
            onPostRefresh()
//            val position = mArrayList.indexOf(currentOrderBean)
//            mArrayList.remove(currentOrderBean)
//            mBaseAdapter.notifyItemRemoved(position)
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        if (requestID == 0x1) {
            toast("抢单失败")
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (view!!.id == R.id.mBtOrderListForQDItem) {
            when (UserInfo.getInstance().authenticationStatus) {
                0 -> {
                    mTvOrderQDNotify.text = "当前帐号正在认证中……"
                }
                1 -> {
                    if (UserInfo.getInstance().isMaster) {
                        currentOrderBean = mArrayList[position]
                        mPresent.getDataByPost(0x1, RequestParamsHelper.ORDER_MODEL, RequestParamsHelper.ACT_ORDER_RECEIVING, RequestParamsHelper.getOrderReceivingParam(currentOrderBean!!.qorder_id))
                    }
                }
                2 -> {
                    mTvOrderQDNotify.text = "审核失败，请重新提交资料……"
                    mTvOrderQDNotify.setOnClickListener {
                        FragmentContainerActivity.from(mContext).setTitle("申请成为师傅").setNeedNetWorking(true).setClazz(MineApplyMasterInfoSubmitFragment::class.java).start()
                    }
                }
                3 -> {
                    mTvOrderQDNotify.text = "当前帐号未认证，暂无法接单，请立即认证"
                    mTvOrderQDNotify.setOnClickListener {
                        FragmentContainerActivity.from(mContext).setTitle("申请成为师傅").setNeedNetWorking(true).setClazz(MineApplyMasterInfoSubmitFragment::class.java).start()
                    }
                }
            }
        }
    }

    private fun showOrderNotifyDialog() {
        FragmentContainerActivity
                .from(mContext)
                .setClazz(LoginFragment::class.java)
                .setTitle("登录")
                .setHiddenToolBar(false)
                .setNeedNetWorking(true)
                .start()

//        val dialog = Dialog(mContext)
//        dialog.setCancelable(true)
//        dialog.setCanceledOnTouchOutside(false)
//        dialog.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(mContext, android.R.color.transparent)))
//        val contentView = View.inflate(mContext,R.layout.dialog_order_notify_layout,null)
////        val tv_time_count = contentView.findViewById<TextView>(R.id.mTvOrderNotifyDialogTimeCount)
////        val spannableString = SpannableString("5s")
//
//        dialog.setContentView(contentView)
//        dialog.show()


//        val dialog = Dialog(mContext)
//        dialog.setCancelable(true)
//        dialog.setCanceledOnTouchOutside(false)
//        dialog.window.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(mContext, android.R.color.transparent)))
//        val contentView = View.inflate(mContext, R.layout.dialog_invite_code_layout, null)
////        val tv_time_count = contentView.findViewById<TextView>(R.id.mTvOrderNotifyDialogTimeCount)
////        val spannableString = SpannableString("5s")
//
//        dialog.setContentView(contentView)
//        dialog.show()
    }

    override fun onDestroyView() {
        unsubscribe(masterAndMoneySubscription)
        unsubscribe(userLogoutSubscription)
        unsubscribe(newOrderMessageSubscription)
        super.onDestroyView()
    }

}