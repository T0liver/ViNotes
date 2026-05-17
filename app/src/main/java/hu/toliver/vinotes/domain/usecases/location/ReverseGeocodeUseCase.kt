package hu.toliver.vinotes.domain.usecases.location

import hu.toliver.vinotes.domain.repository.LocationRepository
import javax.inject.Inject

class ReverseGeocodeUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<String> =
        repository.reverseGeocode(latitude, longitude)
}