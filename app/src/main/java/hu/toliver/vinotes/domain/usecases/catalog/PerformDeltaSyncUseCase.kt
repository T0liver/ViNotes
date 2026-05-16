package hu.toliver.vinotes.domain.usecases.catalog

import hu.toliver.vinotes.domain.model.sync.SyncMetadata
import hu.toliver.vinotes.domain.repository.CatalogSyncRepository
import hu.toliver.vinotes.domain.repository.WineRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.time.Clock

class PerformDeltaSyncUseCase @Inject constructor(
    private val catalogSyncRepository: CatalogSyncRepository,
    private val wineRepository: WineRepository
) {
    suspend operator fun invoke(): Result<Unit> = runCatching {
        val manifest = catalogSyncRepository.fetchManifest().getOrThrow()

        val metadataSync = catalogSyncRepository.getSyncMetadata().firstOrNull()
        val lastAppliedDeltaDate = metadataSync?.lastAppliedDelta

        val deltasToApply = if (lastAppliedDeltaDate != null) {
            manifest.deltas.filter { it.date > lastAppliedDeltaDate }
        } else {
            manifest.deltas
        }

        val sortedDeltas = deltasToApply.sortedBy { it.date }

        for (deltaInfo in sortedDeltas) {
            val delta = catalogSyncRepository.downloadDelta(deltaInfo).getOrThrow()

            if (delta.added.isNotEmpty()) {
                wineRepository.insertAll(delta.added, fromCatalog = true).getOrThrow()
            }

            if (delta.updated.isNotEmpty()) {
                wineRepository.insertAll(delta.updated, fromCatalog = true).getOrThrow()
            }

            if (delta.removedIds.isNotEmpty()) {
                wineRepository.deleteAll(delta.removedIds).getOrThrow()
            }
        }

        if (sortedDeltas.isNotEmpty()) {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val latestDeltaDate = sortedDeltas.last().date
            val updatedMetadata = (metadataSync ?: SyncMetadata(
                isFullImportDone = false,
                totalWinesInCatalog = 0,
                lastSyncDate = null,
                lastAppliedDelta = null
            )).copy(
                lastAppliedDelta = latestDeltaDate,
                lastSyncDate = today
            )
            catalogSyncRepository.updateSyncMetadata(updatedMetadata).getOrThrow()
        }
    }
}