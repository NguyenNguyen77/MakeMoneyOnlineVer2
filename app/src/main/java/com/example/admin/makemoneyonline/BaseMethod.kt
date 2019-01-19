package com.example.admin.makemoneyonline

import android.os.Handler
import android.util.Log
import com.example.admin.makemoneyonline.AsynTaskManager.MakeMoneyOnlineTask
import com.example.admin.makemoneyonline.model.LoginGson
import com.example.admin.makemoneyonline.model.MineUserEntity


import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.google.gson.Gson


open class BaseMethod() {
    var gson = Gson()

    fun httpGetJson() {
        try {
            Fuel.get("api/getAllStaff").responseJson { request, response, result ->
                var responseJson: String = result.get().content.toString()
                Log.d("KhoaNguyen", "Json: " + responseJson)
                var gson = Gson()
                var mMineUserEntity = gson?.fromJson(responseJson, MineUserEntity.MineUserInfo::class.java)
                Log.d("KhoaNguyen", "Status: " + mMineUserEntity.status)
                Log.d("KhoaNguyen", "Code: " + mMineUserEntity.success.code)
                Log.d("KhoaNguyen", "ID[0]: " + mMineUserEntity.success.staff[1].id)
                Log.d("KhoaNguyen", "Name[0]: " + mMineUserEntity.success.staff[1].name)

            }
        } catch (e: Exception) {
//            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() in ViewManager

        } finally {
            //dismissInprogress Dialog to ViewManager
        }
    }

    fun httpPostJson(url: String, parameters: List<Pair<String, Any?>>? = null): String {
        var mMineUserEntity: String = ""
        var isResponse = false
        try {
            Fuel.post(url, parameters).responseJson { request, response, result ->
                var responseJson: String = result.get().content.toString()
                Log.d("KhoaNguyen", "Json: " + responseJson)
                var gson = Gson()
                mMineUserEntity = gson?.fromJson(responseJson, LoginGson.LoginGson::class.java).toString()
                isResponse = true
            }
        } catch (e: Exception) {
            //            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() in ViewManager
        } finally {
            //dismissInprogress Dialog to ViewManager
        }
        while (!isResponse) {
            Thread.sleep(100)
            Log.d("KhoaNguyen", "Wait for http response")
        }
        return mMineUserEntity
    }
}