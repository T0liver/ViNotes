package hu.toliver.vinotes.domain.usecases.wine

import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.repository.WineRepository
import jakarta.inject.Inject

class GetWineByIdUseCase @Inject constructor(
    private val repository: WineRepository
) {
    suspend operator fun invoke(id: String): Result<Wine> = repository.getById(id)
}