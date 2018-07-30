package com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kingadmin on 2018/4/5.
 */

public class DelicacyDetails {

    @SerializedName("resultcode")
    private String resultcode;
    @SerializedName("reason")
    private String reason;
    @SerializedName("result")
    private Result result;
    @SerializedName("error_code")
    private int errorCode;

    public DelicacyDetails(String resultcode, String reason, Result result, int errorCode) {
        this.resultcode = resultcode;
        this.reason = reason;
        this.result = result;
        this.errorCode = errorCode;
    }

    public String getResultcode() {
        return resultcode;
    }

    public String getReason() {
        return reason;
    }

    public Result getResult() {
        return result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public class Result{
        private List<FindRecipes.Datum> data;

        public Result(List<FindRecipes.Datum> data) {
            this.data = data;
        }

        public List<FindRecipes.Datum> getData() {
            return data;
        }
    }

}
