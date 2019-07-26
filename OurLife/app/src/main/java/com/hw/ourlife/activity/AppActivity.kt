package com.hw.ourlife.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.hw.ourlife.R

class AppActivity : AppCompatActivity(){

    var str = "";
    var v1 = "s";

    //    var tvTT: TextView = findViewById(R.id.tv_tt);
    var tvTT: TextView? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        val tvTT = findViewById<TextView>(R.id.tv_tt);
        tvTT.text = "a";
        str = "str is $v1"
        println(str);
    }

    fun anyn(obj: Any): Int? {
        if (obj is String) {
            return obj.length;
        }
        return 0;
    }

    fun myFor() {
        val items = listOf("my", "me", "our", "res")
        //for
        for (item in items) {
            println("list data is $item")
        }
        //for i++
        for (index in items.indices) {
            println("index is $index,item is ${items[index]} or ${items.get(index)}")
        }
    }

    fun myWhile() {
        val items = listOf("my", "me", "our", "res")
        var i = 0
        while (i < items.size) {
            println(" i++ item is ${items[i]}")
            i++
        }
        var j = 0
        var boo = false
        while (!boo) {
            println(" boo item is ${items[i]}")
            j++
            if (j >= items.size) {
                boo = true
            }
        }
    }
}
