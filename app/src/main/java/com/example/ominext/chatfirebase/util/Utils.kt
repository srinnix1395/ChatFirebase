package com.example.ominext.chatfirebase.util

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import com.example.ominext.chatfirebase.ChatApplication
import com.example.ominext.chatfirebase.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ominext on 8/1/2017.
 */

object Utils {

    val SECOND_MILLIS = 1000
    val MINUTE_MILLIS = 60 * SECOND_MILLIS
    val HOUR_MILLIS = 60 * MINUTE_MILLIS
    val DAY_MILLIS = (24 * HOUR_MILLIS).toLong()
    val WEEK_MILLIS = (24 * 7 * HOUR_MILLIS).toLong()
    val MONTH_MILLIS = DAY_MILLIS * 30
    val YEAR_MILLIS = 365 * DAY_MILLIS

    fun hideView(view: View?) {
        if (view == null) {
            return
        }

        if (view.visibility == View.VISIBLE) {
            view.visibility = View.GONE
        }
    }

    fun showView(view: View?) {
        if (view == null) {
            return
        }

        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val mConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return mConnectivityManager.activeNetworkInfo != null
                && mConnectivityManager.activeNetworkInfo.isAvailable
                && mConnectivityManager.activeNetworkInfo.isConnected
    }

    fun getTimeAgoUser(context: Context, time: Long?): String {
        if (time == null) {
            return ""
        }

        val now = System.currentTimeMillis()

        val diff = now - time
        if (diff < MINUTE_MILLIS) {
            return context.getString(R.string.just_now)
        }
        if (diff < 60 * MINUTE_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / MINUTE_MILLIS, context.getString(R.string.minute_ago))
        }
        if (diff < 24 * HOUR_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / HOUR_MILLIS, context.getString(R.string.hour_ago))
        }
        if (diff < 48 * HOUR_MILLIS) {
            return context.getString(R.string.yesterday)
        }
        if (diff < WEEK_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / DAY_MILLIS, context.getString(R.string.day_ago))
        }

        if (diff < MONTH_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / WEEK_MILLIS, context.getString(R.string.week_ago))
        }

        if (diff < YEAR_MILLIS) {
            return String.format(Locale.getDefault(), "%d %s", diff / MONTH_MILLIS, context.getString(R.string.week_ago))
        }

        return String.format(Locale.getDefault(), "%d %s", diff / YEAR_MILLIS, context.getString(R.string.week_ago))
    }
}

fun toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(ChatApplication.app, message, length).show()
}