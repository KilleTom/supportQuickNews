package com.ypz.supportquicknews.rxUtil;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.ypz.supportquicknews.preservationData.PreservationDataSP;
import com.ypz.supportquicknews.statistics.CycletimesFunctionStatistics;
import com.ypz.supportquicknews.statistics.FunctionStatistics;
import com.ypz.supportquicknews.statistics.ModulePercentage;
import com.ypz.supportquicknews.statistics.UserData;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


/**
 * Created by kingadmin on 2018/3/5.
 */

public class MyBmobUtil<T extends BmobObject> {

    private static MyBmobUtil myBmobUtil;
    private Context context;

    private MyBmobUtil(Context context) {
        try {
            this.context = context;
            Log.i("ypz", "init");
            Bmob.initialize(context, "c0779d60cbb1ecfc8084eee03a3ae32c");
        } catch (Exception e) {
            Log.i("ypzBmob", e.getMessage());
        }
    }

    public static void initMyBmobUtil(Context context) {
        if (myBmobUtil == null) {
            synchronized (MyBmobUtil.class){
                myBmobUtil = new MyBmobUtil(context);
            }
        }
    }

    public static MyBmobUtil getMyBmobUtil(Context context) {
        if (myBmobUtil == null) {
            synchronized (MyBmobUtil.class){
                myBmobUtil = new MyBmobUtil(context);
            }
        }
        return myBmobUtil;
    }

    /**
     * 用户登陆
     */
    public void login(String userId, String pwd, final MyBmobUtil.LoginCallback login) {
        isHaveUserData(userId, new QueryCallbak() {
            @Override
            public void haveData(List<UserData> list) {
                for (UserData userData : list) {
                    if (userData.getPassword().equals(pwd)) {
                        login.sucessful(userData);
                        return;
                    }
                }
                login.error("用户密码错误请确认输入正确账号密码");
            }
            @Override
            public void notData() {
                login.error("不存在该用户请你注册");
            }
            @Override
            public void queryError(String message) {
                login.error(message);
            }
        });
    }

    /**
     * 查询是否存在用户
     */
    public void isHaveUserData(String phone, QueryCallbak queryCallbak) {
        BmobQuery<UserData> userDataBmobQuery = new BmobQuery<>();
        userDataBmobQuery.addWhereEqualTo("userId", phone);
        userDataBmobQuery.findObjects(new FindListener<UserData>() {
            @Override
            public void done(List<UserData> list, BmobException e) {
                if (list != null) {
                    if (list.size() >= 1) {
                        queryCallbak.haveData(list);
                    } else queryCallbak.notData();
                } else {
                    if (e != null) {
                        queryCallbak.notData();
                    } else queryCallbak.queryError(e.getMessage());
                }
            }
        });
    }


    /**
     * 保存数据
     */
    public void saveDate(BmobObject t) {
        t.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e != null) Log.i("ypz", e.getMessage());
                else Log.i("ypzBmob", s);
            }
        });
    }

    /**
     * 上传统计数据模块功能使用情况统计插入
     */
    public void updateFunctionStatistics(String date, String moduleName, String functionName) {
        String userId = getUserId();
        getModuleTimes(date, moduleName, functionName, new FindListener<FunctionStatistics>() {
            @Override
            public void done(List<FunctionStatistics> list, BmobException e) {
                if (e != null || list.size() == 0) {
                    if (e != null)
                        Log.i("ypzBmob", "error" + e.getMessage() + "\ncode" + e.getErrorCode());
                    FunctionStatistics functionStatistics = new FunctionStatistics(moduleName, functionName, 1, date, userId);
                    functionStatistics.setTableName("FunctionStatistics");
                    Log.i("ypzBmob", "save");
                    saveDate(functionStatistics);
                } else {
                    String id = list.get(0).getObjectId();
                    /*list.get(0).setTableName("FunctionStatistics");*/
                    list.get(0).setTimes(list.get(0).getTimes() + 1);
                   /* Log.i("ypz", "Objectid" + id+"\n"+list.get(0).getTableName());
                    Log.i("ypz", list.get(0).getTimes() + "");
                    Log.i("ypz",list.get(0).getDate()+"\n"+list.get(0).getFunctionName()+"\n"+list.get(0).getModuleName());*/
                    updateData(list.get(0));
                }
            }
        });
    }

    public void getModuleTimes(String date, String moduleName, String functionName, FindListener findListener) {
        String userId = getUserId();
        BmobQuery<FunctionStatistics> dateQuery = new BmobQuery<>();
        dateQuery.addWhereEqualTo("date", date);
        BmobQuery<FunctionStatistics> userIdQuery = new BmobQuery<>();
        userIdQuery.addWhereEqualTo("userId", userId);
        BmobQuery<FunctionStatistics> moduleQuery = new BmobQuery<>();
        moduleQuery.addWhereEqualTo("moduleName", moduleName);
        BmobQuery<FunctionStatistics> functionNameQuery = new BmobQuery<>();
        functionNameQuery.addWhereEqualTo("functionName", functionName);
        List<BmobQuery<FunctionStatistics>> andQueries = new ArrayList<>();
        andQueries.add(dateQuery);
        andQueries.add(userIdQuery);
        andQueries.add(moduleQuery);
        andQueries.add(functionNameQuery);
        BmobQuery<FunctionStatistics> bmobQuery = new BmobQuery<>();
        bmobQuery.and(andQueries);
        bmobQuery.order("-times");
        bmobQuery.setLimit(1);
        bmobQuery.findObjects(findListener);
    }

    public void setModulePercentage(String date, String moduleName, String functionName) {
        Log.i("ypzBmob", "setModulePercentage");
        getModuleTimes(date, "All", "所有功能统计数", new FindListener<FunctionStatistics>() {
            @Override
            public void done(List<FunctionStatistics> list, BmobException e) {
                if (e != null || list.size() == 0) {
                    if (e != null)
                        Log.i("ypzBmob", "errorMessage" + e.getMessage() + "\nerrorCode" + e.getErrorCode());
                    Log.i("ypzBmob",list.size()+"");
                } else {
                    int times = list.get(0).getTimes();
                    getModuleTimes(date, moduleName, functionName, new FindListener<FunctionStatistics>() {
                        @Override
                        public void done(List<FunctionStatistics> list, BmobException e) {
                            if (e != null || list.size() == 0) {
                                if (e != null) Log.i("ypz", "errorMessage2" + e.getMessage());
                                return;
                            } else {
                                DecimalFormat dec = new DecimalFormat("0.00");
                                double allTimes = times;
                                int moduleTimes = list.get(0).getTimes();
                                double percentage = list.get(0).getTimes() / allTimes;
                                String percentage_str =  dec.format(percentage * 100)+"%";
                                ModulePercentage modulePercentage = new ModulePercentage(
                                        moduleName, date, getUserId(), moduleTimes, percentage_str, times
                                );
                                modulePercentage.setTableName("ModulePercentage");
                                Log.i("ypzBmob", "saveDate");
                                BmobQuery<ModulePercentage> moudleNameQuery = new BmobQuery<>();
                                BmobQuery<ModulePercentage> userIdQuery = new BmobQuery<>();
                                BmobQuery<ModulePercentage> dateQuery = new BmobQuery<>();
                                List<BmobQuery<ModulePercentage>> queries = new ArrayList<>();
                                moudleNameQuery.addWhereEqualTo("ModuleName",moduleName);
                                userIdQuery.addWhereEqualTo("userId",getUserId());
                                dateQuery.addWhereEqualTo("date",date);
                                queries.add(moudleNameQuery);
                                queries.add(userIdQuery);
                                queries.add(dateQuery);
                                BmobQuery<ModulePercentage> query = new BmobQuery<>();
                                query.and(queries);
                                query.order("-times");
                                query.findObjects(new FindListener<ModulePercentage>() {
                                    @Override
                                    public void done(List<ModulePercentage> list, BmobException e) {
                                        if (e!=null||list.size()==0){
                                            saveDate(modulePercentage);
                                        }else {
                                            list.get(0).setTableName("ModulePercentage");
                                            list.get(0).setAllModuleTimes(times);
                                            list.get(0).setPercentage(percentage_str);
                                            list.get(0).setTimes(moduleTimes);
                                            updateData(list.get(0));
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
            }
        });
    }

    public void setAllModulePercentage(String date,String moduleName,String functionName,int times){
        Log.i("ypzBmob", "setModulePercentage");
        getModuleTimes(date, "All", "所有功能统计数", new FindListener<FunctionStatistics>() {
            @Override
            public void done(List<FunctionStatistics> list, BmobException e) {
                if (e != null || list.size() == 0) {
                    if (e != null)
                        Log.i("ypzBmob", "errorMessage" + e.getMessage() + "\nerrorCode" + e.getErrorCode());
                    Log.i("ypzBmob",list.size()+"");
                } else {
                    int allTimes = list.get(0).getTimes();
                    BmobQuery<ModulePercentage> userIdQuery = new BmobQuery<>();
                    BmobQuery<ModulePercentage> dateQuery = new BmobQuery<>();
                    List<BmobQuery<ModulePercentage>> queries = new ArrayList<>();
                    userIdQuery.addWhereEqualTo("userId",getUserId());
                    dateQuery.addWhereEqualTo("date",date);
                    queries.add(userIdQuery);
                    queries.add(dateQuery);
                    BmobQuery<ModulePercentage> query = new BmobQuery<>();
                    query.and(queries);
                    query.order("-times");
                    query.findObjects(new FindListener<ModulePercentage>() {
                        @Override
                        public void done(List<ModulePercentage> modulePercentages, BmobException e) {
                            if (e!=null||modulePercentages.size()==0){
                                setModulePercentage(date, moduleName, functionName);
                            }else {
                                DecimalFormat dec = new DecimalFormat("0.00");
                                String percentage_str = "";
                                for (ModulePercentage m:modulePercentages) {
                                    if (m.getModuleName().equals(moduleName)){
                                        percentage_str = dec.format(times*1.0f/allTimes*100)+"%";
                                        m.setTimes(times);
                                    }else percentage_str = dec.format(m.getTimes()*1.0f/allTimes)+"%";
                                    m.setTableName("ModulePercentage");
                                    m.setPercentage(percentage_str);
                                    m.setAllModuleTimes(allTimes);
                                }
                                List<BmobObject> objects = new ArrayList<>();
                                objects.addAll(modulePercentages);
                                new BmobBatch().updateBatch(objects).doBatch(new QueryListListener<BatchResult>() {
                                    @Override
                                    public void done(List<BatchResult> list, BmobException e) {
                                        if (e==null&&list.size()>0){
                                            for (int i = 0; i < list.size(); i++) {
                                                if (list.get(i).getError()!=null) updateData(modulePercentages.get(i));
                                            }
                                        }else {
                                            for (ModulePercentage m:modulePercentages) {
                                                updateData(m);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    public void updateFunctionStatistics(int index, CycletimesFunctionStatistics... cycletimesFunctionStatistics) {
        String moduleName = cycletimesFunctionStatistics[index - 1].getModuleName(),
                functionName = cycletimesFunctionStatistics[index - 1].getFunctionName(),
                date = cycletimesFunctionStatistics[index - 1].getDate();
        Log.i("ypzBmob","index"+index);
        getModuleTimes(date, moduleName, functionName, new FindListener<FunctionStatistics>() {
                    @Override
                    public void done(List<FunctionStatistics> list, BmobException e) {
                        int times = 0;
                        Log.i("ypzBmob","done index"+(index-1));
                        if (e != null || list.size() == 0) {
                            if (e != null)
                                Log.i("ypzBmob", "error" + e.getMessage() + "\ncode" + e.getErrorCode());
                            FunctionStatistics functionStatistics = new FunctionStatistics(moduleName, functionName, 1, date, getUserId());
                            functionStatistics.setTableName("FunctionStatistics");
                            Log.i("ypzBmob", "save");
                            saveDate(functionStatistics);
                        } else {
                            String id = list.get(0).getObjectId();
                            list.get(0).setTableName("FunctionStatistics");
                            times = list.get(0).getTimes() + 1;
                            list.get(0).setTimes(times);
                            updateData(list.get(0));
                        }
                        if (index == cycletimesFunctionStatistics.length) {
                            Log.i("ypzBmob","set");
                            Log.i("ypzBmob","mName"+moduleName+String.valueOf(!(moduleName.equals("all")||moduleName.equals("All"))));
                            if (!(moduleName.equals("all")||moduleName.equals("All"))){
                                Log.i("ypzBmob","set");
                                setAllModulePercentage(date,moduleName,functionName,times);
                            }
                        } else updateFunctionStatistics(index+1,cycletimesFunctionStatistics);
                    }
                });
    }

    public String getUserId() {
        return TextUtils.isEmpty(PreservationDataSP.getPreservationDataSP().getUserId("")) ?
                Settings.Secure.ANDROID_ID : PreservationDataSP.getPreservationDataSP().getUserId("");
    }

    /**
     * 更新数据
     */
    public void updateData(BmobObject bmobObject) {
        bmobObject.update(bmobObject.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null) Log.i("ypz", e.getMessage() + "\ncode" + e.getErrorCode());
            }
        });
    }

    /**
     * 注册用户
     */
    public void register(UserData userData, String smsCode, RegisterCallback register) {
        BmobSMS.verifySmsCode(userData.getUserId(), smsCode, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    isHaveUserData(userData.getUserId(), new QueryCallbak() {
                        @Override
                        public void haveData(List<UserData> list) {
                            Log.i("ypz", "....已经存在用户无法注册请重新注册");
                            register.error("已经存在用户无法注册请重新注册");
                        }

                        @Override
                        public void notData() {
                            Log.i("ypz", "ssssssss");
                            userData.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        register.sucessful(userData);
                                    } else register.error(e.getMessage());
                                }
                            });
                        }

                        @Override
                        public void queryError(String message) {
                            register.error(message);
                        }
                    });
                } else register.error(e.getMessage());
            }
        });
    }

    /**
     * 发送验证码
     */
    public void sendSmsCode(String phone, SendSmsCodeCallback sendSmsCodeCallback) {
        BmobSMS.requestSMSCode(phone, "easyLive", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) sendSmsCodeCallback.sucessful();
                else sendSmsCodeCallback.error(e.getMessage());
            }
        });
    }

    public void uploadFile(String filePath, UploadFileCallback uploadFileCallback) {
        BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    uploadFileCallback.sucessful(bmobFile.getFileUrl());
                } else {
                    uploadFileCallback.error(e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value) {
                super.onProgress(value);
                uploadFileCallback.progress(value);
            }
        });
    }
    /**
     * 登陆接口回调
     */
    public interface LoginCallback {
        void sucessful(UserData userData);

        void error(String message);
    }
    /**
     * 注册接口回调
     */
    public interface RegisterCallback {
        void sucessful(UserData userData);

        void error(String message);
    }
    /**
     * 发送验证码回调
     */
    public interface SendSmsCodeCallback {
        void sucessful();

        void error(String message);
    }
    /**
     * 查询回调
     */
    public interface QueryCallbak {

        void haveData(List<UserData> list);

        void notData();

        void queryError(String message);
    }
    /**
     * 上传单文件回调
     */
    public interface UploadFileCallback {
        void sucessful(String fileurl);

        void error(String errorMessage);

        void progress(Integer progressValue);
    }
}
