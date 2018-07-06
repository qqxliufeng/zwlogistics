package com.android.ql.lf.carapp.ui.fragments.bottom

import android.support.v4.widget.SwipeRefreshLayout
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.data.RefreshData
import com.android.ql.lf.carapp.data.UpdateNotifyBean
import com.android.ql.lf.carapp.data.UserInfo
import com.android.ql.lf.carapp.present.UserPresent
import com.android.ql.lf.carapp.ui.activities.FragmentContainerActivity
import com.android.ql.lf.carapp.ui.activities.MainActivity
import com.android.ql.lf.carapp.ui.fragments.BaseNetWorkingFragment
import com.android.ql.lf.carapp.ui.fragments.message.MineMessageListFragment
import com.android.ql.lf.carapp.ui.fragments.user.ResetWalletPasswordFragment
import com.android.ql.lf.carapp.ui.fragments.user.SettingFragment
import com.android.ql.lf.carapp.ui.fragments.user.mine.*
import com.android.ql.lf.carapp.utils.*
import kotlinx.android.synthetic.main.fragment_main_mine_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject

/**
 * Created by lf on 18.1.24.
 * @author lf on 18.1.24
 */
class MainMineFragment : BaseNetWorkingFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        val MINE_PERSONAL_INFO_TOKEN = "personal_info_token"
        val MINE_STORE_COLLECTION_TOKEN = "store_collection_token"
        val MINE_GOODS_COLLECTION_TOKEN = "goods_collection_token"
        val MINE_GRADE_TOKEN = "grade_token"
        val MINE_PERSONAL_EDIT_INFO_TOKEN = "personal_edit_info_token"
        val MINE_STORE_INFO_TOKEN = "store_info_token"
        val MINE_Q_CODE_TOKEN = "q_code_token"
        val MINE_MY_WALLET_TOKEN = "my_wallet_token"
        val MINE_FOOT_PRINT_TOKEN = "foot_print_token"
        val MINE_SETTING_TOKEN = "setting_token"
        val MINE_MY_ARTICLE_TOKEN = "my_article_token"
        val MINE_APPLY_MASTER_TOKEN = "apply_master_token"
        val MINE_EVALUATE_TOKEN = "mine_evaluate_token"
        val MINE_MALL_ORDER_FLAG_TOKEN = "mine_mall_order_flag_token"

        val MINE_GOODS_COLLECTION_NUM_TOKEN = "mine_goods_collection_num_token"
        val MINE_STORE_COLLECTION_NUM_TOKEN = "mine_store_collection_num_token"
        val MINE_FOOTS_COLLECTION_NUM_TOKEN = "mine_foots_collection_num_token"

        fun newInstance(): MainMineFragment {
            return MainMineFragment()
        }
    }

    private val userLogoutSubscription by lazy {
        RxBus.getDefault().toObservable(String::class.java).subscribe {
            if (it == UserInfo.LOGOUT_FLAG) {
                onLogoutSuccess()
            }
        }
    }

    private val modifyInfoSubscription by lazy {
        RxBus.getDefault().toObservable(String::class.java).subscribe {
            if (it == "modify info success") {
                mTvMainMineName.text = UserInfo.getInstance().memberName
                GlideManager.loadFaceCircleImage(mContext, UserInfo.getInstance().memberPic, mIvMainMineFace)
            }
        }
    }

    private val updateMessageNotifySubscription by lazy {
        RxBus.getDefault().toObservable(UpdateNotifyBean::class.java).subscribe {
            mViewMainMineMessageNotify.visibility = it.status
        }
    }

    private val updateCollectionNumSubscription by lazy {
        RxBus.getDefault().toObservable(RefreshData::class.java).subscribe {
            if (it.isRefresh) {
                if (it.any == MINE_GOODS_COLLECTION_NUM_TOKEN) {
                    UserInfo.getInstance().goodsCollectionNum++
                    mTvMainMineCollectionGoodsCount.text = "${UserInfo.getInstance().goodsCollectionNum}"
                }
                if (it.any == MINE_STORE_COLLECTION_NUM_TOKEN) {
                    UserInfo.getInstance().storeCollectionNum++
                    mTvMainMineCollectionStoreCount.text = "${UserInfo.getInstance().storeCollectionNum}"
                }
                if (it.any == MINE_FOOTS_COLLECTION_NUM_TOKEN) {
                    UserInfo.getInstance().footsCollectionNum++
                    mTvMainMineCollectionFootPrintCount.text = "${UserInfo.getInstance().footsCollectionNum}"
                }
            }
        }
    }

    private val userPresent by lazy {
        UserPresent()
    }

    override fun getLayoutId() = R.layout.fragment_main_mine_layout

    override fun initView(view: View?) {
        val height = (mContext as MainActivity).statusHeight
        val param = mRlMineTitleContainer.layoutParams as ViewGroup.MarginLayoutParams
        param.topMargin = height
        mRlMineTitleContainer.layoutParams = param
        mSrlMainMineContainer.setColorSchemeColors(resources.getColor(R.color.colorPrimary))
        mSrlMainMineContainer.setOnRefreshListener(this)

        //注册登录成功事件，刷新界面
        registerLoginSuccessBus()
        //注册用户退出事件
        userLogoutSubscription
        //修改个人信息
        modifyInfoSubscription
        //接受修改消息提示事件
        updateMessageNotifySubscription

        //接受收藏数量
        updateCollectionNumSubscription
        setUserInfo(UserInfo.getInstance())
        mFlMainMineMessageNotifyContainer.setOnClickListener {
            RxBus.getDefault().post(UpdateNotifyBean(View.GONE))
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的消息", true, false, MineMessageListFragment::class.java)
        }
        mLlMainMinePersonalInfoContainer.doClickWithUserStatusStart(MINE_PERSONAL_INFO_TOKEN) {
            FragmentContainerActivity.from(mContext).setClazz(MinePersonalInfoFragment::class.java).setTitle("个人中心").setNeedNetWorking(true).start()
        }
        mLlMainMineStoreContainer.doClickWithUserStatusStart(MINE_STORE_COLLECTION_TOKEN) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "店铺收藏", MineStoreCollectionFragment::class.java)
        }
        mLlMainMineGoodsContainer.doClickWithUserStatusStart(MINE_GOODS_COLLECTION_TOKEN) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "商品收藏", MineGoodsCollectionFragment::class.java)
        }
        mTvMainMineGrade.doClickWithUserStatusStart(MINE_GRADE_TOKEN) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的等级", MineGradeFragment::class.java)
        }
        mTvMainServiceEdit.doClickWithUserStatusStart(MINE_PERSONAL_EDIT_INFO_TOKEN) {
            when (UserInfo.getInstance().authenticationStatus) {
                0 -> {
                    toast("认证资料正在审核中……")
                }
                1 -> {
                    if (UserInfo.getInstance().isMaster) {
                        FragmentContainerActivity.startFragmentContainerActivity(mContext, "个人服务信息", MinePersonalServiceEditFragment::class.java)
                    }
                }
                2 -> {
                    toast("资料审核失败，请重新提交……")
                }
                3 -> {
                    FragmentContainerActivity.from(mContext).setTitle("申请成为师傅").setNeedNetWorking(true).setClazz(MineApplyMasterInfoSubmitFragment::class.java).start()
                }
            }
        }
        mTvMainMineStore.doClickWithUserStatusStart(MINE_STORE_INFO_TOKEN) {
            when (UserInfo.getInstance().authenticationSellerStatus) {
                0 -> {
                    toast("认证资料正在审核中……")
                }
                1 -> {
                    FragmentContainerActivity.from(mContext).setTitle("我的店铺").setClazz(MineStoreInfoFragment::class.java).setNeedNetWorking(true).start()
                }
                2 -> {
                    toast("资料审核失败，请重新提交……")
                }
                3 -> {
                    FragmentContainerActivity.from(mContext).setTitle("申请成为商家").setNeedNetWorking(true).setClazz(MineApplySallerInfoSubmitFragment::class.java).start()
                }
            }
        }
        mTvMainMineQCode.doClickWithUserStatusStart(MINE_Q_CODE_TOKEN) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的邀请码", MineQCodeFragment::class.java)
        }
        mTvMainMineWallet.doClickWithUserStatusStart(MINE_MY_WALLET_TOKEN) {
            if (TextUtils.isEmpty(UserInfo.getInstance().memberSecondPw)) {
                FragmentContainerActivity.from(mContext).setTitle("设置钱包密码")
                        .setExtraBundle(bundleOf(Pair(ResetWalletPasswordFragment.PASSWORD_TYPE_FLAG, ResetWalletPasswordFragment.SET_PASSWORD_FLAG)))
                        .setNeedNetWorking(true)
                        .setClazz(ResetWalletPasswordFragment::class.java).start()
            } else {
                mContext.showPayPasswordDialog(resetAction = {
                    FragmentContainerActivity.from(mContext)
                            .setExtraBundle(bundleOf(Pair(ResetWalletPasswordFragment.PASSWORD_TYPE_FLAG, ResetWalletPasswordFragment.RESET_PASSWORD_FLAG)))
                            .setTitle("修改钱包密码").setNeedNetWorking(true).setClazz(ResetWalletPasswordFragment::class.java).start()
                }, forgetAction = {
                    FragmentContainerActivity.from(mContext)
                            .setExtraBundle(bundleOf(Pair(ResetWalletPasswordFragment.PASSWORD_TYPE_FLAG, ResetWalletPasswordFragment.FORGET_PASSWORD_FLAG)))
                            .setTitle("忘记钱包密码").setNeedNetWorking(true).setClazz(ResetWalletPasswordFragment::class.java).start()
                }, action = {
                    mPresent.getDataByPost(0x1,
                            RequestParamsHelper.MEMBER_MODEL,
                            RequestParamsHelper.ACT_VERIFY_SECOND_PW,
                            RequestParamsHelper.getVerifySecondPw(UserInfo.getInstance().memberPhone, it))
                })
            }
        }
        mLlMainMineFootPrintContainer.doClickWithUserStatusStart(MINE_FOOT_PRINT_TOKEN) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的足迹", MineFootPrintFragment::class.java)
        }
        mTvMainMineSetting.doClickWithUserStatusStart(MINE_SETTING_TOKEN) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "设置", SettingFragment::class.java)
        }
        mTvMainMineArticle.doClickWithUserStatusStart(MINE_MY_ARTICLE_TOKEN) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的帖子", MineArticleFragment::class.java)
        }
        mTvMainMineApplyMaster.doClickWithUserStatusStart(MINE_APPLY_MASTER_TOKEN) {
            FragmentContainerActivity.startFragmentContainerActivity(mContext, "申请成为商家", MineApplyMasterFragment::class.java)
        }
        mTvMainMineEvaluate.doClickWithUserStatusStart(MINE_EVALUATE_TOKEN) {
            FragmentContainerActivity.from(mContext).setClazz(MimeEvaluateFragment::class.java).setTitle("我的评价").start()
        }
        mTvMainMineShopOrder.doClickWithUserStatusStart(MINE_MALL_ORDER_FLAG_TOKEN) {
            FragmentContainerActivity.from(mContext).setTitle("购物订单").setClazz(MineMallOrderFragment::class.java).start()
        }
        onRefresh()
    }

    override fun onRefresh() {
        if (UserInfo.getInstance().isLogin) {
            mPresent.getDataByPost(0x0,
                    RequestParamsHelper.MEMBER_MODEL,
                    RequestParamsHelper.ACT_PERSONAL,
                    RequestParamsHelper.getPersonalParam(UserInfo.getInstance().memberId))
        } else {
            onRequestEnd(0x0)
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在验证密码……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        super.onRequestSuccess(requestID, result)
        val check = checkResultCode(result)
        if (requestID == 0x0) {
            if (check != null && check.code == SUCCESS_CODE) {
                val json = check.obj as JSONObject
                userPresent.onLoginNoBus(json.optJSONObject("result"), json.optJSONObject("arr"))
                val arr1 = json.optJSONObject("arr1")
                val s1 = arr1.optString("s1")
                if (!TextUtils.isEmpty(s1)) {
                    UserInfo.getInstance().goodsCollectionNum = s1.toInt()
                }
                val s2 = arr1.optString("s2")
                if (!TextUtils.isEmpty(s2)) {
                    UserInfo.getInstance().storeCollectionNum = s2.toInt()
                }
                val s3 = arr1.optString("s3")
                if (!TextUtils.isEmpty(s3)) {
                    UserInfo.getInstance().footsCollectionNum = s3.toInt()
                }
                mTvMainMineCollectionGoodsCount.text = "${UserInfo.getInstance().goodsCollectionNum}"
                mTvMainMineCollectionStoreCount.text = "${UserInfo.getInstance().storeCollectionNum}"
                mTvMainMineCollectionFootPrintCount.text = "${UserInfo.getInstance().footsCollectionNum}"
            }
        } else if (requestID == 0x1) {
            if (check != null && check.code == SUCCESS_CODE) {
                toast("验证成功")
                FragmentContainerActivity.startFragmentContainerActivity(mContext, "我的钱包", MineWalletFragment::class.java)
            } else {
                toast("验证失败，请重试……")
            }
        }
    }

    override fun onRequestEnd(requestID: Int) {
        super.onRequestEnd(requestID)
        if (requestID == 0x0) {
            if (mSrlMainMineContainer.isRefreshing) {
                mSrlMainMineContainer.post {
                    mSrlMainMineContainer.isRefreshing = false
                }
            }
        }
    }

    /**
     * 登录成功
     */
    override fun onLoginSuccess(it: UserInfo?) {
        if (it != null) {
            mSrlMainMineContainer.post {
                mSrlMainMineContainer.isRefreshing = true
                onRefresh()
            }
            setUserInfo(it)
            when (UserInfo.loginToken) {
                MINE_PERSONAL_INFO_TOKEN -> {
                    mLlMainMinePersonalInfoContainer.doClickWithUseStatusEnd()
                }
                MINE_STORE_COLLECTION_TOKEN -> {
                    mLlMainMineStoreContainer.doClickWithUseStatusEnd()
                }
                MINE_GOODS_COLLECTION_TOKEN -> {
                    mLlMainMineGoodsContainer.doClickWithUseStatusEnd()
                }
                MINE_GRADE_TOKEN -> {
                    mTvMainMineGrade.doClickWithUseStatusEnd()
                }
                MINE_PERSONAL_EDIT_INFO_TOKEN -> {
                    mTvMainServiceEdit.doClickWithUseStatusEnd()
                }
                MINE_STORE_INFO_TOKEN -> {
                    mTvMainMineStore.doClickWithUseStatusEnd()
                }
                MINE_Q_CODE_TOKEN -> {
                    mTvMainMineQCode.doClickWithUseStatusEnd()
                }
                MINE_MY_WALLET_TOKEN -> {
                    mTvMainMineWallet.doClickWithUseStatusEnd()
                }
                MINE_FOOT_PRINT_TOKEN -> {
                    mLlMainMineFootPrintContainer.doClickWithUseStatusEnd()
                }
                MINE_SETTING_TOKEN -> {
                    mTvMainMineSetting.doClickWithUseStatusEnd()
                }
                MINE_MY_ARTICLE_TOKEN -> {
                    mTvMainMineArticle.doClickWithUseStatusEnd()
                }
                MINE_APPLY_MASTER_TOKEN -> {
                    mTvMainMineApplyMaster.doClickWithUseStatusEnd()
                }
                MINE_EVALUATE_TOKEN -> {
                    mTvMainMineEvaluate.doClickWithUseStatusEnd()
                }
                MINE_MALL_ORDER_FLAG_TOKEN -> {
                    mTvMainMineShopOrder.doClickWithUseStatusEnd()
                }
            }
        }
    }

    private fun setUserInfo(it: UserInfo) {
        if (it.isLogin) {
            GlideManager.loadFaceCircleImage(mContext, it!!.memberPic, mIvMainMineFace)
            mTvMainMineEditNameNotify.visibility = View.VISIBLE
            mTvMainMineName.text = it.memberName
            mTvMainMinePhone.text = it.memberPhone.let {
                it.substring(0, 3) + "****" + it.substring(7, it.length)
            }
        }
    }

    /**
     * 登录失败
     */
    private fun onLogoutSuccess() {
        mIvMainMineFace.setImageResource(R.drawable.img_default_mine_icon)
        mTvMainMineName.text = "登录/注册"
        mTvMainMinePhone.text = "暂无"
        mTvMainMineCollectionGoodsCount.text = "0"
        mTvMainMineCollectionStoreCount.text = "0"
        mTvMainMineCollectionFootPrintCount.text = "0"
    }

    override fun onDestroyView() {
        unsubscribe(userLogoutSubscription)
        unsubscribe(modifyInfoSubscription)
        unsubscribe(updateMessageNotifySubscription)
        super.onDestroyView()
    }
}