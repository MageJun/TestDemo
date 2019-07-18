package com.example.testdemo

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.sh.sdk.shareinstall.ShareInstall

class MyApplication :Application(){

     companion object {
         var sInstance: MyApplication? = null
     }
    override fun onCreate() {
        super.onCreate()
        sInstance = this
        if(checkIsMainProcess()){
            ShareInstall.getInstance().init(this)
        }
    }

    private fun checkIsMainProcess():Boolean{
        val mpid = android.os.Process.myPid()
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runProcessArr = am.runningAppProcesses
        runProcessArr.forEach {
                t->
            if(t.pid==mpid){
                return applicationInfo.packageName.equals(t.processName)
            }
        }

        return false
    }

}