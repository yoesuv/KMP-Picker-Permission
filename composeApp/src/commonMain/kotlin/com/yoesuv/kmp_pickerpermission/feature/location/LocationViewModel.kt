package com.yoesuv.kmp_pickerpermission.feature.location

import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile

class LocationViewModel(
    private val permissionsController: PermissionsController
) : ViewModel() {
    
    private val _permissionState = MutableStateFlow(PermissionState.NotDetermined)
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()
    
    private val _currentLocation = MutableStateFlow<LocationData?>(null)
    val currentLocation: StateFlow<LocationData?> = _currentLocation.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val geolocator = Geolocator.mobile()
    
    init {
        viewModelScope.launch {
            // Initialize with current permission state
            _permissionState.value = permissionsController.getPermissionState(Permission.LOCATION)
        }
    }
    
    fun requestLocationPermission() {
        viewModelScope.launch {
            try {
                permissionsController.providePermission(Permission.LOCATION)
                _permissionState.value = PermissionState.Granted
                
                // After permission granted, get actual location using Compass
                getCurrentLocation()
            } catch (e: DeniedException) {
                _permissionState.value = PermissionState.Denied
            } catch (e: DeniedAlwaysException) {
                _permissionState.value = PermissionState.DeniedAlways
            } catch (e: RequestCanceledException) {
                e.printStackTrace()
            }
        }
    }
    
    private suspend fun getCurrentLocation() {
        _isLoading.value = true
        try {
            val result = geolocator.current()
            when (result) {
                is GeolocatorResult.Success -> {
                    _currentLocation.value = LocationData(
                        latitude = result.data.coordinates.latitude,
                        longitude = result.data.coordinates.longitude
                    )
                }
                is GeolocatorResult.Error -> {
                    // Fallback to mock location if geolocation fails
                    _currentLocation.value = LocationData(
                        latitude = 0.0,
                        longitude = 0.0
                    )
                }
            }
        } finally {
            _isLoading.value = false
        }
    }
    
    fun openAppSettings() {
        permissionsController.openAppSettings()
    }
}

data class LocationData(
    val latitude: Double?, 
    val longitude: Double?
)
