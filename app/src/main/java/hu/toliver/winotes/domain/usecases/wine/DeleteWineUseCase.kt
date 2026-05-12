package hu.toliver.winotes.domain.usecases.wine

import hu.toliver.winotes.domain.model.Wine
import hu.toliver.winotes.domain.repository.WineRepository
import jakarta.inject.Inject

class DeleteWineUseCase@Inject constructor(
    private val repository: WineRepository
) {
    suspend operator fun invoke(wine: Wine): Result<Unit> = repository.delete(wine.id)
}