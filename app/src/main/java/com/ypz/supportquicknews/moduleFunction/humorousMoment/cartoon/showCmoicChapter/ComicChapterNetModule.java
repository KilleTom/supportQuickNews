package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.showCmoicChapter;

import android.content.Context;
import android.util.Log;

import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net.ComicChapterResult;
import com.ypz.supportquicknews.net.AppNetClicent;
import com.ypz.supportquicknews.net.NetTypeKey;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kingadmin on 2018/3/12.
 */

public class ComicChapterNetModule {


    private Context context;
    private List<ComicChapterResult.ChapterList> chapterLists;
    private ComicChapterNetCallback comicChapterNetCallback;
    private String skip;

    public ComicChapterNetModule(Context context, ComicChapterNetCallback comicChapterNetCallback) {
        this.context = context;
        this.comicChapterNetCallback = comicChapterNetCallback;
    }

    public void toComicCharpNet(String comicName) {
        try {
            AppNetClicent.getAppNetClicent().reSetApiUrl("http://japi.juhe.cn/comic/");
            if (chapterLists == null || chapterLists.size() == 0) skip = "";
            else skip = chapterLists.size() + "";
            Log.i("ypz","开始net查询");
            AppNetClicent.getAppNetClicent().cartoonApi().
                    chapterList(comicName, skip, NetTypeKey.ComicKey).
                    subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io()).
                    doOnNext(comicChapterList -> {
                        if (comicChapterList!=null){
                            if (comicChapterList.getErrorCode()==200){
                                if (comicChapterList.getResult()!=null){
                                    if (comicChapterList.getResult().getChapterList()!=null){
                                        if (this.chapterLists!=null)
                                            this.chapterLists.addAll( comicChapterList.getResult().getChapterList());
                                        else this.chapterLists = comicChapterList.getResult().getChapterList();
                                        Log.i("ypz","开始插入");
                                        comicChapterList.getResult().insertBmob();
                                        comicChapterNetCallback.sucessfulComicCharp(this.chapterLists);
                                        return;
                                    }
                                }
                            }
                        }
                        comicChapterNetCallback.error(comicChapterList.getReason());
                    }).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribe(comicChapterList -> {

                    });
                    } catch (Exception e) {
            Log.i("ypz", e.getMessage());
        }

    }

    public void toComicCharpBmob(String comicName) {
        Log.i("ypz", "start query by Bmob "+comicName);
        BmobQuery<ComicChapterResult.ChapterList> chapterListBmobQuery = new BmobQuery<>();
        chapterListBmobQuery.setLimit(20);
        if (chapterLists != null && chapterLists.size() >= 1)
            chapterListBmobQuery.setSkip(chapterLists.size());
        Log.i("ypz", "start query by Bmob ");
        chapterListBmobQuery.addWhereEqualTo("comicName", comicName);
        chapterListBmobQuery.findObjects(new FindListener<ComicChapterResult.ChapterList>() {
            @Override
            public void done(List<ComicChapterResult.ChapterList> list, BmobException e) {
                if (e == null) {
                    Log.i("ypz", list.size() + "");
                    if (list.size() > 0) {
                        comicChapterNetCallback.sucessfulComicCharp(list);
                    } else toComicCharpNet(comicName);
                    return;
                } else {
                    Log.i("ypz", "bmob异常"+e.getMessage() + "errorCode" + e.getErrorCode());
                    toComicCharpNet(comicName);
                }
            }
        });
    }

    public interface ComicChapterNetCallback {

        void sucessfulComicCharp(List<ComicChapterResult.ChapterList> chapterLists);

        void error(String message);

    }
}
