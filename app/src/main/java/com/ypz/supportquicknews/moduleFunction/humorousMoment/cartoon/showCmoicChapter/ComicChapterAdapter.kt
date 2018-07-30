package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.showCmoicChapter

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
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net.ComicChapterResult
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.showChapterContent.ChapterContentActivity

/**
 * Created by kingadmin on 2018/3/21.
 */

class ComicChapterAdapter(private val chapterLists: List<ComicChapterResult.ChapterList>?, private val context: Context, private val imageUrl: String) : RecyclerView.Adapter<ComicChapterAdapter.CmicChapterViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CmicChapterViewHolder {
        return CmicChapterViewHolder(LayoutInflater.from(context).inflate(R.layout.comic_chapter_item, viewGroup, false))
    }

    override fun onBindViewHolder(cmicChapterViewHolder: CmicChapterViewHolder, i: Int) {
        cmicChapterViewHolder.setData(chapterLists!![i])
    }

    override fun getItemCount(): Int {
        return chapterLists?.size ?: 0
    }

    inner class CmicChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView
        private val imageView: ImageView

        init {
            name = itemView.findViewById(R.id.chaptername)
            imageView = itemView.findViewById(R.id.chapteric)
        }

        fun setData(chapterList: ComicChapterResult.ChapterList) {
            name.text = chapterList.name
            Glide.with(context).load(imageUrl).into(imageView)
            itemView.setOnClickListener {
                var intent = Intent(context, ChapterContentActivity::class.java)
                intent.putExtra("comicName",chapterList.comicName)
                intent.putExtra("chapterId",chapterList.id.toString())
                context.startActivity(intent)
            }
        }
    }
}
