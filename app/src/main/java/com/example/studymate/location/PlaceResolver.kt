package com.example.studymate.location

import android.content.Context
import android.location.Geocoder
import java.util.Locale

class PlaceResolver(private val context: Context) {

    fun resolve(
        latitude: Double,
        longitude: Double
    ): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)

            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]

                val placeParts = listOfNotNull(
                    addr.subLocality,
                    addr.locality,
                    addr.adminArea
                )

                placeParts.joinToString(", ")
            } else {
                "Lokasi tidak diketahui"
            }
        } catch (e: Exception) {
            "Lokasi tidak tersedia"
        }
    }
}
