package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.showCmoicChapter

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.bumptech.glide.Glide
import com.r0adkll.slidr.Slidr
import com.ypz.supportquicknews.rxUtil.MyBmobUtil
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.databinding.ActivityComicChapterBinding
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net.ComicChapterResult
import com.ypz.supportquicknews.statistics.CycletimesFunctionStatistics
import kotlinx.android.synthetic.main.activity_comic_chapter.*
import java.text.SimpleDateFormat
import java.util.*

class ComicChapterActivity : AppCompatActivity(), ComicChapterNetModule.ComicChapterNetCallback {

    private var chapterNetModule: ComicChapterNetModule? = null
    private lateinit var comicChapterUiModule: ComicChapterUiModule
    private lateinit var comicChapterBinding: ActivityComicChapterBinding
    private var chapterLists: MutableList<ComicChapterResult.ChapterList>? = null
    private lateinit var comicChapterAdapter: ComicChapterAdapter
    private lateinit var imageUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            comicChapterBinding = DataBindingUtil.setContentView(this, R.layout.activity_comic_chapter)
            refresh.isEnableRefresh = false
            comicChapterUiModule = ComicChapterUiModule()
            comicChapterBinding.comicChapterUiModule = comicChapterUiModule
            chapterNetModule = ComicChapterNetModule(this, this)
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            comicchapterlist.layoutManager = layoutManager
            Slidr.attach(this)
        } catch (e: Exception) {
            Log.i("ypz", e.message)
        }
    }

    override fun onStart() {
        super.onStart()
        var bokkId = intent.getStringExtra("ComicBookId")
        comicChapterUiModule.comicChapterToolbarTitle = bokkId
        imageUrl = intent.getStringExtra("imageUrl")
        Glide.with(this).load(imageUrl).into(comic_ic)
        try {
            if (chapterLists == null || chapterLists!!.size == 0)
                chapterNetModule!!.toComicCharpBmob(bokkId)
        } catch (e: Exception) {
            Log.i("ypz", e.message)
        }
    }

    override fun sucessfulComicCharp(chapterLists: List<ComicChapterResult.ChapterList>) {
        comitFunctionStatistics()
        if (this.chapterLists == null) {
            try {
                this.chapterLists = chapterLists as MutableList<ComicChapterResult.ChapterList>
                comicChapterAdapter = ComicChapterAdapter(this.chapterLists, this, imageUrl)
                Log.i("ypz", "set")
                runOnUiThread({ comicchapterlist.adapter = comicChapterAdapter })
            } catch (e: Exception) {
                Log.i("ypz", e.message)
            }
        } else {
            this.chapterLists!!.addAll(chapterLists)
            comicChapterAdapter.notifyDataSetChanged()
        }
    }

    override fun error(message: String) {
        Log.i("ypz", message)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    private fun comitFunctionStatistics(){
        Thread({
            val date = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(Date(System.currentTimeMillis()))
            val yearMonth = date.substring(0,8)
            MyBmobUtil.getMyBmobUtil(this).updateFunctionStatistics(
                    1,
                    CycletimesFunctionStatistics(yearMonth,"All","所有功能统计数"),
                    CycletimesFunctionStatistics(yearMonth,"HumorousMoment","showCmoicChapter"),
                    CycletimesFunctionStatistics(yearMonth,"HumorousMoment","娱乐统计总数"))
        }).start()
    }
}
