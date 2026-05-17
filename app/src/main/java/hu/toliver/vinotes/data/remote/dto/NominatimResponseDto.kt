package hu.toliver.vinotes.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NominatimResponseDto(
    @SerialName("display_name") val displayName: String,
    @SerialName("address") val address: NominatimAddressDto
)

@Serializable
data class NominatimAddressDto(
    @SerialName("city") val city: String? = null,
    @SerialName("town") val town: String? = null,
    @SerialName("village") val village: String? = null,
    @SerialName("county") val county: String? = null,
    @SerialName("country") val country: String? = null
)

