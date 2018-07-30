package com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.chapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.r0adkll.slidr.Slidr
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.base.RecyclerViewDivider
import com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.net.WeChatJoke
import com.ypz.supportquicknews.rxUtil.MyBmobUtil
import com.ypz.supportquicknews.rxUtil.RxToast
import com.ypz.supportquicknews.statistics.CycletimesFunctionStatistics
import kotlinx.android.synthetic.main.activity_joke.*
import java.text.SimpleDateFormat
import java.util.*

class JokeChapterActivity : AppCompatActivity(), JokeNetModule.JokeNetModuleCallback {
    private lateinit var jokeNetModule: JokeNetModule
    private var weChatJokeItems: MutableList<WeChatJoke.WeChatJokeItem>? = null
    private lateinit var jokeChapter: JokeChapter
    override fun refresh(weChatJokeItems: MutableList<WeChatJoke.WeChatJokeItem>?) {
        refresh.finishRefresh()
        if (weChatJokeItems!!.size > 0) {
            this.weChatJokeItems = weChatJokeItems
            jokeChapter = JokeChapter(this.weChatJokeItems, this)
            runOnUiThread({
                joke_rv.adapter = jokeChapter
            })
        }
        comitFunctionStatistics()
    }

    override fun sccesful(weChatJokeItems: MutableList<WeChatJoke.WeChatJokeItem>?) {
        comitFunctionStatistics()
        if (refresh.isLoading) refresh.finishLoadmore()
        if (weChatJokeItems != null)
            if (this.weChatJokeItems == null) {
                this.weChatJokeItems = weChatJokeItems
                jokeChapter = JokeChapter(this.weChatJokeItems, this)
                runOnUiThread({
                    joke_rv.adapter = jokeChapter
                })
            } else {
                this.weChatJokeItems!!.addAll(weChatJokeItems)
                runOnUiThread({
                    /* web.loadUrl(weChatJokeItems!![0].url)*/
                    jokeChapter.notifyDataSetChanged()
                })
            }
    }

    override fun error(meessage: String?) {
        RxToast.error(this, meessage!!).show()
        Log.i("ypz", meessage)
        if (refresh.isRefreshing) refresh.finishRefresh()
        if (refresh.isLoading) refresh.finishLoadmore()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joke)
        jokeNetModule = JokeNetModule(this)
        jokeNetModule.getNetJoke()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        joke_rv.layoutManager = layoutManager
        refresh.isEnableAutoLoadmore = true
        joke_rv.addItemDecoration(RecyclerViewDivider(this,LinearLayoutManager.HORIZONTAL))
        refresh.setDisableContentWhenLoading(true)
        refresh.setDisableContentWhenRefresh(true)
        refresh.setOnRefreshListener { jokeNetModule.refresh() }
        refresh.setOnLoadmoreListener { jokeNetModule.loadMore() }
        Slidr.attach(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun comitFunctionStatistics() {
        Thread({
            val date = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(Date(System.currentTimeMillis()))
            val yearMonth = date.substring(0, 8)
            MyBmobUtil.getMyBmobUtil(this).updateFunctionStatistics(
                    1,
                    CycletimesFunctionStatistics(yearMonth, "All", "所有功能统计数"),
                    CycletimesFunctionStatistics(yearMonth, "HumorousMoment", "WeChatJokeChapter"),
                    CycletimesFunctionStatistics(yearMonth, "HumorousMoment", "娱乐统计总数"))
        }).start()
    }
}
