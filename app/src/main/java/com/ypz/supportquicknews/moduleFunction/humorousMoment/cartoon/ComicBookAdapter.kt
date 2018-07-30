package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net.ComicBookResult
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.showCmoicChapter.ComicChapterActivity

/**
 * Created by kingadmin on 2018/3/12.
 */

class ComicBookAdapter(private val netBookeLists: List<ComicBookResult.NetBookeList>?, private val cartoonActivity: CartoonActivity) : RecyclerView.Adapter<ComicBookAdapter.ComicBookViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ComicBookViewHolder {
        return ComicBookViewHolder(LayoutInflater.from(cartoonActivity).inflate(R.layout.comicbook_item, viewGroup, false))
    }

    override fun onBindViewHolder(comicBookViewHolder: ComicBookViewHolder, i: Int) {
        comicBookViewHolder.setData(netBookeLists!![i])
    }

    override fun getItemCount(): Int {
        return netBookeLists?.size ?: 0
    }

    inner class ComicBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView
        private val name: TextView
        init {
            imageView = itemView.findViewById(R.id.comic_ic)
            name = itemView.findViewById(R.id.comic_name)}
        fun setData(netBookeResult: ComicBookResult.NetBookeList) {
            Glide.with(imageView.context).load(netBookeResult.coverImg).into(imageView)
            name.text = netBookeResult.name
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ComicChapterActivity::class.java)
                intent.putExtra("ComicBookId", netBookeResult.name)
                intent.putExtra("imageUrl", netBookeResult.coverImg)
                itemView.context.startActivity(intent)
            } }}
}
