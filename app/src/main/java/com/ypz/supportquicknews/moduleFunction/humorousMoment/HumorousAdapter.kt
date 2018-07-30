package com.ypz.supportquicknews.moduleFunction.humorousMoment

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.CartoonActivity
import com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.chapter.JokeChapterActivity

/**
 * Created by kingadmin on 2018/4/4.
 */

class HumorousAdapter(private val humorousItems: List<HumorousItem>?, private val context: Context) : RecyclerView.Adapter<HumorousAdapter.HumorousViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): HumorousViewHolder {
        return HumorousViewHolder(LayoutInflater.from(context).inflate(R.layout.humorous_moment_item, viewGroup, false))
    }

    override fun onBindViewHolder(humorousViewHolder: HumorousViewHolder, i: Int) {
        humorousViewHolder.init(humorousItems!![i])
    }

    override fun getItemCount(): Int {
        return humorousItems?.size ?: 0
    }

    inner class HumorousViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView
        private val subHead: TextView
        private val imageView: ImageView

        init {
            title = itemView.findViewById(R.id.title)
            subHead = itemView.findViewById(R.id.subherad)
            imageView = itemView.findViewById(R.id.icon)
        }


        fun init(humorousItem: HumorousItem) {
            title.text = humorousItem.title
            subHead.text = humorousItem.subHead
            Glide.with(imageView).load(humorousItem.drawable).into(imageView)
            itemView.setOnClickListener {
                var intent :Intent ?=null
                if (humorousItem.title.equals("漫画")){
                    intent = Intent(context, CartoonActivity::class.java)
                }else if (humorousItem.title.equals("微信精选")){
                    intent = Intent(context, JokeChapterActivity::class.java)
                }
                if (intent!=null) context.startActivity(intent)
            }
        }
    }
}
