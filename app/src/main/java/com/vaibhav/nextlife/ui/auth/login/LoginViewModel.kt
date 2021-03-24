package com.vaibhav.nextlife.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhav.nextlife.data.repo.AuthRepo
import com.vaibhav.nextlife.utils.Constants
import com.vaibhav.nextlife.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {

    private val _loginState = Channel<Status>()
    val loginState = _loginState.receiveAsFlow()

    fun isLoggedIn() = authRepo.isLoggedIn()


    fun loginUser(email: String, password: String) {
        if (validateFields(email, password)) {
            viewModelScope.launch {
                _loginState.send(Status.Loading)
                authRepo.loginUser(email, password, successListener = {
                    viewModelScope.launch {
                        _loginState.send(Status.Success(Constants.loginSuccessMessage))
                    }
                }, failureListener = { exception ->
                    viewModelScope.launch {
                        _loginState.send(Status.Error(exception.localizedMessage ?: ""))
                    }
                })
            }
        } else {
            viewModelScope.launch {
                _loginState.send(Status.Error("Invalid Fields"))
            }
        }
    }


    private fun validateFields(email: String, password: String): Boolean {
        return !(email.isEmpty() || email == "" || password.isEmpty() || password == "")
    }

}