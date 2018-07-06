package com.android.ql.lf.carapp.ui.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.ql.lf.carapp.R;
import com.android.ql.lf.carapp.application.CarApplication;
import com.android.ql.lf.carapp.component.ApiServerModule;
import com.android.ql.lf.carapp.component.DaggerApiServerComponent;
import com.android.ql.lf.carapp.present.GetDataFromNetPresent;
import com.android.ql.lf.carapp.utils.RequestParamsHelper;
import com.android.ql.lf.carapp.utils.RxBus;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by lf on 2017/11/30 0030.
 *
 * @author lf on 2017/11/30 0030
 */

public class SelectAddressActivity extends BaseActivity implements TabLayout.OnTabSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    public static final int PROVINCE_MODE = 0x0;
    public static final int CITY_MODE = 0x1;
    public static final int AREA_MODE = 0x2;

    @BindView(R.id.mTlSelectAddress)
    TabLayout mTabLayout;
    @BindView(R.id.mSrlSelectAddress)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.mRvSelectAddress)
    RecyclerView mRecyclerView;
    @BindView(R.id.mTvSelectAddressClose)
    TextView mTvClose;

    private ArrayList<SelectAddressItemBean> provinceList = new ArrayList<>();
    private ArrayList<SelectAddressItemBean> cityList = new ArrayList<>();
    private ArrayList<SelectAddressItemBean> areaList = new ArrayList<>();

    @Inject
    GetDataFromNetPresent mPresent;

    private MySelectAddressAdapter adapter;

    private int currentMode = PROVINCE_MODE;

    private SelectAddressItemBean provinceItemBean;
    private SelectAddressItemBean cityItemBean;
    private SelectAddressItemBean areaItemBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerApiServerComponent.builder().apiServerModule(new ApiServerModule()).appComponent(CarApplication.getInstance().getAppComponent()).build().inject(this);
        mPresent.setNetDataPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_address_layout;
    }

    @Override
    public void initView() {
        mTvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.addTab(mTabLayout.newTab().setText("请选择"));
        mTabLayout.addOnTabSelectedListener(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.YELLOW, Color.GREEN);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        adapter = new MySelectAddressAdapter(android.R.layout.simple_list_item_1, provinceList);
        adapter.setEnableLoadMore(false);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (currentMode == PROVINCE_MODE) {
                    provinceItemBean = provinceList.get(position);
                    mTabLayout.getTabAt(0).setText(provinceItemBean.getName());
                    mPresent.getDataByPost(0x1,
                            RequestParamsHelper.Companion.getMEMBER_MODEL(),
                            RequestParamsHelper.Companion.getACT_CITY(),
                            RequestParamsHelper.Companion.getCityParam(provinceItemBean.getId()));
                } else if (currentMode == CITY_MODE) {
                    cityItemBean = cityList.get(position);
                    mTabLayout.getTabAt(1).setText(cityItemBean.getName());
                    mPresent.getDataByPost(0x2,
                            RequestParamsHelper.Companion.getMEMBER_MODEL(),
                            RequestParamsHelper.Companion.getACT_PROVINCE_CITY_AREA(),
                            RequestParamsHelper.Companion.getProvinceCityAreaParam(cityItemBean.getId()));
                } else if (currentMode == AREA_MODE) {
                    areaItemBean = areaList.get(position);
                    SelectAddressItemBean resultItemBean = new SelectAddressItemBean();
                    resultItemBean.setName(provinceItemBean.getName() + cityItemBean.getName() + areaItemBean.getName());
                    resultItemBean.setId(provinceItemBean.getId() + "," + cityItemBean.getId() + "," + areaItemBean.getId());
                    RxBus.getDefault().post(resultItemBean);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mPresent.getDataByPost(0x0,
                RequestParamsHelper.Companion.getMEMBER_MODEL(),
                RequestParamsHelper.Companion.getACT_PROVINCE(),
                RequestParamsHelper.Companion.getProvinceParam());
    }

    @Override
    public void onRequestStart(int requestID) {
        super.onRequestStart(requestID);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onRequestEnd(int requestID) {
        super.onRequestEnd(requestID);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        onRequestEnd(-1);
    }

    @Override
    public <T> void onRequestSuccess(int requestID, T result) {
        super.onRequestSuccess(requestID, result);
        switch (requestID) {
            case 0x0: //省
                try {
                    if (parseJson(provinceList, result)) {
                        currentMode = PROVINCE_MODE;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0x1: //市
                try {
                    cityList.clear();
                    if (parseJson(cityList, result)) {
                        mRecyclerView.smoothScrollToPosition(0);
                        currentMode = CITY_MODE;
                        TabLayout.Tab tab = mTabLayout.newTab();
                        mTabLayout.addTab(tab.setText("请选择"));
                        tab.select();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0x2: //区
                try {
                    areaList.clear();
                    if (parseJson(areaList, result)) {
                        mRecyclerView.smoothScrollToPosition(0);
                        currentMode = AREA_MODE;
                        TabLayout.Tab tab = mTabLayout.newTab();
                        mTabLayout.addTab(tab.setText("请选择"));
                        tab.select();
                    }
                    //如果没有第三级城市 直接返回
                    if (areaList.isEmpty()) {
                        SelectAddressItemBean resultItemBean = new SelectAddressItemBean();
                        resultItemBean.setName(provinceItemBean.getName() + cityItemBean.getName());
                        resultItemBean.setId(provinceItemBean.getId() + "," + cityItemBean.getId());
                        RxBus.getDefault().post(resultItemBean);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private <T> boolean parseJson(ArrayList<SelectAddressItemBean> list, T result) throws JSONException {
        if (result != null) {
            JSONObject resultJson = new JSONObject(result.toString());
            String code = resultJson.optString("code");
            if ("200".equals(code)) {
                JSONArray resultJsonArray = resultJson.optJSONArray("result");
                for (int i = 0; i < resultJsonArray.length(); i++) {
                    JSONObject itemJson = resultJsonArray.optJSONObject(i);
                    list.add(new Gson().fromJson(itemJson.toString(), SelectAddressItemBean.class));
                }
                adapter.setNewData(list);
                adapter.setEnableLoadMore(false);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:
                tab.setText("请选择");
                if (currentMode == AREA_MODE) {
                    mTabLayout.removeTabAt(1);
                    mTabLayout.removeTabAt(1);
                } else if (currentMode == CITY_MODE) {
                    mTabLayout.removeTabAt(1);
                }
                adapter.setNewData(provinceList);
                adapter.setEnableLoadMore(false);
                currentMode = PROVINCE_MODE;
                break;
            case 1:
                tab.setText("请选择");
                if (currentMode == AREA_MODE) {
                    mTabLayout.removeTabAt(2);
                }
                currentMode = CITY_MODE;
                adapter.setNewData(cityList);
                adapter.setEnableLoadMore(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_close, R.anim.activity_close);
    }


    static class MySelectAddressAdapter extends BaseQuickAdapter<SelectAddressItemBean, BaseViewHolder> {

        public MySelectAddressAdapter(int layoutResId, @Nullable List<SelectAddressItemBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, SelectAddressItemBean item) {
            helper.setText(android.R.id.text1, item.getName());
        }
    }

    public static class SelectAddressItemBean {

        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
