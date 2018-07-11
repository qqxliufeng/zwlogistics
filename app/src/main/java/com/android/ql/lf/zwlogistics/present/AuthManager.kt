package com.android.ql.lf.zwlogistics.present

import android.content.Intent
import com.android.ql.lf.zwlogistics.data.CarParamBean
import com.android.ql.lf.zwlogistics.data.PostCarAuthBean
import com.android.ql.lf.zwlogistics.data.PostDriverAuthBean
import com.android.ql.lf.zwlogistics.ui.fragment.mine.car.NewCarAuthFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MinePersonAuthFragment
import com.android.ql.lf.zwlogistics.utils.ImageUploadHelper
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.android.ql.lf.zwlogistics.utils.RxBus
import com.google.gson.Gson
import com.zhihu.matisse.Matisse
import okhttp3.MultipartBody
import org.json.JSONObject

class AuthManager {

    companion object {

        fun authDriver(mPresent: GetDataFromNetPresent, postDriverAuthBean: PostDriverAuthBean, actionStart: () -> Unit, actionEnd: () -> Unit, actionFailed: () -> Unit) {
            ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                override fun onActionStart() {
                    actionStart()
                }

                override fun onActionEnd(builder: MultipartBody.Builder?) {
                    actionEnd()
                    if (builder != null) {
                        builder.addFormDataPart("user_rank_nickname", postDriverAuthBean.driverName)
                        builder.addFormDataPart("user_rank_number", postDriverAuthBean.driverIdCardNum)
                        builder.addFormDataPart("user_rank_phone", postDriverAuthBean.driverPhone)
                        mPresent.uploadFile(0x0, RequestParamsHelper.USER_MODEL, RequestParamsHelper.ACT_RANKDO, builder.build().parts())
                    }
                }

                override fun onActionFailed() {
                    actionFailed()
                }

            }).upload(postDriverAuthBean.photos, postDriverAuthBean.keys)
        }

        fun authCar(mPresent: GetDataFromNetPresent, postCarAuthBean: PostCarAuthBean, actionStart: () -> Unit, actionEnd: () -> Unit, actionFailed: () -> Unit) {
            ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                override fun onActionStart() {
                    actionStart()
                }

                override fun onActionEnd(builder: MultipartBody.Builder?) {
                    actionEnd()
                    if (builder != null) {
                        builder.addFormDataPart("id", postCarAuthBean.driver_car_id)
                        builder.addFormDataPart("vehicle_type", postCarAuthBean.driver_car_type)
                        builder.addFormDataPart("vehicle_length", postCarAuthBean.driver_car_length)
                        builder.addFormDataPart("vehicle_weight", postCarAuthBean.driver_car_weight)
                        builder.addFormDataPart("vehicle_name", postCarAuthBean.driver_car_name)
                        builder.addFormDataPart("vehicle_year", postCarAuthBean.driver_car_date)
                        mPresent.uploadFile(0x0, RequestParamsHelper.USER_MODEL, RequestParamsHelper.ACT_CAR_VEHICLEDO, builder.build().parts())
                    }
                }

                override fun onActionFailed() {
                    actionFailed()
                }

            }).upload(postCarAuthBean.getPhotos(), postCarAuthBean.getKeys())
        }

        fun handleResultData(data: Intent, tempSelectFlag: Int, postDriverAuthBean: PostDriverAuthBean, action: (String) -> Unit) {
            val result = Matisse.obtainPathResult(data)
            when (tempSelectFlag) {
                MinePersonAuthFragment.FACE_FLAG -> {
                    result[0].let {
                        action(it)
                        postDriverAuthBean.faceUrl = it
                    }
                }
                MinePersonAuthFragment.IDCARD_FRONT_FLAG -> {
                    result[0].let {
                        action(it)
                        postDriverAuthBean.idCarFrontdUrl = it
                    }
                }
                MinePersonAuthFragment.IDCARD_BACK_GROUND_FLAG -> {
                    result[0].let {
                        action(it)
                        postDriverAuthBean.idCardBackgroundkUrl = it
                    }
                }
                MinePersonAuthFragment.DRIVER_FLAG -> {
                    result[0].let {
                        action(it)
                        postDriverAuthBean.driverUrl = it
                    }
                }
                MinePersonAuthFragment.CYZG_FLAG -> {
                    result[0].let {
                        action(it)
                        postDriverAuthBean.cyzgUrl = it
                    }
                }
            }
        }

        fun handleResultData(data: Intent, tempSelectFlag: Int, postCarAuthBean: PostCarAuthBean, action: (String) -> Unit) {
            val result = Matisse.obtainPathResult(data)
            when (tempSelectFlag) {
                NewCarAuthFragment.DRIVER_CARD_FLAG -> {
                    result[0].let {
                        action(it)
                        postCarAuthBean.driver_card_path = it
                    }
                }
                NewCarAuthFragment.YYZ_CARD_FLAG -> {
                    result[0].let {
                        action(it)
                        postCarAuthBean.driver_yyz_path = it
                    }
                }
                NewCarAuthFragment.CAR_NUM_FLAG -> {
                    result[0].let {
                        action(it)
                        postCarAuthBean.driver_car_num_path = it
                    }
                }
            }
        }


        fun parseCarParams(name: String, jsonObject: JSONObject): ArrayList<CarParamBean>? {
            val jsonArray = jsonObject.optJSONArray(name)
            return if (jsonArray != null && jsonArray.length() > 0) {
                val arrayList = arrayListOf<CarParamBean>()
                (0 until jsonArray.length()).forEach {
                    arrayList.add(Gson().fromJson(jsonArray.optJSONObject(it).toString(), CarParamBean::class.java))
                }
                arrayList
            } else {
                null
            }
        }

        fun rxBusPostCarApply(flag:String = ""){
            RxBus.getDefault().post(flag)
        }

    }

}