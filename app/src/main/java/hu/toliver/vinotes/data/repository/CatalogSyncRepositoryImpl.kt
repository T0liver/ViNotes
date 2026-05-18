package hu.toliver.vinotes.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.toliver.vinotes.R
import hu.toliver.vinotes.data.dao.SyncMetadataDao
import hu.toliver.vinotes.data.local.entity.toDomain
import hu.toliver.vinotes.data.local.entity.toEntity
import hu.toliver.vinotes.data.remote.api.CatalogApi
import hu.toliver.vinotes.domain.repository.AppPreferencesRepository
import hu.toliver.vinotes.data.remote.dto.FullCatalogDto
import hu.toliver.vinotes.data.remote.mapper.toDomain
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.sync.CatalogDelta
import hu.toliver.vinotes.domain.model.sync.CatalogManifest
import hu.toliver.vinotes.domain.model.sync.DeltaInfo
import hu.toliver.vinotes.domain.model.sync.SyncMetadata
import hu.toliver.vinotes.domain.repository.CatalogSyncRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.serialization.json.Json
import hu.toliver.vinotes.ui.AppConstants
import java.net.UnknownHostException

class CatalogSyncRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val syncMetadataDao: SyncMetadataDao,
    private val catalogApi: CatalogApi,
    private val appPreferencesRepository: AppPreferencesRepository
) : CatalogSyncRepository {

    override fun getSyncMetadata(): Flow<SyncMetadata?> =
        syncMetadataDao.get().map { it?.toDomain() }

    override suspend fun updateSyncMetadata(metadata: SyncMetadata): Result<Unit> = runCatching {
        syncMetadataDao.upsert(metadata.toEntity())
    }

    override suspend fun fetchManifest(): Result<CatalogManifest> = runCatching {
        val rawBase = appPreferencesRepository.catalogUrl.firstOrNull() ?: AppConstants.DEFAULT_CATALOG_URL
        val base = if (rawBase.startsWith("http://") || rawBase.startsWith("https://")) rawBase else "https://$rawBase"
        val url = base.trimEnd('/') + "/manifest.json"
        try {
            catalogApi.getManifest(url).toDomain()
        } catch (e: UnknownHostException) {
            throw Exception(
                context.getString(
                    R.string.dns_resolution_failed_for_host_when_fetching_manifest,
                    e.message
                ), e)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun downloadFull(): Result<List<Wine>> = runCatching {
        val rawBase = appPreferencesRepository.catalogUrl.firstOrNull() ?: AppConstants.DEFAULT_CATALOG_URL
        val base = if (rawBase.startsWith("http://") || rawBase.startsWith("https://")) rawBase else "https://$rawBase"
        val url = base.trimEnd('/') + "/full.json"
        try {
            catalogApi.getFullCatalog(url).toDomain()
        } catch (e: UnknownHostException) {
            throw Exception(
                context.getString(
                    R.string.dns_resolution_failed_for_host_when_downloading_full_catalog,
                    e.message
                ), e)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun downloadDelta(deltaInfo: DeltaInfo): Result<CatalogDelta> = runCatching {
        // deltaInfo.url is expected to be a relative filename (e.g. "delta-2025-05-16.json")
        val relative = deltaInfo.url
        val rawBase = appPreferencesRepository.catalogUrl.firstOrNull() ?: AppConstants.DEFAULT_CATALOG_URL
        val base = if (rawBase.startsWith("http://") || rawBase.startsWith("https://")) rawBase else "https://$rawBase"
        val fullUrl = if (relative.startsWith("http://") || relative.startsWith("https://")) {
            relative
        } else {
            base.trimEnd('/') + "/" + relative.trimStart('/')
        }
        try {
            catalogApi.getDelta(fullUrl).toDomain()
        } catch (e: UnknownHostException) {
            throw Exception(
                context.getString(
                    R.string.dns_resolution_failed_for_host_when_downloading_delta,
                    e.message
                ), e)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun importFromLocalFile(uri: Uri): Result<List<Wine>> = runCatching {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        val displayName = uri.displayName()
        val mimeType = context.contentResolver.getType(uri)
        if (!looksLikeJson(displayName, mimeType)) {
            invalidCatalog("the selected file does not look like a JSON file")
        }

        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException(context.getString(R.string.could_not_open_file, uri))
        val content = inputStream.bufferedReader().use { it.readText() }
        val fullCatalogDto = try {
            json.decodeFromString<FullCatalogDto>(content)
        } catch (_: Exception) {
            invalidCatalog("it could not be parsed as JSON")
        }

        validateFullCatalog(fullCatalogDto)

        fullCatalogDto.toDomain()
    }

    private fun Uri.displayName(): String? = context.contentResolver.query(
        this,
        arrayOf(OpenableColumns.DISPLAY_NAME),
        null,
        null,
        null,
    )?.use { cursor ->
        if (cursor.moveToFirst()) cursor.getString(0) else null
    }

    private fun looksLikeJson(displayName: String?, mimeType: String?): Boolean =
        displayName?.endsWith(".json", ignoreCase = true) == true ||
            mimeType?.contains("json", ignoreCase = true) == true

    private fun validateFullCatalog(fullCatalogDto: FullCatalogDto) {
        if (!fullCatalogDto.type.equals("full", ignoreCase = true)) {
            invalidCatalog("catalog type must be 'full'")
        }

        if (fullCatalogDto.wines.isEmpty()) {
            invalidCatalog("the catalog does not contain any wines")
        }

        runCatching { LocalDate.parse(fullCatalogDto.exportedAt) }
            .getOrElse { invalidCatalog("exportedAt is not a valid ISO date") }

        val duplicateIds = fullCatalogDto.wines
            .groupingBy { it.id }
            .eachCount()
            .filterValues { it > 1 }

        if (duplicateIds.isNotEmpty()) {
            invalidCatalog("the catalog contains duplicate wine IDs")
        }

        fullCatalogDto.wines.forEachIndexed { index, wine ->
            validateWineDto(wine, index)
        }
    }

    private fun validateWineDto(wine: hu.toliver.vinotes.data.remote.dto.WineDto, index: Int) {
        val wineLabel = "wine #${index + 1}"

        if (wine.id.isBlank()) invalidCatalog("$wineLabel is missing an id")
        if (wine.name.isBlank()) invalidCatalog("$wineLabel is missing a name")
        if (wine.producer.isBlank()) invalidCatalog("$wineLabel is missing a producer")
        if (wine.grape.isBlank()) invalidCatalog("$wineLabel is missing a grape")
        if (wine.country.isBlank()) invalidCatalog("$wineLabel is missing a country")
        if (wine.region.isBlank()) invalidCatalog("$wineLabel is missing a region")
        if (wine.description.isBlank()) invalidCatalog("$wineLabel is missing a description")

        if (wine.year !in 1800..2100) invalidCatalog("$wineLabel has an invalid vintage year")
        if (wine.sugar < 0f) invalidCatalog("$wineLabel has a negative sugar value")
        if (wine.alcoholPercentage < 0.0 || wine.alcoholPercentage > 25.0) {
            invalidCatalog("$wineLabel has an invalid alcohol percentage")
        }
        if (wine.volume <= 0) invalidCatalog("$wineLabel has an invalid volume")

        if (hu.toliver.vinotes.domain.model.enums.WineColour.entries.none { it.name == wine.colour }) {
            invalidCatalog("$wineLabel has an invalid colour value")
        }

        if (hu.toliver.vinotes.domain.model.enums.WineSweetness.entries.none { it.name == wine.sweetness }) {
            invalidCatalog("$wineLabel has an invalid sweetness value")
        }
    }

    private fun invalidCatalog(reason: String): Nothing = throw IllegalArgumentException(
        context.getString(R.string.invalid_wine_catalog_file, reason)
    )
}