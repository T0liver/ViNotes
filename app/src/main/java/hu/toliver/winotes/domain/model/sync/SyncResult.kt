package hu.toliver.winotes.domain.model.sync

sealed class SyncResult {
    data class Success(val addedCount: Int, val updatedCount: Int, val removedCount: Int) : SyncResult()
    data class UpToDate(val message: String) : SyncResult()
    data class Error(val cause: Throwable) : SyncResult()
}