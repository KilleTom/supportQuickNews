package com.ypz.supportquicknews.moduleFunction.setting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ypz.supportquicknews.R;

import java.util.List;

/**
 * Created by kingadmin on 2018/4/8.
 */

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {

    private List<SettingItem> items;

    private Context context;

    public SettingAdapter(List<SettingItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SettingViewHolder(LayoutInflater.from(context).inflate(R.layout.setting_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder settingViewHolder, int i) {
        settingViewHolder.initData(items.get(i));

    }

    @Override
    public int getItemCount() {
        if (items==null) return 0;
        Log.i("ypz",items.size()+"");
        return items.size();
    }

    public class SettingViewHolder extends RecyclerView.ViewHolder{
        private ImageView icon ;
        private TextView message;

        public SettingViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            message = itemView.findViewById(R.id.message);
        }

        public void initData(SettingItem settingItem) {
            Glide.with(icon).load(settingItem.getDrawable()).into(icon);
            message.setText(settingItem.getMessage());
            itemView.setOnClickListener(view -> settingItem.getItemClickToDo());
        }
    }
}
