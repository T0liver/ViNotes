package hu.toliver.vinotes.domain.model.sync

data class FullCatalogInfo(
    val url: String,
    val wineCount: Int,
    val sha256: String
)