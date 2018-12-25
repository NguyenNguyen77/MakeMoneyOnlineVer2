package com.example.admin.makemoneyonline

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.admin.makemoneyonline.adapter.TabPagerAdapter

import kotlinx.android.synthetic.main.activity_tablayout_demo.*


class TablayoutDemo : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tablayout_demo)

        val fragmentAdapter = TabPagerAdapter(supportFragmentManager)
        if(viewpager_main!=null) {
            viewpager_main.adapter = fragmentAdapter
            tab_layout.setupWithViewPager(viewpager_main)
        }


    }

}
