package com.vaibhav.nextlife.utils

import com.vaibhav.nextlife.data.models.BloodGroupModel

object Constants {

    const val loginSuccessMessage = "Successfully logged in"
    const val loginFailureMessage = "Failed to login"
    const val IMAGE_REQUEST_CODE = 105

    val bloodGroups = listOf(
        BloodGroupModel(0, "A +"),
        BloodGroupModel(1, "A -"),
        BloodGroupModel(2, "B +"),
        BloodGroupModel(3, "B -"),
        BloodGroupModel(4, "O +"),
        BloodGroupModel(5, "O -"),
        BloodGroupModel(6, "AB +"),
        BloodGroupModel(7, "AB -")
    )

}