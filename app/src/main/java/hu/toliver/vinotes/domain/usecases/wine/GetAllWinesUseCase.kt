package hu.toliver.vinotes.domain.usecases.wine

import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.repository.WineRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllWinesUseCase @Inject constructor(
    private val repository: WineRepository
) {
    operator fun invoke(): Flow<List<Wine>> = repository.getAll()
}