package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;

/**
 * Created by kingadmin on 2018/3/9.
 */

public class ComicBookResult {

    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("reason")
    private String reason;
    @SerializedName("result")
    private Result result;

    public ComicBookResult(int errorCode, String reason, Result result) {
        this.errorCode = errorCode;
        this.reason = reason;
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getReason() {
        return reason;
    }

    public Result getResult() {
        return result;
    }

    public class BookList extends BmobObject {

        @SerializedName("name")
        private String name;
        @SerializedName("type")
        private String type;
        @SerializedName("area")
        private String area;
        @SerializedName("des")
        private String des;
        @SerializedName("lastUpdate")
        private int lastUpdate;
        @SerializedName("coverImg")
        private String coverImg;
        @SerializedName("finshStatus")
        private String finshStatus = "完结";

        public BookList(String name, String type, String area, String des, int lastUpdate, String coverImg) {
            this.name = name;
            this.type = type;
            this.area = area;
            this.des = des;
            this.lastUpdate = lastUpdate;
            this.coverImg = coverImg;
        }

        public BookList() {

        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public String getArea() {
            return area;
        }

        public String getDes() {
            return des;
        }

        public int getLastUpdate() {
            return lastUpdate;
        }

        public String getCoverImg() {
            return coverImg;
        }

        public String getFinshStatus() {
            return finshStatus;
        }

        public void setFinshStatus(String finshStatus) {
            this.finshStatus = finshStatus;
        }

        public BookList getBookList(){
            return this;
        }
    }

    public class NetBookeList extends BookList{
        @SerializedName("finish")
        private Boolean finish;

        public NetBookeList() {
            super();
            setTableName("NetBookeList");
        }

        public NetBookeList(String name, String type, String area, String des, Boolean finish, int lastUpdate, String coverImg) {
            super(name, type, area, des,lastUpdate, coverImg);
            this.finish = finish;
            if (finish) setFinshStatus("完结");
            else setFinshStatus("连载");
        }

        public Boolean getFinish() {
            return finish;
        }
    }

    public class Result {
        @SerializedName("total")
        private int total;
        @SerializedName("limit")
        private int limit;
        @SerializedName("skip")
        private int skip;
        @SerializedName("bookList")
        private List<NetBookeList> bookList = null;

        public Result(int total, int limit, int skip, List<NetBookeList> bookList) {
            this.total = total;
            this.limit = limit;
            this.skip = skip;
            this.bookList = bookList;
        }

        public int getTotal() {
            return total;
        }

        public int getLimit() {
            return limit;
        }

        public int getSkip() {
            return skip;
        }

        public List<NetBookeList> getBookList() {
            return bookList;
        }

        public void insertBmob(){
            if (bookList != null) {
                List<BmobObject> bmobObjects = new ArrayList<>();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            for (NetBookeList b : bookList) {
                                b.setTableName("NetBookeList");
                                if (b.getFinish()){
                                    b.setFinshStatus("完结");
                                }else b.setFinshStatus("连载");
                                bmobObjects.add(b.getBookList());
                            }
                            new BmobBatch().insertBatch(bmobObjects).doBatch(new QueryListListener<BatchResult>() {
                                @Override
                                public void done(List<BatchResult> list, BmobException e) {
                                    if (e == null) {
                                    }else Log.i("ypz","漫画书列表插入失败"+e.getMessage());
                                }
                            });
                        } catch (Exception e) {
                            Log.i("ypz",e.getMessage());
                        }

                    }
                }.start();
            }
        }
    }
}
