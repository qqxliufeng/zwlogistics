package com.android.ql.lf.carapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.android.ql.lf.carapp.R;
import com.android.ql.lf.carapp.application.CarApplication;
import com.android.ql.lf.carapp.component.ApiServerModule;
import com.android.ql.lf.carapp.component.DaggerApiServerComponent;
import com.android.ql.lf.carapp.present.GetDataFromNetPresent;
import com.android.ql.lf.carapp.utils.RxBus;

import java.lang.reflect.Method;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/17 0017.
 *
 * @author lf
 */

public class FragmentContainerActivity extends BaseActivity {

    public static final String EXTRA_INFO_FLAG = "extra_info_flag";

    private IntentExtraInfo extraInfo;

    @BindView(R.id.id_tl_activity_fragment_container)
    Toolbar mToolbar;
    @BindView(R.id.id_ll_tl_container)
    LinearLayout ll_container;

    @Inject
    GetDataFromNetPresent present;

    private int statusBarColor = Color.WHITE;
    public int actionBarHeight = 0;


    public GetDataFromNetPresent getPresent() {
        return present;
    }

    private void parseExtraInfo() {
        extraInfo = getIntent().getParcelableExtra(EXTRA_INFO_FLAG);
        if (extraInfo == null) {
            throw new NullPointerException("extra_info is null");
        }
        if (extraInfo.isNeedNetWorking()) {
            DaggerApiServerComponent.builder().apiServerModule(new ApiServerModule()).appComponent(CarApplication.getInstance().getAppComponent()).build().inject(this);
        }
        ll_container.setVisibility(extraInfo.isHiddenToolBar ? View.GONE : View.VISIBLE);
        if (!extraInfo.isHiddenToolBar) {
            mToolbar.setTitle(extraInfo.getTitle());
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        initFragment();
    }

    private void initFragment() {
        Fragment fragment;
        Method method;
        try {
            if (extraInfo.extraBundle != null) {
                method = extraInfo.clazz.getMethod("newInstance", Bundle.class);
                fragment = (Fragment) method.invoke(null, extraInfo.extraBundle);
            } else {
                method = extraInfo.clazz.getMethod("newInstance");
                fragment = (Fragment) method.invoke(null);
            }
        } catch (NoSuchMethodException e) {
            try {
                fragment = (Fragment) extraInfo.clazz.newInstance();
                fragment.setArguments(extraInfo.extraBundle);
            } catch (Exception e1) {
                fragment = null;
            }
        } catch (Exception e) {
            fragment = null;
        }
        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.id_fl_fragment_container, fragment);
            transaction.commit();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_fragment_container_layout;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            intent.putExtra("requestCode", -1);
            RxBus.getDefault().post(intent);
        }
    }

    @Override
    public void initView() {
        ll_container.setPadding(0, getStatusHeight(), 0, 0);
        statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary);
        parseExtraInfo();
    }

    public void setToolBarBackgroundColor(int color) {
        statusBarColor = color;
        ll_container.setBackgroundColor(color);
        setStatusBarColor();
    }

    @Override
    public int getStatusBarColor() {
        return statusBarColor;
    }

    public void setStatusBarLightColor(boolean isLight) {
        if (isLight) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public static void startFragmentContainerActivity(Context context, IntentExtraInfo intentExtraInfo) {
        Intent intent = new Intent(context, FragmentContainerActivity.class);
        intent.putExtra(FragmentContainerActivity.EXTRA_INFO_FLAG, intentExtraInfo);
        context.startActivity(intent);
    }

    public static <T> void startFragmentContainerActivity(Context context, String title, boolean isNetWorking, boolean isHiddenBar, Class<T> clazz) {
        IntentExtraInfo<T> intentExtraInfo = new IntentExtraInfo<>();
        intentExtraInfo.isHiddenToolBar = isHiddenBar;
        intentExtraInfo.isNeedNetWorking = isNetWorking;
        intentExtraInfo.title = title;
        intentExtraInfo.clazz = clazz;
        startFragmentContainerActivity(context, intentExtraInfo);
    }

    public static <T> void startFragmentContainerActivity(Context context, String title, Class<T> clazz) {
        IntentExtraInfo<T> intentExtraInfo = new IntentExtraInfo<>();
        intentExtraInfo.isHiddenToolBar = false;
        intentExtraInfo.isNeedNetWorking = true;
        intentExtraInfo.title = title;
        intentExtraInfo.clazz = clazz;
        startFragmentContainerActivity(context, intentExtraInfo);
    }

    public static <T> void startFragmentContainerActivity(Context context, String title, boolean isNetWorking, boolean isHiddenBar, Bundle bundle, Class<T> clazz) {
        IntentExtraInfo<T> intentExtraInfo = new IntentExtraInfo<>();
        intentExtraInfo.isHiddenToolBar = isHiddenBar;
        intentExtraInfo.isNeedNetWorking = isNetWorking;
        intentExtraInfo.title = title;
        intentExtraInfo.extraBundle = bundle;
        intentExtraInfo.clazz = clazz;
        startFragmentContainerActivity(context, intentExtraInfo);
    }

    public static IntentExtraInfo from(Context context) {
        return new IntentExtraInfo(context);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (present != null) {
            present = null;
        }
        super.onDestroy();
    }

    public static class IntentExtraInfo<T> implements Parcelable {

        private Context context = null;
        private Bundle extraBundle = null;
        private String title = "";
        private boolean isNeedNetWorking = false;
        private boolean isHiddenToolBar = false;

        private Class<T> clazz = null;

        public Class<T> getClazz() {
            return clazz;
        }

        public IntentExtraInfo setClazz(Class<T> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Bundle getExtraBundle() {
            return extraBundle;
        }

        public IntentExtraInfo setExtraBundle(Bundle extraBundle) {
            this.extraBundle = extraBundle;
            return this;
        }

        public String getTitle() {
            return title;
        }

        public IntentExtraInfo setTitle(String title) {
            this.title = title;
            return this;
        }

        public boolean isNeedNetWorking() {
            return isNeedNetWorking;
        }

        public IntentExtraInfo setNeedNetWorking(boolean needNetWorking) {
            isNeedNetWorking = needNetWorking;
            return this;
        }

        public boolean isHiddenToolBar() {
            return isHiddenToolBar;
        }

        public IntentExtraInfo setHiddenToolBar(boolean hiddenToolBar) {
            isHiddenToolBar = hiddenToolBar;
            return this;
        }

        public IntentExtraInfo(Context context) {
            this.context = context;
        }

        public IntentExtraInfo() {
        }

        public void start() {
            if (context == null) {
                throw new NullPointerException("Context is null");
            }
            if (clazz == null) {
                throw new NullPointerException("clazz is null");
            }
            Intent intent = new Intent(context, FragmentContainerActivity.class);
            intent.putExtra(FragmentContainerActivity.EXTRA_INFO_FLAG, this);
            context.startActivity(intent);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeBundle(this.extraBundle);
            dest.writeString(this.title);
            dest.writeByte(this.isNeedNetWorking ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isHiddenToolBar ? (byte) 1 : (byte) 0);
            dest.writeSerializable(this.clazz);
        }

        protected IntentExtraInfo(Parcel in) {
            this.extraBundle = in.readBundle();
            this.title = in.readString();
            this.isNeedNetWorking = in.readByte() != 0;
            this.isHiddenToolBar = in.readByte() != 0;
            this.clazz = (Class<T>) in.readSerializable();
        }

        public static final Creator<IntentExtraInfo> CREATOR = new Creator<IntentExtraInfo>() {
            @Override
            public IntentExtraInfo createFromParcel(Parcel source) {
                return new IntentExtraInfo(source);
            }

            @Override
            public IntentExtraInfo[] newArray(int size) {
                return new IntentExtraInfo[size];
            }
        };
    }
}
