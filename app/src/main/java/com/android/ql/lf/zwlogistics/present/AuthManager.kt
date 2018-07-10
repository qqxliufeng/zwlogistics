package com.android.ql.lf.zwlogistics.present

import android.content.Intent
import com.android.ql.lf.zwlogistics.data.PostDriverAuthBean
import com.android.ql.lf.zwlogistics.ui.fragment.mine.driver.MinePersonAuthFragment
import com.android.ql.lf.zwlogistics.utils.ImageUploadHelper
import com.android.ql.lf.zwlogistics.utils.RequestParamsHelper
import com.zhihu.matisse.Matisse
import okhttp3.MultipartBody

class AuthManager{

    companion object {

        fun authDriver(mPresent:GetDataFromNetPresent,postDriverAuthBean:PostDriverAuthBean,actionStart:()->Unit,actionEnd:()->Unit,actionFailed:()->Unit){
            ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                override fun onActionStart() {
                    actionStart()
                }

                override fun onActionEnd(builder: MultipartBody.Builder?) {
                    actionEnd()
                    if (builder!=null){
                        builder.addFormDataPart("user_rank_nickname",postDriverAuthBean.driverName)
                        builder.addFormDataPart("user_rank_number",postDriverAuthBean.driverIdCardNum)
                        builder.addFormDataPart("user_rank_phone",postDriverAuthBean.driverPhone)
                        mPresent.uploadFile(0x0, RequestParamsHelper.USER_MODEL, RequestParamsHelper.ACT_RANKDO,builder.build().parts())
                    }
                }

                override fun onActionFailed() {
                    actionFailed()
                }

            }).upload(postDriverAuthBean.photos,postDriverAuthBean.keys)
        }

        fun handleResultData(data:Intent,tempSelectFlag:Int,postDriverAuthBean: PostDriverAuthBean,action:(String)->Unit){
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

    }

}