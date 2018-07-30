package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.showChapterContent

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.r0adkll.slidr.Slidr
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net.ChapterContentResult
import com.ypz.supportquicknews.rxUtil.MyBmobUtil
import com.ypz.supportquicknews.statistics.CycletimesFunctionStatistics
import kotlinx.android.synthetic.main.activity_chapter_content.*
import java.text.SimpleDateFormat
import java.util.*

class ChapterContentActivity : AppCompatActivity() , ChapterContentNetModule.ChapterContentNetCallback {

    override fun sucessful(imageLists: MutableList<ChapterContentResult.ContentImages>?) {
        Log.i("ypz","有数据了")
        runOnUiThread({
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rv.layoutManager = layoutManager
            val chapterContentAdapter = ChapterContentAdapter(imageLists, this)
            rv.adapter = chapterContentAdapter
            comitFunctionStatistics()
        })
    }

    override fun error(message: String?) {
        Log.i("ypz",message)
    }

    private lateinit var chapterContentNetModule: ChapterContentNetModule
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_content)
        chapterContentNetModule = ChapterContentNetModule(this, this)
        chapterContentNetModule.toQueryBmob(intent.getStringExtra("comicName"),intent.getStringExtra("chapterId"))
        Slidr.attach(this)

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
                    CycletimesFunctionStatistics(yearMonth,"HumorousMoment","showCmoic"),
                    CycletimesFunctionStatistics(yearMonth,"HumorousMoment","娱乐统计总数"))
        }).start()
    }
}
