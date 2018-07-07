package com.android.ql.lf.zwlogistics.ui.fragment.order

import android.text.Html
import android.view.View
import com.android.ql.lf.carapp.utils.fromHtml
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
        mTvOrderInfoOrderSn.setDiffColorText("订单编号：", "123456789")
        mTvOrderInfoOrderSendTime.setDiffColorText("发货日期：", "2018-07-29")
        mTvOrderInfoOrderReceiverTime.setDiffColorText("交货日期：", "2018-07-29")
        mTvOrderInfoOrderPayType.setDiffColorText("支付方式：", "全现金")
        mTvOrderInfoOrderPersonNum.setDiffColorText("竞标人数：", "10人", color2 = "#ff2222")
        mTvOrderInfoGoodsZXType.setDiffColorText("装卸方式：", "一装一卸")

        mTvOrderInfoGoodsInfo.text = Html.fromHtml("重量：".fromHtml()+"5吨".fromHtml("#545557")+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;体积：".fromHtml()+"10立方米".fromHtml("#545557"))
        mTvOrderInfoGoodsSendAddress.setDiffColorText("出发地：","山东济南")
        mTvOrderInfoGoodsTJAddress.setDiffColorText("途径地：","123456789")
        mTvOrderInfoGoodsReceiverAddress.setDiffColorText("目的地：","123456789")

        mTvOrderInfoCarType.setDiffColorText("用车类型：","123456789")
        mTvOrderInfoCarHeight.setDiffColorText("车长限制：","123456789")
        mTvOrderInfoCarCX.setDiffColorText("车型限制：","123456789")
        mTvOrderInfoJB.setOnClickListener {
//            showInfoDialog("您尚未进行认证，只有认证通过后才能进行竞标哦~", "放弃", "前往认证", null, {
//                FragmentContainerActivity.from(mContext).setClazz(MinePersonAuthFragment::class.java).setNeedNetWorking(true).setTitle("司机身份认证").start()
//            }
//            )
            FragmentContainerActivity.from(mContext).setTitle("竞标信息").setNeedNetWorking(true).setClazz(TenderInfoFragment::class.java).start()
        }
    }
}