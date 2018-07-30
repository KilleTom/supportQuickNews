package com.ypz.supportquicknews.moduleFunction.humorousMoment;

import android.graphics.drawable.Drawable;

/**
 * Created by kingadmin on 2018/4/4.
 */

public class HumorousItem {

    private String title;

    private String subHead;

    private Drawable drawable;

    public HumorousItem(String title, String subHead, Drawable drawable) {
        this.title = title;
        this.subHead = subHead;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public String getSubHead() {
        return subHead;
    }

    public Drawable getDrawable() {
        return drawable;
    }
}
