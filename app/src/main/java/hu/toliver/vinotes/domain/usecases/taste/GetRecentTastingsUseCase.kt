package hu.toliver.vinotes.domain.usecases.taste

import hu.toliver.vinotes.domain.model.TasteWithWine
import hu.toliver.vinotes.domain.repository.TasteRepository
import hu.toliver.vinotes.domain.repository.WineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetRecentTastingsUseCase @Inject constructor(
    private val tasteRepository: TasteRepository,
    private val wineRepository: WineRepository,
) {
    operator fun invoke(limit: Int = 5): Flow<List<TasteWithWine>> =
        combine(tasteRepository.getAll(), wineRepository.getAll()) { tastes, wines ->
            val winesById = wines.associateBy { it.id }
            tastes
                .sortedByDescending { it.date }
                .take(limit.coerceAtLeast(0))
                .mapNotNull { taste ->
                    winesById[taste.wineId]?.let { wine ->
                        TasteWithWine(
                            taste = taste,
                            wine = wine,
                        )
                    }
                }
        }
}
