package hu.toliver.vinotes.domain.usecases.taste

import hu.toliver.vinotes.domain.model.Taste
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.repository.TasteRepository
import hu.toliver.vinotes.domain.repository.WineRepository
import jakarta.inject.Inject

class SaveTastingUseCase @Inject constructor(
    private val tasteRepository: TasteRepository,
    private val wineRepository: WineRepository
) {
    suspend operator fun invoke(taste: Taste, wine: Wine): Result<Unit> {
        wineRepository.insertIfNotExists(wine)
        return tasteRepository.save(taste)
    }
}