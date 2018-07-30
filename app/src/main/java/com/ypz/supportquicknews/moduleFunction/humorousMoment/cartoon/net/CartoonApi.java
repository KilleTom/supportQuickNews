package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kingadmin on 2018/3/9.
 */

public interface CartoonApi {

    @POST("category")
    Observable<CategoryResult> CatroonCategory(@Query("key") String key);

    @POST("book")
    Observable<ComicBookResult> comicBookList(@Query("name")String name, @Query("type")String type,
                                              @Query("skip")String skip, @Query("finish") int finsh, @Query("key")String apiKey);

    @POST("chapter")
    Observable<ComicChapterResult> chapterList(@Query("comicName")String comicName,
                                               @Query("skip")String skip, @Query("key")String apiKey);

    @POST("chapterContent")
    Observable<ChapterContentResult> contentResultObservable(@Query("comicName")String comicName,@Query("id")String chapterId,@Query("key")String apiKey);
}
