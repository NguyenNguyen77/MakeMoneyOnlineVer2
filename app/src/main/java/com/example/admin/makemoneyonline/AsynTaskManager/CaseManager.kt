package com.example.admin.makemoneyonline.AsynTaskManager


import android.net.Uri
import com.example.admin.makemoneyonline.BaseMethod

class CaseManager : BaseMethod {
    var case: String
    var url: String
    internal var mBuilder: Uri.Builder? = null
    var paramJson: List<Pair<String, Any?>>? = null


    constructor(aCase: String, url: String, mBuilder: Uri.Builder) {
        case = aCase
        this.url = url
        this.mBuilder = mBuilder
    }

    constructor(aCase: String, url: String, paramJson: List<Pair<String, Any?>>? = null) {
        case = aCase
        this.url = url
        this.paramJson = paramJson
    }

    fun getmBuilder(): Uri.Builder? {
        return mBuilder
    }

    fun setmBuilder(mBuilder: Uri.Builder) {
        this.mBuilder = mBuilder
    }
}

