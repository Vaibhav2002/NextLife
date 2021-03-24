package com.vaibhav.nextlife.utils

import android.app.Activity
import androidx.core.content.res.ResourcesCompat
import com.vaibhav.nextlife.R
import www.sanju.motiontoast.MotionToast

fun Activity.showSuccessToastLight(title: String, message: String) {
    MotionToast.createColorToast(
        this,
        title,
        message,
        MotionToast.TOAST_SUCCESS,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(this, R.font.poppins)
    )
}

fun Activity.showErrorToastLight(title: String, message: String) {
    MotionToast.createColorToast(
        this,
        title,
        message,
        MotionToast.TOAST_ERROR,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(this, R.font.poppins)
    )
}
