package com.vaibhav.nextlife.utils

sealed class LocationResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : LocationResource<T>(data)
    class Error<T>(message: String, data: T? = null) : LocationResource<T>(data, message)
    class Loading<T> : LocationResource<T>()
}