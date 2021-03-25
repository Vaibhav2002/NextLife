package com.vaibhav.nextlife.ui.homeScreen.requirementDetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vaibhav.nextlife.R
import com.vaibhav.nextlife.data.models.PostModel
import com.vaibhav.nextlife.databinding.FragmentRequirementDetailBinding
import timber.log.Timber

class RequirementDetailFragment : Fragment(R.layout.fragment_requirement_detail),
    OnMapReadyCallback {
    private var map: GoogleMap? = null
    private lateinit var binding: FragmentRequirementDetailBinding
    private val args by navArgs<RequirementDetailFragmentArgs>()
    private lateinit var post: PostModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRequirementDetailBinding.bind(view)
        binding.mapView.onCreate(savedInstanceState)
        post = args.post
        Timber.d("${post.lat} ${post.long}")
        binding.mapView.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap?) {
        val latLng = LatLng(post.lat, post.long)
        Timber.d(latLng.toString())
        Timber.d(p0.toString())
        p0?.let {
            it.setMinZoomPreference(10F)
            it.addMarker(MarkerOptions().position(latLng))
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
            binding.mapView.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }


}