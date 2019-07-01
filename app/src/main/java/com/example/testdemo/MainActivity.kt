package com.example.testdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

}
