package hu.toliver.winotes.domain.usecases.taste

import hu.toliver.winotes.domain.model.Taste
import hu.toliver.winotes.domain.repository.TasteRepository
import jakarta.inject.Inject

class updateTasingUseCase @Inject constructor(
    private val repository: TasteRepository
) {
    suspend operator fun invoke(taste: Taste): Result<Unit> {
        return repository.update(taste)
    }
}