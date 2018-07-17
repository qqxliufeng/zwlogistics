package com.android.ql.lf.zwlogistics.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.ql.lf.zwlogistics.application.MyApplication;
import com.android.ql.lf.zwlogistics.component.ApiServerModule;
import com.android.ql.lf.zwlogistics.component.DaggerApiServerComponent;
import com.android.ql.lf.zwlogistics.present.GetDataFromNetPresent;

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
            option.setInterval(2000);
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

    public static class MyLocationListener implements AMapLocationListener{

        private GetDataFromNetPresent present;

        public void setPresent(GetDataFromNetPresent present) {
            this.present = present;
        }

        public void unSetPresent(){
            this.present = null;
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (present!=null){
                Log.e("TAG",present.toString());
            }
            Log.e("TAG","address--> "+aMapLocation.getAddress());
            Log.e("TAG","address--> "+aMapLocation.getLatitude());
            Log.e("TAG","address--> "+aMapLocation.getLongitude());
        }
    }

}
