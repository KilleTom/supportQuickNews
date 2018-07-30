package com.ypz.supportquicknews.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by kingadmin on 2018/4/3.
 */

public class MyWebView extends WebView {

    public interface MyWebViewScrollCallback{
        void onScrollChange(int dx,int dy);
    }
    private MyWebViewScrollCallback myWebViewScrollCallback;

    public void setMyWebViewScrollCallback(MyWebViewScrollCallback myWebViewScrollCallback) {
        this.myWebViewScrollCallback = myWebViewScrollCallback;
    }

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (myWebViewScrollCallback!=null){
            if (getScrollY()==0){
                myWebViewScrollCallback.onScrollChange(l-oldl,0);
            }else
            myWebViewScrollCallback.onScrollChange(l-oldl,t-oldt);
        }
    }


}
