package hu.toliver.winotes.domain.model.sync

data class FullCatalogInfo(
    val url: String,
    val wineCount: Int,
    val sha256: String
)