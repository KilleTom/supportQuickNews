package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net

import android.util.Log
import cn.bmob.v3.BmobBatch
import cn.bmob.v3.BmobObject
import cn.bmob.v3.datatype.BatchResult
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListListener
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by kingadmin on 2018/3/22.
 */

class ChapterContentResult(@field:SerializedName("error_code")
                           val errorCode: Int, @field:SerializedName("reason")
                           val reason: String, @field:SerializedName("result")
                           val result: Result) {

    inner class Result(@field:SerializedName("comicName")
                       val comicName: String, @field:SerializedName("chapterId")
                       val chapterId: Int, imageList: List<ContentImages>) {
        @SerializedName("imageList")
        var imageList: List<ContentImages>? = null

        init {
            this.imageList = imageList
        }

        fun insertBmobContentImages() {
            Log.i("ypz", (imageList == null || imageList!!.size == 0).toString())
            if (imageList == null || imageList!!.size == 0) return
            for (imageLists in imageList!!) {
                imageLists.tableName = "ContentImages"
                imageLists.comicName = comicName
                imageLists.chapterId = chapterId.toString()
                imageLists.chapterContentId = "comicName" + "-" + comicName + "-chapterId-" + chapterId + "-imageId:" + imageLists.id
            }
            val bmobObjects = ArrayList<BmobObject>()
            bmobObjects.addAll(this.imageList!!)
            Log.i("ypz", "kaishi插入" + bmobObjects.size)
            object : Thread() {
                override fun run() {
                    try {
                        BmobBatch().insertBatch(bmobObjects).doBatch(object : QueryListListener<BatchResult>() {
                            override fun done(list: List<BatchResult>, e: BmobException?) {
                                if (e == null) {
                                    for (batchResult in list) {
                                        Log.i("ypz", batchResult.isSuccess.toString() + "")
                                    }
                                } else
                                    Log.i("ypz", "漫画书" + comicName + "章节" + chapterId + "漫画内容插入失败" + e.message)
                            }
                        })
                    } catch (e: Exception) {
                        Log.i("ypz", e.message)
                    }

                }
            }.start()
        }
    }

    inner class ContentImages(@field:SerializedName("imageUrl")
                              val imageUrl: String, @field:SerializedName("id")
                              val id: Int) : BmobObject() {
        var comicName: String? = null
        var chapterId: String? = null
        var chapterContentId: String? = null
    }

}


