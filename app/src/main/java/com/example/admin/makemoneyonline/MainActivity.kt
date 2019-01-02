package com.example.admin.makemoneyonline

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.admin.makemoneyonline.adapter.TabPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = TabPagerAdapter(supportFragmentManager)
        if(viewpager_main!=null) {
            viewpager_main.adapter = fragmentAdapter
            tab_layout.setupWithViewPager(viewpager_main)
        }

    }


}
