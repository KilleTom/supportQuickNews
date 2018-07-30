package com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.delicacyDetails;

import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net.DelicacyDetails;
import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net.FindRecipes;
import com.ypz.supportquicknews.net.AppNetClicent;
import com.ypz.supportquicknews.net.NetTypeKey;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kingadmin on 2018/4/6.
 */

public class DelicacyDetailsNetModule {
    private DelicacyDetailsCallback delicacyDetailsCallback;

    public DelicacyDetailsNetModule(DelicacyDetailsCallback delicacyDetailsCallback) {
        this.delicacyDetailsCallback = delicacyDetailsCallback;
    }

    public void getDelicacyDetails(int id) {
        AppNetClicent.getAppNetClicent().reSetApiUrl("http://apis.juhe.cn/cook/");
        AppNetClicent.getAppNetClicent().cookingApi().delicacyDetails(id, "json", NetTypeKey.cookingKey).
                subscribeOn(Schedulers.newThread()).observeOn(Schedulers.immediate()).
                doOnNext(delicacyDetails -> {
                    if (delicacyDetails.getErrorCode() == 0) {
                        if (delicacyDetails.getResult() != null) {
                            if (delicacyDetails.getResult().getData() != null) {
                                delicacyDetailsCallback.sucessful(delicacyDetails.getResult().getData().get(0));
                                return;
                            }
                        }
                        delicacyDetailsCallback.error("获取数据失败");
                    } else delicacyDetailsCallback.error(delicacyDetails.getReason());
                }).subscribeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<DelicacyDetails>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        delicacyDetailsCallback.error(throwable.getMessage());
                    }

                    @Override
                    public void onNext(DelicacyDetails delicacyDetails) {

                    }
                });
    }

    public interface DelicacyDetailsCallback {
        void sucessful(FindRecipes.Datum datum);

        void error(String message);
    }
}
