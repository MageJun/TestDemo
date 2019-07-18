package com.example.testdemo;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.List;

public class ActivityUtils {

    private ActivityUtils(){}

    private static ActivityUtils instance;

    public static ActivityUtils getInstance(){
        if(instance==null){
            instance = new ActivityUtils();
        }
        return instance;
    }


    public void moveVideoTaskToFront() {
        android.app.ActivityManager manager = (android.app.ActivityManager) MyApplication.Companion.getSInstance().getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        List<ActivityManager.RunningTaskInfo> taskInfo = manager
                .getRunningTasks(20);
        for (int i = 0; i < taskInfo.size(); i++) {
            ActivityManager.RunningTaskInfo info = taskInfo.get(i);
            Log.d("ActivityUtils",  "TaskInfo id = "+info.id+",packageName = "+info.baseActivity.getPackageName()+",class = "+info.baseActivity.getShortClassName());
            // 判断包名是否相同
            if (info.baseActivity.getPackageName().equals(MyApplication.Companion.getSInstance().getPackageName())
                    // 判断是否是视频页面
                    && info.baseActivity.getShortClassName().contains(SecondActivity.class.getSimpleName())) {

                while (!isTopActivity(SecondActivity.class.getName())){
                   if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q){

                       manager.moveTaskToFront(info.id, android.app.ActivityManager.MOVE_TASK_WITH_HOME);//关键
                   }else{
                        manager.moveTaskToFront(info.taskId, android.app.ActivityManager.MOVE_TASK_WITH_HOME);
                   }
                }

                return;
            }
        }
//
    }

    public void startActivity(Context context,ActivityManager.RunningTaskInfo info){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHE");
        intent.addCategory("android.intent.action.VIEW");
        ComponentName cn = new ComponentName(context.getPackageName(),SecondActivity.class.getName());
        intent.setComponent(cn);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.Companion.getSInstance().startActivity(intent);
    }
    private boolean isTopActivity(String className)
    {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) MyApplication.Companion.getSInstance().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Log.d("ActivityUtils", "isTopActivity = " + cn.getClassName());
        if (cn.getClassName().equals(className))
        {
            isTop = true;
        }
        Log.d("ActivityUtils", "isTop = " + isTop);
        return isTop;
    }
}
