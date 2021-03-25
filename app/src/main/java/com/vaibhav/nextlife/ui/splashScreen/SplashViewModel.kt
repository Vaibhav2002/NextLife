package com.vaibhav.nextlife.ui.splashScreen

import androidx.lifecycle.ViewModel
import com.vaibhav.nextlife.data.repo.AuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {


    fun isLoggedIn() = authRepo.isLoggedIn()
}