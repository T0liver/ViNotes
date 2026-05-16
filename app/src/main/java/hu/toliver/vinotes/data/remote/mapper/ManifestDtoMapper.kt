package hu.toliver.vinotes.data.remote.mapper

import hu.toliver.vinotes.data.remote.dto.CatalogDeltaDto
import hu.toliver.vinotes.data.remote.dto.DeltaInfoDto
import hu.toliver.vinotes.data.remote.dto.FullCatalogDto
import hu.toliver.vinotes.data.remote.dto.ManifestDto
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.sync.CatalogDelta
import hu.toliver.vinotes.domain.model.sync.CatalogManifest
import hu.toliver.vinotes.domain.model.sync.DeltaInfo
import hu.toliver.vinotes.domain.model.sync.FullCatalogInfo
import kotlinx.datetime.LocalDate

fun ManifestDto.toDomain(): CatalogManifest = CatalogManifest(
    schemaVersion = schemaVersion,
    lastUpdated = LocalDate.parse(lastUpdated),
    full = full.toDomain(),
    deltas = deltas.map { it.toDomain() }
)

fun hu.toliver.vinotes.data.remote.dto.FullCatalogInfoDto.toDomain(): FullCatalogInfo =
    FullCatalogInfo(
        url = url,
        wineCount = wineCount,
        sha256 = sha256
    )

fun DeltaInfoDto.toDomain(): DeltaInfo = DeltaInfo(
    date = LocalDate.parse(date),
    url = url,
    addedCount = addedCount,
    sha256 = sha256
)

fun FullCatalogDto.toDomain(): List<Wine> =
    wines.map { it.toDomain() }

fun CatalogDeltaDto.toDomain(): CatalogDelta = CatalogDelta(
    date = LocalDate.parse(date),
    added = added.map { it.toDomain() },
    updated = updated.map { it.toDomain() },
    removedIds = removedIds
)