package com.jpmorgan.test.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefs {

  private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * SharedPreferences extension function
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    fun setString(key: String, value: String) {
        preferences.edit {
            it.putString(key, value)
            it.apply()
        }
    }

    fun getString(key: String): String {
        return preferences.getString(key, "").toString()
    }

}