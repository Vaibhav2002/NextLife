package com.vaibhav.nextlife.ui.homeScreen.postSreen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.vaibhav.nextlife.R
import com.vaibhav.nextlife.databinding.FragmentPostBinding
import com.vaibhav.nextlife.ui.homeScreen.HomeViewModel
import com.vaibhav.nextlife.utils.Constants
import com.vaibhav.nextlife.utils.Status
import com.vaibhav.nextlife.utils.adapters.BloodGroupAdapter
import com.vaibhav.nextlife.utils.showErrorToastLight
import com.vaibhav.nextlife.utils.showSuccessToastLight
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class PostFragment : Fragment(R.layout.fragment_post) {

    private var bloodGroup = ""
    private lateinit var binding: FragmentPostBinding
    private val sharedViewModel: HomeViewModel by activityViewModels()
    private lateinit var bloodGroupAdapter: BloodGroupAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPostBinding.bind(view)
        bloodGroupAdapter = BloodGroupAdapter(requireContext(), onCLickListener = {
            bloodGroup = if (it.isChecked) it.text.replace(" ", "") else ""
        })
        binding.bloodGroupRecycle.apply {
            adapter = bloodGroupAdapter
            setHasFixedSize(true)
        }
        bloodGroupAdapter.submitList(Constants.bloodGroups)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            sharedViewModel.postStatus.collect {
                when (it) {
                    is Status.Error -> {
                        binding.loadingAnim.isVisible = false
                        requireActivity().showErrorToastLight("Failed to post", it.errorMessage)
                    }
                    Status.Loading -> {
                        binding.loadingAnim.isVisible = true
                    }
                    is Status.Success -> {
                        binding.loadingAnim.isVisible = false
                        requireActivity().showSuccessToastLight(
                            "Posted successfully",
                            "Post was uploaded successfully"
                        )
                        findNavController().popBackStack()
                    }
                }
            }
        }

        binding.postButton.setOnClickListener {
            val title = binding.titleInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val mobile = binding.mobileInput.text.toString()
            if (checkData(title, description, mobile)) {
                sharedViewModel.postRequest(title, description, mobile, bloodGroup)
            }
        }

    }

    private fun checkData(title: String, description: String, mobile: String): Boolean {
        var titleError = false
        var descriptionError = false
        var mobileError = false

        titleError = title.isEmpty() || title == ""
        descriptionError = description.isEmpty() || description == ""
        mobileError = mobile.length != 10 || mobile.isEmpty() || mobile == ""
        if (titleError)
            binding.titleInput.error = "Field cannot be empty"
        if (descriptionError)
            binding.descriptionInput.error = "Field cannot be empty"
        if (mobileError)
            binding.mobileInput.error = "Invalid input"
        Timber.d(titleError.toString())
        Timber.d(descriptionError.toString())
        Timber.d(mobileError.toString())
        Timber.d((bloodGroup != "").toString())
        return !titleError && !descriptionError && !mobileError && bloodGroup != ""
    }
}