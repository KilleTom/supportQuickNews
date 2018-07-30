package com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.net;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kingadmin on 2018/3/26.
 */

public class WeChatJoke {

    @SerializedName("reason")
    private String reason;
    @SerializedName("result")
    private Result result;
    @SerializedName("error_code")
    private int errorCode;

    public WeChatJoke(String reason, Result result, int errorCode) {
        this.reason = reason;
        this.result = result;
        this.errorCode = errorCode;

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
        @SerializedName("totalPage")
        private int totalPage;
        @SerializedName("ps")
        private int ps;
        @SerializedName("pno")
        private int pno;
        @SerializedName("list")
        private List<WeChatJokeItem> weChatJokeItemList;

        public Result(int totalPage, int ps, int pno, List<WeChatJokeItem> weChatJokeItemList) {
            this.totalPage = totalPage;
            this.ps = ps;
            this.pno = pno;
            this.weChatJokeItemList = weChatJokeItemList;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public int getPs() {
            return ps;
        }

        public int getPno() {
            return pno;
        }

        public List<WeChatJokeItem> getWeChatJokeItemList() {
            return weChatJokeItemList;
        }
    }

    public class WeChatJokeItem{

        @SerializedName("id")
        private String id;
        @SerializedName("title")
        private String title;
        @SerializedName("source")
        private String source;
        @SerializedName("firstImg")
        private String firstImg;
        @SerializedName("mark")
        private String mark;
        @SerializedName("url")
        private String url;

        public WeChatJokeItem(String id, String title, String source, String firstImg, String mark, String url) {
            this.id = id;
            this.title = title;
            this.source = source;
            this.firstImg = firstImg;
            this.mark = mark;
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getSource() {
            return source;
        }

        public String getFirstImg() {
            return firstImg;
        }

        public String getMark() {
            return mark;
        }

        public String getUrl() {
            return url;
        }
    }
}
