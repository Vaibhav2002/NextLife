package com.vaibhav.nextlife.ui.homeScreen.feedScreen

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.vaibhav.nextlife.R
import com.vaibhav.nextlife.databinding.FragmentFeedBinding
import com.vaibhav.nextlife.ui.auth.AuthorizationActivity
import com.vaibhav.nextlife.ui.homeScreen.HomeViewModel
import com.vaibhav.nextlife.utils.Constants
import com.vaibhav.nextlife.utils.Resource
import com.vaibhav.nextlife.utils.adapters.BloodGroupAdapter
import com.vaibhav.nextlife.utils.adapters.RequirementsAdapter
import com.vaibhav.nextlife.utils.showErrorToastLight
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed) {

    private lateinit var binding: FragmentFeedBinding
    private val sharedViewModel: HomeViewModel by activityViewModels()
    private lateinit var bloodGroupAdapter: BloodGroupAdapter
    private lateinit var requirementsAdapter: RequirementsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFeedBinding.bind(view)
        bloodGroupAdapter = BloodGroupAdapter(requireContext(), onCLickListener = {
            if (it.isChecked) {
                sharedViewModel.setBloodType(it.text.replace(" ", ""))
                sharedViewModel.fetchAllRequirements(it.text.replace(" ", ""))
            } else {
                sharedViewModel.setBloodType("")
                sharedViewModel.fetchAllRequirements("")
            }
            Timber.d(it.isChecked.toString())
        })
        setHasOptionsMenu(true)
        requirementsAdapter = RequirementsAdapter {
            val action = FeedFragmentDirections.actionFeedFragmentToRequirementDetailFragment(it)
            findNavController().navigate(action)
        }
        binding.addFab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_postFragment)
        }
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = true
            val type = bloodGroupAdapter.getPressedBloodGroup()
            type?.let {
                sharedViewModel.fetchAllRequirements(it.text.replace(" ", ""))
            } ?: sharedViewModel.fetchAllRequirements("")
            binding.refresh.isRefreshing = false
        }
        sharedViewModel.allRequests.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Loading -> binding.loadingAnim.isVisible = true
                is Resource.Success -> {
                    binding.loadingAnim.isVisible = false
                    requirementsAdapter.submitList(it.data)
                    if (it.data.isNullOrEmpty())
                        Toast.makeText(
                            requireContext(),
                            "No requirements found in your state",
                            Toast.LENGTH_SHORT
                        ).show()
                }
                is Resource.Error -> {
                    binding.loadingAnim.isVisible = false
                    requireActivity().showErrorToastLight(
                        "Error loading requirements",
                        it.message ?: "Oops something went wrong"
                    )
                }
            }
        })
        binding.bloodGroupRecycle.apply {
            adapter = bloodGroupAdapter
            setHasFixedSize(true)
        }
        binding.requirementsRecycle.apply {
            adapter = requirementsAdapter
            setHasFixedSize(false)
        }
        bloodGroupAdapter.submitList(Constants.bloodGroups)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.logout -> {
                sharedViewModel.logout()
                requireActivity().startActivity(
                    Intent(
                        requireContext(),
                        AuthorizationActivity::class.java
                    )
                )
                requireActivity().finish()
                return true
            }
            R.id.profile -> {
                findNavController().navigate(R.id.action_feedFragment_to_profileFragment)
                return true
            }
            else -> return false
        }
    }
}