package com.android.ql.lf.zwlogistics.ui.fragment.bottom

import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.carapp.utils.doClickWithUseStatusEnd
import com.android.ql.lf.carapp.utils.doClickWithUserStatusStart
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.data.UserInfo
import com.android.ql.lf.zwlogistics.present.UserPresent
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.activity.MainActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.MineCustomServiceFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.MineInfoFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.MineInviteFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.car.MineCarListFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MineDriverInfoEmptyFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MineDriverInfoForComplementAndAuthingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MineDriverInfoForFailedFragment
import com.android.ql.lf.zwlogistics.utils.GlideManager
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.android.ql.lf.zwlogistics.utils.RxBus
import kotlinx.android.synthetic.main.fragment_mine_layout.*
import org.json.JSONObject

class MineFragment : BaseNetWorkingFragment() {

    companion object {
        const val USER_INFO_FLAG = "user_info_flag"
        const val AUTH_PERSON_FLAG = "auth_person_flag"
        const val AUTH_CAR_FLAG = "auth_car_flag"
        const val INVITE_CODE_FLAG = "invite_code_flag"
        const val KE_FU_FLAG = "ke_fu_flag"
    }

    private val userPresent by lazy {
        UserPresent()
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
        mSrfMine.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        mSrfMine.setOnRefreshListener {
            if (UserInfo.getInstance().isLogin) {
                mPresent.getDataByPost(0x0, RequestParamsHelper.getPersonalParam(UserInfo.getInstance().user_id))
            } else {
                if (UserInfo.isCacheUserId(mContext)) {
                    mPresent.getDataByPost(0x0, RequestParamsHelper.getPersonalParam(UserInfo.getUserIdFromCache(mContext)))
                } else {
                    mSrfMine.post {
                        mSrfMine.isRefreshing = false
                    }
                }
            }
        }

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

        mTvMineAuthInfo.doClickWithUserStatusStart(AUTH_PERSON_FLAG) {
            when(UserInfo.getInstance().user_is_rank.toInt()){
                UserInfo.UserInfoAuthStatus.WEI_SHENG_HE.statusCode->{
                    FragmentContainerActivity.from(mContext).setTitle("我的认证").setNeedNetWorking(true).setClazz(MineDriverInfoEmptyFragment::class.java).start()
                }
                UserInfo.UserInfoAuthStatus.SHENG_HE_ZHONG.statusCode->{
                    FragmentContainerActivity.from(mContext).setTitle("我的认证").setNeedNetWorking(true).setClazz(MineDriverInfoForComplementAndAuthingFragment::class.java).start()
                }
                UserInfo.UserInfoAuthStatus.SHENG_HE_TONG_GUO.statusCode->{
                    FragmentContainerActivity.from(mContext).setTitle("我的认证").setNeedNetWorking(true).setClazz(MineDriverInfoForComplementAndAuthingFragment::class.java).start()
                }
                UserInfo.UserInfoAuthStatus.SHENG_HE_SHI_BAI.statusCode->{
                    FragmentContainerActivity.from(mContext).setTitle("我的认证").setNeedNetWorking(true).setClazz(MineDriverInfoForFailedFragment::class.java).start()
                }
            }
        }

        mMineCarList.doClickWithUserStatusStart(AUTH_CAR_FLAG) {
            when(UserInfo.getInstance().user_is_rank.toInt()){
                UserInfo.UserInfoAuthStatus.WEI_SHENG_HE.statusCode->{
                    Snackbar.make(mSrfMine,"您还未认证成为当司机，请先认证成为司机",Snackbar.LENGTH_LONG).show()
                }
                UserInfo.UserInfoAuthStatus.SHENG_HE_ZHONG.statusCode->{
                    Snackbar.make(mSrfMine,"您当前提交的司机身份正在审核，请耐心等待",Snackbar.LENGTH_LONG).show()
                }
                UserInfo.UserInfoAuthStatus.SHENG_HE_TONG_GUO.statusCode->{
                    FragmentContainerActivity.from(mContext).setTitle("我的车辆").setClazz(MineCarListFragment::class.java).setNeedNetWorking(true).start()
                }
                UserInfo.UserInfoAuthStatus.SHENG_HE_SHI_BAI.statusCode->{
                    Snackbar.make(mSrfMine,"您当前提交的司机身份审核失败，请重新提交认证",Snackbar.LENGTH_LONG).show()
                }
            }
        }

        mTvMineInviteCode.doClickWithUserStatusStart(INVITE_CODE_FLAG) {
            FragmentContainerActivity.from(mContext).setTitle("我的邀请码").setClazz(MineInviteFragment::class.java).setNeedNetWorking(true).start()
        }

        mTvMineKefu.doClickWithUserStatusStart(KE_FU_FLAG) {
            FragmentContainerActivity.from(mContext).setTitle("我的客服").setClazz(MineCustomServiceFragment::class.java).setNeedNetWorking(false).start()
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        if (requestID == 0x0) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                loadUserInfo()
                userPresent.onLoginNoBus((check.obj as JSONObject).optJSONObject(RESULT_OBJECT))
            }
        }
    }

    override fun onRequestEnd(requestID: Int) {
        super.onRequestEnd(requestID)
        mSrfMine.post { mSrfMine.isRefreshing = false }
    }

    override fun onLoginSuccess(userInfo: UserInfo?) {
        super.onLoginSuccess(userInfo)
        loadUserInfo()
        when (UserInfo.loginToken) {
            USER_INFO_FLAG -> {
                mTvMineUserInfo.doClickWithUseStatusEnd()
            }
            AUTH_PERSON_FLAG -> {
                mTvMineAuthInfo.doClickWithUseStatusEnd()
            }
            AUTH_CAR_FLAG -> {
                mMineCarList.doClickWithUseStatusEnd()
            }
            INVITE_CODE_FLAG -> {
                mTvMineInviteCode.doClickWithUseStatusEnd()
            }
            KE_FU_FLAG -> {
                mTvMineKefu.doClickWithUseStatusEnd()
            }
        }
    }

    private fun loadUserInfo() {
        if (UserInfo.getInstance().isLogin) {
            if (TextUtils.isEmpty(UserInfo.getInstance().user_pic)) {
                mMineUserFace.setImageResource(R.drawable.icon_default_face)
            } else {
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