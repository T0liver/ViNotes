package hu.toliver.vinotes.data.remote.api

import hu.toliver.vinotes.data.remote.dto.NominatimResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimApi {
    @GET("reverse")
    suspend fun reverseGeocode(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("format") format: String = "json",
        @Query("accept-language") language: String = "hu",
        @Query("zoom") zoom: Int = 16
    ): NominatimResponseDto
}

