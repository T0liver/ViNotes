package hu.toliver.winotes.domain.usecases.taste

import hu.toliver.winotes.domain.repository.TasteRepository
import jakarta.inject.Inject

class GetTastingByWineUseCase @Inject constructor(
    private val repository: TasteRepository
) {
    suspend operator fun invoke(wineId: String) = repository.getByWineId(wineId)
}