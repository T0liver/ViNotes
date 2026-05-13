package hu.toliver.vinotes.domain.usecases.taste

import hu.toliver.vinotes.domain.repository.TasteRepository
import javax.inject.Inject

class GetTasteByIDUseCase @Inject constructor(
    private val repository: TasteRepository
) {
    suspend operator fun invoke(id: String) = repository.getById(id)
}