package com.vaibhav.nextlife.data.models

import java.io.Serializable

data class PostModel(
    val id: String = "",
    val userId: String = "",
    val username: String = "",
    val title: String = "",
    val description: String = "",
    val phoneNumber: String = "",
    val bloodType: String = "",
    val timeStamp: String = "",
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val address: String = "",
    val locality: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = ""
) : Serializable
