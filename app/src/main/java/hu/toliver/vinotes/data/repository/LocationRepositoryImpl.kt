package hu.toliver.vinotes.data.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.toliver.vinotes.R
import hu.toliver.vinotes.data.remote.api.NominatimApi
import hu.toliver.vinotes.domain.repository.LocationRepository
import javax.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val nominatimApi: NominatimApi,
) : LocationRepository {

    override suspend fun getCurrentLocation(): Result<Pair<Double, Double>> = runCatching {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val hasFinePermission = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarsePermission = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasFinePermission && !hasCoarsePermission) {
            error(context.getString(R.string.no_location_permission_granted))
        }

        val provider = when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                LocationManager.GPS_PROVIDER
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                LocationManager.NETWORK_PROVIDER
            else -> error(context.getString(R.string.no_avaliable_location_provider))
        }

        val lastKnown = locationManager.getLastKnownLocation(provider)
        val fiveMinutesAgo = System.currentTimeMillis() - 5 * 60 * 1000
        if (lastKnown != null && lastKnown.time > fiveMinutesAgo) {
            return@runCatching Pair(lastKnown.latitude, lastKnown.longitude)
        }

        suspendCancellableCoroutine { continuation ->
            val listener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    locationManager.removeUpdates(this)
                    @Suppress("DEPRECATION")
                    continuation.resume(Pair(location.latitude, location.longitude))
                }

                override fun onProviderDisabled(provider: String) {
                    locationManager.removeUpdates(this)
                    continuation.cancel(Exception(context.getString(R.string.the_location_provider_was_disabled)))
                }
            }

            @Suppress("DEPRECATION")
            locationManager.requestSingleUpdate(provider, listener, Looper.getMainLooper())

            continuation.invokeOnCancellation {
                locationManager.removeUpdates(listener)
            }
        }
    }

    override suspend fun reverseGeocode(latitude: Double, longitude: Double): Result<String> = runCatching {
        val response = nominatimApi.reverseGeocode(latitude, longitude)
        val settlement = response.address.city
            ?: response.address.town
            ?: response.address.village
        val county = response.address.county
        val country = response.address.country

        when {
            settlement != null && county != null -> "$settlement, $county"
            settlement != null && country != null -> "$settlement, $country"
            settlement != null -> settlement
            county != null && country != null -> "$county, $country"
            county != null -> county
            country != null -> country
            else -> response.displayName
        }
    }
}