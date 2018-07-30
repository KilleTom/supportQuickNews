package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon

import android.content.Context
import android.util.Log
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net.CategoryResult
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net.ComicBookResult
import com.ypz.supportquicknews.net.AppNetClicent
import com.ypz.supportquicknews.net.NetTypeKey
import com.ypz.supportquicknews.rxUtil.MyBmobUtil
import com.ypz.supportquicknews.statistics.CycletimesFunctionStatistics
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kingadmin on 2018/3/10.
 */

class CartoonNetModule(private val context: Context, private val cartoonNetModuleResult: CartoonNetModuleResult) {
    private var mapKey = 2
    private var typeKey = "全部"
    private val bookListMap = HashMap<Int, Map<String, List<ComicBookResult.NetBookeList>>>()
    init {
        try {
            getComicType()
            getComicBookList()
        } catch (e: Exception) {
            Log.i("ypz", e.message)
        }
    }
    /**
     * 设置是否连载
     */
    fun setMapKey(finshStatus: String) {
        if (finshStatus == "连载") {
            mapKey = 0
        } else if (finshStatus == "完结") {
            mapKey = 1
        } else
            mapKey = 2
        getComicBookList()
    }

    /**
     * 设置漫画类型
     */
    fun setTypeKey(typeKey: String) {
        this.typeKey = typeKey
        Log.i("ypz", "设置漫画类型$typeKey")
        getQueryComicBookList()
    }

    /**
     * 获取漫画数据的判断
     */
    fun getComicBookList() {
        comitFunctionStatistics()
        if (bookListMap[mapKey] == null) {
            getQueryComicBookList()
        } else if (bookListMap[mapKey]!![typeKey] == null) {
            getQueryComicBookList()
        } else if (bookListMap[mapKey]!![typeKey] != null) {
            cartoonNetModuleResult.getBookList(bookListMap[mapKey]!!.get(typeKey)!!)
        }
    }

    /**
     * Bmob云数据的获取
     */
    private fun getQueryComicBookList() {
        val bookListBmobQuery = BmobQuery<ComicBookResult.NetBookeList>()
        val typeNetBookeListBmobQuery = BmobQuery<ComicBookResult.NetBookeList>()
        val finshStatusNetBookeListBmobQuery = BmobQuery<ComicBookResult.NetBookeList>()
        val queries = ArrayList<BmobQuery<ComicBookResult.NetBookeList>>()
        Log.i("ypz", "typeKey:$typeKey")
        if (typeKey != "全部") typeNetBookeListBmobQuery.addWhereEqualTo("type", typeKey)
        else typeNetBookeListBmobQuery.addWhereNotEqualTo("type", "")
        when (mapKey) {
            0 -> finshStatusNetBookeListBmobQuery.addWhereEqualTo("finshStatus", "连载")
            1 -> finshStatusNetBookeListBmobQuery.addWhereEqualTo("finshStatus", "完结")
            2 -> finshStatusNetBookeListBmobQuery.addWhereNotEqualTo("finshStatus", "")
        }
        queries.add(typeNetBookeListBmobQuery)
        queries.add(finshStatusNetBookeListBmobQuery)
        bookListBmobQuery.and(queries)
        bookListBmobQuery.setLimit(20)
        bookListBmobQuery.findObjects(object : FindListener<ComicBookResult.NetBookeList>() {
            override fun done(result: List<ComicBookResult.NetBookeList>, e: BmobException?) {
                if (e == null) {
                    Log.i("ypz", "bmob查询的数据条数" + result.size + "")
                    if (result.size >= 20) {
                        val map = HashMap<String, List<ComicBookResult.NetBookeList>>()
                        map[typeKey] = result
                        bookListMap[mapKey] = map
                        cartoonNetModuleResult.getBookList(result)
                    } else if (result.size == 0)
                        getCmoicNetBookList(0)
                    else if (result.size > 0 && result.size < 19) getCmoicNetBookList(result.size)
                } else {
                    Log.i("ypz", e.message + "errorCode" + e.errorCode)
                    getCmoicNetBookList(0)
                }
            }
        })
    }
    private fun getCmoicNetBookList(skip: Int) {
        Log.i("ypz", "$typeKey/////$skip////$mapKey")
        AppNetClicent.getAppNetClicent().reSetApiUrl("http://japi.juhe.cn/comic/")
        val skipString: String
        val typeString: String
        if (typeKey == "全部")
            typeString = ""
        else
            typeString = typeKey
        if (skip > 0)
            skipString = skip.toString() + ""
        else
            skipString = ""
        AppNetClicent.getAppNetClicent().cartoonApi().comicBookList("", typeString, skipString, mapKey, NetTypeKey.ComicKey)
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io())
                .doOnNext { comicBookList ->
                    if (comicBookList.errorCode == 200) {
                        if (comicBookList.result.bookList != null) {
                            Log.i("ypz", "查询成功")
                            comicBookList.result.insertBmob()
                            cartoonNetModuleResult.getBookList(comicBookList.result.bookList)
                        }
                    }
                }.observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ComicBookResult> {
                    override fun onCompleted() {

                    }

                    override fun onError(throwable: Throwable) {
                        Log.i("ypz", "网络查询漫画书列表失败" + throwable.message)
                    }

                    override fun onNext(comicBookResult: ComicBookResult) {

                    }
                })
    }
    fun getComicType() {
        val comicTypeBmobQuery = BmobQuery<CategoryResult.ComicType>()
        comicTypeBmobQuery.findObjects(object : FindListener<CategoryResult.ComicType>() {
            override fun done(list: List<CategoryResult.ComicType>?, e: BmobException?) {
                if (e == null) {
                    val types = ArrayList<String>()
                    if (list != null && list.size <= 1) {
                        getNetComicType()
                        return
                    }
                    for (comicType in list!!) {
                        types.add(comicType.type)
                    }
                    Log.i("ypz", "chaxun")
                    cartoonNetModuleResult.getComicType(types)
                } else {
                    getNetComicType()
                }
            }
        })
    }
    private fun getNetComicType() {
        AppNetClicent.getAppNetClicent().reSetApiUrl("http://japi.juhe.cn/comic/")
        AppNetClicent.getAppNetClicent().cartoonApi().CatroonCategory("262428ec3261bc703e67d1409a4a73fb")
                .subscribeOn(Schedulers.newThread()).observeOn(Schedulers.io())
                .doOnNext { categoryResult ->
                    Log.i("ypz", "getNet")
                    if (categoryResult.result != null)
                        cartoonNetModuleResult.getComicType(categoryResult.result)
                }.observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<CategoryResult> {
                    override fun onCompleted() {
                        Log.i("ypz", "onCompleted()")
                    }

                    override fun onError(throwable: Throwable) {
                        Log.i("ypz", throwable.message)
                    }

                    override fun onNext(categoryResult: CategoryResult) {
                        Log.i("ypz", "onNext")
                    }
                })
    }
    interface CartoonNetModuleResult {
        fun getComicType(comicTypes: List<String>)
        fun getBookList(bookLists: List<ComicBookResult.NetBookeList>)
    }
    private fun comitFunctionStatistics() {
        Thread({
            val date = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(Date(System.currentTimeMillis()))
            val yearMonth = date.substring(0, 8)
            MyBmobUtil.getMyBmobUtil(context).updateFunctionStatistics(
                    1,
                    CycletimesFunctionStatistics(yearMonth, "All", "所有功能统计数"),
                    CycletimesFunctionStatistics(yearMonth, "HumorousMoment", "showCmoicList"),
                    CycletimesFunctionStatistics(yearMonth, "HumorousMoment", "娱乐统计总数"))
        }).start()
    }
}
