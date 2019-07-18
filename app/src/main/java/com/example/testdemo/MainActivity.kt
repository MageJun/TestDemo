package com.example.testdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.sh.sdk.shareinstall.ShareInstall
import com.sh.sdk.shareinstall.listener.AppGetInfoListener
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ShareInstall.getInstance().getInfo(intent,shareInstallListener)
        findViewById<Button>(R.id.button2).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                var intent = Intent()
                intent.setClass(this@MainActivity,SecondActivity::class.java)
                startActivity(intent)
            }

        })



    }
    fun tmp(strs:Array<String>){
        var st = {"你";"好"}
        val sss = arrayOf("你","好")
        var ssss = Array<String>(2){"你";"好"}
        var strs = Array<String>(4){"你";"好";"漂";"亮"}


    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ShareInstall.getInstance().getInfo(getIntent(),shareInstallListener)
    }

    //ShareInstall一键拉起回调方法
    private val shareInstallListener =object: AppGetInfoListener {
        override fun onGetInfoFinish(info: String?) {
            // 客户端获取到的参数是json字符串格式
            Log.println(Log.DEBUG,"ShareInstall", "info = $info")
            try {
                val `object` = JSONObject(info)
                // 通过该方法拿到设置的渠道值，剩余值为自定义的其他参数
                val channel = `object`.optString("channel")
                Log.d("ShareInstall", "channel = $channel")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }

}
