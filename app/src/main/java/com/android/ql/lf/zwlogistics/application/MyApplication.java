package com.android.ql.lf.zwlogistics.application;

import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.android.ql.lf.zwlogistics.LocationService;
import com.android.ql.lf.zwlogistics.component.AppComponent;
import com.android.ql.lf.zwlogistics.component.AppModule;
import com.android.ql.lf.zwlogistics.component.DaggerAppComponent;

public class MyApplication extends MultiDexApplication {

    private AppComponent appComponent;

    public static MyApplication application;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        startService(new Intent(this,LocationService.class));
    }

    public static MyApplication getInstance() {
        return application;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
