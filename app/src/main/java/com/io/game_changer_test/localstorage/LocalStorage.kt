package com.io.game_changer_test.localstorage

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences


class LocalStorage(context: Activity) {

    private val PREFERENCE_NAME = "MyPreferences"
    private val activity: Context = context

    private val storage: SharedPreferences
    private var ed: SharedPreferences.Editor? = null


    init {
        storage = android.preference.PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun putString(name: String, value: String?) {
        storage.edit().putString(name, value).apply()
    }

    fun getString(name: String): String? {
        return storage.getString(name, "")
    }

    fun putInt(name: String, value: Int) {
        storage.edit().putInt(name, value).apply()
    }

    fun getInt(name: String): Int {
        return storage.getInt(name, -1)
    }

    fun putBoolean(name: String, value: Boolean) {
        storage.edit().putBoolean(name, value).apply()
    }

    fun getBoolean(name: String): Boolean {
        return storage.getBoolean(name,false)
    }

    fun clearAll() {
        storage.edit().clear().apply()
    }

    fun editor() {
        ed = storage.edit()
    }

    fun commit() {
        ed!!.commit()
        ed!!.apply()
    }

    companion object {
        val GITHUB_ISSUE_DATA: String= "GITHUB_ISSUE_DATA"
        val lastApiCallDate: String = "lastApiCallDate"
        private var instance: LocalStorage? = null

        val otpToken = "otpToken"
        val isLoggedIn = "IsLoggedIn"




        fun getInstance(context: Activity): LocalStorage {
            if (instance == null)
                instance = LocalStorage(context)
            return instance as LocalStorage
        }
    }





}
