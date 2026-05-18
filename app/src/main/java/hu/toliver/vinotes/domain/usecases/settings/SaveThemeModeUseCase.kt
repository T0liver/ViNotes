package hu.toliver.vinotes.domain.usecases.settings

import hu.toliver.vinotes.domain.model.enums.ThemeMode
import hu.toliver.vinotes.domain.repository.AppPreferencesRepository
import javax.inject.Inject

class SaveThemeModeUseCase @Inject constructor(
    private val repo: AppPreferencesRepository,
) {
    suspend operator fun invoke(mode: ThemeMode) {
        repo.saveThemeMode(mode)
    }
}