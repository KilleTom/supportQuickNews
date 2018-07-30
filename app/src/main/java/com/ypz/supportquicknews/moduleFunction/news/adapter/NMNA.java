package com.ypz.supportquicknews.moduleFunction.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.ypz.supportquicknews.moduleFunction.news.emptyFragment.NewsEmptyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kingadmin on 2017/5/13.
 */

public class NMNA extends FragmentStatePagerAdapter {
    private List<NewsEmptyFragment> newsEmptyFragments;
    private List<Integer> destoriesPosition = new ArrayList<>();
    private List<String> titles;

    public NMNA(FragmentManager fm, List<NewsEmptyFragment> newsEmptyFragments, List<String> titles) {
        super(fm);
        this.newsEmptyFragments = newsEmptyFragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return newsEmptyFragments.get(position);
    }

    @Override
    public int getCount() {
        return newsEmptyFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.i("ypz","psss"+position);
        return titles.get(position%titles.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i("ypz","destroyItem"+position);
        destoriesPosition.add(position);
        super.destroyItem(container, position, object);
    }

    public List<Integer> getDestoriesPosition() {
        return destoriesPosition;
    }
}
