package com.ypz.supportquicknews.moduleFunction.news;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.ypz.supportquicknews.R;
import com.ypz.supportquicknews.databinding.NewsFragmentBinding;
import com.ypz.supportquicknews.moduleFunction.news.adapter.NMNA;
import com.ypz.supportquicknews.moduleFunction.news.emptyFragment.NewsEmptyFragment;
import com.ypz.supportquicknews.moduleFunction.news.emptyModule.LoadMoreORRefrsh;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kingadmin on 2018/2/8.
 */

public class NewsFragment extends Fragment {

    private List<NewsEmptyFragment> newsEmptyFragents = new ArrayList<>();
    private NewsFragmentBinding newsFragmentBinding;
    private List<String> titles = Arrays.asList("头条", "社会", "国内", "国际",
            "娱乐", "体育", "军事", "科技", "财经", "时尚");
    private List<String> typeKeys = Arrays.asList("top", "shehui", "guonei", "guoji",
            "yule", "tiyu", "junshi", "keji", "caijing", "shishang");
    private SmartRefreshLayout smartRefreshLayout;
    private int pagePosstion;

    public NewsFragment() {
        for (int i = 0; i < titles.size(); i++) {
            NewsEmptyFragment newsEmptyFragent = new NewsEmptyFragment();
            newsEmptyFragent.setNetType(typeKeys.get(i));
            newsEmptyFragent.setTitle(titles.get(i));
            newsEmptyFragents.add(newsEmptyFragent);
        }
        Log.i("ypz", newsEmptyFragents.size() + "size");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EventBus.getDefault().isRegistered(this)) return;
        else EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        newsFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.news_fragment, container, false);
        View view = newsFragmentBinding.getRoot();
        ViewPager viewPager = view.findViewById(R.id.vp);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        NewsUIModule newsUIModule = new NewsUIModule();
       // toolbar.setTitle("头条");
        newsFragmentBinding.setUiModule(newsUIModule);
        viewPager.setAdapter(new NMNA(getChildFragmentManager(), newsEmptyFragents, titles));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                pagePosstion = position;
                NewsEmptyFragment.Companion.setSaveShowNetType(typeKeys.get(position));
                newsEmptyFragents.get(position).setNetType(typeKeys.get(position));
                newsEmptyFragents.get(pagePosstion).showData();
                toolbar.setTitle(titles.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        TabLayout tabLayout = view.findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        Log.i("ypz", "setAda");
        smartRefreshLayout = view.findViewById(R.id.smartrefresh);
        smartRefreshLayout.setOnRefreshListener(refreshlayout -> newsEmptyFragents.get(pagePosstion).refresh());
        smartRefreshLayout.setOnLoadmoreListener(refreshlayout -> newsEmptyFragents.get(pagePosstion).loadMore());
        smartRefreshLayout.setDisableContentWhenLoading(true);
        smartRefreshLayout.setDisableContentWhenRefresh(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (newsEmptyFragents!=null){
            Log.i("ypz", newsEmptyFragents.size() + "newsEmptyFragents.size()");
            for (int i = 0; i < newsEmptyFragents.size(); i++) {
                if (i>=titles.size()) return;
                newsEmptyFragents.get(i).setTitle(titles.get(i));
                newsEmptyFragents.get(i).setNetType(typeKeys.get(i));
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void finshloadMoreOrRefresh(LoadMoreORRefrsh loadMoreORRefrsh) {
        switch (loadMoreORRefrsh.getStatus()) {
            case 0:smartRefreshLayout.finishRefresh();
                break;
            case 1:smartRefreshLayout.finishLoadmore();
                break;
            case -1:smartRefreshLayout.autoRefresh();
                break;
        }
    }


}
