package com.example.admin.makemoneyonline.AsynTaskManager

import android.os.AsyncTask
import android.util.Log
import com.example.admin.makemoneyonline.KeyManager
import com.example.admin.makemoneyonline.presenter.LoginPresenter
import org.json.JSONObject


class MakeMoneyOnlineTask(asynCallBack: AsyncTaskCompleteListener<ResultObject>) : AsyncTask<CaseManager, String, ResultObject>() {
    internal var AsynCallBack: AsyncTaskCompleteListener<ResultObject>? = asynCallBack

    override fun doInBackground(vararg caseManagers: CaseManager?): ResultObject? {
        val reqJson = caseManagers[0]!!.case
        var mResultObject: ResultObject? = null
        var result = ""
        if (reqJson == KeyManager().REQ_LOGIN) {
            result = caseManagers[0]!!.httpPostJson(caseManagers[0]!!.url, caseManagers[0]!!.paramJson!!)
            mResultObject = ResultObject(KeyManager().REQ_LOGIN, result)
        } else  if (reqJson == KeyManager().REQ_GETALLSTAFF) {
            result = caseManagers[0]!!.httpGetJson(caseManagers[0]!!.url)
            mResultObject = ResultObject(KeyManager().REQ_GETALLSTAFF, result)
        }
        return mResultObject
    }

    override fun onPreExecute() {
        // Before doInBackground
    }

    override fun onProgressUpdate(vararg values: String?) {
        try {
            var json = JSONObject(values[0])
            Log.d("KhoaNguyen", "json: " + json)

            val status = json.get("status")
            val success = json.getJSONObject("success")
            val code = success.get("code")

            Log.d("KhoaNguyen", "Status: " + status + ";Code: " + code)

        } catch (ex: Exception) {

        }
    }

    override fun onPostExecute(resuiltObject: ResultObject?) {
        if (resuiltObject != null) {
            AsynCallBack!!.onTaskCompleted(resuiltObject!!.result, resuiltObject!!.case)
        }
        super.onPostExecute(resuiltObject)
    }
}