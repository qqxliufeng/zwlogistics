package com.android.ql.lf.zwlogistics.application;

import android.support.multidex.MultiDexApplication;

import com.android.ql.lf.zwlogistics.component.AppComponent;
import com.android.ql.lf.zwlogistics.component.AppModule;
import com.android.ql.lf.zwlogistics.component.DaggerAppComponent;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends MultiDexApplication {

    private AppComponent appComponent;

    public static MyApplication application;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        JPushInterface.init(this);
    }

    public static MyApplication getInstance() {
        return application;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
