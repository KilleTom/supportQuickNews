package com.ypz.supportquicknews.moduleFunction.news

import android.databinding.BaseObservable

/**
 * Created by kingadmin on 2018/2/8.
 */

class NewsUIModule : BaseObservable{
    constructor(){

    }

    /*@Bindable*/
    var newstitle = "头条"
    set(value) {
        field = value
      /*  notifyPropertyChanged(BR.newstitle)*/
    }

}
