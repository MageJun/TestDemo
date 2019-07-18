package com.example.testdemo

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.ugirls.app02.permission.PermissionUtils
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

//        findViewById<Button>(R.id.button).setOnClickListener(object : View.OnClickListener{
//            override fun onClick(p0: View?) {
//                requestPermission()
//            }
//
//        })
        button.setOnClickListener(object:View.OnClickListener{
            override fun onClick(p0: View?) {
                niceToast("权限验证")
                startFloatWindowService()
//                startActivity(Intent(this@SecondActivity,ThirdActivity::class.java))

//                requestPermission()

            }
        })
    }

    private fun startFloatWindowService(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(!Settings.canDrawOverlays(this)){
                startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")),0)
            }else{
                bindService(Intent(this,FloatWindowService::class.java),serviceCOnnection, Context.BIND_AUTO_CREATE)
            }
        }else{
            bindService(Intent(this,FloatWindowService::class.java),serviceCOnnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (requestCode == 0) {
                if (!Settings.canDrawOverlays(this)) {
                    niceToast("授权失败")
                }else{
                    startFloatWindowService()
                }
            }
        }
    }
    private var service:FloatWindowService?= null
    private val serviceCOnnection = object:ServiceConnection{
        override fun onServiceDisconnected(p0: ComponentName?) {

        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            service = (p1 as FloatWindowService.InnerBinder).getService()

            service!!.setListener(object:FloatWindowService.OnClickListener{
                override fun onClick() {
                    niceToast("点击事件")
                    ActivityUtils.getInstance().moveVideoTaskToFront()
//                    ActivityUtils.getInstance().startActivity(this@SecondActivity,null)
                }

            })

            service!!.startFloatWindow()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        service!!.stopFloatWindow()
        unbindService(serviceCOnnection)
    }

    /**
     * 判断权限是否已经全部授予
     */

    fun requestPermission() {
        PermissionUtils.requestRuntimePermission(this,object :PermissionUtils.OnPermissionListener{
            override fun onPermissionGranted(permissions: MutableList<String>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPermissionDenied(data: MutableList<String>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        },
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        PermissionUtils.requestRuntimePermission(
//            this,
//            object: PermissionUtils.OnPermissionListener {
//                override onPermissionGranted(permissions:List<String>) {
//                    // 接受视频请求
//                }
//
//                override onPermissionDenied(List<String> data) {
//                    // 接受视频请求
//
//                }
//            },
//            Manifest.permission.CAMERA,
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        );
    }

    fun niceToast(message:String,tag:String = javaClass.simpleName,length:Int = Toast.LENGTH_SHORT){
        Toast.makeText(this,"[$localClassName] $message",length).show()
    }
}
