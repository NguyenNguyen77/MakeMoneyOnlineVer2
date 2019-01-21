package com.example.admin.makemoneyonline.presenter

import android.content.Context
import com.example.admin.makemoneyonline.*
import com.example.admin.makemoneyonline.AsynTaskManager.AsyncTaskCompleteListener
import com.example.admin.makemoneyonline.AsynTaskManager.CaseManager
import com.example.admin.makemoneyonline.AsynTaskManager.MakeMoneyOnlineTask
import com.example.admin.makemoneyonline.AsynTaskManager.ResultObject

class LoginPresenter(context: Context, loginActivity: LoginActivity) : BaseMethod(), ILoginPresenter, AsyncTaskCompleteListener<ResultObject> {

    private var mLoginActivity: ILoginActivity? = null
    internal var mContext: Context? = null

    fun LoginPresenter(context: Context, loginActivity: ILoginActivity) {
        this.mContext = context
        this.mLoginActivity = loginActivity
    }

    override fun sendRequestLogin(userName: String, passWord: String) {
        MakeMoneyOnlineTask(this).execute(CaseManager(KeyManager().REQ_LOGIN, UrlManager().LOGIN_URL, getParamJson(userName, passWord)));
    }

    fun getParamJson(userName: String, passWord: String): List<Pair<String, Any?>>? {
        var paramJson: List<Pair<String, Any?>>? = null
        paramJson = listOf(KeyManager().PARAM_USER_NAME to userName, KeyManager().PARAM_PASS_WORD to passWord)
        return paramJson
    }




    override fun onTaskError(s: String, CaseRequest: String) {

    }

    override fun onTaskCompleted(s: String, CaseRequest: String) {
        val tem = ""
    }

}