package com.android.ql.lf.zwlogistics.ui.fragment.mine.driver

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.ql.lf.zwlogistics.R
import com.android.ql.lf.zwlogistics.ui.activity.FragmentContainerActivity
import com.android.ql.lf.zwlogistics.ui.activity.MainActivity
import com.android.ql.lf.zwlogistics.ui.fragment.base.BaseNetWorkingFragment
import com.android.ql.lf.zwlogistics.ui.fragment.mine.car.NewCarAuthFragment
import com.android.ql.lf.zwlogistics.utils.showInfoDialog
import kotlinx.android.synthetic.main.fragment_mine_person_auth_layout.*

class MinePersonAuthFragment : BaseNetWorkingFragment(), FragmentContainerActivity.OnBackPressListener {


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId() = R.layout.fragment_mine_person_auth_layout

    override fun initView(view: View?) {
        (mContext as FragmentContainerActivity).setOnBackPressListener(this)
        mTvPersonAuthNext.setOnClickListener {
            FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setTitle("新车认证").setClazz(NewCarAuthFragment::class.java).start()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.auth_person,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.mMenuJump){
            startActivity(Intent(mContext,MainActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPress(): Boolean {
        showInfoDialog("身份认证通过才能参与竞标，建议继续完善资料~","放弃","继续完善",{
            finish()
        },null)
        return true
    }
}