package com.android.ql.lf.zwlogistics.data

import com.android.ql.lf.carapp.data.ImageBean

class PostCarAuthBean {

    var driver_car_id = "0"
    var driver_card_path = ""
    var driver_yyz_path = ""
    var driver_car_num_path = ""

    var driver_car_type = ""
    var driver_car_length = ""
    var driver_car_weight = ""
    var driver_car_name = ""
    var driver_car_date = ""


    fun getImageLists():ArrayList<String>?{
        if (driver_card_path == "" || driver_yyz_path == "" || driver_car_num_path == ""){
            return null
        }
        return arrayListOf(driver_card_path,driver_yyz_path,driver_car_num_path)
    }


    fun getKeys():ArrayList<String>{
        return arrayListOf("vehicle_driving","vehicle_run","vehicle_number")
    }

    fun getPhotos(): java.util.ArrayList<ImageBean> {
        val arrayList = java.util.ArrayList<ImageBean>()
        arrayList.add(ImageBean(null, driver_card_path))
        arrayList.add(ImageBean(null, driver_yyz_path))
        arrayList.add(ImageBean(null, driver_car_num_path))
        return arrayList
    }

    fun isEmpty():String{
        if (driver_card_path == ""){
            return "请选择车辆行驶证"
        }
        if (driver_yyz_path == ""){
            return "请选择车辆营运证"
        }
        if (driver_car_num_path == ""){
            return "请选择车牌号"
        }

        if (driver_car_type == ""){
            return "请选择车型"
        }
        if (driver_car_length == ""){
            return "请选择车长"
        }
        if (driver_car_weight == ""){
            return "请输入载重"
        }
        if (driver_car_name == ""){
            return "请输入车辆名称"
        }
        if (driver_car_date == ""){
            return "请选择出厂年份"
        }
        return ""
    }

}