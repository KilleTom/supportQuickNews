package com.ypz.supportquicknews.moduleFunction.news.emptyFragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.moduleFunction.news.adapter.NewsAdapter
import com.ypz.supportquicknews.moduleFunction.news.emptyModule.LoadMoreORRefrsh
import com.ypz.supportquicknews.moduleFunction.news.emptyModule.NewsEmptyNetModule
import com.ypz.supportquicknews.moduleFunction.news.net.NewsResult
import com.ypz.supportquicknews.preservationData.PreservationDataSP
import org.greenrobot.eventbus.EventBus

/**
 * Created by kingadmin on 2018/2/7.
 */

class NewsEmptyFragment : Fragment(), NewsEmptyNetModule.NewsEmptyNetStatus {

    override fun refreshFinsh() {
        EventBus.getDefault().post(LoadMoreORRefrsh(0))
        loadMoreBeforeIndex = 1
        recyclerView.post {
            kotlin.run {
                Log.i("ypz", "refresh")
                recyclerView.adapter = newsAdapter
            }
        }
    }

    override fun loadMoreFinsh() {
        EventBus.getDefault().post(LoadMoreORRefrsh(1))
        recyclerView.post {
            kotlin.run {
                recyclerView.adapter = newsAdapter
                if (loadMoreBeforeIndex <= resultDataItems!!.size) {
                    Log.i("ypz", loadMoreBeforeIndex.toString() + ":::")
                    /*recyclerView.scrollToPosition(loadMoreBeforeIndex - 1)*/
                    recyclerView.layoutManager.scrollToPosition(loadMoreBeforeIndex - 1)
                }
            }
        }
    }

    override fun sucessful(resultDataItems: ArrayList<NewsResult.ResultDataItem>) {
        Log.i("ypz", "isdata")
        this.resultDataItems = resultDataItems
        newsAdapter = NewsAdapter(resultDataItems)
        if (recyclerView.adapter != null) {
        } else {
            recyclerView.post {
                kotlin.run {
                    recyclerView.adapter = newsAdapter
                }
            }
        }
    }

    fun refresh() {
        Log.i("ypz", netType + "")
        reSetSaveNewsNetType()
        netModule.refresh(netType!!)
    }

    private var loadMoreBeforeIndex = 1
    fun loadMore() {
        loadMoreBeforeIndex = resultDataItems!!.size
        reSetSaveNewsNetType()
        netModule.loadMore(netType!!)
    }

    private lateinit var netModule: NewsEmptyNetModule
    private lateinit var recyclerView: RecyclerView
    private lateinit var smartRefreshLayout: SmartRefreshLayout
    private var resultDataItems: ArrayList<NewsResult.ResultDataItem>? = null
    private var newsAdapter: NewsAdapter? = null

    companion object {
        var saveShowNetType: String = "shehui"
    }

    var title: String? = null
    var netType: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_news_empty, container, false)
        recyclerView = rootView.findViewById(R.id.news_rv)

        return rootView
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        /*Log.i("ypz","init............")*/
        showData()
    }

    fun showData() {
        try {
            netModule = NewsEmptyNetModule(this, context)
            Log.i("ypz", "next")
            reSetSaveNewsNetType()
            Log.i("ypz", "next net type"+netType)
            if (PreservationDataSP.getPreservationDataSP()==null) PreservationDataSP.init(context)
            Log.i("ypz","is null"+PreservationDataSP.getPreservationDataSP())
            if ((System.currentTimeMillis() - PreservationDataSP.getPreservationDataSP().getNewsKeyTimeValues(netType!!)) <= 1000 * 60 * 10) {
                if (TextUtils.isEmpty(PreservationDataSP.getPreservationDataSP().getSaveNewsKeyValues(netType))) {
                    Log.i("ypz", "next")
                    showNewsData()
                    return
                } else resultDataItems = Gson().fromJson(PreservationDataSP.getPreservationDataSP().getSaveNewsKeyValues(netType),
                        object : TypeToken<List<NewsResult.ResultDataItem>>() {}.type)
                if (resultDataItems != null) {
                    Log.i("ypz", "size" + resultDataItems!!.size.toString())
                    netModule.setResultDataItems(resultDataItems)
                    sucessful(resultDataItems!!)
                    return
                }
            } else {
                if (!TextUtils.isEmpty(PreservationDataSP.getPreservationDataSP().getSaveNewsKeyValues(netType))) {
                    EventBus.getDefault().post(LoadMoreORRefrsh(-1))
                }
                showNewsData()
            }
        } catch (e: Exception) {
            Log.i("ypz", e.message)
        }
    }

    private fun showNewsData() {
        reSetSaveNewsNetType()
        Log.i("ypz", netType.toString() + "netType")
        netModule.showNewsEmpty(netType!!)
    }

    private fun reSetSaveNewsNetType() {
        if (netType == null) netType = saveShowNetType
    }
}
