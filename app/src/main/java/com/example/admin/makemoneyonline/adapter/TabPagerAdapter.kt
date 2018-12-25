package com.example.admin.makemoneyonline.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.admin.makemoneyonline.viewfragment.home_fragment
import com.example.admin.makemoneyonline.viewfragment.setting_fragment
import com.example.admin.makemoneyonline.viewfragment.user_fragment

class TabPagerAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                home_fragment()
            }
            1 -> user_fragment()
            else -> {
                return setting_fragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Home"
            1 -> "User"
            else -> {
                return "Setting"
            }
        }
    }
}