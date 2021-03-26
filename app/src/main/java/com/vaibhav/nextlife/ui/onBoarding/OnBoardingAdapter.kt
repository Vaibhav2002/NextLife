package com.vaibhav.nextlife.ui.onBoarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.nextlife.data.models.OnBoarding
import com.vaibhav.nextlife.databinding.OnboardingDesignBinding

class OnBoardingAdapter(
    private val list: List<OnBoarding>,
    private val onContinueClicked: () -> Unit
) :
    RecyclerView.Adapter<OnBoardingAdapter.onBoardingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): onBoardingViewHolder {
        val binding = LayoutInflater.from(parent.context)
        return onBoardingViewHolder(OnboardingDesignBinding.inflate(binding, parent, false))
    }

    override fun onBindViewHolder(holder: onBoardingViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class onBoardingViewHolder(private val binding: OnboardingDesignBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.continueButton.setOnClickListener {
                onContinueClicked()
            }
        }

        fun bind(onboarding: OnBoarding) {
            binding.continueButton.isVisible = adapterPosition == list.size - 1
            binding.onborading = onboarding
        }


    }

}

