package com.vaibhav.nextlife.ui.homeScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vaibhav.nextlife.data.models.LocationModel
import com.vaibhav.nextlife.utils.location.LocationLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val context: Application) :
    AndroidViewModel(context) {

    private val _location = LocationLiveData(context = context)
    val location = _location

    private val _userLocation = MutableLiveData<LocationModel>(null)
    val userLocation: LiveData<LocationModel> = _userLocation

    init {
        startListeningToLocation()
    }

    fun setLocation(locationModel: LocationModel) {
        _userLocation.postValue(locationModel)
    }

    fun startListeningToLocation() {
        _location.postLoading()
        try {
            _location.startListening()
        } catch (exception: Exception) {
            _location.postError()
        }

    }

    fun stopListeningToLocation() {
        _location.stopListening()
    }
}