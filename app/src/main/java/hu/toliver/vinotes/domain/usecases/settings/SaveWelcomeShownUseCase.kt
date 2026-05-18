package hu.toliver.vinotes.domain.usecases.settings

import hu.toliver.vinotes.domain.repository.AppPreferencesRepository
import javax.inject.Inject

class SaveWelcomeShownUseCase @Inject constructor(
    private val repo: AppPreferencesRepository,
) {
    suspend operator fun invoke(shown: Boolean) = repo.saveWelcomeShown(shown)
}

