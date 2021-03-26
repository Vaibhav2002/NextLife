package com.vaibhav.nextlife.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.vaibhav.nextlife.data.models.PostModel

@BindingAdapter("setAddress")
fun TextView.setAddress(postModel: PostModel) {
    text = "${postModel.city}, ${postModel.state}"
}

@BindingAdapter("setOnBoardingImage")
fun ImageView.setOnBoardingImage(resource: Int) {
    Glide.with(this).load(resource).into(this)
}