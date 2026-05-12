package hu.toliver.winotes.domain.usecases.wine

import hu.toliver.winotes.domain.model.Wine
import hu.toliver.winotes.domain.repository.WineRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllWinesUseCase @Inject constructor(
    private val repository: WineRepository
) {
    operator fun invoke(): Flow<List<Wine>> = repository.getAll()
}