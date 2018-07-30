package com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.findRecipes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ypz.supportquicknews.R;
import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.delicacyDetails.DelicacyDetailsActivity;
import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net.FindRecipes;

import java.util.List;

/**
 * Created by kingadmin on 2018/4/6.
 */

public class CookingItemAdapter extends RecyclerView.Adapter<CookingItemAdapter.CookingItemViewHolder> {

    private List<FindRecipes.Datum> datumList;
    private Context context;

    public CookingItemAdapter(List<FindRecipes.Datum> datumList,Context context) {
        this.datumList = datumList;
        this.context = context;
    }

    @NonNull
    @Override
    public CookingItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CookingItemViewHolder(LayoutInflater.from(context).inflate(R.layout.cooking_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CookingItemViewHolder cookingItemViewHolder, int i) {
        cookingItemViewHolder.initItem(datumList.get(i));

    }

    @Override
    public int getItemCount() {
        if (datumList == null)
            return 0;
        return datumList.size();
    }

    public class CookingItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public CookingItemViewHolder(View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.icon);
            textView = itemView.findViewById(R.id.title);
        }


        public void initItem(FindRecipes.Datum datum) {
            Glide.with(imageView).load(datum.getAlbums().get(0)).into(imageView);
            textView.setText(datum.getTitle());
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, DelicacyDetailsActivity.class);
                intent.putExtra("id",datum.getId());
                context.startActivity(intent);
            });
        }
    }
}
