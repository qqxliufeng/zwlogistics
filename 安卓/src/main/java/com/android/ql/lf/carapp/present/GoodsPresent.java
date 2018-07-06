package com.android.ql.lf.carapp.present;

import com.android.ql.lf.carapp.data.RefreshData;
import com.android.ql.lf.carapp.ui.fragments.bottom.MainMallFragment;
import com.android.ql.lf.carapp.utils.RxBus;

/**
 * Created by lf on 18.3.29.
 *
 * @author lf on 18.3.29
 */

public class GoodsPresent {

    /**
     * 通知刷新商品状态
     */
    public static void notifyRefreshGoodsStatus() {
        post(MainMallFragment.Companion.getREFRESH_COLLECTION_STATUS_FLAG());
    }

    /**
     * 通知刷新收藏商品数量
     */
    public static void notifyGoodsCollectionNum() {
        post(MainMallFragment.Companion.getREFRESH_COLLECTION_STATUS_FLAG());
    }

    /**
     * 通知刷新收藏店铺数量
     */
    public static void notifyStoreCollectionNum() {
        post(MainMallFragment.Companion.getREFRESH_COLLECTION_STATUS_FLAG());
    }

    public static void post(Object object){
        RefreshData refreshData = RefreshData.INSTANCE;
        refreshData.setRefresh(true);
        refreshData.setAny(object);
        RxBus.getDefault().post(RefreshData.INSTANCE);
    }
}
