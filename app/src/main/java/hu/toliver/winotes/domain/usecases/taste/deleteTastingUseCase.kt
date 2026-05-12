package hu.toliver.winotes.domain.usecases.taste

import hu.toliver.winotes.domain.repository.TasteRepository
import jakarta.inject.Inject

class deleteTastingUseCase @Inject constructor (
    private val repository: TasteRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.delete(id)
}