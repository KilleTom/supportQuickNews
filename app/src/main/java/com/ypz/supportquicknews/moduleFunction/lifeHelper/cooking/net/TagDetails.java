package com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kingadmin on 2018/4/5.
 */

public class TagDetails {

    @SerializedName("resultcode")
    private String resultcode;
    @SerializedName("reason")
    private String reason;
    @SerializedName("result")
    private List<Result> result = null;
    @SerializedName("error_code")
    private int errorCode;

    public TagDetails(String resultcode, String reason, List<Result> result, int errorCode) {
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

    public List<Result> getResult() {
        return result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public class Result{
        @SerializedName("parentId")
        private String parentId;
        @SerializedName("name")
        private String name;
        @SerializedName("list")
        private List<TagItem> list ;

        public Result(String parentId, String name, List<TagItem> list) {
            this.parentId = parentId;
            this.name = name;
            this.list = list;
        }

        public String getParentId() {
            return parentId;
        }

        public String getName() {
            return name;
        }

        public List<TagItem> getList() {
            return list;
        }
    }

    public class TagItem {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("parentId")
        private String parentId;

        public TagItem(int id, String name, String parentId) {
            this.id = id;
            this.name = name;
            this.parentId = parentId;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getParentId() {
            return parentId;
        }

    }
}
