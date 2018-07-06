package com.android.ql.lf.carapp.present;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Toast;

import com.android.ql.lf.carapp.data.RefreshData;
import com.android.ql.lf.carapp.data.WXPayBean;
import com.android.ql.lf.carapp.ui.fragments.mall.order.OrderSubmitFragment;
import com.android.ql.lf.carapp.ui.fragments.mall.shoppingcar.ShoppingCarFragment;
import com.android.ql.lf.carapp.ui.fragments.user.mine.MainMallOrderItemFragment;
import com.android.ql.lf.carapp.ui.views.SelectPayTypeView;
import com.android.ql.lf.carapp.utils.PayManager;
import com.android.ql.lf.carapp.utils.PreferenceUtils;
import com.android.ql.lf.carapp.utils.RxBus;
import com.android.ql.lf.carapp.wxapi.WXPayEntryActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by lf on 18.3.27.
 *
 * @author lf on 18.3.27
 */

public class MallOrderPresent {

    public enum MallOrderStatus {
        //0 待付款 1 待发货 2 已发货/待收货 3 已收货/待评价 4 取消订单 5 申请退款 6 已退款 7 已评价/订单完成
        WAITING_FOR_MONEY("0", "待付款"),
        WAITING_FOR_SEND("1", "待发货"),
        WAITING_FOR_RECEIVER("2", "待收货"),
        WAITING_FOR_EVALUATE("3", "待评价"),
        MALL_ORDER_CANCEL("4", "订单取消"),
        MALL_ORDER_APPLY_BACK("5", "申请退款"),
        MALL_ORDER_HAS_BACK("6", "已退款"),
        MALL_ORDER_COMPLEMENT("7", "订单完成"),
        ALL("-1", "");
        public String index;
        public String description;

        private MallOrderStatus(String index, String description) {
            this.index = index;
            this.description = description;
        }

        public static String getDescriptionByIndex(String index) {
            for (MallOrderStatus status : MallOrderStatus.values()) {
                if (index.equals(status.index)) {
                    return status.description;
                }
            }
            return "";
        }
    }


    /**
     * 通知刷新购物车列表
     */
    public static void notifyRefreshShoppingCarList() {
        RefreshData refreshData = RefreshData.INSTANCE;
        refreshData.setRefresh(true);
        refreshData.setAny(ShoppingCarFragment.Companion.getREFRESH_SHOPPING_CAR_FLAG());
        RxBus.getDefault().post(RefreshData.INSTANCE);
    }


    /**
     * 刷新MainMineFragment中的各个状态的数字
     */
    public static void notifyRefreshOrderNum() {
//        RefreshData refreshData = RefreshData.INSTANCE;
//        refreshData.setRefresh(true);
//        refreshData.setAny(MainMineFragment.Companion.getREFRESH_QBADGE_VIEW_FLAG());
//        RxBus.getDefault().post(RefreshData.INSTANCE);
    }

    /**
     * 通知刷新订单列表
     */
    public static void notifyRefreshOrderList() {
        RefreshData refreshData = RefreshData.INSTANCE;
        refreshData.setRefresh(true);
        refreshData.setAny(MainMallOrderItemFragment.Companion.getREFRESH_ORDER_FLAG());
        RxBus.getDefault().post(RefreshData.INSTANCE);
    }

    public static void notifyOnPay() {
        RefreshData refreshData = RefreshData.INSTANCE;
        refreshData.setRefresh(true);
        refreshData.setAny(OrderSubmitFragment.Companion.getPAY_MALL_ORDER_FLAG());
        RxBus.getDefault().post(RefreshData.INSTANCE);
    }

    /**
     * 订单取消
     */
    public static void onOrderCancel() {
        notifyRefreshOrderList();
    }

    /**
     * 订单支付
     *
     * @param context    上下文
     * @param jsonObject 支付json
     * @param payType    支付方式
     * @param handler    支付宝处理Handler
     */
    public static void onOrderPaySuccess(Context context, JSONObject jsonObject, String payType, String oid, Handler handler) {
        PreferenceUtils.setPrefString(context, "order_id", oid);
        switch (payType) {
            case SelectPayTypeView.WX_PAY:
                PreferenceUtils.setPrefBoolean(context, "is_mall_order", true);
                PayManager.wxPay(context, new Gson().fromJson(jsonObject.optJSONObject("result").toString(), WXPayBean.class));
                break;
            case SelectPayTypeView.ALI_PAY:
                PayManager.aliPay(context, handler, jsonObject.optString("result"));
                break;
            case SelectPayTypeView.ACCOUNT_PAY:
                Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                notifyRefreshOrderList();
                break;
            default:
                break;
        }
    }

    public static void showPayDialog(Context mContext, final OnConfirmClickListener onConfirmClickListener) {
        final BottomSheetDialog bottomPayDialog = new BottomSheetDialog(mContext);
        final SelectPayTypeView contentView = new SelectPayTypeView(mContext);
        contentView.removeAllViews();
        contentView.setShowAccount(true);
        contentView.init();
        contentView.setShowConfirmView(View.VISIBLE);
        contentView.setOnConfirmClickListener(new SelectPayTypeView.OnConfirmClickListener() {
            @Override
            public void onConfirmClick() {
                bottomPayDialog.dismiss();
                if (onConfirmClickListener != null) {
                    onConfirmClickListener.onConfirmClick(contentView.getPayType());
                }
            }
        });
        bottomPayDialog.setContentView(contentView);
        bottomPayDialog.show();
    }


    /**
     * 收货成功
     */
    public static void onOrderReceiver() {
        MallOrderPresent.notifyRefreshOrderList();
    }

    public interface OnConfirmClickListener {
        void onConfirmClick(String payType);
    }

}
