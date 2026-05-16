package hu.toliver.vinotes.data.remote.api

import hu.toliver.vinotes.data.remote.dto.CatalogDeltaDto
import hu.toliver.vinotes.data.remote.dto.FullCatalogDto
import hu.toliver.vinotes.data.remote.dto.ManifestDto
import retrofit2.http.GET
import retrofit2.http.Url

interface CatalogApi {
    @GET
    suspend fun getManifest(@Url url: String): ManifestDto

    @GET
    suspend fun getFullCatalog(@Url url: String): FullCatalogDto

    @GET
    suspend fun getDelta(@Url url: String): CatalogDeltaDto
}