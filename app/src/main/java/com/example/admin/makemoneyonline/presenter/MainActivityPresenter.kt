package com.example.admin.makemoneyonline.presenter

import android.content.Context
import com.example.admin.makemoneyonline.*
import com.example.admin.makemoneyonline.AsynTaskManager.AsyncTaskCompleteListener
import com.example.admin.makemoneyonline.AsynTaskManager.CaseManager
import com.example.admin.makemoneyonline.AsynTaskManager.MakeMoneyOnlineTask
import com.example.admin.makemoneyonline.AsynTaskManager.ResultObject

class MainActivityPresenter(context: Context, mainActivity: MainActivity): BaseMethod(), IMainActivityPresenter, AsyncTaskCompleteListener<ResultObject> {

    private var mMainActivity: IMainActivity? = null
    internal var mContext: Context? = null

    fun MainActivityPresenter(context: Context, mainActivity: IMainActivity) {
        this.mContext = context
        this.mMainActivity = mainActivity
    }


    override fun sendRequestGetAllStaff() {
        MakeMoneyOnlineTask(this).execute(CaseManager(KeyManager().REQ_GETALLSTAFF, UrlManager().GET_ALLSTAFF, null));
    }
    override fun onTaskError(s: String, CaseRequest: String) {

    }

    override fun onTaskCompleted(s: String, CaseRequest: String) {
        val tem = ""

    }

}