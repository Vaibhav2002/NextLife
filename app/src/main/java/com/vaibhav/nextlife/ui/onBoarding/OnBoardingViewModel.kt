package com.vaibhav.nextlife.ui.onBoarding

import androidx.lifecycle.ViewModel
import com.vaibhav.nextlife.data.repo.AuthRepo
import com.vaibhav.nextlife.data.repo.PreferencesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val preferencesRepo: PreferencesRepo,
    private val authRepo: AuthRepo
) : ViewModel() {


    fun setOnBoardingComplete() = preferencesRepo.setOnBoardingComplete()

    fun isLoggedIn() = authRepo.isLoggedIn()

}