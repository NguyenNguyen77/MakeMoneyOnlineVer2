package com.example.admin.makemoneyonline.AsynTaskManager

interface AsyncTaskCompleteListener<T> {
    abstract fun onTaskCompleted(s: String, CaseRequest: String)

    abstract fun onTaskError(s: String, CaseRequest: String)
}