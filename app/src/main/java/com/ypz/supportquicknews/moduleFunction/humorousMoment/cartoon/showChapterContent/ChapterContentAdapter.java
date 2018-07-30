package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.showChapterContent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ypz.supportquicknews.R;
import com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net.ChapterContentResult;

import java.util.List;

/**
 * Created by kingadmin on 2018/3/22.
 */

public class ChapterContentAdapter extends RecyclerView.Adapter<ChapterContentAdapter.ChapterContentViewHolder> {

    private List<ChapterContentResult.ContentImages> contentImages;
    private Context context;

    public ChapterContentAdapter(List<ChapterContentResult.ContentImages> contentImages, Context context) {
        this.contentImages = contentImages;
        this.context = context;
    }

    @NonNull
    @Override
    public ChapterContentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChapterContentViewHolder(LayoutInflater.from(context).inflate(R.layout.chapter_content_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterContentViewHolder chapterContentViewHolder, int i) {
        chapterContentViewHolder.setData(contentImages.get(i));
    }

    @Override
    public int getItemCount() {
        if (contentImages==null)
        return 0;
        return contentImages.size();
    }

    public class ChapterContentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ChapterContentViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.content_ic);
        }

        public void setData(ChapterContentResult.ContentImages images) {
            Glide.with(imageView).load(images.getImageUrl()).into(imageView);
        }
    }
}
