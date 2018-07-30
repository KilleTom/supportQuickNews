package com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.findRecipes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.ypz.supportquicknews.R;
import com.ypz.supportquicknews.moduleFunction.lifeHelper.cooking.net.TagDetails;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * Created by kingadmin on 2018/4/5.
 */

public class FlowAdapter extends TagAdapter<TagDetails.TagItem> {

    private List<TagDetails.TagItem> results;
    private Context context;

    public FlowAdapter(List<TagDetails.TagItem> datas,Context context) {
        super(datas);
        results = datas;
        this.context = context;
    }


    @Override
    public View getView(FlowLayout parent, int position, TagDetails.TagItem tagItem) {
        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.tv, parent, false);
        textView.setText(tagItem.getName());
        return textView;
    }
}
