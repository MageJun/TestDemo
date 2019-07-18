package com.example.testdemo

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import org.greenrobot.eventbus.EventBus

class AccessibilityHelperService:AccessibilityService() {
    companion object{
         val tag = AccessibilityHelperService::class.java.simpleName
    }


    //服务启动时初始化回调方法
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        serviceInfo.packageNames= getAllAppNamesPackages().toTypedArray()


    }
    //服务中断时回调方法
    override fun onInterrupt() {
    }

    //主要方法，反馈的事件返回
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        val eventType = p0!!.eventType
        val typeStr =AccessibilityEvent.eventTypeToString(eventType)
        Log.d(tag,"eventyType = $typeStr")
        when(eventType){
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED->{//窗口内视图内容发生变化，如视图树的布局
                val className = p0!!.className
                Log.d(tag,"className = $className")
                if(className==null||className.startsWith("android.widget")){
                    return
                }
                EventBus.getDefault().post(MessageEvent(className.toString()))
            }
        }
    }
    fun getAllAppNamesPackages():List<String>
    {   var i=0
        val pm = packageManager;
        val list = pm.getInstalledPackages (PackageManager.GET_UNINSTALLED_PACKAGES);
        val resultList = ArrayList<String>()
        list.forEach{
                packageInfo->
        //获取到设备上已经安装的应用的名字,即在AndriodMainfest中的app_name。
        val appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
        //获取到应用所在包的名字,即在AndriodMainfest中的package的值。
        val pn = packageInfo.packageName;
        Log.d(tag, "应用的名字:$appName");
        Log.d(tag, "应用的包名字:$pn");
        if(packageName != pn){
            resultList.add(pn)
        }
        i++;
    }
        Log.i(tag, "应用的总个数:$i");
        return resultList
    }

}