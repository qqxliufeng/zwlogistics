package com.android.ql.lf.zwlogistics.component;


import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity;
import com.android.ql.lf.zwlogistics.ui.activity.SelectAddressActivity;
import com.android.ql.lf.zwlogistics.ui.activity.SplashActivity;
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment;

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
//
    void inject(BaseNetWorkingFragment baseNetWorkingFragment);
//
    void inject(SplashActivity splashActivity);

}
