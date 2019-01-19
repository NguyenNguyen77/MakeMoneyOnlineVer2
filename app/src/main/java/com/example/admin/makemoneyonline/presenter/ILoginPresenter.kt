package com.example.admin.makemoneyonline.presenter

import com.example.admin.makemoneyonline.AsynTaskManager.MakeMoneyOnlineTask

interface ILoginPresenter {
    abstract fun sendRequestLogin(userName: String, passWord: String)
}