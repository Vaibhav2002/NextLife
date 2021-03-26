package com.vaibhav.nextlife.data.repo

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesRepo @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun isFirstTime() = sharedPreferences.getBoolean("isFirstTime", true);

    fun setOnBoardingComplete() {
        sharedPreferences.edit().putBoolean("isFirstTime", false).apply()
    }


}