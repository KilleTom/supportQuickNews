package com.ypz.supportquicknews.base;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by kingadmin on 2018/4/8.
 */

public class ImageAdapter extends BaseObservable {
    @BindingAdapter("imageUrl")
    public static void glideLoad(ImageView imageView,String url){
        Glide.with(imageView).load(url).into(imageView);
    }
}
