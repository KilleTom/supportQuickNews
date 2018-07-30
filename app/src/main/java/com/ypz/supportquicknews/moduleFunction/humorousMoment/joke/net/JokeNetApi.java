package com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.net;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kingadmin on 2018/3/26.
 */

public interface JokeNetApi {

    @GET("query")
    Observable<WeChatJoke> weChatJokeObservable(@Query("pno")int pno,@Query("ps")int ps,@Query("type") String type,@Query("key")String key);
}
