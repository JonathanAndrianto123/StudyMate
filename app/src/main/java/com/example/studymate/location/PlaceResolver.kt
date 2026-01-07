package com.example.studymate.location

import android.content.Context
import android.location.Geocoder
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PlaceResolver(private val context: Context) {

    @Suppress("DEPRECATION")
    suspend fun resolvePlaceName(lat: Double, lon: Double): String = withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                suspendCoroutine { cont ->
                    geocoder.getFromLocation(lat, lon, 1) { addresses ->
                        if (addresses.isNotEmpty()) {
                            val address = addresses[0]
                            // Try suitable address lines like locality or subAdminArea
                            val place = address.locality ?: address.subAdminArea ?: address.getAddressLine(0)
                            cont.resume(place ?: "Unknown Location")
                        } else {
                            cont.resume("Unknown Location")
                        }
                    }
                }
            } else {
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    address.locality ?: address.subAdminArea ?: address.getAddressLine(0) ?: "Unknown Location"
                } else {
                    "Unknown Location"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Unknown Location"
        }
    }
}
