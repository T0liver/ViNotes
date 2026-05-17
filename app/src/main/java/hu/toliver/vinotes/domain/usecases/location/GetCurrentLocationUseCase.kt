package hu.toliver.vinotes.domain.usecases.location

import hu.toliver.vinotes.domain.repository.LocationRepository
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val repository: LocationRepository
) {
    suspend operator fun invoke(): Result<Pair<Double, Double>> =
        repository.getCurrentLocation()
}