package com.example.studymate.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class LocationProvider(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun fetchOneTimeLocation(
        onSuccess: (Double, Double) -> Unit,
        onError: (String) -> Unit
    ) {
        if (!hasPermission()) {
            onError("Location permission not granted")
            return
        }

        try {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        onSuccess(location.latitude, location.longitude)
                    } else {
                        fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc: Location? ->
                            if (lastLoc != null) {
                                onSuccess(lastLoc.latitude, lastLoc.longitude)
                            } else {
                                onError("Unable to retrieve location")
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    onError(e.message ?: "Location error")
                }
        } catch (e: Exception) {
            onError(e.message ?: "Exception getting location")
        }
    }
}
