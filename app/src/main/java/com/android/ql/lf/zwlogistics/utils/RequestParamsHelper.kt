package com.android.ql.lf.zwlogistics.utils

import com.android.ql.lf.zwlogistics.component.ApiParams
import com.android.ql.lf.zwlogistics.data.UserInfo

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

        private fun getBaseParams(mod:String,act:String): ApiParams {
            val params = ApiParams()
            params.addParam(ApiParams.MOD_NAME, mod).addParam(ApiParams.ACT_NAME, act)
            params.addParam("pt", "android")
            return params
        }

        private fun getWithIdParams(): ApiParams {
            val params = getBaseParams()
            params.addParam("uid", UserInfo.getInstance().user_id)
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


        /**              system model  start           **/
        val SYSTEM_MODEL = "system"
        val ACT_PHONE = "phone"

        fun getPhoneParam(phone: String) = getBaseParams(SYSTEM_MODEL, ACT_PHONE)
                .addParam("phone",phone)



        /**              system model  end           **/





        /**              login model  start           **/
        val LOGIN_MODEL = "login"
        val ACT_REGISTER = "loginDo"
        val ACT_LOGIN = "login"
        val ACT_FORGETPW = "loginUp"
        val ACT_BIND_PHONE = "loginUpDo"

        fun getRegisterParams(phone: String = "", pass: String = "",code: String = ""): ApiParams {
            val params = getBaseParams().addParam(ApiParams.MOD_NAME, LOGIN_MODEL).addParam(ApiParams.ACT_NAME, ACT_REGISTER)
            params.addParam("phone", phone).addParam("pass", pass).addParam("code",code)
            return params
        }

        fun getLoginParams(phone: String = "", pass: String = ""): ApiParams {
            val params = getBaseParams().addParam(ApiParams.MOD_NAME, LOGIN_MODEL).addParam(ApiParams.ACT_NAME, ACT_LOGIN)
            params.addParam("phone", phone).addParam("pass", pass)
            return params
        }

        fun getBindPhoneParams(uid: String,phone:String) = getBaseParams().addParam("uid",uid)
                .addParam(ApiParams.MOD_NAME, LOGIN_MODEL).addParam(ApiParams.ACT_NAME, ACT_BIND_PHONE)
                .addParam("phone",phone)


        fun getUserProtocolParam(type: String = "1") = getBaseParams(LOGIN_MODEL,"deal").addParam("type",type)


        fun getForgetPWParams(phone: String = "", pass: String = "",repass:String = ""): ApiParams {
            val params = getBaseParams().addParam(ApiParams.MOD_NAME, LOGIN_MODEL).addParam(ApiParams.ACT_NAME,ACT_FORGETPW)
            params.addParam("phone", phone).addParam("pass", pass).addParam("repass",repass)
            return params
        }


        val ACT_QQLOGIN = "loginQq"
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

        val USER_MODEL = "user"

        val ACT_UPDATE_FACE = "pic"

        val ACT_UPDATE_NICK_NAME = "nickname"

        val ACT_UPDATE_PASSWORD = "password"

        val ACT_RANKDO = "rankDo"


        fun getUpdateNickNameParams(nickname:String) = getWithIdParams().addParam(ApiParams.MOD_NAME, USER_MODEL)
                .addParam(ApiParams.ACT_NAME, ACT_UPDATE_NICK_NAME).addParam("nickname",nickname)

        fun getResetPasswordParam(ypass:String,xpass:String,rpass:String) = getWithIdParams().addParam(ApiParams.MOD_NAME, USER_MODEL)
        .addParam(ApiParams.ACT_NAME, ACT_UPDATE_PASSWORD).addParam("ypass",ypass).addParam("xpass",xpass).addParam("rpass",rpass)


        fun getDriverAuthParams() = getWithIdParams()
                        .addParam(ApiParams.MOD_NAME, USER_MODEL)
                        .addParam(ApiParams.ACT_NAME, ACT_RANKDO)


        val ACT_PERSONAL = "info"
        fun getPersonalParam(uid: String) = getBaseParams().addParam(ApiParams.MOD_NAME, USER_MODEL).addParam(ApiParams.ACT_NAME,ACT_PERSONAL).addParam("uid", uid)


        val ACT_RANK_INFO = "rankinfo"

        fun getAuthInfoParams() = getWithIdParams().addParam(ApiParams.MOD_NAME, USER_MODEL).addParam(ApiParams.ACT_NAME, ACT_RANK_INFO)


        val ACT_CAR_LIST = "vehicleList"
        fun getCarListParam() = getWithIdParams().addParam(ApiParams.MOD_NAME, USER_MODEL).addParam(ApiParams.ACT_NAME,ACT_CAR_LIST)

        val ACT_CAR_UPDATE = "vehicleUp"


        fun getCarParamsParams(id:String = "") = getWithIdParams().addParam(ApiParams.MOD_NAME, USER_MODEL).addParam(ApiParams.ACT_NAME, ACT_CAR_UPDATE).addParam("id",id)

        val ACT_CAR_VEHICLEDO = "vehicleDo"

        val ACT_CAR_VEHICLEDEL = "vehicledel"

        fun getCarDeleteParam(id: String= "") = getWithIdParams().addParam(ApiParams.MOD_NAME, USER_MODEL).addParam(ApiParams.ACT_NAME, ACT_CAR_VEHICLEDEL).addParam("id",id)


        val ACT_CAR_VEHICLEINFO = "vehicleinfo"

        fun getCarInfoParam(id: String) = getWithIdParams().addParam(ApiParams.MOD_NAME, USER_MODEL).addParam(ApiParams.ACT_NAME,ACT_CAR_VEHICLEINFO).addParam("id",id)

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


        /**             area model start    **/

        val AREA_MODEL = "area"

        val ACT_PROVINCE = "province"
        val ACT_CITY = "city"
        val ACT_PROVINCE_CITY_AREA = "district"


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

        /**             area model end    **/

    }
}