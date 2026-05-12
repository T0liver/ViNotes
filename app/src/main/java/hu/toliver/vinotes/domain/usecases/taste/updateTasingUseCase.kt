package hu.toliver.vinotes.domain.usecases.taste

import hu.toliver.vinotes.domain.model.Taste
import hu.toliver.vinotes.domain.repository.TasteRepository
import jakarta.inject.Inject

class updateTasingUseCase @Inject constructor(
    private val repository: TasteRepository
) {
    suspend operator fun invoke(taste: Taste): Result<Unit> {
        return repository.update(taste)
    }
}