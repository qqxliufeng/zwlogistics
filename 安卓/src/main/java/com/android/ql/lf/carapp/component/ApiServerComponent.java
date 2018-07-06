package com.android.ql.lf.carapp.component;

import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity;
import com.android.ql.lf.carapp.ui.activities.SelectAddressActivity;
import com.android.ql.lf.carapp.ui.activities.SplashActivity;
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment;

import dagger.Component;

/**
 * @author Administrator
 * @date 2017/10/16 0016
 */

@ApiServerScope
@Component(modules = {ApiServerModule.class}, dependencies = AppComponent.class)
public interface ApiServerComponent {

    void inject(FragmentContainerActivity activity);

    void inject(SelectAddressActivity selectAddressActivity);

    void inject(BaseNetWorkingFragment baseNetWorkingFragment);

    void inject(SplashActivity splashActivity);

}
