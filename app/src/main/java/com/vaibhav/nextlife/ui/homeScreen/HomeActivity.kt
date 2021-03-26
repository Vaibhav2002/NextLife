package com.vaibhav.nextlife.ui.homeScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.vaibhav.nextlife.R
import com.vaibhav.nextlife.databinding.ActivityMainBinding
import com.vaibhav.nextlife.utils.Resource
import com.vaibhav.nextlife.utils.location.GpsUtils
import com.vaibhav.nextlife.utils.showErrorToastLight
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    var isGPSEnabled = false
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.fragment2)
        GpsUtils(this).turnGPSOn(object : GpsUtils.OnGpsListener {

            override fun gpsStatus(isGPSEnable: Boolean) {
                this@HomeActivity.isGPSEnabled = isGPSEnable
            }
        })
        invokeLocationAction()
        setSupportActionBar(binding.myToolbar)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.requirementDetailFragment -> supportActionBar?.hide()
                else -> supportActionBar?.show()
            }
        }
        viewModel.userLocation.observe(this, {
            if (it != null) {
                viewModel.fetchAllRequirements("")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPSEnabled = true
            }
        }
    }

    private fun invokeLocationAction() {
        if (isPermissionsGranted()) {
            when {
                !isGPSEnabled -> showErrorToastLight("Enable GPS", "Enable GPS and restart app")

                isPermissionsGranted() -> startLocationUpdate()

                shouldShowRequestPermissionRationale() -> showErrorToastLight(
                    "Permissions Required",
                    "Give permissions for app to continue"
                )
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST
            )
        }

    }

    private fun startLocationUpdate() {
        viewModel.startListeningToLocation()
        viewModel.location.observe(this, {
            when (it) {
                is Resource.Error -> {
                    Timber.d("Error")
                }
                is Resource.Loading -> Timber.d("Loading")
                is Resource.Success -> {
                    it.data?.let { location ->
                        Timber.d("${location.lat} ${location.long} ${location.address} ${location.city}")
                        if (viewModel.userLocation.value == null)
                            viewModel.setLocation(location)
                        viewModel.stopListeningToLocation()
                    }

                }
            }

        })
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }
}

const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 10
