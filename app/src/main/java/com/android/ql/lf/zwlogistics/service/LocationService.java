package com.android.ql.lf.zwlogistics.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.ql.lf.zwlogistics.application.MyApplication;
import com.android.ql.lf.zwlogistics.component.ApiServerModule;
import com.android.ql.lf.zwlogistics.component.DaggerApiServerComponent;
import com.android.ql.lf.zwlogistics.data.UserInfo;
import com.android.ql.lf.zwlogistics.interfaces.INetDataPresenter;
import com.android.ql.lf.zwlogistics.present.GetDataFromNetPresent;
import com.android.ql.lf.zwlogistics.utils.Constants;
import com.android.ql.lf.zwlogistics.utils.ContextKtKt;
import com.android.ql.lf.zwlogistics.utils.PreferenceUtils;
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public class LocationService extends Service {

    @Inject
    GetDataFromNetPresent present;


    private AMapLocationClient aMapLocationClient;
    private MyLocationListener aMapLocationListener;


    @Override
    public void onCreate() {
        super.onCreate();
        if (present == null) {
            DaggerApiServerComponent.builder().apiServerModule(new ApiServerModule()).appComponent(MyApplication.getInstance().getAppComponent()).build().inject(this);
        }
        if (aMapLocationClient == null) {
            aMapLocationClient = new AMapLocationClient(getApplicationContext());
            aMapLocationListener = new MyLocationListener();
            aMapLocationListener.setPresent(present);
            aMapLocationClient.setLocationListener(aMapLocationListener);
            AMapLocationClientOption option = new AMapLocationClientOption();
            option.setInterval(1000 * 10);
            option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
            aMapLocationClient.setLocationOption(option);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        aMapLocationClient.stopLocation();
        aMapLocationClient.startLocation();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        if (aMapLocationClient!=null){
            aMapLocationClient.stopLocation();
            if (aMapLocationListener!=null) {
                aMapLocationListener.unSetPresent();
            }
            aMapLocationClient = null;
            aMapLocationListener = null;
        }
        super.onDestroy();
    }

    public static class MyLocationListener implements AMapLocationListener,INetDataPresenter {

        private GetDataFromNetPresent present;

        private String id;

        public void setPresent(GetDataFromNetPresent present) {
            id = UserInfo.getInstance().getNeedGpsOrder();
            this.present = present;
            this.present.setNetDataPresenter(this);
        }

        public void unSetPresent(){
            this.present = null;
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (present!=null && UserInfo.getInstance().isLogin() && !TextUtils.isEmpty(id)){
                present.getDataByPost(0x0,RequestParamsHelper.Companion.getPositionParams(id,aMapLocation.getLongitude(),aMapLocation.getLatitude()));
            }
        }

        @Override
        public void onRequestStart(int requestID) {

        }

        @Override
        public void onRequestFail(int requestID, @NotNull Throwable e) {

        }

        @Override
        public <T> void onRequestSuccess(int requestID, T result) {

        }

        @Override
        public void onRequestEnd(int requestID) {

        }
    }

}
