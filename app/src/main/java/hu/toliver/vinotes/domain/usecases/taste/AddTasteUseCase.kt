package hu.toliver.vinotes.domain.usecases.taste

import hu.toliver.vinotes.domain.model.Taste
import hu.toliver.vinotes.domain.repository.TasteRepository
import javax.inject.Inject

class AddTasteUseCase @Inject constructor(
    private val tasteRepository: TasteRepository,
) {
    suspend operator fun invoke(taste: Taste) = tasteRepository.save(taste)
}
