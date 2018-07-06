package com.android.ql.lf.zwlogistics.ui.fragment.order

import android.view.View
import com.android.ql.lf.carapp.utils.setDiffColorText
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MinePersonAuthFragment
import com.android.ql.lf.zwlogistics.utils.showInfoDialog
import kotlinx.android.synthetic.main.fragment_order_info_layout.*

class OrderInfoFragment : BaseNetWorkingFragment() {


    override fun getLayoutId() = R.layout.fragment_order_info_layout

    override fun initView(view: View?) {
        mTvOrderInfoOrderSn.setDiffColorText("订单编号", "123456789")
        mTvOrderInfoOrderSendTime.setDiffColorText("发货日期", "2018-07-29")
        mTvOrderInfoOrderReceiverTime.setDiffColorText("交货日期", "2018-07-29")
        mTvOrderInfoOrderPayType.setDiffColorText("支付方式", "全现金")
        mTvOrderInfoOrderPersonNum.setDiffColorText("竞标人数", "10人", color2 = "#ff2222")
        mTvOrderInfoGoodsZXType.setDiffColorText("装卸方式", "一装一卸")
        mTvOrderInfoGoodsInfo.setDiffColorText("重量：5吨  体积：10立方米", "123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
//        mTvOrderInfoOrderSn.setDiffColorText("订单编号","123456789")
        mTvOrderInfoJB.setOnClickListener {
//            showInfoDialog("您尚未进行认证，只有认证通过后才能进行竞标哦~", "放弃", "前往认证", null, {
//                FragmentContainerActivity.from(mContext).setClazz(MinePersonAuthFragment::class.java).setNeedNetWorking(true).setTitle("司机身份认证").start()
//            }
//            )
            FragmentContainerActivity.from(mContext).setTitle("竞标信息").setNeedNetWorking(true).setClazz(TenderInfoFragment::class.java).start()
        }
    }
}