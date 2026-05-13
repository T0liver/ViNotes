package hu.toliver.vinotes.domain.usecases.taste

import hu.toliver.vinotes.domain.model.Taste
import hu.toliver.vinotes.domain.repository.TasteRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllTastingsUseCase @Inject constructor(
    private val repository: TasteRepository
) {
    operator fun invoke(): Flow<List<Taste>> = repository.getAll()
}