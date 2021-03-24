package com.vaibhav.nextlife.ui.auth.register

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.nextlife.data.repo.AuthRepo
import com.vaibhav.nextlife.utils.Constants.loginSuccessMessage
import com.vaibhav.nextlife.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _registerState = Channel<Status>()
    val registerState = _registerState.receiveAsFlow()

    fun isLoggedIn() = authRepo.isLoggedIn()

    fun registerUser(username: String, email: String, password: String, image: Uri) {
        if (validateFields(email, password, username, image)) {
            viewModelScope.launch(Dispatchers.IO) {
                _registerState.send(Status.Loading)
                authRepo.registerUser(username, email, password, image, successListener = {
                    viewModelScope.launch {
                        _registerState.send(Status.Success(loginSuccessMessage))
                    }
                }, failureListener = { exception ->
                    viewModelScope.launch {
                        _registerState.send(Status.Error(exception.localizedMessage ?: ""))
                    }
                })
            }
        } else {
            viewModelScope.launch {
                _registerState.send(Status.Error("Invalid Fields"))
            }
        }
    }

    private fun validateFields(
        email: String,
        password: String,
        username: String,
        image: Uri
    ): Boolean {
        return !(email.isEmpty() || email == "" || password.isEmpty() || password == "" || username.isEmpty() || username == "" ||
                image == Uri.parse("android.resource://com.vaibhav.nextlife/drawable/avatar_image"))
    }
}