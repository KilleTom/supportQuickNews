package com.ypz.supportquicknews.moduleFunction.humorousMoment.cartoon.net;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;

/**
 * Created by kingadmin on 2018/3/9.
 */

public class CategoryResult {

    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("reason")
    private String reason;
    @SerializedName("result")
    private List<String> result = null;

    public class ComicType extends BmobObject {
       private String type;

        public ComicType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }


    public CategoryResult(int errorCode, String reason, List<String> result) {
        this.errorCode = errorCode;
        this.reason = reason;
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getReason() {
        return reason;
    }

    public List<String> getResult() {
        new Thread(() -> {
            Log.i("ypz","开始插入数据");
            if (result != null) {
                Log.i("ypz","....");
                List<BmobObject> typeList = new ArrayList<>();
                for (String type : result) {
                    typeList.add(new ComicType(type));
                }
                Log.i("ypz","开始插入数据");
                new BmobBatch().insertBatch(typeList).doBatch(new QueryListListener<BatchResult>() {
                    @Override
                    public void done(List<BatchResult> list, BmobException e) {
                        if(e==null){
                            for(int i=0;i<list.size();i++){
                                BatchResult result = list.get(i);
                                BmobException ex =result.getError();
                                if(ex==null){
                                    Log.i("ypz","第"+i+"个数据批量添加成功："+result.getCreatedAt()+","+result.getObjectId()+","+result.getUpdatedAt());
                                }else{
                                    Log.i("ypz","第"+i+"个数据批量添加失败："+ex.getMessage()+","+ex.getErrorCode());
                                }
                            }
                        }else{
                            Log.i("ypz","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        }).start();
        return result;
    }

}
