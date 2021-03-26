package com.vaibhav.nextlife.ui.homeScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vaibhav.nextlife.data.models.LocationModel
import com.vaibhav.nextlife.data.models.PostModel
import com.vaibhav.nextlife.data.models.User
import com.vaibhav.nextlife.data.repo.AuthRepo
import com.vaibhav.nextlife.data.repo.PostRepo
import com.vaibhav.nextlife.utils.Resource
import com.vaibhav.nextlife.utils.Status
import com.vaibhav.nextlife.utils.location.LocationLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    context: Application,
    private val authRepo: AuthRepo,
    private val postRepo: PostRepo,
) :
    AndroidViewModel(context) {

    private val _bloodType = MutableLiveData<String>("")


    private val _postStatus = Channel<Status>()
    val postStatus = _postStatus.receiveAsFlow()

    private val _location = LocationLiveData(context = context)
    val location = _location

    private val _userLocation = MutableLiveData<LocationModel>(null)
    val userLocation: LiveData<LocationModel> = _userLocation

    private val _allRequests = MutableLiveData<Resource<List<PostModel>>>()
    val allRequests: LiveData<Resource<List<PostModel>>> = _allRequests

    private val _userDetails = MutableLiveData<Resource<User>>()
    val userDetails: LiveData<Resource<User>> = _userDetails

    fun setLocation(locationModel: LocationModel) {
        _userLocation.postValue(locationModel)
    }

    init {
//        startListeningToLocation()
        getUserDetails()
    }

    fun logout() {
        authRepo.logOut()
    }

    fun setBloodType(bloodType: String) {
        _bloodType.postValue(bloodType)
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            try {
                _userDetails.postValue(Resource.Loading())
                authRepo.getUserDetails(
                    authRepo.getCurrentUserId(),
                    successListener = {
                        _userDetails.postValue(Resource.Success(it))
                    },
                    failureListener = {
                        _userDetails.postValue(
                            Resource.Error(
                                it.message ?: "Oops something went wrong"
                            )
                        )
                    })
            } catch (exception: Exception) {
                _userDetails.postValue(Resource.Error("Oops something went wrong"))
            }

        }
    }

    fun postRequest(title: String, description: String, mobile: String, bloodType: String) {
        viewModelScope.launch {
            try {
                _postStatus.send(Status.Loading)
                _userLocation.value?.let {
                    postRepo.postRequest(
                        userDetails.value?.data?.fullName ?: "",
                        title,
                        description,
                        mobile,
                        bloodType,
                        it.lat,
                        it.long,
                        it.address,
                        it.locality,
                        it.city,
                        it.state,
                        it.country,
                        successListener = {
                            viewModelScope.launch { _postStatus.send(Status.Success("Posted successfully")) }
                        },
                        failureListener = {
                            viewModelScope.launch {
                                _postStatus.send(
                                    Status.Error(
                                        it.message ?: "Oops something went wrong"
                                    )
                                )
                            }
                        }
                    )
                } ?: _postStatus.send(Status.Error("Error fetching location"))
            } catch (exception: Exception) {
                _postStatus.send(Status.Error("Oops something went wrong"))
            }
        }
    }


    fun fetchAllRequirements(bloodType: String) {
        viewModelScope.launch {
            try {
                _allRequests.postValue(Resource.Loading())
                _userLocation.value?.let { location ->
                    Timber.d(location.state)
                    postRepo.getAllPosts(
                        bloodType = bloodType,
                        state = location.state,
                        successListener = {
                            _allRequests.postValue(Resource.Success(it))
                        },
                        failureListener = {
                            _allRequests.postValue(
                                Resource.Error(
                                    it.message ?: "Oops something went wrong"
                                )
                            )
                        })
                } ?: _allRequests.postValue(Resource.Error("Error fetching location"))
            } catch (exception: Exception) {
                _allRequests.postValue(Resource.Error("Oops something went wrong"))
            }
        }
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