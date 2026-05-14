package hu.toliver.vinotes.domain.usecases.wine

import hu.toliver.vinotes.domain.model.WineWithTastings
import hu.toliver.vinotes.domain.repository.TasteRepository
import hu.toliver.vinotes.domain.repository.WineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWineWithTastingsUseCase @Inject constructor(
    private val wineRepository: WineRepository,
    private val tasteRepository: TasteRepository,
) {
    operator fun invoke(wineId: String): Flow<WineWithTastings?> =
        combine(
            tasteRepository.getByWineId(wineId),
            flow { emit(wineRepository.getById(wineId).getOrNull()) }
        ) { tastes, wine ->
            wine?.let {
                WineWithTastings(
                    wine = it,
                    tastings = tastes.sortedByDescending { t -> t.date },
                )
            }
        }
}


