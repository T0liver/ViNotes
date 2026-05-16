package hu.toliver.vinotes.domain.usecases.settings

import hu.toliver.vinotes.domain.repository.AppPreferencesRepository
import javax.inject.Inject

class SaveCatalogUrlUseCase @Inject constructor(
    private val repo: AppPreferencesRepository,
) {
    suspend operator fun invoke(url: String) = repo.saveCatalogUrl(url.trim())
}

