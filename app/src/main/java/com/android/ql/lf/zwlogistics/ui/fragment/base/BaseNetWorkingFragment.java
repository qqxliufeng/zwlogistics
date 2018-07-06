package com.android.ql.lf.zwlogistics.ui.fragment.base;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.ql.lf.electronicbusiness.interfaces.INetDataPresenter;
import com.android.ql.lf.zwlogistics.application.MyApplication;
import com.android.ql.lf.zwlogistics.component.DaggerApiServerComponent;
import com.android.ql.lf.zwlogistics.data.BaseNetResult;
import com.android.ql.lf.zwlogistics.present.GetDataFromNetPresent;
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;



import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Administrator
 * @date 2017/10/17 0017
 */

public abstract class BaseNetWorkingFragment extends BaseFragment implements INetDataPresenter {

    public final String RESULT_CODE = "code";
    public final String RESULT_OBJECT = "result";
    public final String MSG_FLAG = "msg";
    public final String SUCCESS_CODE = "200";

    @Inject
    public GetDataFromNetPresent mPresent;

    public ProgressDialog progressDialog;

    protected Subscription logoutSubscription;


    public void registerLoginSuccessBus() {
    }

    public void registerLogoutSuccessBus() {
    }

//    public void onLoginSuccess(UserInfo userInfo) {
//    }

    public void onLogoutSuccess(String logout) {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentContainerActivity) {
            if (getParentFragment() == null) {
                GetDataFromNetPresent present = ((FragmentContainerActivity) context).getPresent();
                if (present != null) {
                    this.mPresent = present;
                }else {
                    DaggerApiServerComponent.builder().appComponent(MyApplication.getInstance().getAppComponent()).build().inject(this);
                }
            } else {
                DaggerApiServerComponent.builder().appComponent(MyApplication.getInstance().getAppComponent()).build().inject(this);
            }
        } else {
            DaggerApiServerComponent.builder().appComponent(MyApplication.getInstance().getAppComponent()).build().inject(this);
        }
        if (mPresent != null) {
            this.mPresent.setNetDataPresenter(this);
        }
    }

    public void getFastProgressDialog(String message) {
        progressDialog = ProgressDialog.show(mContext, null, message);
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
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    public <T> BaseNetResult checkResultCode(T json) {
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json.toString());
                return new BaseNetResult(jsonObject.optString(RESULT_CODE), jsonObject);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        unsubscribe(logoutSubscription);
        super.onDestroyView();
        if (mPresent != null) {
            mPresent.unSubscription();
            mPresent = null;
        }
    }

}
