package com.android.ql.lf.carapp.present;


import com.android.ql.lf.carapp.component.ApiServer;
import com.android.ql.lf.carapp.utils.ApiParams;
import com.android.ql.lf.carapp.utils.Constants;
import com.android.ql.lf.electronicbusiness.interfaces.INetDataPresenter;

import java.lang.ref.SoftReference;
import java.util.List;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * @author Administrator
 * @date 2017/10/16 0016
 */

public class GetDataFromNetPresent {

    private ApiServer apiServer;
    private SoftReference<INetDataPresenter> iNetDataPresenter;

    private Observable<String> observable;

    public GetDataFromNetPresent(ApiServer apiServer) {
        this.apiServer = apiServer;
    }

    public GetDataFromNetPresent(ApiServer apiServer, SoftReference<INetDataPresenter> iNetDataPresenter) {
        this.apiServer = apiServer;
        this.iNetDataPresenter = iNetDataPresenter;
    }

    public void setNetDataPresenter(INetDataPresenter iNetDataPresenter) {
        this.iNetDataPresenter = new SoftReference<>(iNetDataPresenter);
    }

    /**
     * 带参的POST
     *
     * @param requestId 请求ID
     * @param postfix1  请求路径1
     * @param postfix2  请求路径2
     * @param params    请求参数
     */
    public void getDataByPost(final int requestId, String postfix1, String postfix2, ApiParams params) {
        checkObservable();
        observable = apiServer.getDataByPost(postfix1, postfix2, params, Constants.md5Token());
        parseData(requestId);
    }

    /**
     * 不带参的POST
     *
     * @param requestId 请求ID
     * @param postfix1  请求路径1
     * @param postfix2  请求路径2
     */
    public void getDataByPost(final int requestId, String postfix1, String postfix2) {
        checkObservable();
        observable = apiServer.getDataByPost(postfix1, postfix2, Constants.md5Token());
        parseData(requestId);
    }

    public void getDataByGet(final int requestId, String postfix1, String postfix2, ApiParams params) {
        checkObservable();
        observable = apiServer.getDataByGet(postfix1, postfix2, params);
        parseData(requestId);
    }

    public void uploadFile(final int requestId, String postfix1, String postfix2, List<MultipartBody.Part> partList) {
        checkObservable();
        observable = apiServer.uploadFiles(postfix1, postfix2, partList);
        parseData(requestId);
    }

    private void checkObservable() {
        if (observable != null) {
            observable.unsubscribeOn(Schedulers.io());
            observable = null;
        }
    }

    private void parseData(final int requestId) {
        if (observable != null) {
            observable.subscribeOn(Schedulers.io())
                    .doOnSubscribe(new StartAction(requestId))
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {

                        @Override
                        public void onCompleted() {
                            if (iNetDataPresenter != null && iNetDataPresenter.get() != null) {
                                iNetDataPresenter.get().onRequestEnd(requestId);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (iNetDataPresenter != null && iNetDataPresenter.get() != null) {
                                iNetDataPresenter.get().onRequestFail(requestId, e);
                                iNetDataPresenter.get().onRequestEnd(requestId);
                            }
                        }

                        @Override
                        public void onNext(String s) {
                            if (iNetDataPresenter != null && iNetDataPresenter.get() != null) {
                                iNetDataPresenter.get().onRequestSuccess(requestId, s);
                            }
                        }
                    });
        }
    }

    public void unSubscription() {
        if (observable != null) {
            observable.unsubscribeOn(Schedulers.io());
            observable = null;
            apiServer = null;
            iNetDataPresenter = null;
        }
    }

    class StartAction implements Action0 {

        private int requestId;

        StartAction(int requestId) {
            this.requestId = requestId;
        }

        @Override
        public void call() {
            if (iNetDataPresenter != null && iNetDataPresenter.get() != null) {
                iNetDataPresenter.get().onRequestStart(requestId);
            }
        }
    }

}
