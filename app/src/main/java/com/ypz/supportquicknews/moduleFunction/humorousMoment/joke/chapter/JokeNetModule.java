package com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.chapter;

import android.util.Log;

import com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.net.WeChatJoke;
import com.ypz.supportquicknews.net.AppNetClicent;
import com.ypz.supportquicknews.net.NetTypeKey;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kingadmin on 2018/3/26.
 */

public class JokeNetModule {

    private JokeNetModuleCallback jokeNetModuleCallback;
    private int pno = 1;
    private boolean refresh = false ,loadMore = false;

    public JokeNetModule(JokeNetModuleCallback jokeNetModuleCallback) {
        this.jokeNetModuleCallback = jokeNetModuleCallback;
    }

    public void getNetJoke() {
        AppNetClicent.getAppNetClicent().reSetApiUrl("http://v.juhe.cn/weixin/");
        AppNetClicent.getAppNetClicent().jokeNetApi().weChatJokeObservable(pno, 16, "json", NetTypeKey.WeChatJokeKey).
                subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io()).
                doOnNext(
                        weChatJoke -> {
                            if (weChatJoke.getErrorCode()==0){
                                if (weChatJoke.getResult()!=null)
                                    if (weChatJoke.getResult().getWeChatJokeItemList()!=null){
                                    if (refresh){
                                        refresh = false;
                                        Log.i("ypz","refresh");
                                        jokeNetModuleCallback.refresh(weChatJoke.getResult().getWeChatJokeItemList());
                                    }else if (loadMore){
                                        loadMore = false;
                                        jokeNetModuleCallback.sccesful(weChatJoke.getResult().getWeChatJokeItemList());
                                    }else {
                                        jokeNetModuleCallback.sccesful(weChatJoke.getResult().getWeChatJokeItemList());
                                    }
                                    }
                            }else jokeNetModuleCallback.error(weChatJoke.getReason());
                        }
                ).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<WeChatJoke>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable throwable) {
                        jokeNetModuleCallback.error(throwable.getMessage());
                    }
                    @Override
                    public void onNext(WeChatJoke weChatJoke) {}
                });
    }

    public void refresh(){
        pno +=1;
        refresh = true;
        Log.i("ypz","refresh"+pno);
        getNetJoke();
    }

    public void loadMore(){
        pno +=1;
        loadMore = true;
        Log.i("ypz","refresh"+pno);
        getNetJoke();
    }

    public interface JokeNetModuleCallback {

        void sccesful(List<WeChatJoke.WeChatJokeItem> weChatJokeItems);

        void error(String meessage);

        void refresh(List<WeChatJoke.WeChatJokeItem> weChatJokeItems);
    }
}
