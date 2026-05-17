package hu.toliver.vinotes.data.repository

import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.toliver.vinotes.data.remote.api.NominatimApi
import hu.toliver.vinotes.domain.repository.LocationRepository
import javax.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine

class LocationRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val nominatimApi: NominatimApi,
) : LocationRepository {

    override suspend fun getCurrentLocation(): Result<Pair<Double, Double>> = runCatching {
        suspendCancellableCoroutine<Pair<Double, Double>> { cont ->
            try {
                val client = LocationServices.getFusedLocationProviderClient(context)
                val tokenSource = CancellationTokenSource()

                cont.invokeOnCancellation {
                    tokenSource.cancel()
                }

                client.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    tokenSource.token
                )
                    .addOnSuccessListener { location: Location? ->
                        if (!cont.isActive) return@addOnSuccessListener
                        if (location != null) {
                            cont.resumeWith(Result.success(location.latitude to location.longitude))
                        } else {
                            cont.resumeWith(Result.failure(Exception("Nem sikerült lekérni a pozíciót")))
                        }
                    }
                    .addOnFailureListener { error: Exception ->
                        if (cont.isActive) {
                            cont.resumeWith(Result.failure(error))
                        }
                    }
            } catch (securityException: SecurityException) {
                if (cont.isActive) {
                    cont.resumeWith(Result.failure(securityException))
                }
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