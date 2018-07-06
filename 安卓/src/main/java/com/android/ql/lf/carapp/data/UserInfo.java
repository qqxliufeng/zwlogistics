package com.android.ql.lf.carapp.data;

import android.content.Context;
import android.text.TextUtils;

import com.android.ql.lf.carapp.utils.PreferenceUtils;

/**
 * Created by lf on 18.2.10.
 *
 * @author lf on 18.2.10
 */

public class UserInfo {

    public static final String USER_ID_FLAG = "user_id";

    public static final String LOGOUT_FLAG = "user_logout_flag";

    public static String loginToken = "NONE";

    public static void resetLoginSuccessDoActionToken() {
        loginToken = "NONE";
    }

    private UserInfo() {
    }

    private static UserInfo instance;

    public static UserInfo getInstance() {
        if (instance == null) {
            synchronized (UserInfo.class) {
                if (instance == null) {
                    instance = new UserInfo();
                }
            }
        }
        return instance;
    }

    private String memberId;
    private String memberPhone;
    private String memberName = "";
    private String memberPic;
    private String memberMyInviteCode;
    private String memberRank;
    private String memberOpenid; // 提现wxopenId
    private String memberIswxAuth; //提现是否授权  1 已授权 0 未授权
    private String memberWxOpenid; // 登录wxopenId
    private String memberAddress;
    private String memberInviteCode;
    private String memberIsEnsureMoney;
    private String memberIsMaster;
    private String memberGrade;
    private String memberOrderNum;
    private String memberIdCard = null;
    private String memberAuthentication; // 0 师傅待审核 1 已是师傅 2 师傅审核失败 3 暂无师傅店铺信息
    private String memberQQOpenid;
    private String memberAlias;
    //    private String memberWxIs
    private ShopInfo shopInfo;
    private String memberSecondPw = null;

    private String memberHxname;
    private String memberHxpw;

    public String getMemberHxname() {
        return memberHxname;
    }

    public void setMemberHxname(String memberHxname) {
        this.memberHxname = memberHxname;
    }

    public String getMemberHxpw() {
        return memberHxpw;
    }

    public void setMemberHxpw(String memberHxpw) {
        this.memberHxpw = memberHxpw;
    }

    public String getMemberSecondPw() {
        return memberSecondPw;
    }

    public void setMemberSecondPw(String memberSecondPw) {
        this.memberSecondPw = memberSecondPw;
    }

    private int goodsCollectionNum = 0;
    private int storeCollectionNum = 0;
    private int footsCollectionNum = 0;

    public int getGoodsCollectionNum() {
        return goodsCollectionNum;
    }

    public void setGoodsCollectionNum(int goodsCollectionNum) {
        this.goodsCollectionNum = goodsCollectionNum;
    }

    public int getStoreCollectionNum() {
        return storeCollectionNum;
    }

    public void setStoreCollectionNum(int storeCollectionNum) {
        this.storeCollectionNum = storeCollectionNum;
    }

    public int getFootsCollectionNum() {
        return footsCollectionNum;
    }

    public void setFootsCollectionNum(int footsCollectionNum) {
        this.footsCollectionNum = footsCollectionNum;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPic() {
        return memberPic;
    }

    public void setMemberPic(String memberPic) {
        this.memberPic = memberPic;
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(memberId);
    }

    public boolean isMaster() {
        return "1".equals(memberIsMaster) && "1".equals(memberAuthentication);
    }

    public boolean isPayEnsureMoney() {
        return "1".equals(memberIsEnsureMoney);
    }

    public String getMemberMyInviteCode() {
        return memberMyInviteCode;
    }

    public void setMemberMyInviteCode(String memberMyInviteCode) {
        this.memberMyInviteCode = memberMyInviteCode;
    }

    public String getMemberRank() {
        return memberRank;
    }

    public void setMemberRank(String memberRank) {
        this.memberRank = memberRank;
    }

    public String getMemberOpenid() {
        return memberOpenid;
    }

    public void setMemberOpenid(String memberOpenid) {
        this.memberOpenid = memberOpenid;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public String getMemberInviteCode() {
        return memberInviteCode;
    }

    public void setMemberInviteCode(String memberInviteCode) {
        this.memberInviteCode = memberInviteCode;
    }

    public void setMemberIsEnsureMoney(String memberIsEnsureMoney) {
        this.memberIsEnsureMoney = memberIsEnsureMoney;
    }

    public void setMemberIsMaster(String memberIsMaster) {
        this.memberIsMaster = memberIsMaster;
    }

    public String getMemberGrade() {
        return memberGrade;
    }

    public void setMemberGrade(String memberGrade) {
        this.memberGrade = memberGrade;
    }

    public String getMemberOrderNum() {
        return memberOrderNum;
    }

    public void setMemberOrderNum(String memberOrderNum) {
        this.memberOrderNum = memberOrderNum;
    }

    public String getMemberIdCard() {
        return memberIdCard;
    }

    public void setMemberIdCard(String memberIdCard) {
        this.memberIdCard = memberIdCard;
    }

    public boolean isCheckingMaster() {
        return "0".equals(memberAuthentication);
    }

    public void setMemberAuthentication(String memberAuthentication) {
        this.memberAuthentication = memberAuthentication;
    }

    public int getAuthenticationStatus() {
        try {
            if (TextUtils.isEmpty(memberAuthentication) || TextUtils.equals("null", memberAuthentication)) {
                return 3;
            }
            return Integer.parseInt(memberAuthentication);
        } catch (NumberFormatException e) {
            return 3;
        }
    }


    public String getMemberQQOpenid() {
        return memberQQOpenid;
    }

    public void setMemberQQOpenid(String memberQQOpenid) {
        this.memberQQOpenid = memberQQOpenid;
    }

    public String getMemberAlias() {
        return memberAlias;
    }

    public void setMemberAlias(String memberAlias) {
        this.memberAlias = memberAlias;
    }

    public String getMemberIswxAuth() {
        return memberIswxAuth;
    }

    public void setMemberIswxAuth(String memberIswxAuth) {
        this.memberIswxAuth = memberIswxAuth;
    }

    public String getMemberWxOpenid() {
        return memberWxOpenid;
    }

    public void setMemberWxOpenid(String memberWxOpenid) {
        this.memberWxOpenid = memberWxOpenid;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public void loginOut() {
        memberId = null;
        instance = null;
    }

    public void exitApp() {
        if (instance != null) {
            instance = null;
        }
    }

    public static void clearUserCache(Context context) {
        PreferenceUtils.setPrefString(context, USER_ID_FLAG, "");
    }

    public static boolean isCacheUserId(Context context) {
        return PreferenceUtils.hasKey(context, USER_ID_FLAG) && !TextUtils.isEmpty(PreferenceUtils.getPrefString(context, USER_ID_FLAG, ""));
    }

    public static String getUserIdFromCache(Context context) {
        return PreferenceUtils.getPrefString(context, USER_ID_FLAG, "");
    }

    public static class ShopInfo {

        private String shop_id;
        private String shop_mpic;
        private String shop_sypic;
        private String shop_name;
        private String shop_phone;
        private String shop_address;
        private String shop_num;
        private String shop_content;
        private String shop_type;
        private String shop_token;
        private String shop_time;
        private String shop_uid;
        private String shop_stoken;
        private String shop_sn;
        private String shop_coorp;
        private String shop_ppa;
        private String shop_start_time;
        private String shop_end_time;
        private String shop_d;

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getShop_mpic() {
            return shop_mpic;
        }

        public void setShop_mpic(String shop_mpic) {
            this.shop_mpic = shop_mpic;
        }

        public String getShop_sypic() {
            return shop_sypic;
        }

        public void setShop_sypic(String shop_sypic) {
            this.shop_sypic = shop_sypic;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_phone() {
            return shop_phone;
        }

        public void setShop_phone(String shop_phone) {
            this.shop_phone = shop_phone;
        }

        public String getShop_address() {
            return shop_address;
        }

        public void setShop_address(String shop_address) {
            this.shop_address = shop_address;
        }

        public String getShop_num() {
            return shop_num;
        }

        public void setShop_num(String shop_num) {
            this.shop_num = shop_num;
        }

        public String getShop_content() {
            return shop_content;
        }

        public void setShop_content(String shop_content) {
            this.shop_content = shop_content;
        }

        public String getShop_type() {
            return shop_type;
        }

        public void setShop_type(String shop_type) {
            this.shop_type = shop_type;
        }

        public String getShop_token() {
            return shop_token;
        }

        public void setShop_token(String shop_token) {
            this.shop_token = shop_token;
        }

        public String getShop_time() {
            return shop_time;
        }

        public void setShop_time(String shop_time) {
            this.shop_time = shop_time;
        }

        public String getShop_uid() {
            return shop_uid;
        }

        public void setShop_uid(String shop_uid) {
            this.shop_uid = shop_uid;
        }

        public String getShop_stoken() {
            return shop_stoken;
        }

        public void setShop_stoken(String shop_stoken) {
            this.shop_stoken = shop_stoken;
        }

        public String getShop_sn() {
            return shop_sn;
        }

        public void setShop_sn(String shop_sn) {
            this.shop_sn = shop_sn;
        }

        public String getShop_coorp() {
            return shop_coorp;
        }

        public void setShop_coorp(String shop_coorp) {
            this.shop_coorp = shop_coorp;
        }

        public String getShop_ppa() {
            return shop_ppa;
        }

        public void setShop_ppa(String shop_ppa) {
            this.shop_ppa = shop_ppa;
        }

        public String getShop_start_time() {
            return shop_start_time;
        }

        public void setShop_start_time(String shop_start_time) {
            this.shop_start_time = shop_start_time;
        }

        public String getShop_end_time() {
            return shop_end_time;
        }

        public void setShop_end_time(String shop_end_time) {
            this.shop_end_time = shop_end_time;
        }

        public String getShop_d() {
            return shop_d;
        }

        public void setShop_d(String shop_d) {
            this.shop_d = shop_d;
        }
    }


    //二期新增字段
    private String memberAuthenticationSeller;

    public void setMemberAuthenticationSeller(String memberAuthenticationSeller) {
        this.memberAuthenticationSeller = memberAuthenticationSeller;
    }

    public int getAuthenticationSellerStatus() {
        try {
            if (TextUtils.isEmpty(memberAuthenticationSeller) || TextUtils.equals("null", memberAuthenticationSeller)) {
                return 3;
            }
            return Integer.parseInt(memberAuthenticationSeller);
        } catch (NumberFormatException e) {
            return 3;
        }
    }
}
