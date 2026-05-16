package hu.toliver.vinotes.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WineDto(
    val id: String,
    val name: String,
    val producer: String,
    val year: Int,
    val grape: String,
    val isCuvee: Boolean,
    val cuveeComponents: List<String>,
    val colour: String,
    val sugar: Int,
    val sweetness: String,
    val country: String,
    val region: String,
    val alcoholPercentage: Double,
    val volume: Int,
    val description: String,
    val image: String
)