package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.showChapterContent;

import android.content.Context;
import android.util.Log;

import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net.ChapterContentResult;
import com.ypz.supportquicknews.net.AppNetClicent;
import com.ypz.supportquicknews.net.NetTypeKey;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kingadmin on 2018/3/22.
 */

public class ChapterContentNetModule {

    private Context context;
    private ChapterContentNetCallback chapterContentNetCallback;

    public ChapterContentNetModule(Context context, ChapterContentNetCallback chapterContentNetCallback) {
        this.context = context;
        this.chapterContentNetCallback = chapterContentNetCallback;
    }

    public void toQueryBmob(String comicName, String comicChapterId) {
        Log.i("ypz",comicName+"/////"+comicChapterId);
        BmobQuery<ChapterContentResult.ContentImages> comicNameQuery = new BmobQuery<>();
        BmobQuery<ChapterContentResult.ContentImages> comicChapterIdQuery = new BmobQuery<>();
        comicNameQuery.addWhereEqualTo("comicName", comicName);
        comicChapterIdQuery.addWhereEqualTo("chapterId", comicChapterId.trim());
        List<BmobQuery<ChapterContentResult.ContentImages>> queries = new ArrayList<>();
        queries.add(comicNameQuery);
        queries.add(comicChapterIdQuery);
        BmobQuery<ChapterContentResult.ContentImages> imageListBmobQuery = new BmobQuery<>();
        imageListBmobQuery.and(queries);
        imageListBmobQuery.order("id");
        imageListBmobQuery.setLimit(200);
        imageListBmobQuery.findObjects(new FindListener<ChapterContentResult.ContentImages>() {
            @Override
            public void done(List<ChapterContentResult.ContentImages> list, BmobException e) {
                if (e == null) {
                    Log.i("ypz",list.size()+"");
                    if (list.size() > 0) {
                        chapterContentNetCallback.sucessful(list);
                    } else toQueryNet(comicName, comicChapterId);
                } else {
                    Log.i("ypz", "查询异常" + e.getErrorCode() + "..." + e.getMessage());
                    toQueryNet(comicName,comicChapterId);
                }
            }
        });
    }

    public void toQueryNet(String comicName, String comicChapterId) {
        Log.i("ypz",comicName+"/////"+comicChapterId);
        AppNetClicent.getAppNetClicent().reSetApiUrl("http://japi.juhe.cn/comic/");
        AppNetClicent.getAppNetClicent().cartoonApi().contentResultObservable(comicName, comicChapterId.trim(), NetTypeKey.ComicKey).
                subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io()).
                doOnNext(chapterContentResult -> {

                    if (chapterContentResult.getErrorCode() == 200) {
                        if (chapterContentResult.getResult() != null) {
                            if (chapterContentResult.getResult().getImageList() != null) {
                                chapterContentNetCallback.sucessful(chapterContentResult.getResult().getImageList());
                                chapterContentResult.getResult().insertBmobContentImages();
                                return;
                            }
                        }
                    }
                    chapterContentNetCallback.error(chapterContentResult.getReason());
                    chapterContentNetCallback.error("数据加载失败");
                }).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<ChapterContentResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.i("ypz", throwable.getMessage());
                    }

                    @Override
                    public void onNext(ChapterContentResult chapterContentResult) {

                    }
                });
    }

    public interface ChapterContentNetCallback {

        void sucessful(List<ChapterContentResult.ContentImages> imageLists);

        void error(String message);

    }
}
