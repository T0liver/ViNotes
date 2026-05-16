package hu.toliver.vinotes.domain.usecases.settings

import hu.toliver.vinotes.domain.repository.AppPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAppPreferencesUseCase @Inject constructor(
    private val repo: AppPreferencesRepository,
) {
    data class Prefs(
        val catalogUrl: String,
        val username: String,
    )

    operator fun invoke(): Flow<Prefs> =
        combine(repo.catalogUrl, repo.username) { url, name ->
            Prefs(catalogUrl = url, username = name)
        }
}

