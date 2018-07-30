package com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.findRecipes;

import android.util.Log;

import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net.FindRecipes;
import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net.TagDetails;
import com.ypz.supportquicknews.net.AppNetClicent;
import com.ypz.supportquicknews.net.NetTypeKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kingadmin on 2018/4/5.
 */

public class FindRecipesNetModule {

    private FindRecipesCallback findRecipesCallback;
    private int pno = 0;
    private String name;
    private int id;
    private int serarchType = 0;

    public FindRecipesNetModule(FindRecipesCallback findRecipesCallback) {
        this.findRecipesCallback = findRecipesCallback;
    }

    public void getNetTag() {
        AppNetClicent.getAppNetClicent().reSetApiUrl("http://apis.juhe.cn/cook/");
        try {
            Log.i("yp","set");
            AppNetClicent.getAppNetClicent().cookingApi().tagDetails("", "json", NetTypeKey.cookingKey).
                    subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io()).
                    doOnNext((TagDetails tagDetails) -> {
                        Log.i("ypz","doNext");
                        Log.i("ypz",tagDetails.getErrorCode()+"");
                        Log.i("ypz",findRecipesCallback+"");
                        if (tagDetails.getErrorCode() == 0) {
                            if (tagDetails.getResult() != null) {
                                Log.i("ypz",findRecipesCallback+"");
                                new Thread(() -> {
                                    List<TagDetails.TagItem> tagItems = new ArrayList<>();
                                    for (TagDetails.Result r : tagDetails.getResult()) {
                                        try {
                                            tagItems.addAll(r.getList());
                                        } catch (Exception e) {
                                            Log.i("ypz",e.getMessage());
                                        }
                                    }
                                    Collections.sort(tagItems,new shortTagName());
                                    Log.i("ypz",tagItems.size()+"");
                                    findRecipesCallback.sucessTag(tagItems);
                                }).start();
                            }
                        } else findRecipesCallback.error(tagDetails.getReason()+"");
                    }).subscribeOn(AndroidSchedulers.mainThread()).
                    subscribe(new Observer<TagDetails>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            findRecipesCallback.error(throwable.getMessage());
                        }

                        @Override
                        public void onNext(TagDetails tagDetails) {

                        }
                    });
        } catch (Exception e) {
            Log.i("ypz",e.getMessage());
        }

    }

    public void getTagSearch(int id, boolean isResetPno) {
        this.id = id;
        serarchType = 1;
        if (isResetPno) pno = 0;
        AppNetClicent.getAppNetClicent().reSetApiUrl("http://apis.juhe.cn/cook/");
        AppNetClicent.getAppNetClicent().cookingApi().findRecipesForTag(id, "json", pno, 10, 0, NetTypeKey.cookingKey).
                subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io()).
                doOnNext(findRecipes -> {
                    if (findRecipes.getErrorCode() == 0) {
                        if (findRecipes.getResult() != null) {
                            if (findRecipes.getResult().getData() != null)

                                findRecipesCallback.secess(findRecipes.getResult().getData());
                        }else findRecipesCallback.error("获取不到数据");
                    }else findRecipesCallback.error(findRecipes.getReason());
                }).subscribeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<FindRecipes>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        findRecipesCallback.error(throwable.getMessage());
                    }

                    @Override
                    public void onNext(FindRecipes findRecipes) {

                    }
                });
    }

    public void getNameSearch(String name, boolean isResetPno) {
        this.name = name;
        serarchType = 0;
        if (isResetPno) pno = 0;
        AppNetClicent.getAppNetClicent().reSetApiUrl("http://apis.juhe.cn/cook/");
        AppNetClicent.getAppNetClicent().cookingApi().findRecipesForName(name, "json", pno, 10, 0, NetTypeKey.cookingKey).
                subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io()).
                doOnNext(findRecipes -> {
                    if (findRecipes.getErrorCode() == 0) {
                        if (findRecipes.getResult() != null) {
                            if (findRecipes.getResult().getData() != null)
                                findRecipesCallback.secess(findRecipes.getResult().getData());
                        }else findRecipesCallback.error("获取不到数据");
                    }else findRecipesCallback.error(findRecipes.getReason());
                }).subscribeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<FindRecipes>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        findRecipesCallback.error(throwable.getMessage());
                    }

                    @Override
                    public void onNext(FindRecipes findRecipes) {

                    }
                });
    }

    public void refrseh() {
        pno += 1;
        Log.i("ypz","refrseh() ");
        switch (serarchType) {
            case 0:
                Log.i("ypz","GetName ");
                getNameSearch(name, false);
                break;
            case 1:
                getTagSearch(id, false);
                break;

        }
    }

    public void loadMore() {
        pno += 1;
        Log.i("ypz","loadMore() ");
        switch (serarchType) {
            case 0:
                Log.i("ypz","GetName ");
                getNameSearch(name, false);
                break;
            case 1:

                getTagSearch(id, false);
                break;

        }
    }

    public interface FindRecipesCallback {
        void sucessTag(List<TagDetails.TagItem> tagItems);

        void error(String message);

        void secess(List<FindRecipes.Datum> data);
    }

    private class shortTagName implements Comparator<TagDetails.TagItem>{

        @Override
        public int compare(TagDetails.TagItem tagItem, TagDetails.TagItem t1) {
            if (tagItem.getName().length()<=3&&t1.getName().length()<=3) return 0;
            if ((tagItem.getName().length()/2) >(t1.getName().length()/2) ){
                return 1;
            }else if ((tagItem.getName().length()/2) <(t1.getName().length()/2)){
                return -1;
            }
            return 0;
        }
    }
}
