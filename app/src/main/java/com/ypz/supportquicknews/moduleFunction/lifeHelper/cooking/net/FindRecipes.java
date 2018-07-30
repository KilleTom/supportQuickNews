package com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by kingadmin on 2018/4/5.
 */

public class FindRecipes {

    @SerializedName("resultcode")
    private String resultcode;
    @SerializedName("reason")
    private String reason;
    @SerializedName("result")
    private Result result;
    @SerializedName("error_code")
    private int errorCode;

    public FindRecipes(String resultcode, String reason, Result result, int errorCode) {
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
        @SerializedName("data")
        private List<Datum> data = null;
        @SerializedName("totalNum")
        private String totalNum;
        @SerializedName("pn")
        private String pn;
        @SerializedName("rn")
        private String rn;

        public Result(List<Datum> data, String totalNum, String pn, String rn) {
            this.data = data;
            this.totalNum = totalNum;
            this.pn = pn;
            this.rn = rn;
        }

        public List<Datum> getData() {
            return data;
        }

        public String getTotalNum() {
            return totalNum;
        }

        public String getPn() {
            return pn;
        }

        public String getRn() {
            return rn;
        }
    }

    public class Datum extends BmobObject {
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("tags")
        private String tags;
        @SerializedName("imtro")
        private String imtro;
        @SerializedName("ingredients")
        private String ingredients;
        @SerializedName("burden")
        private String burden;
        @SerializedName("albums")
        private List<String> albums ;
        @SerializedName("steps")
        private List<Step> steps ;

        public Datum(int id, String title, String tags, String imtro, String ingredients, String burden, List<String> albums, List<Step> steps) {
            this.id = id;
            this.title = title;
            this.tags = tags;
            this.imtro = imtro;
            this.ingredients = ingredients;
            this.burden = burden;
            this.albums = albums;
            this.steps = steps;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getTags() {
            return tags;
        }

        public String getImtro() {
            return imtro;
        }

        public String getIngredients() {
            return ingredients;
        }

        public String getBurden() {
            return burden;
        }

        public List<String> getAlbums() {
            return albums;
        }

        public List<Step> getSteps() {
            return steps;
        }

    }

    public class Step {
        @SerializedName("img")
        private String img;
        @SerializedName("step")
        private String step;

        public Step(String img, String step) {
            this.img = img;
            this.step = step;
        }

        public String getImg() {
            return img;
        }

        public String getStep() {
            return step;
        }
    }
}
