package com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.delicacyDetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ypz.supportquicknews.R;
import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net.FindRecipes;

/**
 * Created by kingadmin on 2018/4/6.
 */

public class DelicacyDetailsAdapter extends RecyclerView.Adapter<DelicacyDetailsAdapter.DelicacyDetailsViewHolder> {

    private Context context;

    private FindRecipes.Datum datum;

    private DelicacyDetailsAdapterFolderCallback delicacyDetailsAdapterFolderCallback;

    public DelicacyDetailsAdapter(Context context, FindRecipes.Datum datum, DelicacyDetailsAdapterFolderCallback delicacyDetailsAdapterFolderCallback) {
        this.context = context;
        this.datum = datum;
        this.delicacyDetailsAdapterFolderCallback = delicacyDetailsAdapterFolderCallback;
    }

    @NonNull
    @Override
    public DelicacyDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DelicacyDetailsViewHolder(LayoutInflater.from(context).inflate(R.layout.delicacy_details_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DelicacyDetailsViewHolder delicacyDetailsViewHolder, int i) {
        delicacyDetailsViewHolder.init(datum, i, delicacyDetailsAdapterFolderCallback);
    }

    @Override
    public int getItemCount() {
        if (datum != null && datum.getSteps() != null) return 1 + datum.getSteps().size();
        return 0;
    }

    public class DelicacyDetailsViewHolder extends RecyclerView.ViewHolder {

        private TextView title, details;
        private ImageView imageView;

        public DelicacyDetailsViewHolder(View itemView) {
            super(itemView);
            itemView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT));
            title = itemView.findViewById(R.id.title);
            details = itemView.findViewById(R.id.details_text);
            imageView = (ImageView) itemView.findViewById(R.id.icon);
        }

        public void init(FindRecipes.Datum datum, int i, DelicacyDetailsAdapterFolderCallback delicacyDetailsAdapterFolderCallback) {
            title.setText(datum.getTitle());
            String imageUrl = "";
            String detailsString = "";
            if (i == 0) {
                detailsString = datum.getImtro() + "\n" + datum.getIngredients() + "\n" + datum.getBurden();
                imageUrl = datum.getAlbums().get(0);
            } else {
                imageUrl = datum.getSteps().get(i - 1).getImg();
                detailsString = datum.getSteps().get(i - 1).getStep();
            }
            Glide.with(imageView).load(imageUrl).into(imageView);
            details.setText(detailsString);
            final String finalImageUrl = imageUrl;
            final String finalDetailsString = detailsString;
            itemView.setOnClickListener(view ->
                    delicacyDetailsAdapterFolderCallback.openFloder(itemView, finalImageUrl, datum.getTitle(), finalDetailsString)
            );
        }
    }

    public interface DelicacyDetailsAdapterFolderCallback {
        void openFloder(View view, String imageUrl, String title, String description);
    }
}
