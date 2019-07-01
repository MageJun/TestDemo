package com.example.testdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ThirdActivity : AppCompatActivity() {

    val mAdapter:InnerAdapter by lazy {
        val items = Array<String>(10){"$it"}
        InnerAdapter(items)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = mAdapter

    }

    class InnerAdapter(val items:Array<String>):RecyclerView.Adapter<InnerHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
            val textView =TextView(parent.context)
            return InnerHolder(textView)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: InnerHolder, position: Int) {
            holder.textView.text=items[position]
        }

    }

    class InnerHolder(itemView:View) : ViewHolder(itemView) {
        val textView = itemView as TextView
    }
}
