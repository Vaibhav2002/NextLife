package com.vaibhav.nextlife.data.models

data class User(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val imageUrl: String = ""
) {
    var isVerifiedDonor = false
    var bloodType = ""
}