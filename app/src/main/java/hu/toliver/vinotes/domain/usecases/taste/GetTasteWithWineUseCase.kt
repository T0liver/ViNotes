package hu.toliver.vinotes.domain.usecases.taste

import hu.toliver.vinotes.domain.model.TasteWithWine
import hu.toliver.vinotes.domain.repository.TasteRepository
import hu.toliver.vinotes.domain.repository.WineRepository
import javax.inject.Inject

class GetTasteWithWineUseCase @Inject constructor(
    private val tasteRepository: TasteRepository,
    private val wineRepository: WineRepository,
) {
    suspend operator fun invoke(tasteId: String): Result<TasteWithWine> = runCatching {
        val taste = tasteRepository.getById(tasteId).getOrThrow()
        val wine = wineRepository.getById(taste.wineId).getOrThrow()
        TasteWithWine(taste = taste, wine = wine)
    }
}