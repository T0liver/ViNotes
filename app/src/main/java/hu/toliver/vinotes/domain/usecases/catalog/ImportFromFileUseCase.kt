package hu.toliver.vinotes.domain.usecases.catalog

import android.net.Uri
import hu.toliver.vinotes.domain.model.sync.SyncMetadata
import hu.toliver.vinotes.domain.repository.CatalogSyncRepository
import hu.toliver.vinotes.domain.repository.WineRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.time.Clock

class ImportFromFileUseCase @Inject constructor(
    private val catalogSyncRepository: CatalogSyncRepository,
    private val wineRepository: WineRepository
) {
    suspend operator fun invoke(uri: Uri): Result<Unit> = runCatching {
        val wines = catalogSyncRepository.importFromLocalFile(uri).getOrThrow()

        wineRepository.insertAll(wines, fromCatalog = true).getOrThrow()

        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val metadata = SyncMetadata(
            isFullImportDone = true,
            totalWinesInCatalog = wines.size,
            lastSyncDate = today,
            lastAppliedDelta = null,
        )
        catalogSyncRepository.updateSyncMetadata(metadata).getOrThrow()
    }
}

