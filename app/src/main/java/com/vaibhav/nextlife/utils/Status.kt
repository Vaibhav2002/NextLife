package com.vaibhav.nextlife.utils

sealed class Status {
    object Loading : Status()
    data class Success(val successMessage: String) : Status()
    data class Error(val errorMessage: String) : Status()
}
