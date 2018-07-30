package com.ypz.supportquicknews.statistics;

import cn.bmob.v3.BmobObject;

/**
 * Created by kingadmin on 2018/3/6.
 */

public class NewsTypeStatistics extends BmobObject {

    private String newsType;
    private String date;
    private String yearMonth;
    private String id;

    public NewsTypeStatistics(String newsType, String date, String yearMonth, String id) {
        this.newsType = newsType;
        this.date = date;
        this.yearMonth = yearMonth;
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
