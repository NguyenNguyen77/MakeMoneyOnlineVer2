package com.example.admin.makemoneyonline

import android.os.Handler
import android.util.Log
import com.example.admin.makemoneyonline.AsynTaskManager.MakeMoneyOnlineTask
import com.example.admin.makemoneyonline.model.LoginGson
import com.example.admin.makemoneyonline.model.MineUserEntity


import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.google.gson.Gson

private val TAG = "Debug"

open class BaseMethod() {


    fun httpGetJson(url: String): String {
        var gsonResponse = ""
        var isResponse = false
        var timeout = 0
        try {
            Fuel.get(url).responseJson { request, response, result ->
                var responseJson: String = result.get().content
                Log.d(TAG, "KhoaNguyen - Json: " + responseJson)
                var gson = Gson()
                gsonResponse = gson?.fromJson(responseJson, MineUserEntity.MineUserInfo::class.java).toString()
                isResponse = true
            }
        } catch (e: Exception) {
//            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() in ViewManager

        } finally {
        }
        while (!isResponse) {
            if (timeout > 200) {
                break
            }
            Thread.sleep(100)
            timeout++
            Log.d(TAG, "KhoaNguyen - Wait for http response")
        }
        return gsonResponse
    }


    fun httpGetJson(url: String, parameters: List<Pair<String, Any?>>? = null): String {
        var gsonResponse = ""
        var isResponse = false
        var timeout = 0
        try {
            Fuel.get(url, parameters).responseJson { request, response, result ->
                var responseJson: String = result.get().content
                Log.d(TAG, "KhoaNguyen - Json: " + responseJson)
                var gson = Gson()
                gsonResponse = gson?.fromJson(responseJson, MineUserEntity.MineUserInfo::class.java).toString()
                isResponse = true
            }
        } catch (e: Exception) {
//            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() in ViewManager

        } finally {
        }
        while (!isResponse) {
            if (timeout > 200) {
                break
            }
            Thread.sleep(100)
            timeout++
            Log.d(TAG, "KhoaNguyen - Wait for http response")
        }
        return gsonResponse
    }



    fun httpPostJson(url: String): String {
        var gsonResponse = ""
        var isResponse = false
        var timeout = 0
        try {
            Fuel.post(url).responseJson { request, response, result ->
                var responseJson: String = result.get().content
                Log.d(TAG, "KhoaNguyen - Json: " + responseJson)
                var gson = Gson()
                gsonResponse = gson?.fromJson(responseJson, LoginGson.LoginGson::class.java).toString()
                isResponse = true
            }
        } catch (e: Exception) {
            //            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() in ViewManager
        } finally {
        }
        while (!isResponse) {
            if (timeout > 200) {
                break
            }
            Thread.sleep(100)
            timeout++
            Log.d(TAG, "KhoaNguyen - Wait for http response")
        }
        return gsonResponse
    }


    fun httpPostJson(url: String, parameters: List<Pair<String, Any?>>? = null): String {
        var gsonResponse = ""
        var isResponse = false
        var timeout = 0
        try {
            Fuel.post(url, parameters).responseJson { request, response, result ->
                var responseJson: String = result.get().content
                Log.d(TAG, "KhoaNguyen - Json: " + responseJson)
                var gson = Gson()
                gsonResponse = gson?.fromJson(responseJson, LoginGson.LoginGson::class.java).toString()
                isResponse = true
            }
        } catch (e: Exception) {
            //            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show() in ViewManager
        } finally {
        }
        while (!isResponse) {
            if (timeout > 200) {
                break
            }
            Thread.sleep(100)
            timeout++
            Log.d(TAG, "KhoaNguyen - Wait for http response")
        }
        return gsonResponse
    }
}