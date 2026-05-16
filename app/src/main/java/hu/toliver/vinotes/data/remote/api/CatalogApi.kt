package hu.toliver.vinotes.data.remote.api

import hu.toliver.vinotes.data.remote.dto.CatalogDeltaDto
import hu.toliver.vinotes.data.remote.dto.FullCatalogDto
import hu.toliver.vinotes.data.remote.dto.ManifestDto
import retrofit2.http.GET
import retrofit2.http.Url

interface CatalogApi {
    @GET("manifest.json")
    suspend fun getManifest(): ManifestDto

    @GET("full.json")
    suspend fun getFullCatalog(): FullCatalogDto

    @GET
    suspend fun getDelta(@Url url: String): CatalogDeltaDto
}