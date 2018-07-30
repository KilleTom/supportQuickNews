package com.ypz.supportquicknews.moduleFunction.news.emptyModule

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.ypz.supportquicknews.moduleFunction.news.net.NewsResult
import com.ypz.supportquicknews.net.AppNetClicent
import com.ypz.supportquicknews.net.NetTypeKey
import com.ypz.supportquicknews.preservationData.PreservationDataSP
import org.greenrobot.eventbus.EventBus
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by kingadmin on 2018/2/8.
 */

class NewsEmptyNetModule(private val newsEmptyNetStatus: NewsEmptyNetStatus, private val context: Context?) {

    private var resultDataItems: ArrayList<NewsResult.ResultDataItem>? = null
    fun setResultDataItems(resultDataItems: ArrayList<NewsResult.ResultDataItem>?){
        this.resultDataItems = resultDataItems
    }

    fun showNewsEmpty(type: String) {
        getNetData(type, 2)
    }

    fun refresh(type: String) {
        getNetData(type, 0)
    }

    fun loadMore(type: String) {
        getNetData(type, 1)
    }

    private fun getNetData(type: String, wantDoType: Int) {
        Log.i("ypz", "getNetData")
        AppNetClicent.getAppNetClicent().reSetApiUrl("http://v.juhe.cn/toutiao/")
        AppNetClicent.getAppNetClicent().creatNewsApi().newsResult(type, NetTypeKey.NewsKey)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io()).doOnNext { newsResult ->
                    if (newsResult.errorCode!=0){
                        EventBus.getDefault().post(LoadMoreORRefrsh(0))
                        EventBus.getDefault().post(LoadMoreORRefrsh(1))
                    }
                    when (wantDoType) {
                        0 -> if (newsResult.errorCode == 0)
                                wantDo(type,(newsResult.result.dataItems as ArrayList<NewsResult.ResultDataItem>),false)
                        1 -> if (newsResult.errorCode == 0)
                                wantDo(type,(newsResult.result.dataItems as ArrayList<NewsResult.ResultDataItem>),true)
                        2 -> if (newsResult.errorCode == 0)
                                wantDo(type,(newsResult.result.dataItems as ArrayList<NewsResult.ResultDataItem>),false)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread()).subscribe(object : Subscriber<NewsResult>() {
                    override fun onNext(t: NewsResult?) {}
                    override fun onCompleted() {}
                    override fun onError(e: Throwable?) {
                        Log.i("ypz", e!!.message + "...........................")
                        when (wantDoType) {
                            0 -> newsEmptyNetStatus.refreshFinsh()
                            1 -> newsEmptyNetStatus.loadMoreFinsh()
                            2 -> newsEmptyNetStatus.refreshFinsh()
                        } }})
    }

    private fun wantDo(type: String, dataItems: ArrayList<NewsResult.ResultDataItem>, boolean: Boolean){
        PreservationDataSP.getPreservationDataSP().saveNewsKeyTimeValues(type,System.currentTimeMillis())
        PreservationDataSP.getPreservationDataSP().saveNewsKeyValues(type, Gson().toJson(dataItems).toString())
        isLoadMore(dataItems, boolean)
    }

    private fun isLoadMore(resultDataItems: ArrayList<NewsResult.ResultDataItem>, isloadMore: Boolean) {
        Log.i("ypz", "isLoadMore" + isloadMore)

        if (isloadMore) {
            if (this.resultDataItems != null) {
                Log.i("ypz", "loadBeforeSize" + this.resultDataItems!!.size)
                this.resultDataItems!!.addAll(resultDataItems)
                Log.i("ypz", "loadaffterSize" + this.resultDataItems!!.size)
            } else this.resultDataItems = resultDataItems
            newsEmptyNetStatus.sucessful(this.resultDataItems!!)
            newsEmptyNetStatus.loadMoreFinsh()
            return
        } else {
            this.resultDataItems = resultDataItems
            newsEmptyNetStatus.sucessful(this.resultDataItems!!)
            newsEmptyNetStatus.refreshFinsh()
            return
        }

    }

    interface NewsEmptyNetStatus {

        fun sucessful(resultDataItems: ArrayList<NewsResult.ResultDataItem>)

        fun loadMoreFinsh()

        fun refreshFinsh()

    }
}
