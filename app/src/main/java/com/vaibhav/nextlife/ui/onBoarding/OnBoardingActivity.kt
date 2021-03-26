package com.vaibhav.nextlife.ui.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.vaibhav.nextlife.R
import com.vaibhav.nextlife.data.models.OnBoarding
import com.vaibhav.nextlife.databinding.ActivityOnBoardingBinding
import com.vaibhav.nextlife.ui.auth.AuthorizationActivity
import com.vaibhav.nextlife.ui.homeScreen.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private val viewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val onboardingList = mutableListOf<OnBoarding>()
        onboardingList.add(
            OnBoarding(
                R.drawable.search,
                getString(R.string.onboarding1),
            )
        )
        onboardingList.add(
            OnBoarding(
                R.drawable.blood,
                getString(R.string.onboarding2),

                )
        )
        onboardingList.add(
            OnBoarding(
                R.drawable.handshake,
                getString(R.string.onboarding3),

                )
        )
        val onBoardingAdapter = OnBoardingAdapter(onboardingList) {
            viewModel.setOnBoardingComplete()
            if (viewModel.isLoggedIn())
                startActivity(Intent(this, HomeActivity::class.java))
            else
                startActivity(Intent(this, AuthorizationActivity::class.java))
            finish()
        }
        binding.onboardingViewpager.adapter = onBoardingAdapter
        binding.wormDotsIndicator.setViewPager2(binding.onboardingViewpager)
    }
}