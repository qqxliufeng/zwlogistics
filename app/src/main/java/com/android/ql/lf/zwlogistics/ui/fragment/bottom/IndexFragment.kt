package com.android.ql.lf.zwlogistics.ui.fragment.bottom

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

import android.view.ViewGroup
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.*
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.activity.MainActivity
import com.android.ql.lf.zwlogistics.ui.activity.SelectAddressActivity
import com.android.ql.lf.zwlogistics.ui.adapter.OrderItemAdapter
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseRecyclerViewFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.LoginFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.car.SelectMultiTypeFragment
import com.android.ql.lf.zwlogistics.ui.fragment.order.OrderInfoFragment
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.android.ql.lf.zwlogistics.utils.VersionHelp
import com.android.ql.lf.zwlogistics.utils.alert
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_index_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONArray
import org.json.JSONObject


class IndexFragment : BaseRecyclerViewFragment<OrderBean>() {

    companion object {
        const val INDEX_ITEM_INFO_FLAG = "index_item_info_flag"
    }

    private var sourceAddress: SelectAddressActivity.SelectAddressItemBean? = null

    private var desAddress: SelectAddressActivity.SelectAddressItemBean? = null

    private var carTypeList: ArrayList<CarParamBean>? = null

    private var lengthList: ArrayList<CarParamBean>? = null

    private var isLoad = false

    private var currentItem: OrderBean? = null

    private val postSelectOrderBean by lazy {
        PostSelectOrderBean()
    }

    override fun getLayoutId() = R.layout.fragment_index_layout


    override fun createAdapter() = OrderItemAdapter(R.layout.adapter_index_order_item_layout, mArrayList)

    private val selectMultiTypeFragment by lazy {
        SelectMultiTypeFragment()
    }

    override fun initView(view: View?) {
        super.initView(view)
        registerLoginSuccessBus()
        val param = mTvMainIndexTitle.layoutParams as ViewGroup.MarginLayoutParams
        param.topMargin = (mContext as MainActivity).statusHeight

        mLlIndexOrderSourceAddressContainer.setOnClickListener {
            mCtvIndexOrderSourceAddress.isChecked = true
            SelectAddressActivity.startAddressActivityForResult(this, 1)
        }

        mLlIndexOrderDesAddressContainer.setOnClickListener {
            mCtvIndexOrderDesAddress.isChecked = true
            SelectAddressActivity.startAddressActivityForResult(this, 1)
        }

        mLlIndexOrderCarTypeContainer.setOnClickListener {
            if (carTypeList != null && lengthList != null) {
                mCtvIndexOrderCarType.isChecked = true
                selectMultiTypeFragment.myShow(childFragmentManager, "select_multi_type_dialog", { lengthList, typeList ->
                    mCtvIndexOrderCarType.isChecked = false
                    if (lengthList!!.isEmpty()) {
                        return@myShow
                    }
                    if (lengthList!![0].type == 0) {
                        postSelectOrderBean.lengthParams = "0"
                        mCtvIndexOrderCarType.text = "不限车长"
                    } else {
                        val lengthParams = StringBuilder()
                        lengthList.forEach {
                            lengthParams.append(it.name).append(",")
                        }
                        postSelectOrderBean.lengthParams = lengthParams.toString()
                        mCtvIndexOrderCarType.text = postSelectOrderBean.lengthParams
                    }

                    if (typeList!![0].type == 0) {
                        postSelectOrderBean.carTypeParams = "0"
                        mCtvIndexOrderCarType.text = "${mCtvIndexOrderCarType.text} 不限车型"
                    } else {
                        val carTypeParams = StringBuilder()
                        typeList.forEach {
                            carTypeParams.append(it.name).append(",")
                        }
                        postSelectOrderBean.carTypeParams = carTypeParams.toString()
                        mCtvIndexOrderCarType.text = "${mCtvIndexOrderCarType.text} ${postSelectOrderBean.carTypeParams}"
                    }

                    Log.e("TAG", mCtvIndexOrderCarType.text.toString())

                    //重新加载数据
                    onPostRefresh()
                }) {
                    mCtvIndexOrderCarType.isChecked = false
                }
            }
        }

        mPresent.getDataByPost(0x1, RequestParamsHelper.getVersionUpdate())
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, RequestParamsHelper.getIndexListParam(postSelectOrderBean, page = currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, RequestParamsHelper.getIndexListParam(postSelectOrderBean, page = currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            processList(result as String, OrderBean::class.java)
            if (!isLoad) {
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    onHandleSuccess(requestID, (check.obj as JSONObject))
                }
            }
        } else if (requestID == 0x1) {
            try {
                val check = checkResultCode(result)
                if (check != null && check.code == SUCCESS_CODE) {
                    val dataJson = (check.obj as JSONObject).optJSONObject(RESULT_OBJECT)
                    val versionCode = dataJson.optString("appApkVer")
                    if (versionCode.toInt() > VersionHelp.currentVersionCode(mContext)) {
                        VersionInfo.getInstance().versionCode = versionCode.toInt()
                        VersionInfo.getInstance().content = dataJson.optString("appApkIntro")
                        VersionInfo.getInstance().downUrl = dataJson.optString("appApk")
                        alert("发现新版本", VersionInfo.getInstance().content, "立即下载", "暂不下载", { _, _ ->
                            toast("正在下载……")
                            VersionHelp.downNewVersion(mContext, Uri.parse(VersionInfo.getInstance().downUrl), "${System.currentTimeMillis()}")
                        }, null)
                    }
                }
            } catch (e: Exception) {
            }
        }
    }


    override fun onHandleSuccess(requestID: Int, jsonObject: JSONObject?) {
        if (jsonObject != null) {
            val modelJSONArray = jsonObject.optJSONArray("model")
            if (modelJSONArray != null && modelJSONArray.length() > 0) {
                carTypeList = arrayListOf()
                carTypeList?.addAll(parseJsonArray(modelJSONArray))
                if (!carTypeList!!.isEmpty()) {
                    selectMultiTypeFragment.setCarTypeDataSource(carTypeList!!)
                }
            }
            val lengthJSONArray = jsonObject.optJSONArray("length")
            if (lengthJSONArray != null && lengthJSONArray.length() > 0) {
                lengthList = arrayListOf()
                lengthList?.addAll(parseJsonArray(lengthJSONArray))
                if (!lengthList!!.isEmpty()) {
                    selectMultiTypeFragment.setLengthDataSource(lengthList!!)
                }
            }
            isLoad = true
        }
    }

    private fun parseJsonArray(jsonArray: JSONArray): ArrayList<CarParamBean> {
        val tempList = arrayListOf<CarParamBean>()
        (0 until jsonArray.length()).forEach {
            tempList.add(Gson().fromJson(jsonArray.optJSONObject(it).toString(), CarParamBean::class.java))
        }
        return tempList
    }


    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.TRANSPARENT))
        return itemDecoration
    }


    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        currentItem = mArrayList[position]
        if (UserInfo.getInstance().isLogin) {
            enterInfo(currentItem!!)
        } else {
            UserInfo.loginToken = INDEX_ITEM_INFO_FLAG
            LoginFragment.startLogin(mContext)
        }
    }


    override fun onLoginSuccess(userInfo: UserInfo?) {
        super.onLoginSuccess(userInfo)
        if (UserInfo.loginToken == INDEX_ITEM_INFO_FLAG) {
            enterInfo(currentItem!!)
        }
    }


    private fun enterInfo(orderBean: OrderBean) {
        FragmentContainerActivity.from(mContext).setTitle("我要竞标").setExtraBundle(bundleOf(Pair("oid", orderBean.need_id))).setClazz(OrderInfoFragment::class.java).setNeedNetWorking(true).start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (data != null) {
                val addressBean = data.getParcelableExtra<SelectAddressActivity.SelectAddressItemBean>(SelectAddressActivity.REQUEST_DATA_FALG)
                if (mCtvIndexOrderSourceAddress.isChecked) {
                    sourceAddress = addressBean
                    mCtvIndexOrderSourceAddress.text = sourceAddress?.name
                    postSelectOrderBean.srcAddress = "${sourceAddress?.id},"
                    onPostRefresh()
                }
                if (mCtvIndexOrderDesAddress.isChecked) {
                    desAddress = addressBean
                    mCtvIndexOrderDesAddress.text = desAddress?.name
                    postSelectOrderBean.desAddress = "${desAddress?.id},"
                    onPostRefresh()
                }
            }
            mCtvIndexOrderSourceAddress.isChecked = false
            mCtvIndexOrderDesAddress.isChecked = false
        }
    }

}