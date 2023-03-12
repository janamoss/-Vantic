package com.example.vanticproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.vanticproject.admin.HomeAdminActivity

class SessionManager {
    var pref: SharedPreferences
    var edior: SharedPreferences.Editor
    var context: Context
    var PRIVATE_MODE: Int = 0

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        edior = pref.edit()
    }
    companion object {
        val PREF_NAME: String = "SessionDemo"
        val IS_LOGIN: String = "isLogin"
        val KEY_FNAME: String = "First name"
        val KEY_LNAME: String = "Last name"
        val KEY_PHONE: String = "Phone"
        val KEY_EMAIL: String = "Email"
        val KEY_PASSWORD: String = "Password"
        val KEY_USERTYPE: String = "Type"
    }
    fun createLoginSession(email: String, fname: String, lname: String,phone : String, password : String,usertype : Int) {
        edior.putBoolean(IS_LOGIN, true)
        edior.putString(KEY_FNAME, fname)
        edior.putString(KEY_LNAME, lname)
        edior.putString(KEY_PHONE, phone)
        edior.putString(KEY_EMAIL, email)
        edior.putString(KEY_PASSWORD,password)
        edior.putInt(KEY_USERTYPE,usertype)
        edior.commit()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun getLoggedInUserType(): Int {
        return pref.getInt(KEY_USERTYPE, 0)
    }

    fun getHomeActivityIntent(context: Context): Intent {
        return if (getLoggedInUserType() == 2) {
            Intent(context, HomeAdminActivity::class.java)
        } else {
            Intent(context, HomeActivity::class.java)
        }
    }
}