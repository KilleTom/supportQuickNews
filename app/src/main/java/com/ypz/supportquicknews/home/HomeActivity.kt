package com.ypz.supportquicknews.home


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.jaeger.library.StatusBarUtil
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.base.DepthPageTransformer
import com.ypz.supportquicknews.base.LR_Sucess
import com.ypz.supportquicknews.moduleFunction.humorousMoment.HumorousMomentFragment
import com.ypz.supportquicknews.moduleFunction.lifeHelper.LifehelperFragment
import com.ypz.supportquicknews.moduleFunction.news.NewsFragment
import com.ypz.supportquicknews.moduleFunction.setting.SettingFragment
import kotlinx.android.synthetic.main.activity_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeActivity : FragmentActivity() {

    private lateinit var moduleFragments: MutableList<Fragment>
    private lateinit var homeViewpagerAdapter: HomeViewpagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        EventBus.getDefault().register(this)
        StatusBarUtil.setColor(this, resources.getColor(android.R.color.holo_blue_bright), 156)
        moduleFragments = ArrayList<Fragment>()
        moduleFragments.add(NewsFragment())
        moduleFragments.add(HumorousMomentFragment())
        moduleFragments.add(LifehelperFragment())
        val settingFragment = SettingFragment()
        moduleFragments.add(settingFragment)
        homeViewpagerAdapter = HomeViewpagerAdapter(supportFragmentManager, moduleFragments, arrayListOf("新闻咨询", "幽默时刻", "轻松生活", "个人设置"))
        vp.adapter = homeViewpagerAdapter
        var menuItem: MenuItem? = null
        disableShiftMode(bnv)
        bnv.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.news -> {
                    vp.setCurrentItem(0, true)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.humorous -> {
                    vp.setCurrentItem(1, true)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.life -> {
                    vp.setCurrentItem(2, true)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.settings -> {
                    vp.setCurrentItem(3, true)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                runOnUiThread {
                    if (position == 0 || position == 3) StatusBarUtil.setColor(this@HomeActivity, resources.getColor(android.R.color.holo_blue_bright), 156)
                    if (position == 1) StatusBarUtil.setColor(this@HomeActivity, Color.parseColor("#F4F4E4"))
                    if (position == 2) StatusBarUtil.setColor(this@HomeActivity,resources.getColor(R.color.navajowhite),128)
                    if (menuItem != null) menuItem!!.setChecked(false)
                    else bnv.menu.getItem(0).setChecked(false)
                    menuItem = bnv.menu.getItem(position)
                    menuItem!!.setChecked(true)
                }
            }
        })
        vp.setPageTransformer(true, DepthPageTransformer())

    }

    override fun onStart() {
        super.onStart()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun lrSucess(lrLR_Sucess: LR_Sucess) {
        (moduleFragments[3] as SettingFragment).showUi()
    }


    @SuppressLint("RestrictedApi")
    fun disableShiftMode(navigationView: BottomNavigationView) {

        val menuView = navigationView.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuView.childCount) {
                val itemView = menuView.getChildAt(i) as BottomNavigationItemView
                itemView.setShiftingMode(false)
                itemView.setChecked(itemView.itemData.isChecked)
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }
}
