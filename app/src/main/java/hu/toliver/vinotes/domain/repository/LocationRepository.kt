package hu.toliver.vinotes.domain.repository

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<Pair<Double, Double>>
    suspend fun reverseGeocode(latitude: Double, longitude: Double): Result<String>
}