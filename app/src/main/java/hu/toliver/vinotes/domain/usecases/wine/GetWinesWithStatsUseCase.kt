package hu.toliver.vinotes.domain.usecases.wine

import hu.toliver.vinotes.domain.model.WineWithStats
import hu.toliver.vinotes.domain.repository.TasteRepository
import hu.toliver.vinotes.domain.repository.WineRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetWinesWithStatsUseCase @Inject constructor(
    private val wineRepository: WineRepository,
    private val tasteRepository: TasteRepository,
) {
    operator fun invoke(): Flow<List<WineWithStats>> =
        combine(wineRepository.getAll(), tasteRepository.getAll()) { wines, tastes ->
            val tastesByWine = tastes.groupBy { it.wineId }

            wines.map { wine ->
                val wineTastes = tastesByWine[wine.id] ?: emptyList()
                val latest = wineTastes.maxByOrNull { it.date }   // java.util.Date összehasonlítható

                WineWithStats(
                    wine = wine,
                    tastingCount = wineTastes.size,
                    latestRating = latest?.rating,
                    latestTastingDate = latest?.date,
                )
            }
        }
}

