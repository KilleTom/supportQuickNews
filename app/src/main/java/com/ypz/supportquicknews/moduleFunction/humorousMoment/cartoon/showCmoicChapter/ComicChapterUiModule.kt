package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.showCmoicChapter

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.ypz.supportquicknews.BR

/**
 * Created by kingadmin on 2018/3/21.
 */

class ComicChapterUiModule : BaseObservable(){

    @Bindable
    var comicChapterToolbarTitle = ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.comicChapterToolbarTitle)
    }


}
