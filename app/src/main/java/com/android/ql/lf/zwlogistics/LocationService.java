package com.android.ql.lf.zwlogistics;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.ql.lf.zwlogistics.application.MyApplication;
import com.android.ql.lf.zwlogistics.component.ApiServerModule;
import com.android.ql.lf.zwlogistics.component.DaggerApiServerComponent;
import com.android.ql.lf.zwlogistics.present.GetDataFromNetPresent;

import javax.inject.Inject;

public class LocationService extends Service {

    @Inject
    GetDataFromNetPresent present;


    @Override
    public void onCreate() {
        super.onCreate();
        if (present == null){
            DaggerApiServerComponent.builder().apiServerModule(new ApiServerModule()).appComponent(MyApplication.getInstance().getAppComponent()).build().inject(this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TAG",present.toString());
        return super.onStartCommand(intent, flags, startId);
    }
}
