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

public class ComicChapterResult {


    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("reason")
    private String reason;
    @SerializedName("result")
    private Result result;

    public ComicChapterResult(int errorCode, String reason, Result result) {
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

    public class ChapterList extends BmobObject{
        @SerializedName("name")
        private String name;
        @SerializedName("id")
        private int id;
        private String comicName;
        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public ChapterList(String name, int id) {
            this.name = name;
            this.id = id;
            setTableName("ChapterList");
        }

        public String getComicName() {
            return comicName;
        }

        public void setComicName(String comicName) {
            this.comicName = comicName;
        }

    }

    public class Result {
        @SerializedName("total")
        private int total;
        @SerializedName("comicName")
        private String comicName;
        @SerializedName("chapterList")
        private List<ChapterList> chapterList = null;
        @SerializedName("limit")
        private int limit;
        @SerializedName("skip")
        private int skip;

        public Result(int total, String comicName, List<ChapterList> chapterList, int limit, int skip) {
            this.total = total;
            this.comicName = comicName;
            this.chapterList = chapterList;
            this.limit = limit;
            this.skip = skip;
            if(this.chapterList!=null){
                for (ChapterList chapterList1: this.chapterList) chapterList1.setComicName(comicName);
            }
        }

        public int getTotal() {
            return total;
        }

        public String getComicName() {
            return comicName;
        }

        public List<ChapterList> getChapterList() {
            if (chapterList!=null){
                for (ChapterList c:chapterList) {
                    c.setComicName(comicName);
                    c.setTableName("ChapterList");
                }
            }
            return chapterList;
        }

        public int getLimit() {
            return limit;
        }

        public int getSkip() {
            return skip;
        }

        public void insertBmob(){
            Log.i("ypz","开始插入");
            if (chapterList != null) {
                List<BmobObject> bmobObjects = new ArrayList<>();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            bmobObjects.addAll(chapterList);
                            new BmobBatch().insertBatch(bmobObjects).doBatch(new QueryListListener<BatchResult>() {
                                @Override
                                public void done(List<BatchResult> list, BmobException e) {
                                    if (e == null) {
                                        Log.i("ypz","漫画书"+comicName+"章节插入成功");
                                    }else Log.i("ypz","漫画书"+comicName+"章节插入失败"+e.getMessage());
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
