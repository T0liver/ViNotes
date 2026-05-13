package hu.toliver.vinotes.domain.usecases.wine

import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.repository.WineRepository
import javax.inject.Inject

class UpdateWineUseCase @Inject constructor(
    private val repository: WineRepository
) {
    suspend operator fun invoke(wine: Wine): Result<Unit> = repository.update(wine)
}