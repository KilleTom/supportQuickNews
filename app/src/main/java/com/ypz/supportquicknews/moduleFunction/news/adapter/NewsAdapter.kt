package com.ypz.supportquicknews.moduleFunction.news.adapter

import android.content.Intent
import android.provider.Settings
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.showJoke.Main3Activity
import com.ypz.supportquicknews.moduleFunction.news.net.NewsResult
import com.ypz.supportquicknews.preservationData.PreservationDataSP
import com.ypz.supportquicknews.rxUtil.MyBmobUtil
import com.ypz.supportquicknews.statistics.CycletimesFunctionStatistics
import com.ypz.supportquicknews.statistics.NewsTypeStatistics
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by kingadmin on 2018/2/8.
 */

class NewsAdapter(private val dataItems: ArrayList<NewsResult.ResultDataItem>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> return NewsFirstImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_first_item, parent, false))
            2 -> return NewsSecondImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_second_item, parent, false))
            else -> return NewsFirstImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_first_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            1 -> {
                (holder as NewsFirstImageViewHolder).init(dataItems!![position])
            }
            2 -> {
                (holder as NewsSecondImageViewHolder).init(dataItems!![position])
            }
        }

    }

    override fun getItemCount(): Int {
        return dataItems?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataItems!![position].getIamges().size >= 3) 2
        else 1
    }

    inner class NewsFirstImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var title: TextView
        private lateinit var catorgty: TextView
        private lateinit var comment: TextView
        private lateinit var icon_0: ImageView

        fun init(data: NewsResult.ResultDataItem) {
            title = itemView.findViewById<View>(R.id.news_title) as TextView
            catorgty = itemView.findViewById<View>(R.id.news_catory) as TextView
            comment = itemView.findViewById<View>(R.id.news_comment) as TextView
            icon_0 = itemView.findViewById<View>(R.id.news_icon) as ImageView
            title.text = data.title
            catorgty.text = data.category
            Glide.with(icon_0.context).load(data.image0).into(icon_0)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, Main3Activity::class.java)
                intent.putExtra("url", data.url)
                intent.putExtra("title", if (data.title.length > 10) data.title.substring(0, 11) else data.title)
                itemView.context.startActivity(intent)
            }
        }
    }

    inner class NewsSecondImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var title: TextView
        private lateinit var catorgty: TextView
        private lateinit var comment: TextView
        private lateinit var icon_0: ImageView
        private lateinit var icon_1: ImageView
        private lateinit var icon_2: ImageView
        fun init(data: NewsResult.ResultDataItem) {
            title = itemView.findViewById<View>(R.id.news_title2) as TextView
            catorgty = itemView.findViewById<View>(R.id.news_catory2) as TextView
            comment = itemView.findViewById<View>(R.id.news_comment2) as TextView
            icon_0 = itemView.findViewById<View>(R.id.news_icon1) as ImageView
            icon_1 = itemView.findViewById<View>(R.id.news_icon2) as ImageView
            icon_2 = itemView.findViewById<View>(R.id.news_icon3) as ImageView
            Glide.with(icon_0.context).load(data.image0).into(icon_0)
            Glide.with(icon_1.context).load(data.iamge1).into(icon_1)
            Glide.with(icon_2.context).load(data.image2).into(icon_2)
            title.text = data.title
            catorgty.text = data.category
            itemView.setOnClickListener {
                val userId =
                        if (TextUtils.isEmpty(PreservationDataSP.getPreservationDataSP().getUserId("")))
                            Settings.Secure.ANDROID_ID
                        else PreservationDataSP.getPreservationDataSP().getUserId("")
                val date = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(Date(System.currentTimeMillis()))
                val yearMonth = date.substring(0,8)
                var userNewsTypeStatistics = NewsTypeStatistics(
                        data.category,date , yearMonth,userId)
                Log.i("ypzNews", "kaishi保存数据")
                Thread({
                    MyBmobUtil.getMyBmobUtil(itemView.context).saveDate(userNewsTypeStatistics)
                    /*MyBmobUtil.getMyBmobUtil(itemView.context).updateFunctionStatistics(yearMonth,"All","所有功能统计数")
                    MyBmobUtil.getMyBmobUtil(itemView.context).updateFunctionStatistics(yearMonth,"News",catorgty.text.toString())
                    MyBmobUtil.getMyBmobUtil(itemView.context).updateFunctionStatistics(yearMonth,"News","新闻阅读总数")
                    MyBmobUtil.getMyBmobUtil(itemView.context).setModulePercentage(yearMonth,"News","新闻阅读总数")*/
                    MyBmobUtil.getMyBmobUtil(itemView.context).updateFunctionStatistics(
                            1,
                            CycletimesFunctionStatistics(yearMonth,"All","所有功能统计数"),
                            CycletimesFunctionStatistics(yearMonth,"News",catorgty.text.toString()),
                            CycletimesFunctionStatistics(yearMonth,"News","新闻阅读总数")
                    )
                }).start()
                val intent = Intent(itemView.context, Main3Activity::class.java)
                intent.putExtra("url", data.url)
                intent.putExtra("title", if (data.title.length > 10) data.title.substring(0, 11) else data.title)
                itemView.context.startActivity(intent)
            }
        }
    }
}
