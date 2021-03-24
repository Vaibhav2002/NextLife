package com.vaibhav.nextlife.ui.auth.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.vaibhav.nextlife.R
import com.vaibhav.nextlife.databinding.FragmentRegisterBinding
import com.vaibhav.nextlife.ui.homeScreen.HomeActivity
import com.vaibhav.nextlife.utils.Constants.IMAGE_REQUEST_CODE
import com.vaibhav.nextlife.utils.Status
import com.vaibhav.nextlife.utils.showErrorToastLight
import com.vaibhav.nextlife.utils.showSuccessToastLight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    var imageUri = Uri.parse("android.resource://com.vaibhav.nextlife/drawable/avatar_image")
    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        binding.goToLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val password = binding.passInput.text.toString()
            registerViewModel.registerUser(username, email, password, imageUri)
        }

        binding.addImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_REQUEST_CODE)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            registerViewModel.registerState.collect { state ->
                when (state) {
                    is Status.Success -> {
                        binding.loadingAnim.isVisible = false
                        showToast("Registered Successfully", state.successMessage, false)
                        requireActivity().startActivity(
                            Intent(
                                requireContext(),
                                HomeActivity::class.java
                            )
                        )
                        requireActivity().finish()

                    }
                    is Status.Error -> {
                        binding.loadingAnim.isVisible = false
                        Timber.d(state.errorMessage)
                        showToast("Failed to register", state.errorMessage, true)
                    }
                    Status.Loading -> binding.loadingAnim.isVisible =
                        true
                }
            }
        }
    }

    private fun showToast(title: String, message: String, isError: Boolean) {
        if (!isError)
            requireActivity().showSuccessToastLight(title, message)
        else
            requireActivity().showErrorToastLight(title, message)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            data?.data?.let {
                imageUri = it
            }
            Glide.with(requireContext()).load(imageUri).circleCrop().into(binding.profileImage)
        }
    }
}