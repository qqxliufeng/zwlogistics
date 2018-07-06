package com.android.ql.lf.zwlogistics.ui.fragment.bottom

import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.activity.MainActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.LoginFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.MineCustomServiceFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.MineInfoFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.MineInviteFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.car.MineCarListFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MineDriverInfoEmptyFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MineDriverInfoForComplementAndAuthingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MineDriverInfoForFailedFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MinePersonAuthFragment
import kotlinx.android.synthetic.main.fragment_mine_layout.*

class MineFragment:BaseNetWorkingFragment(){

    override fun getLayoutId() = R.layout.fragment_mine_layout

    override fun initView(view: View?) {
        val param = mTvMainMineTitle.layoutParams as ViewGroup.MarginLayoutParams
        param.topMargin = (mContext as MainActivity).statusHeight

        mLlMineUserInfoContainer.setOnClickListener {
            LoginFragment.startLogin(mContext)
        }
        mTvMineUserInfo.setOnClickListener {
            FragmentContainerActivity.from(mContext).setTitle("我的信息").setClazz(MineInfoFragment::class.java).setNeedNetWorking(true).start()
        }

        mTvMineAuthInfo.setOnClickListener {
            FragmentContainerActivity.from(mContext).setTitle("我的认证").setNeedNetWorking(true).setClazz(MineDriverInfoEmptyFragment::class.java).start()
        }

        mMineCarList.setOnClickListener {
            FragmentContainerActivity.from(mContext).setTitle("我的车辆").setClazz(MineCarListFragment::class.java).setNeedNetWorking(true).start()
        }

        mTvMineInviteCode.setOnClickListener {
            FragmentContainerActivity.from(mContext).setTitle("我的邀请码").setClazz(MineInviteFragment::class.java).setNeedNetWorking(true).start()
        }

        mTvMineKefu.setOnClickListener {
            FragmentContainerActivity.from(mContext).setTitle("我的客服").setClazz(MineCustomServiceFragment::class.java).setNeedNetWorking(false).start()
        }
    }

}