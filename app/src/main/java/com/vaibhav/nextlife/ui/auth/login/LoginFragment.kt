package com.vaibhav.nextlife.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vaibhav.nextlife.R
import com.vaibhav.nextlife.databinding.FragmentLoginBinding
import com.vaibhav.nextlife.ui.homeScreen.HomeActivity
import com.vaibhav.nextlife.utils.Status
import com.vaibhav.nextlife.utils.showErrorToastLight
import com.vaibhav.nextlife.utils.showSuccessToastLight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.goToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passInput.text.toString()
            loginViewModel.loginUser(email, password)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            loginViewModel.loginState.collect {
                when (it) {
                    Status.Loading -> {
                        binding.loadingAnim.isVisible = true
                    }
                    is Status.Success -> {
                        binding.loadingAnim.isVisible = false
                        showToast("Login Success", it.successMessage, false)
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
                        Timber.d(it.errorMessage)
                        showToast("Login Failed", it.errorMessage, true)
                    }
                }
            }
        }

    }


    override fun onStart() {
        super.onStart()
        if (loginViewModel.isLoggedIn()) {
            requireActivity().startActivity(Intent(requireContext(), HomeActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun showToast(title: String, message: String, isError: Boolean) {
        requireActivity().apply {
            if (!isError)
                showSuccessToastLight(title, message)
            else
                showErrorToastLight(title, message)
        }
    }


}