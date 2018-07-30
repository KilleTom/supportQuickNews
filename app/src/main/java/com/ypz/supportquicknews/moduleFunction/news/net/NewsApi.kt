package com.ypz.supportquicknews.moduleFunction.news.net

import retrofit2.http.POST
import retrofit2.http.Query
import rx.Observable

/**
 * Created by kingadmin on 2017/5/6.
 */

interface NewsApi {
    @POST("index")
    fun newsResult(@Query("type") type: String, @Query("key") key: String): Observable<NewsResult>
}
