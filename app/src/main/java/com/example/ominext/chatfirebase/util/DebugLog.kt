package com.example.ominext.chatfirebase.util

import android.util.Log

/**
 * Created by anhtu on 8/6/2017.
 */


object DebugLog {
    private var className: String? = null
    private var methodName: String? = null
    private var lineNumber: Int = 0

    private fun getMethodName(sElement: Array<StackTraceElement>) {
        className = sElement[1].fileName
        methodName = sElement[1].methodName
        lineNumber = sElement[1].lineNumber
    }

    private fun createLog(log: String): String {

        val builder = StringBuilder()
        builder.append("[function ")
        builder.append(methodName)
        builder.append(" - line ")
        builder.append(lineNumber)
        builder.append("] ")
        builder.append(log)

        return builder.toString()
    }

    fun i(message: String) {
        getMethodName(Throwable().stackTrace)
        Log.i(className, createLog(message))
    }

    fun e(message: String) {
        getMethodName(Throwable().stackTrace)
        Log.e(className, createLog(message))
    }
}
