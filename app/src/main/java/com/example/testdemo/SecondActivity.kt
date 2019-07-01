package com.example.testdemo

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
                startActivity(Intent(this@SecondActivity,ThirdActivity::class.java))

//                requestPermission()

            }
        })
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
