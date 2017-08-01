package com.example.ominext.plaidfork.ui.chat

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ominext on 8/1/2017.
 */

object Utils {
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

    fun getTimeAgoMessage(time: Long): String {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentDay = calendar.get(Calendar.DAY_OF_YEAR)

        calendar.timeInMillis = time
        if (calendar.get(Calendar.YEAR) == currentYear && calendar.get(Calendar.DAY_OF_YEAR) == currentDay) {
            val dateFormat = SimpleDateFormat("kk:mm", Locale.getDefault())
            return dateFormat.format(Date(time))
        } else {
            val dateFormat = SimpleDateFormat("EEE - kk:mm", Locale.getDefault())
            return dateFormat.format(Date(time)).replace("-", "LÚC")
        }
    }
}