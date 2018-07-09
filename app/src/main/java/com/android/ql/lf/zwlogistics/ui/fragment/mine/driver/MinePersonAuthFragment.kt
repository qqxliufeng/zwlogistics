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
import org.jetbrains.anko.bundleOf

class MinePersonAuthFragment : BaseNetWorkingFragment(), FragmentContainerActivity.OnBackPressListener {

    companion object {
        val IS_SHOW_JUMP = "is_show_jump"
        val SHOW_JUMP = 0
        val NO_SHOW_JUMP = 1

        fun startAuthFragment(mContext:Context,showJump:Int){
            FragmentContainerActivity
                    .from(mContext)
                    .setClazz(MinePersonAuthFragment::class.java)
                    .setTitle("司机身份认证")
                    .setExtraBundle(bundleOf(Pair(IS_SHOW_JUMP, showJump)))
                    .setNeedNetWorking(true)
                    .start()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId() = R.layout.fragment_mine_person_auth_layout

    override fun initView(view: View?) {
        (mContext as FragmentContainerActivity).setOnBackPressListener(this)
        mTvPersonAuthNext.setOnClickListener {
            FragmentContainerActivity
                    .from(mContext)
                    .setNeedNetWorking(true)
                    .setTitle("新车认证")
                    .setExtraBundle(bundleOf(Pair(IS_SHOW_JUMP, SHOW_JUMP)))
                    .setClazz(NewCarAuthFragment::class.java)
                    .start()
        }

        if (arguments!!.getInt(IS_SHOW_JUMP) == SHOW_JUMP){
            mAsvStep.visibility = View.VISIBLE
        }else{
            mAsvStep.visibility = View.GONE
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (arguments!!.getInt(IS_SHOW_JUMP) == SHOW_JUMP) {
            inflater!!.inflate(R.menu.auth_person, menu)
        }else{
            super.onCreateOptionsMenu(menu, inflater)
        }
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