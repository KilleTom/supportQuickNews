package com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.chapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.net.WeChatJoke
import com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.showJoke.Main3Activity
import com.ypz.supportquicknews.rxUtil.MyBmobUtil
import com.ypz.supportquicknews.statistics.CycletimesFunctionStatistics
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kingadmin on 2018/4/2.
 */

class JokeChapter(private val weChatJokeItemList: List<WeChatJoke.WeChatJokeItem>?, private val context: JokeChapterActivity) : RecyclerView.Adapter<JokeChapter.JokeChapterViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): JokeChapterViewHolder {
        return JokeChapterViewHolder(LayoutInflater.from(context).inflate(R.layout.wechat_joke_item, viewGroup, false))
    }

    override fun onBindViewHolder(jokeChapterViewHolder: JokeChapterViewHolder, i: Int) {
        jokeChapterViewHolder.setData(weChatJokeItemList!![i])
    }

    override fun getItemCount(): Int {
        return weChatJokeItemList?.size ?: 0
    }

    inner class JokeChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView
        private val textView: TextView

        init {
            imageView = itemView.findViewById(R.id.icon)
            textView = itemView.findViewById(R.id.title)
        }

        fun setData(weChatJokeItem: WeChatJoke.WeChatJokeItem) {
            Glide.with(imageView).load(weChatJokeItem.firstImg).into(imageView)
            textView.text = weChatJokeItem.title
            itemView.setOnClickListener { view ->
                comitFunctionStatistics()
                val intent = Intent(context, Main3Activity::class.java)
                intent.putExtra("url", weChatJokeItem.url)
                intent.putExtra("title", if (weChatJokeItem.title.length > 10) weChatJokeItem.title.substring(0, 11) + "...." else weChatJokeItem.title)
                context.startActivity(intent)
            }
        }

        private fun comitFunctionStatistics() {
            Thread {
                val date = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(Date(System.currentTimeMillis()))
                val yearMonth = date.substring(0, 8)
                MyBmobUtil.getMyBmobUtil(context).updateFunctionStatistics(
                        1,
                        CycletimesFunctionStatistics(yearMonth, "All", "所有功能统计数"),
                        CycletimesFunctionStatistics(yearMonth, "HumorousMoment", "ShowWeChatJoke"),
                        CycletimesFunctionStatistics(yearMonth, "HumorousMoment", "娱乐统计总数")
                )
            }.start()
        }
    }


}
