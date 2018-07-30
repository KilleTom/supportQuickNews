package com.ypz.supportquicknews.base.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.text.TextUtils

/**
 * Created by kingadmin on 2017/10/7.
 */

open class BaseFragmentStatePagerAdapter(fm: FragmentManager, val baseFragmentEntities: List<Fragment>?, val titles: List<String?>)
    : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = baseFragmentEntities!![position]
    override fun getPageTitle(position: Int): CharSequence? =
            if (TextUtils.isEmpty(titles[position]) || titles[position] == null) "" else titles[position]

    override fun getCount(): Int =
            if (baseFragmentEntities == null) 0
            else baseFragmentEntities.size
}
