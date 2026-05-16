package hu.toliver.vinotes.domain.usecases.catalog

import android.net.Uri
import hu.toliver.vinotes.domain.repository.CatalogSyncRepository
import hu.toliver.vinotes.domain.repository.WineRepository
import javax.inject.Inject

class ImportFromFileUseCase @Inject constructor(
    private val catalogSyncRepository: CatalogSyncRepository,
    private val wineRepository: WineRepository
) {
    suspend operator fun invoke(uri: Uri): Result<Unit> = runCatching {
        val wines = catalogSyncRepository.importFromLocalFile(uri).getOrThrow()

        wineRepository.insertAll(wines, fromCatalog = false).getOrThrow()
    }
}

