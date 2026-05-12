package hu.toliver.winotes.domain.usecases.taste

import hu.toliver.winotes.domain.model.Taste
import hu.toliver.winotes.domain.model.Wine
import hu.toliver.winotes.domain.repository.TasteRepository
import hu.toliver.winotes.domain.repository.WineRepository
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