package com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kingadmin on 2018/4/5.
 */

public interface CookingApi {

    final String key = "1290745749d121b2a7091393eca62b82";
    @POST("query")
    Observable<FindRecipes> findRecipesForName(
            @Query("menu")String name,@Query("dtype")String dataType,
            @Query("pn") int pageIndex,@Query("rn") int pageSize,
            @Query("albums")int albums,@Query("key") String key);

    @POST("index")
    Observable<FindRecipes> findRecipesForTag(
            @Query("cid")int tagId,@Query("dtype")String dataType,
            @Query("pn") int pageIndex,@Query("rn") int pageSize,
            @Query("format")int format,@Query("key") String key);

    @POST("queryid")
    Observable<DelicacyDetails> delicacyDetails(@Query("id") int cookNameId,@Query("dtype")String dataType,@Query("key")String key);

    @POST("category")
    Observable<TagDetails> tagDetails(@Query("parentid")String parentId,@Query("dtype")String dataType,@Query("key")String key);

}
