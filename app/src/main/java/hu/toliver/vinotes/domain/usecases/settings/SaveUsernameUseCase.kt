package hu.toliver.vinotes.domain.usecases.settings

import hu.toliver.vinotes.domain.repository.AppPreferencesRepository
import javax.inject.Inject

class SaveUsernameUseCase @Inject constructor(
    private val repo: AppPreferencesRepository,
) {
    suspend operator fun invoke(name: String) = repo.saveUsername(name.trim())
}

