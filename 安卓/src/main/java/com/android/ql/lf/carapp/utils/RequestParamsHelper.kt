package com.android.ql.lf.carapp.utils

import android.text.TextUtils
import com.android.ql.lf.carapp.data.UserInfo

/**
 * Created by lf on 2017/11/13 0013.
 * @author lf on 2017/11/13 0013
 */
class RequestParamsHelper {

    companion object {

        private fun getBaseParams(): ApiParams {
            val params = ApiParams()
            params.addParam("pt", "android")
            return params
        }

        private fun getWithIdParams(): ApiParams {
            val params = getBaseParams()
            params.addParam("uid", UserInfo.getInstance().memberId)
            return params
        }

        fun getWithPageParams(page: Int, pageSize: Int = 10): ApiParams {
            val param = if (UserInfo.getInstance().isLogin) {
                getWithIdParams()
            } else {
                getBaseParams()
            }
            param.addParam("page", page)
            param.addParam("pagesize", pageSize)
            return param
        }

        /**              login model  start           **/
        val LOGIN_MODEL = "login"
        val ACT_REGISTER = "Register"
        val ACT_CODE = "getcode"
        val ACT_LOGIN = "Login"
        val ACT_FORGETPW = "forgetpw"
        fun getCodeParams(tel: String = ""): ApiParams {
            val params = getBaseParams()
            return params.addParam("tel", tel)
        }

        fun getRegisterParams(tel: String = "", pw: String = ""): ApiParams {
            val params = getBaseParams()
            params.addParam("tel", tel).addParam("pw", pw)
            return params
        }

        fun getLoginParams(tel: String = "", pw: String = ""): ApiParams {
            val params = getBaseParams()
            params.addParam("tel", tel).addParam("pw", pw).addParam("stoken", "1")
            return params
        }

        fun getForgetPWParams(tel: String = "", pw: String = ""): ApiParams {
            val params = getBaseParams()
            params.addParam("tel", tel).addParam("pw", pw)
            return params
        }


        val ACT_QQLOGIN = "qqlogin"
        fun getQQloginParam(phone: String, openid: String, accessToken: String) =
                getBaseParams().addParam("phone", phone).addParam("openid", openid).addParam("access_token", accessToken)

        val ACT_WX_PERFECT = "wx_perfect"
        fun getWXCompleteDataParam(phone: String, headimgurl: String, openid: String, nickname: String): ApiParams {
            val params = getBaseParams()
            params.addParam("phone", phone)
            params.addParam("headimgurl", headimgurl)
            params.addParam("openid", openid)
            params.addParam("nickname", nickname)
            return params
        }
        /**              login model  end           **/


        /**              member model  start           **/

        val MEMBER_MODEL = "member"
        val ACT_EDIT_PW = "edit_pw"
        fun getEditPWParams(pw: String, newpw: String): ApiParams {
            val param = getWithIdParams()
            return param.addParam("pw", pw).addParam("newpw", newpw)
        }

        val ACT_EDIT_PERSONAL = "edit_personal"
        fun getEditPersonalParam(account: String = "", idcard: String = ""): ApiParams {
            val params = getWithIdParams()
            if (!TextUtils.isEmpty(account)) {
                params.addParam("account", account)
            }
            if (!TextUtils.isEmpty(idcard)) {
                params.addParam("idcard", idcard)
            }
            return params
        }

        val ACT_MY_GRADES = "my_grades"
        fun getMyGradesParam() = getWithIdParams()

        val ACT_PTGG = "ptgg"
        fun getPtggParam(pid: String) = getBaseParams().addParam("pid", pid)

        val ACT_INTEGRAL = "integral"
        fun getIntegralParam(page: Int) = getWithPageParams(page)

        val ACT_PERSONAL_SERVICE = "personal_service"
        fun getPersonalServiceParam() = getWithIdParams()

        val ACT_APPLY = "apply"
        fun getApplyParam(type: String, name: String, mpic: ArrayList<String>, sypic: ArrayList<String>, phone: String, address: String, detailAddress: String, num: String, content: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("type", type)
            param.addParam("name", name)
            param.addParam("mpic", mpic.toString().removePrefix("[").removeSuffix("]"))
            param.addParam("sypic", sypic.toString().removePrefix("[").removeSuffix("]"))
            param.addParam("phone", phone)
            param.addParam("address", address)
            param.addParam("detail_address", detailAddress)
            param.addParam("num", num)
            param.addParam("content", content)
            return param
        }

        val ACT_MY_WITHDRAW = "my_withdraw"
        fun getMyWithdrawParam() = getWithIdParams()

        val ACT_MY_WITHDRAW_RECORD = "my_withdraw_record"
        fun getMyWithdrawRecordParam(page: Int) = getWithPageParams(page)

        val ACT_MY_WITHDRAW_OPERATION = "my_withdraw_operation"
        fun getMyWithdrawOperationParam(price: String, type: String) = getWithIdParams().addParam("price", price).addParam("type", type)

        val ACT_BIND_ALIPAY = "bind_alipay"
        fun getBindAlipayParam(account: String, autonym: String) = getWithIdParams().addParam("account", account).addParam("autonym", autonym)

        val ACT_BIND_WXPAY = "bind_wxpay"
        fun getBindWxpayParam(idcard: String, autonym: String) = getWithIdParams().addParam("idcard", idcard).addParam("autonym", autonym)

        val ACT_WX_AUTHORIZATION = "wx_authorization"
        fun getWxAuthorizationParam(code: String) = getWithIdParams().addParam("code", code)

        val ACT_M_P = "m_p"
        fun getEnsureMoneyProductParam() = getWithIdParams()

        val ACT_EDIT_PERSONAL_SERVICE = "edit_personal_service"
        fun getEditePersonalServiceParam(sid: String, address: String, detailAddress: String, ppa: String, starttime: String, endtime: String, content: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("sid", sid)
            param.addParam("address", address)
            param.addParam("detail_address", detailAddress)
            param.addParam("ppa", ppa)
            param.addParam("starttime", starttime)
            param.addParam("endtime", endtime)
            param.addParam("content", content)
            return param
        }

        val ACT_PAYMENT_DEPOSIT = "payment_deposit"
        fun getPaymentDepositParam(type: String, mid: String, paytype: String, price: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("type", type)
            param.addParam("mid", mid)
            param.addParam("paytype", paytype)
            param.addParam("price", price)
            return param
        }

        val ACT_REFUND_DEPOSIT = "refund_deposit"
        fun getRefundDepositParam(type: String) = getWithIdParams().addParam("type", type)

        val ACT_PERSONAL = "personal"
        fun getPersonalParam(uid: String) = getBaseParams().addParam("uid", uid)

        val ACT_MY_MSG = "my_msg"
        fun getMyMsgParam() = getWithIdParams()

        val ACT_MY_MSG_DETAIL = "my_msg_detail"
        fun getMyMsgDetailParam(status: String, page: Int) = getWithPageParams(page).addParam("status", status)

        val ACT_EDIT_MYMSG_STATUS = "edit_mymsg_status"
        fun getEditMyMsgStatus(mid: String) = getWithIdParams().addParam("mid", mid)

        val ACT_ADD_ADDRESS = "add_address"
        fun getAddAddressListParams(aid: String? = "", name: String = "", phone: String = "", addressInfo: String = "", code: String = "", detail: String = ""): ApiParams {
            val params = getWithIdParams()
            params.addParam("aid", aid)
            params.addParam("name", name)
            params.addParam("phone", phone)
            params.addParam("address", addressInfo)
            params.addParam("detail", detail)
            params.addParam("postcode", code)
            return params
        }

        val ACT_ADDRESS_LIST = "address"
        fun getAddressListParams() = getWithIdParams()

        val ACT_DEFAULT_ADDRESS = "default_address"
        fun getDefaultAddressParams(topAid: String, setAid: String): ApiParams {
            val params = getWithIdParams()
            params.addParam("topaid", topAid)
            params.addParam("aid", setAid)
            return params
        }

        val ACT_DEL_ADDRESS = "del_address"
        fun getDelAddressParams(aid: String): ApiParams {
            val params = getWithIdParams()
            params.addParam("aid", aid)
            return params
        }

        val ACT_BIND_ACCOUNT = "bind_account"
        fun getBindAccountParam() = getWithIdParams()

        //购物车
        val ACT_SHOPCART = "shopcart"

        fun getShopcartParam(page: Int, pageSize: Int = 10) = getWithPageParams(page, pageSize)

        val ACT_ADD_SHOPCART = "add_shopcart"
        fun getAddShopcartParam(gid: String, shopid: String, num: String, specification: String, pic: String, price: String, key: String) =
                getWithIdParams()
                        .addParam("gid", gid)
                        .addParam("shopid", shopid)
                        .addParam("num", num)
                        .addParam("pic", pic)
                        .addParam("specification", specification)
                        .addParam("price", price)
                        .addParam("key", key)


        //删除购物车商品
        val ACT_DEL_SHOPCART = "del_shopcart"

        fun getDelShopcartParam(cid: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("cid", cid)
            return param
        }


        //修改商品数量
        val ACT_UPDATE_SHOPCART = "update_shopcart"

        fun getUpdateShopcart(cid: String, num: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("cid", cid)
            param.addParam("num", num)
            return param
        }


        val ACT_MYORDER_STATUS = "myorder_status"
        fun getMyorderStatusParams(status: String, page: Int) = getWithPageParams(page).addParam("status", status)

        val ACT_MYORDER = "myorder"
        fun getMyorderParams(page: Int) = getWithPageParams(page)

        val ACT_EDIT_ORDER_STATUS = "edit_order_status"
        fun getEditOrderStatusParam(oid: String, status: String) = getWithIdParams()
                .addParam("status", status)
                .addParam("oid", oid)

        val ACT_EVALUATE = "evaluate"
        fun getEvaluateParam(oid: String, gid: String, content: String, f: String, sn: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("oid", oid)
            param.addParam("gid", gid)
            param.addParam("content", content)
            param.addParam("f", f)
            param.addParam("sn", sn)
            return param
        }

        val ACT_MY_SHOP_COLLECT = "my_shop_collect"
        fun getMyShopCollectParams(page: Int) = getWithPageParams(page)

        val ACT_MY_CONCERM_SHOP = "my_concerm_shop"
        fun getMyConcermShopParams(page: Int) = getWithPageParams(page)

        val ACT_MY_SPOOR = "my_spoor"
        fun getMySpoorParmas(page: Int) = getWithPageParams(page)

        val ACT_VERIFY_SECOND_PW = "verify_second_pw"
        fun getVerifySecondPw(tel: String, second_pw: String) = getWithIdParams().addParam("tel", tel).addParam("second_pw", second_pw)

        val ACT_SET_SECOND_PW = "set_second_pw"
        fun getSetSecondPW(tel: String, second_pw: String) = getWithIdParams().addParam("tel", tel).addParam("second_pw", second_pw)

        val ACT_EDIT_SECOND_PW = "edit_second_pw"
        fun getEditSecondPW(tel: String, old_second_pw: String, new_second_pw: String) =
                getWithIdParams().addParam("tel", tel).addParam("old_second_pw", old_second_pw).addParam("new_second_pw", new_second_pw)

        val ACT_FORGET_SECOND_PW = "forget_second_pw"
        fun getForgetSecondPW(tel: String, second_pw: String) = getWithIdParams().addParam("tel", tel).addParam("second_pw", second_pw)

        val ACT_MY_QRCODE = "my_qrcode"
        fun getMyQrcodeParam() = getWithIdParams()

        val ACT_MY_SHOP_HEADER = "my_shop_header"
        fun getMyShopHeaderParam() = getWithIdParams()

        val ACT_EDIT_MY_SHOP_HEADER = "edit_my_shop_header"
        fun getEditMyShopHeaderParam(sid: String, name: String, phone: String, address: String, d: String, content: String) =
                getWithIdParams()
                        .addParam("sid", sid)
                        .addParam("name", name)
                        .addParam("phone", phone)
                        .addParam("address", address)
                        .addParam("d", d)
                        .addParam("content", content)

        val ACT_MY_COMMENT = "my_comment"
        fun getMyCommentParam(f: String, page: Int) = getWithPageParams(page).addParam("f", f)

        //快递查询
        val ACT_GETLOGISTICS = "getlogistics"

        fun getGetlogisticsParam(num: String): ApiParams {
            val param = getWithIdParams()
            param.addParam("nu", num)
            return param
        }

        val ACT_GET_DISCOUNT = "getdiscount"
        fun getDiscountParam(theme: String) = getWithIdParams().addParam("theme", theme)

        /**              member model  end           **/

        /**              product model start         **/

        val PRODUCT_MODEL = "product"

        val ACT_PRODUCT = "product"
        fun getProductParams(page: Int) = getWithPageParams(page)


        val ACT_COLLECT_PRODUCT = "collect_product"
        fun getCollectProductParam(pid: String) = getWithIdParams().addParam("pid", pid)

        val ACT_PRODUCT_FADDISH = "product_faddish"
        fun getProductFaddishParams(type: String, sort: String, page: Int) =
                getWithPageParams(page)
                        .addParam("type", type)
                        .addParam("sort", sort)

        val ACT_CONCERM_SHOP = "concerm_shop"
        fun getConcermShopParams(sid: String) = getWithIdParams().addParam("sid", sid)

        val ACT_PRODUCT_SHOP_TYPE_SEARCH = "product_shop_type_search"
        fun getProductShopTypeSearchParams(type_id: String, stype_id: String, sid: String, sort: String, page: Int) =
                getWithPageParams(page)
                        .addParam("type_id", type_id)
                        .addParam("stype_id", stype_id)
                        .addParam("sid", sid)
                        .addParam("sort", sort)

        val ACT_PRODUCT_TYPE = "product_type"
        fun getProductTypeParams(pid: String) = getBaseParams().addParam("pid", pid)

        val ACT_PRODUCT_SEARCH = "product_search"
        fun getProductSearchParams(keyword: String, sort: String, type: String, page: Int) =
                getWithPageParams(page)
                        .addParam("keyword", keyword)
                        .addParam("sort", sort)
                        .addParam("type", type)

        val ACT_M_PRODUCT_SEARCH = "m_product_search"


        val ACT_PRODUCT_TYPE_SEARCH = "product_type_search"
        fun getProductTypeSearchParams(type_id: String, stype_id: String, sort: String, page: Int) =
                getWithPageParams(page)
                        .addParam("type_id", type_id)
                        .addParam("stype_id", stype_id)
                        .addParam("sort", sort)

        val ACT_PRODUCT_DETAIL = "product_detail"
        fun getPoductDetailParams(gid: String) = getWithIdParams().addParam("gid", gid)

        val ACT_PRODUCT_SHOP_SEARCH = "product_shop_search"
        fun getProductShopSearchParams(keyword: String, sid: String, sort: String, type: String = "", page: Int) =
                getWithPageParams(page)
                        .addParam("keyword", keyword)
                        .addParam("sid", sid)
                        .addParam("sort", sort)
                        .addParam("type", type)

        val ACT_PRODUCT_SHOP = "product_shop"
        fun getProductShopParams(sid: String, sort: String, page: Int) =
                getWithPageParams(page)
                        .addParam("sid", sid)
                        .addParam("sort", sort)


        val ACT_ALL_COMMENT = "all_comment"
        fun getAllCommentParams(gid: String, page: Int) = getWithPageParams(page).addParam("gid", gid)

        val ACT_ORDER_DETAIL = "order_detail"
        fun getMallOrderDetailParam(oid: String) = getWithIdParams().addParam("oid", oid)

        val ACT_SKU_SELECT = "sku_select"
        fun getSkuSelect(gid: String, attr: String): ApiParams = getWithIdParams().addParam("gid", gid).addParam("attr", attr)

        val ACT_CARRY_PRODUCT = "carry_product"
        fun getCarryProductParam(gid: String) = getWithIdParams().addParam("gid", gid)


        /**              qaa model  start           **/
        val QAA_MODEL = "qaa"
        val ACT_QUIZ = "quiz"

        fun getQuizParam(ktype: Int, page: Int): ApiParams {
            val param = getWithPageParams(page)
            param.addParam("ktype", ktype)
            return param
        }

        val ACT_COMMUNITY_LUNBO_DETAIL = "community_lunbo_detail"
        fun getCommunityLunboDetail(lid: String): ApiParams {
            return getWithIdParams().addParam("lid", lid)
        }

        val ACT_QUIZ_DETAIL = "quiz_detail"
        fun getQuizDetailParams(qid: String, page: Int): ApiParams {
            return getWithPageParams(page).addParam("qid", qid)
        }

        val ACT_QUIZ_LOOK = "quiz_look"
        fun getQuizLookParams(qid: String): ApiParams {
            return getWithIdParams().addParam("qid", qid)
        }

        val ACT_ADD_ANSWER = "add_answer"
        fun getAddAnswerParams(qid: String, content: String): ApiParams {
            return getWithIdParams().addParam("qid", qid).addParam("content", content)
        }

        val ACT_GET_MYQUIZ = "get_myquiz"
        fun getGetMyQuizParam(page: Int) = getWithPageParams(page)

        val ACT_GET_MYANSWER = "get_myanswer"
        fun getGetMyAnswerParam(page: Int) = getWithPageParams(page)

        val ACT_DEL_QAA = "del_qaa"
        fun getDelQaaParam(qid: String = "", aid: String = ""): ApiParams {
            return getWithIdParams().addParam("qid", qid).addParam("aid", aid)
        }

        val ACT_TAG = "tag"
        fun getTagParam() = getBaseParams()

        val ACT_ADD_QUIZ = "add_quiz"
        fun getAddQuizParam(title: String, content: String, type: String): ApiParams {
            return getWithIdParams().addParam("title", title).addParam("content", content).addParam("type", type)
        }

        val ACT_QUIZ_TYPE_SEARCH = "quiz_type_search"
        fun getQuizTypeSearchParam(type: String = "", page: Int): ApiParams {
            return getWithPageParams(page).addParam("type", type)
        }

        val ACT_QUIZ_SEARCH = "quiz_search"
        fun getQuizSearchParam(keyword: String): ApiParams {
            return getBaseParams().addParam("keyword", keyword)
        }

        val ACT_PRAISE = "praise"
        fun getPraiseParams(aid: String): ApiParams {
            return getWithIdParams().addParam("aid", aid)
        }

        val ACT_ARTICLE_PRAISE = "quiz_praise"
        fun getArticlePraiseParam(qid: String): ApiParams {
            return getWithIdParams().addParam("qid", qid)
        }

        /**              qaa model  end           **/
        /**              order model start        **/

        val ORDER_MODEL = "order"
        val ACT_QORDER = "qorder"
        fun getQorderParam(location: String = "", page: Int): ApiParams {
            return getWithPageParams(page).addParam("location", location)
        }

        val ACT_ORDER_RECEIVING = "order_receiving"
        fun getOrderReceivingParam(oid: String) = getWithIdParams().addParam("oid", oid)

        val ACT_MY_QORDER = "my_qorder"
        fun getMyQorderParam(status: String, page: Int) = getWithPageParams(page).addParam("status", status)

        val ACT_MY_ROADWORK_QORDER = "my_roadwork_qorder"
        fun getMyRoadworkQorder(page: Int) = getWithPageParams(page)

        val ACT_PLAY_PIC = "play_pic"

        val ACT_EDIT_QORDER_STATUS = "edit_qorder_status"
        fun getEditQorderStatusParam(oid: String, status: String, code: String, checkCode: String = "") =
                getWithIdParams()
                        .addParam("oid", oid)
                        .addParam("status", status)
                        .addParam("code", code)
                        .addParam("check_code", checkCode)

        val ACT_QORDER_DETAIL = "qorder_detail"
        fun getOrderDetailParam(oid: String) = getWithIdParams().addParam("oid", oid)

        val ACT_QORDER_DEPOSIT = "qorder_deposit"
        fun getQorderDepositParam(oid: String) = getWithIdParams().addParam("oid", oid)

        val ACT_ORDER_TIME = "edit_appointment_time"
        fun getOrderTimeParam(qid: String, appointment_time: String) = getWithIdParams().addParam("qid", qid).addParam("appointment_time", appointment_time)

        val ACT_MY_SALE_QORDER = "my_sale_qorder"
        fun getMySaleQorderParam(page: Int) = getWithPageParams(page)

        val ACT_ADD_ORDER = "add_order"
        fun getAddOrderParams(invoice: String = "0", paytype: String, post_data: String, discount: String) =
                getWithIdParams()
                        .addParam("invoice", invoice)
                        .addParam("paytype", paytype)
                        .addParam("discount", discount)
                        .addParam("post_data", post_data)

        val ACT_PAY = "pay"
        fun getPayParam(oid: String, gid: String, paytype: String) =
                getWithIdParams()
                        .addParam("oid", oid)
                        .addParam("gid", gid)
                        .addParam("paytype", paytype)

        val ACT_REFUND = "refund"
        fun getRefundParam(name: String, phone: String, oid: String, content: String) =
                getWithIdParams()
                        .addParam("oid", oid)
                        .addParam("name", name)
                        .addParam("phone", phone)
                        .addParam("content", content)

        //优惠券列表
        val ACT_MY_DISCOUNT = "my_discount"

        fun getMyDiscountParam(shopid: String) = getWithIdParams().addParam("shopid", shopid)

        val ACT_ADDRESS = "address"
        fun getAddressParams(aid: String, freight: String) = getWithIdParams().addParam("aid", aid).addParam("freight", freight)


        /**              order model end        **/


        /**              web url                **/
        val ACT_ABOUT_URL = "${Constants.BASE_IP}view/about.html"
        val ACT_HELP_URL = "${Constants.BASE_IP}view/help.html"
        val ACT_PROTOCOL_URL = "${Constants.BASE_IP}view/t_rules.html?pid=13"


        val ACT_PROVINCE = "province"
        val ACT_CITY = "city"
        val ACT_PROVINCE_CITY_AREA = "province_city_area"

        fun getDefaultAddress(freight: String) = getWithIdParams().addParam("freight", freight)

        fun getProvinceParam() = getBaseParams()

        fun getCityParam(pid: String): ApiParams {
            val param = getBaseParams()
            param.addParam("pid", pid)
            return param
        }

        fun getProvinceCityAreaParam(cid: String): ApiParams {
            val param = getBaseParams()
            param.addParam("cid", cid)
            return param
        }


        val ACT_PLATFORM_DISCOUNT = "platform_discount"
        fun getPlatformDiscountParam() = getBaseParams()
    }
}