package com.ypz.supportquicknews.preservationData;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

/**
 * Created by kingadmin on 2018/3/4.
 */

public class PreservationDataSP {

    private static PreservationDataSP preservationDataSP;
    private SharedPreferences sharedPreferences;

    private PreservationDataSP(Context context){
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences("user", Context.MODE_PRIVATE);
    }

    public static void init(Context context){
        if (preservationDataSP==null){
            synchronized (PreservationDataSP.class){
                preservationDataSP = new PreservationDataSP(context);
            }
        }
    }

    public static PreservationDataSP getPreservationDataSP() {
        return preservationDataSP;
    }

    public void saveNewsKeyValues(String key,String values){
        sharedPreferences.edit().putString("News"+key, values).apply();
    }

    public String getSaveNewsKeyValues(String key){
        return sharedPreferences.getString("News"+key, "");
    }

    public void saveUserId(String userId){
        sharedPreferences.edit().putString("userId", userId).apply();
    }

    public String getUserId(String userId){
        return sharedPreferences.getString("userId", "");
    }

    public void saveUserSex(String sex){
        sharedPreferences.edit().putString("sex", sex).apply();
    }

    public String getUserSex(){
        return sharedPreferences.getString("sex","男" );
    }

    public String getUserIcomUrl(){return sharedPreferences.getString("userIcon","");}

    public void setUserIcomUrl(String url){ sharedPreferences.edit().putString("userIcon",url).apply();}

    public void saveUserNickname(String nickName){
        sharedPreferences.edit().putString("nickname",nickName).apply();
    }

    public String getUserNickname(){
        return sharedPreferences.getString("nickname","");
    }

    public void saveNewsKeyTimeValues(@NotNull String type, long currentTimeMillis) {
        sharedPreferences.edit().putLong("NewsTime"+type,currentTimeMillis).apply();
    }
    public long getNewsKeyTimeValues(@NotNull String type){
        return sharedPreferences.getLong("NewsTime"+type,0);
    }
}
