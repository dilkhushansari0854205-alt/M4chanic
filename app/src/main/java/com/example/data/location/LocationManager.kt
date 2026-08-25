package com.example.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.domain.model.LocationPoint
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationManager(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Default reference location from the prompt and UI design: Koramangala, Bengaluru / 25.7800, 87.4700
    private val _currentLocation = MutableStateFlow(
        LocationPoint(
            lat = 25.7800,
            lng = 87.4700,
            address = "24, 5th Cross, Koramangala 4th Block, Bengaluru, Karnataka 560034"
        )
    )
    val currentLocation: StateFlow<LocationPoint> = _currentLocation.asStateFlow()

    fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocation || coarseLocation
    }

    fun requestSingleLocation() {
        CoroutineScope(Dispatchers.IO).launch {
            fetchCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun fetchCurrentLocation(): LocationPoint = withContext(Dispatchers.IO) {
        if (!hasLocationPermission()) {
            return@withContext _currentLocation.value
        }

        try {
            val cts = CancellationTokenSource()
            val location: Location? = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cts.token
            ).await()

            if (location != null) {
                val address = resolveAddress(location.latitude, location.longitude)
                val point = LocationPoint(
                    lat = location.latitude,
                    lng = location.longitude,
                    address = address
                )
                _currentLocation.value = point
                return@withContext point
            }
        } catch (_: Exception) {
            // fallback to last known or default
        }

        return@withContext _currentLocation.value
    }

    fun updateManualLocation(lat: Double, lng: Double, address: String) {
        _currentLocation.value = LocationPoint(lat, lng, address)
    }

    private fun resolveAddress(lat: Double, lng: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                val street = addr.getAddressLine(0)
                street ?: "${addr.locality ?: ""}, ${addr.adminArea ?: ""}"
            } else {
                "24, 5th Cross, Koramangala 4th Block, Bengaluru"
            }
        } catch (_: Exception) {
            "24, 5th Cross, Koramangala 4th Block, Bengaluru, Karnataka 560034"
        }
    }

    companion object {
        fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val r = 6371.0 // Radius of earth in km
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(dLon / 2) * sin(dLon / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            val distance = r * c
            return Math.round(distance * 10.0) / 10.0
        }
    }
}
