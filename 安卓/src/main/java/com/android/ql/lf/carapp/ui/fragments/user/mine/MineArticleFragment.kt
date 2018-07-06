package com.android.ql.lf.carapp.ui.fragments.user.mine

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.android.ql.lf.carapp.R
import com.android.ql.lf.carapp.ui.fragments.BaseFragment
import kotlinx.android.synthetic.main.fragment_mine_article_layout.*
import org.jetbrains.anko.bundleOf
import android.widget.LinearLayout
import android.util.TypedValue
import android.support.design.widget.TabLayout
import java.lang.reflect.Field


/**
 * Created by liufeng on 2018/2/5.
 */
class MineArticleFragment : BaseFragment() {

    companion object {
        val TITLES = listOf("发帖", "回帖")
    }

    override fun getLayoutId() = R.layout.fragment_mine_article_layout

    override fun initView(view: View?) {
        mVpMineArticleContainer.adapter = MineArticleAdapter(childFragmentManager)
        mTlMineArticleTitle.setupWithViewPager(mVpMineArticleContainer)
        mTlMineArticleTitle.post {
            setIndicator(mTlMineArticleTitle, 60, 60)
        }
    }

    private fun setIndicator(tabs: TabLayout, leftDip: Int, rightDip: Int) {
        val tabLayout = tabs.javaClass
        var tabStrip: Field? = null
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        tabStrip!!.isAccessible = true
        var llTab: LinearLayout? = null
        try {
            llTab = tabStrip.get(tabs) as LinearLayout
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        val left = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip.toFloat(), resources.displayMetrics).toInt()
        val right = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip.toFloat(), resources.displayMetrics).toInt()

        for (i in 0 until llTab!!.childCount) {
            val child = llTab.getChildAt(i)
            child.setPadding(0, 0, 0, 0)
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.leftMargin = left
            params.rightMargin = right
            child.layoutParams = params
            child.invalidate()
        }
    }

    class MineArticleAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

        override fun getItem(position: Int) = when (position) {
            0 -> {
                MineArticleWriteItemFragment.newInstance()
            }
            1 -> {
                MineArticleReplyItemFragment.newInstance()
            }
            else -> {
                null
            }
        }

        override fun getCount() = TITLES.size

        override fun getPageTitle(position: Int) = TITLES[position]
    }

}