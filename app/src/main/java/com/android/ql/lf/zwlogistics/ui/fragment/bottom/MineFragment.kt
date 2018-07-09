package com.android.ql.lf.zwlogistics.ui.fragment.bottom

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.carapp.utils.doClickWithUseStatusEnd
import com.android.ql.lf.carapp.utils.doClickWithUserStatusStart
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.UserInfo
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
import com.android.ql.lf.zwlogistics.utils.GlideManager
import com.android.ql.lf.zwlogistics.utils.RxBus
import kotlinx.android.synthetic.main.fragment_mine_layout.*

class MineFragment : BaseNetWorkingFragment() {

    companion object {
        const val USER_INFO_FLAG = "user_info_flag"
    }


    private val modifyInfoSubscription by lazy {
        RxBus.getDefault().toObservable(String::class.java).subscribe {
            if (it == "modify info success") {
                loadUserInfo()
            }
        }
    }

    override fun getLayoutId() = R.layout.fragment_mine_layout

    override fun initView(view: View?) {
        val param = mTvMainMineTitle.layoutParams as ViewGroup.MarginLayoutParams
        param.topMargin = (mContext as MainActivity).statusHeight

        registerLoginSuccessBus()
        registerLogoutSuccessBus()
        modifyInfoSubscription

        loadUserInfo()


        mLlMineUserInfoContainer.doClickWithUserStatusStart(USER_INFO_FLAG) {
            FragmentContainerActivity.from(mContext).setTitle("我的信息").setClazz(MineInfoFragment::class.java).setNeedNetWorking(true).start()
        }

        mTvMineUserInfo.doClickWithUserStatusStart(USER_INFO_FLAG) {
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


    override fun onLoginSuccess(userInfo: UserInfo?) {
        super.onLoginSuccess(userInfo)
        loadUserInfo()
        when (UserInfo.loginToken) {
            USER_INFO_FLAG -> {
                mTvMineUserInfo.doClickWithUseStatusEnd()
            }
        }
    }

    private fun loadUserInfo() {
        if (UserInfo.getInstance().isLogin) {
            if (TextUtils.isEmpty(UserInfo.getInstance().user_pic)){
                mMineUserFace.setImageResource(R.drawable.icon_default_face)
            }else {
                GlideManager.loadFaceCircleImage(mContext, UserInfo.getInstance().user_pic, mMineUserFace)
            }
            mMineUserNickName.text = UserInfo.getInstance().user_nickname
            mMineUserYFaFangShouYi.text = "￥${UserInfo.getInstance().user_y_sum}"
            mMineUserWFaFangShouYi.text = "￥${UserInfo.getInstance().user_w_sum}"
        }
    }

    override fun onLogoutSuccess(logout: String?) {
        super.onLogoutSuccess(logout)
        mMineUserFace.setImageResource(R.drawable.icon_default_face)
        mMineUserNickName.text = "点击登录"
        mMineUserYFaFangShouYi.text = "￥0.00"
        mMineUserWFaFangShouYi.text = "￥0.00"
    }

    override fun onDestroyView() {
        unsubscribe(modifyInfoSubscription)
        super.onDestroyView()
    }

}