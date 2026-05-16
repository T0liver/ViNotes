package hu.toliver.vinotes.domain.usecases.settings

import hu.toliver.vinotes.domain.repository.TasteRepository
import hu.toliver.vinotes.domain.repository.WineRepository
import javax.inject.Inject

class ClearAllDataUseCase @Inject constructor(
    private val wineRepository: WineRepository,
    private val tasteRepository: TasteRepository,
) {
    suspend operator fun invoke() {
        tasteRepository.deleteAll().getOrThrow()
        wineRepository.deleteAll().getOrThrow()
    }
}


