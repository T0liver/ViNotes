package hu.toliver.vinotes.domain.usecases.taste

import hu.toliver.vinotes.domain.repository.TasteRepository
import javax.inject.Inject

class GetTasteByIdUseCase @Inject constructor(
    private val tasteRepository: TasteRepository,
) {
    suspend operator fun invoke(id: String) = tasteRepository.getById(id)
}
