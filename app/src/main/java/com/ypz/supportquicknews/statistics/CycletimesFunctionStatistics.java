package com.ypz.supportquicknews.statistics;

/**
 * Created by kingadmin on 2018/4/7.
 */

public class CycletimesFunctionStatistics {

    private String date;

    private String ModuleName;

    private String FunctionName;

    public CycletimesFunctionStatistics(String date, String moduleName, String functionName) {
        this.date = date;
        ModuleName = moduleName;
        FunctionName = functionName;
    }

    public String getDate() {
        return date;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public String getFunctionName() {
        return FunctionName;
    }
}
