package hu.toliver.vinotes.domain.usecases.settings

import hu.toliver.vinotes.domain.repository.AppPreferencesRepository
import hu.toliver.vinotes.domain.model.enums.AppLanguage
import hu.toliver.vinotes.domain.model.enums.ThemeMode
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAppPreferencesUseCase @Inject constructor(
    private val repo: AppPreferencesRepository,
) {
    data class Prefs(
        val catalogUrl: String,
        val username: String,
        val themeMode: ThemeMode,
        val appLanguage: AppLanguage,
    )

    operator fun invoke(): Flow<Prefs> =
        combine(repo.catalogUrl, repo.username, repo.themeMode, repo.appLanguage) { url, name, theme, language ->
            Prefs(catalogUrl = url, username = name, themeMode = theme, appLanguage = language)
        }
}