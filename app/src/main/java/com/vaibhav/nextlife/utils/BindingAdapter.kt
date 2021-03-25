package com.vaibhav.nextlife.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.vaibhav.nextlife.data.models.PostModel

@BindingAdapter("setAddress")
fun TextView.setAddress(postModel: PostModel) {
    text = "${postModel.city} ${postModel.state}"
}