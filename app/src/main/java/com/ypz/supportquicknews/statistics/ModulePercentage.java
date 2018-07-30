package com.ypz.supportquicknews.statistics;

import cn.bmob.v3.BmobObject;

/**
 * Created by kingadmin on 2018/4/7.
 */

public class ModulePercentage extends BmobObject{

    private String ModuleName;

    private String date;

    private String userId;

    private int times;

    private String percentage;

    private int allModuleTimes;

    public ModulePercentage(String moduleName, String date, String userId, int times, String percentage, int allModuleTimes) {
        ModuleName = moduleName;
        this.date = date;
        this.userId = userId;
        this.times = times;
        this.percentage = percentage;
        this.allModuleTimes = allModuleTimes;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public int getAllModuleTimes() {
        return allModuleTimes;
    }

    public void setAllModuleTimes(int allModuleTimes) {
        this.allModuleTimes = allModuleTimes;
    }
}
