package com.example.ominext.chatfirebase.widget

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.example.ominext.chatfirebase.model.Message
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by anhtu on 8/11/2017.
 */


fun ArrayList<Any?>.isExistIn(message: Message?): Boolean {
    val result = this.lastOrNull {
        it is Message && it.id == message?.id
    }
    return result != null
}

fun ArrayList<Any?>.isNotExistIn(message: Message?): Boolean {
    return !isExistIn(message)
}

fun Date.isTheSameDay(date: Date): Boolean {
    return this.year == date.year && this.month == date.month && this.date == date.date
}

fun Date.isTheSameYear(date: Date): Boolean {
    return this.year == date.year
}

fun isInXDaysAgo(time: Long, x: Int): Boolean {
    val diff = System.currentTimeMillis() - time

    return diff <= 3_600_000 * 24 * x
}

fun TextView.setTimeAgo(postTime: Long) {
    val dateFormat: SimpleDateFormat = SimpleDateFormat("", Locale.getDefault())
    val currentDate = Date(System.currentTimeMillis())
    val postDate = Date(postTime)

    this.text = when {
        currentDate.isTheSameDay(postDate) -> {
            dateFormat.applyPattern("kk:mm")
            dateFormat.format(postDate)
        }
        isInXDaysAgo(postTime, 7) -> {
            dateFormat.applyPattern("EEE - kk:mm")
            StringBuilder(dateFormat.format(postDate).replace("-", "LÚC"))
                    .insert(2, "ứ")
                    .toString()
                    .toUpperCase()
        }
        currentDate.isTheSameYear(postDate) -> {
            dateFormat.applyPattern("dd MMM - kk:mm")
            dateFormat.format(postDate).replace("-", "LÚC")
        }
        else -> {
            dateFormat.applyPattern("dd MMM yyyy - kk:mm")
            dateFormat.format(postDate).replace("-", "LÚC")
        }
    }
}

fun Disposable.addToCompositeDisposable(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun Int.toast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun String.toast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}