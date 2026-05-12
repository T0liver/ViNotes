package hu.toliver.vinotes.domain.usecases.taste

import hu.toliver.vinotes.domain.repository.TasteRepository
import jakarta.inject.Inject

class deleteTastingUseCase @Inject constructor (
    private val repository: TasteRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.delete(id)
}