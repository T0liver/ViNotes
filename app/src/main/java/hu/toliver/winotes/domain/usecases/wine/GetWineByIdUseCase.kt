package hu.toliver.winotes.domain.usecases.wine

import hu.toliver.winotes.domain.model.Wine
import hu.toliver.winotes.domain.repository.WineRepository
import jakarta.inject.Inject

class GetWineByIdUseCase @Inject constructor(
    private val repository: WineRepository
) {
    suspend operator fun invoke(id: String): Result<Wine> = repository.getById(id)
}