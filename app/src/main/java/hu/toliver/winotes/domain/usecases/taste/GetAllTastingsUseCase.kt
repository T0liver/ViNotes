package hu.toliver.winotes.domain.usecases.taste

import hu.toliver.winotes.domain.model.Taste
import hu.toliver.winotes.domain.repository.TasteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllTastingsUseCase @Inject constructor(
    private val repository: TasteRepository
) {
    operator fun invoke(): Flow<List<Taste>> = repository.getAll()
}