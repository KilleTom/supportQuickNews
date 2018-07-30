package com.ypz.supportquicknews.statistics;

import cn.bmob.v3.BmobObject;

/**
 * Created by kingadmin on 2018/3/6.
 */

public class FunctionStatistics extends BmobObject{

    private String moduleName;

    private String functionName;

    private int times;

    private String date;

    private String userId;

    public FunctionStatistics(String moduleName, String functionName, int times, String date, String userId) {
        this.moduleName = moduleName;
        this.functionName = functionName;
        this.times = times;
        this.date = date;
        this.userId = userId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
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
}
