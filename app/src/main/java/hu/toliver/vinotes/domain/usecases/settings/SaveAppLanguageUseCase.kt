package hu.toliver.vinotes.domain.usecases.settings

import hu.toliver.vinotes.domain.model.enums.AppLanguage
import hu.toliver.vinotes.domain.repository.AppPreferencesRepository
import javax.inject.Inject

class SaveAppLanguageUseCase @Inject constructor(
    private val repo: AppPreferencesRepository,
) {
    suspend operator fun invoke(language: AppLanguage) {
        repo.saveAppLanguage(language)
    }
}

