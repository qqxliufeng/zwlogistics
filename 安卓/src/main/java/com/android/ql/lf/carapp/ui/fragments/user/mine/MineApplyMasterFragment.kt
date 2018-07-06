package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.text.Html
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.ProtocolBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.fragments.BaseFragment
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.utils.RequestParamsHelper
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_mine_apply_master_item_layout.*
import kotlinx.android.synthetic.main.fragment_mine_apply_master_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject
import java.lang.reflect.Field

/**
 * Created by lf on 18.2.8.
 * @author lf on 18.2.8
 */
class MineApplyMasterFragment : BaseFragment() {

    companion object {
        val TITLES = listOf("安装门店", "同城商家")
    }

    override fun getLayoutId() = R.layout.fragment_mine_apply_master_layout

    override fun initView(view: View?) {
        mVpMineApplyMaster.adapter = ApplyMasterViewPagerAdapter(childFragmentManager)
        mTlMineApplyMaster.setupWithViewPager(mVpMineApplyMaster)
        setIndicator(mTlMineApplyMaster, 30, 30)
    }

    private fun setIndicator(tabs: TabLayout, leftDip: Int, rightDip: Int) {
        val tabLayout = tabs.javaClass
        var tabStrip: Field? = null
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        tabStrip!!.isAccessible = true
        var llTab: LinearLayout? = null
        try {
            llTab = tabStrip.get(tabs) as LinearLayout
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        val left = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip.toFloat(), resources.displayMetrics).toInt()
        val right = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip.toFloat(), resources.displayMetrics).toInt()

        for (i in 0 until llTab!!.childCount) {
            val child = llTab.getChildAt(i)
            child.setPadding(0, 0, 0, 0)
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.leftMargin = left
            params.rightMargin = right
            child.layoutParams = params
            child.invalidate()
        }
    }

    class ApplyMasterViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

        override fun getItem(position: Int) = MineApplyMasterItemFragment.newInstance(bundleOf(Pair("TYPE", position + 1)))

        override fun getCount() = TITLES.size

        override fun getPageTitle(position: Int) = TITLES[position]
    }
}

class MineApplyMasterItemFragment : BaseNetWorkingFragment() {

    companion object {
        fun newInstance(bundle: Bundle): MineApplyMasterItemFragment {
            val fragment = MineApplyMasterItemFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId() = R.layout.fragment_mine_apply_master_item_layout

    override fun initView(view: View?) {
        if (arguments.getInt("TYPE", 1) == 1) { // 师傅
            when (UserInfo.getInstance().authenticationStatus) {
                0 -> {
                    mBtApplyMasterApply.text = "资料正在审核中……"
                    mBtApplyMasterApply.isEnabled = false
                }
                1 -> {
                    mBtApplyMasterApply.text = "已经成为师傅"
                    mBtApplyMasterApply.isEnabled = false
                }
                2 -> {
                    mBtApplyMasterApply.text = "资料审核失败，请重新提交……"
                    mBtApplyMasterApply.isEnabled = true
                }
                3 -> {
                    mBtApplyMasterApply.text = "申请成为师傅"
                    mBtApplyMasterApply.isEnabled = true
                }
            }
            mBtApplyMasterApply.setOnClickListener {
                FragmentContainerActivity.from(mContext).setTitle("申请成为师傅").setNeedNetWorking(true).setClazz(MineApplyMasterInfoSubmitFragment::class.java).start()
                finish()
            }
            mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PTGG, RequestParamsHelper.getPtggParam("3"))
        } else { //商家
            when (UserInfo.getInstance().authenticationSellerStatus) {
                0 -> {
                    mBtApplyMasterApply.text = "资料正在审核中……"
                    mBtApplyMasterApply.isEnabled = false
                }
                1 -> {
                    mBtApplyMasterApply.text = "已经成为商家"
                    mBtApplyMasterApply.isEnabled = false
                }
                2 -> {
                    mBtApplyMasterApply.text = "资料审核失败，请重新提交……"
                    mBtApplyMasterApply.isEnabled = true
                }
                3 -> {
                    mBtApplyMasterApply.text = "申请成为商家"
                    mBtApplyMasterApply.isEnabled = true
                }
            }
            mBtApplyMasterApply.setOnClickListener {
                FragmentContainerActivity.from(mContext).setTitle("申请成为商家").setNeedNetWorking(true).setClazz(MineApplySallerInfoSubmitFragment::class.java).start()
                finish()
            }
            mPresent.getDataByPost(0x0, RequestParamsHelper.MEMBER_MODEL, RequestParamsHelper.ACT_PTGG, RequestParamsHelper.getPtggParam("12"))
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (check != null && check.code == SUCCESS_CODE) {
            val protocolBean = Gson().fromJson((check.obj as JSONObject).optJSONObject("result").toString(), ProtocolBean::class.java)
            mTvApplyMasterInfo.text = Html.fromHtml(protocolBean.ptgg_content)
        } else {
            mTvApplyMasterInfo.text = "加载失败"
        }
    }

    override fun onRequestFail(requestID: Int, e: Throwable) {
        super.onRequestFail(requestID, e)
        mTvApplyMasterInfo.text = "加载失败"
    }

}
