package com.example.ominext.chatfirebase.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Ominext on 8/2/2017.
 */


object SharedPreUtils {
    var sharedPreferences: SharedPreferences? = null

    fun get(context: Context): SharedPreUtils {
        if (sharedPreferences == null) {
            synchronized(SharedPreUtils::class.java) {
                if (sharedPreferences == null) {
                    sharedPreferences = context.getSharedPreferences("CHAT_FIREBASE", Context.MODE_PRIVATE)
                }
            }
        }
        return this
    }

    fun putInt(key: String, vaue: Int) {
        sharedPreferences?.let {
            val editor = sharedPreferences!!.edit()
            editor.putInt(key, vaue)
            editor.apply()
        }
    }

    fun getInt(key: String, defVal: Int): Int {
        return sharedPreferences?.getInt(key, defVal) ?: defVal
    }

    fun putString(key: String, value: String) {
        sharedPreferences?.let {
            val editor = sharedPreferences!!.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun getString(key: String, defVal: String): String? {
        return sharedPreferences?.getString(key, defVal)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences?.let {
            val editor = sharedPreferences!!.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean? {
        return sharedPreferences?.getBoolean(key, defValue)
    }

    fun remove(key: String) {
        sharedPreferences?.let {
            val editor = sharedPreferences!!.edit()
            editor.remove(key)
            editor.apply()
        }
    }

    fun getUserId(): String? {
        return sharedPreferences?.getString("USER_ID", null)
    }

//    val isUserSignedIn: Boolean
//        get() = sharedPreferences.getBoolean(AppConstant.IS_USER_SIGNED_IN, false)

//    val accountType: Int
//        get() = sharedPreferences.getInt(AppConstant.USER_TYPE, AppConstant.ACCOUNT_GUESTS)
//
//    val accountName: String
//        get() = sharedPreferences.getString(AppConstant.NAME, "")
//
//    var lastEmailFragmentLogin: String
//        get() = sharedPreferences.getString(AppConstant.LAST_EMAIL_FRAGMENT_LOGIN, "")
//        set(email) = putString(AppConstant.LAST_EMAIL_FRAGMENT_LOGIN, email)
//
//    val classId: String
//        get() = sharedPreferences.getString(AppConstant._ID_CLASS, null)
//
//    fun saveUserData(user: User, children: ArrayList<Child>) {
//        val editor = sharedPreferences.edit()
//        editor.putBoolean(AppConstant.IS_USER_SIGNED_IN, true)
//        editor.putString(AppConstant.USER_ID, user.getId())
//        editor.putString(AppConstant.EMAIL, user.getEmail())
//        editor.putString(AppConstant.NAME, user.getName())
//        editor.putString(AppConstant.GENDER, user.getGender())
//        editor.putInt(AppConstant.USER_TYPE, user.getAccountType())
//        if (user.getAccountType() === AppConstant.ACCOUNT_TEACHERS) {
//            editor.putString(AppConstant._ID_CLASS, user.getIdClass())
//            editor.putString(AppConstant.CLASS_NAME, user.getClassName())
//            editor.putString(AppConstant.IMAGE, user.getImage())
//        } else {
//            editor.putString(AppConstant.IMAGE, if (!children.isEmpty()) children[0].getImage() else "")
//        }
//        editor.putBoolean(AppConstant.KEY_RECEIVE_NOTIFICATION, true)
//        editor.putString(AppConstant.TOKEN, user.getToken())
//        editor.apply()
//    }
//
//    val token: String
//        get() = sharedPreferences.getString(AppConstant.TOKEN, "")
//
//    var serverHasDeviceToken: Boolean
//        get() = sharedPreferences.getBoolean(AppConstant.SERVER_HAS_DEVICE_TOKEN, false)
//        set(b) {
//            val editor = sharedPreferences.edit()
//            editor.putBoolean(AppConstant.SERVER_HAS_DEVICE_TOKEN, b)
//            editor.apply()
//        }
//
//    val image: String
//        get() = sharedPreferences.getString(AppConstant.IMAGE, "")
//
//    fun clearUserData() {
//        val editor = sharedPreferences.edit()
//        editor.clear()
//        editor.apply()
//    }
//
//    val email: String
//        get() = sharedPreferences.getString(AppConstant.EMAIL, "")
//
//    var flagReceiveNotification: Boolean
//        get() = sharedPreferences.getBoolean(AppConstant.KEY_RECEIVE_NOTIFICATION, false)
//        set(flag) {
//            val editor = sharedPreferences.edit()
//            editor.putBoolean(AppConstant.KEY_RECEIVE_NOTIFICATION, flag)
//            editor.apply()
//        }
//
//    var language: String
//        get() = sharedPreferences.getString(AppConstant.LANGUAGE, "Tiếng Việt")
//        set(language) {
//            val editor = sharedPreferences.edit()
//            editor.putString(AppConstant.LANGUAGE, language)
//            editor.apply()
//        }
//
//    val gender: String
//        get() = sharedPreferences.getString(AppConstant.GENDER, AppConstant.FEMALE)
//
//    val className: String
//        get() = sharedPreferences.getString(AppConstant.CLASS_NAME, "")
}
