package com.android.ql.lf.carapp.ui.fragments.user

import android.net.Uri
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.data.VersionInfo
import com.android.ql.lf.carapp.present.UserPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.ui.fragments.WebViewContentFragment
import com.android.ql.lf.carapp.ui.fragments.mall.address.AddressManagerFragment
import com.android.ql.lf.carapp.utils.*
import kotlinx.android.synthetic.main.fragment_setting_layout.*
import org.jetbrains.anko.bundleOf

/**
 * Created by liufeng on 2018/2/3.
 */
class SettingFragment : BaseNetWorkingFragment() {

    private val userPresent by lazy {
        UserPresent()
    }

    override fun getLayoutId() = R.layout.fragment_setting_layout

    override fun initView(view: View?) {
        val packageInfo = mContext.packageManager.getPackageInfo(mContext.packageName, 0)
        val versionName = packageInfo.versionName
        mVersionName.text = "V$versionName"
        if (packageInfo.versionCode < VersionInfo.getInstance().versionCode) {
            mTvNewVersionNotify.visibility = View.VISIBLE
            mVersionName.text = "V${VersionInfo.getInstance().versionCode}"
        } else {
            mTvNewVersionNotify.visibility = View.GONE
        }
        mRlVersionUpContainer.setOnClickListener {
            if (!TextUtils.isEmpty(VersionInfo.getInstance().downUrl)) {
                VersionHelp.downNewVersion(mContext.applicationContext, Uri.parse(VersionInfo.getInstance().downUrl), "${System.currentTimeMillis()}")
            }
        }
        val cacheSize = CacheDataManager.getTotalCacheSize(mContext)
        mCacheSize.text = "$cacheSize"
        mCacheSizeContainer.setOnClickListener {
            CacheDataManager.clearAllCache(mContext)
            mCacheSize.text = "暂无缓存"
        }
        mBtLogout.isEnabled = UserInfo.getInstance().isLogin
        mBtLogout.setOnClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setNegativeButton("否", null)
            builder.setPositiveButton("是") { _, _ ->
                userPresent.onLogout()
                finish()
            }
            builder.setMessage("是否要退出登录？")
            builder.create().show()
        }
        mTvSettingAddressManager.setOnClickListener {
            FragmentContainerActivity.from(mContext).setClazz(AddressManagerFragment::class.java).setNeedNetWorking(true).setTitle("收货地址管理").start()
        }
        mTvSettingAboutUs.setOnClickListener {
            FragmentContainerActivity.from(mContext)
                    .setClazz(WebViewContentFragment::class.java)
                    .setTitle(mTvSettingAboutUs.text.toString())
                    .setExtraBundle(bundleOf(Pair(WebViewContentFragment.PATH_FLAG, RequestParamsHelper.ACT_ABOUT_URL)))
                    .setNeedNetWorking(true)
                    .start()
        }
        mTvSettingHelp.setOnClickListener {
            FragmentContainerActivity.from(mContext)
                    .setClazz(WebViewContentFragment::class.java)
                    .setTitle(mTvSettingHelp.text.toString())
                    .setExtraBundle(bundleOf(Pair(WebViewContentFragment.PATH_FLAG, RequestParamsHelper.ACT_HELP_URL)))
                    .setNeedNetWorking(true)
                    .start()
        }
        mTvSettingProtocol.setOnClickListener {
            FragmentContainerActivity.from(mContext)
                    .setClazz(WebViewContentFragment::class.java)
                    .setTitle(mTvSettingProtocol.text.toString())
                    .setExtraBundle(bundleOf(Pair(WebViewContentFragment.PATH_FLAG, RequestParamsHelper.ACT_PROTOCOL_URL)))
                    .setNeedNetWorking(true)
                    .start()
        }
    }
}