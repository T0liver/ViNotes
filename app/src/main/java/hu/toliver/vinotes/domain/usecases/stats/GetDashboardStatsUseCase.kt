package hu.toliver.vinotes.domain.usecases.stats

import hu.toliver.vinotes.domain.model.DashboardStats
import hu.toliver.vinotes.domain.repository.TasteRepository
import hu.toliver.vinotes.domain.repository.WineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetDashboardStatsUseCase @Inject constructor(
    private val wineRepository: WineRepository,
    private val tasteRepository: TasteRepository,
) {
    operator fun invoke(): Flow<DashboardStats> =
        combine(wineRepository.getAll(), tasteRepository.getAll()) { wines, tastes ->
            val totalWines = wines.size
            val totalTastings = tastes.size
            val averageRating = if (tastes.isEmpty()) 0.0 else tastes.map { it.rating }.average()
            val wineMap = wines.associateBy { it.id }
            val topRegion = tastes
                .mapNotNull { wineMap[it.wineId]?.region }
                .groupingBy { it }
                .eachCount()
                .maxByOrNull { it.value }
                ?.key
                .orEmpty()
            DashboardStats(
                totalWines = totalWines,
                totalTastings = totalTastings,
                averageRating = averageRating,
                topRegion = topRegion,
            )
        }
}
