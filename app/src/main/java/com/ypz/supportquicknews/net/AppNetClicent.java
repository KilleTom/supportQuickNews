package com.ypz.supportquicknews.net;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net.CartoonApi;
import com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.net.JokeNetApi;
import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net.CookingApi;
import com.ypz.supportquicknews.moduleFunction.news.net.NewsApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kingadmin on 2018/2/8.
 */

public class AppNetClicent {

    private static AppNetClicent appNetClicent;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private Retrofit retrofit;

    public static AppNetClicent getAppNetClicent() {
        if (appNetClicent == null) {
            synchronized (AppNetClicent.class) {
                appNetClicent = new AppNetClicent();
            }
        }
        return appNetClicent;
    }

    private AppNetClicent() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
        gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.baidu.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        Log.i("ypz", "next_0");
    }

    public void reSetApiUrl(String url){
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public NewsApi creatNewsApi(){
        return retrofit.create(NewsApi.class);
    }

    public CartoonApi cartoonApi(){return retrofit.create(CartoonApi.class);}

    public JokeNetApi jokeNetApi(){return retrofit.create(JokeNetApi.class);}

    public CookingApi cookingApi() {return retrofit.create(CookingApi.class);}
}
