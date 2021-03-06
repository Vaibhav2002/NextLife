package com.vaibhav.nextlife.ui.splashScreen

import androidx.lifecycle.ViewModel
import com.vaibhav.nextlife.data.repo.AuthRepo
import com.vaibhav.nextlife.data.repo.PreferencesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val preferencesRepo: PreferencesRepo
) : ViewModel() {

    fun isFirstTime() = preferencesRepo.isFirstTime()

    fun isLoggedIn() = authRepo.isLoggedIn()
}