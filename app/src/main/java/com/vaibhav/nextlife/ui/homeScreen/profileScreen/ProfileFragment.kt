package com.vaibhav.nextlife.ui.homeScreen.profileScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.vaibhav.nextlife.R
import com.vaibhav.nextlife.databinding.FragmentProfileBinding
import com.vaibhav.nextlife.ui.auth.AuthorizationActivity
import com.vaibhav.nextlife.ui.homeScreen.HomeViewModel
import com.vaibhav.nextlife.utils.Resource
import com.vaibhav.nextlife.utils.showErrorToastLight
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val sharedViewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        setHasOptionsMenu(false)
        sharedViewModel.userDetails.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> {
                    binding.loadingAnim.isVisible = true
                    binding.logoutbutton.isEnabled = false
                }
                is Resource.Success -> {
                    binding.apply {
                        loadingAnim.isVisible = false
                        logoutbutton.isEnabled = true
                        Glide.with(requireContext()).load(it.data?.imageUrl)
                            .error(R.drawable.avatar_image).into(imageView)
                        nameText.text = it.data?.fullName ?: ""
                        emailText.text = it.data?.email ?: ""
                        addressText.text = sharedViewModel.userLocation.value?.address ?: ""
                    }
                }
                is Resource.Error -> {
                    binding.loadingAnim.isVisible = false
                    binding.logoutbutton.isEnabled = false
                    requireActivity().showErrorToastLight(
                        "Failed to load User Data",
                        "There was an error fetching user details"
                    )
                }
            }
        })
        binding.logoutbutton.setOnClickListener {
            sharedViewModel.logout()
            requireActivity().startActivity(
                Intent(
                    requireContext(),
                    AuthorizationActivity::class.java
                )
            )
            requireActivity().finish()
        }
    }

}