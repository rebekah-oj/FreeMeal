package com.example.freemeal

import android.content.Context
import android.content.SharedPreferences

private const val PREFERENCES_NAME = "SharePreferences"
private const val PREFERENCES_SCAN_COUNT = "Scan Count"

class SharedPreferences(context: Context) {

    private val preferences: SharedPreferences? =
            context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getScan(): Int {
        return preferences!!.getInt(PREFERENCES_SCAN_COUNT, 0)
    }

    fun setScan(count: Int) {
        val editor = preferences?.edit()
        editor!!.putInt(PREFERENCES_SCAN_COUNT, count)
        editor.apply()
    }
}