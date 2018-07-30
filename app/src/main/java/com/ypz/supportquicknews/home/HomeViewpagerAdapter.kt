package com.ypz.supportquicknews.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.ypz.supportquicknews.base.adapter.BaseFragmentStatePagerAdapter


/**
 * Created by kingadmin on 2018/2/8.
 */

class HomeViewpagerAdapter(fm: FragmentManager, baseFragmentEntities: List<out Fragment>?, titles: ArrayList<String>) :
        BaseFragmentStatePagerAdapter(fm, baseFragmentEntities, titles)
