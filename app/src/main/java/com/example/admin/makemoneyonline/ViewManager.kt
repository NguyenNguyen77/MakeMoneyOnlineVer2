package com.example.admin.makemoneyonline

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.provider.AlarmClock
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat.startActivity
import android.util.Log
import com.example.admin.makemoneyonline.LoginActivity
import com.example.admin.makemoneyonline.MainActivity
import com.example.admin.makemoneyonline.ViewManager.VIEW_KEY
import java.util.ArrayList

public class ViewManager private constructor(){

    private var currentActivity: Activity? = null
    private var mViewKey = VIEW_KEY.LOGIN_SCREEN
    private val mListActivity = ArrayList<Activity>()

    var mProgressDialog: ProgressDialog? = null
    val TAG = "ViewManager"

    @Volatile
    private var instance: ViewManager? = null
    public var instance1 : ViewManager? = null

//    @Synchronized
//    fun getInstance(): ViewManager {
//        if (instance == null) {
//            instance = ViewManager()
//        }
//        return instance as ViewManager
//    }

    init {
        // define in constructor
    }

    private object Holder { val INSTANCE = ViewManager() }

    companion object {
        @JvmStatic
        fun getInstance(): ViewManager{
            return Holder.INSTANCE
        }
    }

    fun setActivity(a: Activity) {
        currentActivity = a
        if (mListActivity.contains(a) == false) {
            mListActivity.add(a)
        }
    }



    private var pickerManager: ViewManager? = null

    fun getPickerManager(): ViewManager? {
        return pickerManager
    }

    fun setPickerManager(pickerManager: ViewManager) {
        this.pickerManager = pickerManager
    }

    /////
    enum class VIEW_KEY {
        LOGIN_SCREEN,
        MAIN_SCREEN,
    }

    enum class ERROR_CODE {
        LOGIN_FAIL,
        PLUS_POINT_FAILED,
    }

    public fun setView (key : VIEW_KEY) {
        when (key) {
            VIEW_KEY.LOGIN_SCREEN -> viewLoginActivity()
            VIEW_KEY.MAIN_SCREEN -> viewMainActivity()
        }

    }

    public fun setViewKey(viewKey: VIEW_KEY) {
        mViewKey = viewKey;
    }

    fun getActivity(): Class<*>? {
        Log.d(TAG, "getView()=" + getView().toString())
        return convKEYtoClass(getView())
    }

    fun convKEYtoClass (viewKey : VIEW_KEY) : Class<out Activity> {
        val activity : Class <out Activity>
       when (viewKey) {
           VIEW_KEY.LOGIN_SCREEN -> activity = LoginActivity::class.java
           VIEW_KEY.MAIN_SCREEN -> activity = MainActivity::class.java
       }

        return activity
    }

    fun getView() : VIEW_KEY {
        return mViewKey
    }

    private fun viewLoginActivity() {
        val activity = currentActivity

        if (activity == null) {
            return
        }
        val intent = Intent(activity.getApplicationContext(), LoginActivity::class.java)
        activity.startActivity(intent)
        setViewKey(VIEW_KEY.LOGIN_SCREEN)
        removeOtherActivity(convKEYtoClass(VIEW_KEY.LOGIN_SCREEN).newInstance())

    }

    private fun viewMainActivity() {
        val activity = currentActivity

        if (activity == null) {
            return
        }
        val intent = Intent(activity.getApplicationContext(), MainActivity::class.java)
        activity.startActivity(intent)
        setViewKey(VIEW_KEY.MAIN_SCREEN)
        removeOtherActivity(convKEYtoClass(VIEW_KEY.MAIN_SCREEN).newInstance())
    }

    public fun handleBackScreen () {
        Log.d(TAG, "handleBackScreen. mViewKey: $mViewKey")
        when (mViewKey) {
            VIEW_KEY.MAIN_SCREEN -> setView(VIEW_KEY.LOGIN_SCREEN)
        }
    }

    fun finishListActivity() {
        for (stack in mListActivity) {
            stack.finish()
        }
        mListActivity.clear()
    }

    fun finishActivity(activity: Activity) {
        Log.d(TAG, "finishActivity. activity: $activity")
        Log.d(TAG, "finishActivity. mListActivity.size: " + mListActivity.size)
        for (stack in mListActivity) {
            Log.d(TAG, "finishActivity. stack: $stack")
            if (stack == activity) {
                stack.finish()
                mListActivity.remove(stack)
            }
        }
    }

    fun removeOtherActivity (activity: Activity) {
        Log.d(TAG, "finishActivity. activity: $activity")
        Log.d(TAG, "finishActivity. mListActivity.size: " + mListActivity.size)
        for (stack in mListActivity) {
            Log.d(TAG, "finishActivity. stack: $stack")
            if (stack != activity) {
                stack.finish()
                mListActivity.remove(stack)
            }
        }
    }
}