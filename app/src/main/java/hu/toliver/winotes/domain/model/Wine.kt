package hu.toliver.winotes.domain.model

import hu.toliver.winotes.domain.model.enums.WineColour
import hu.toliver.winotes.domain.model.enums.WineSweetness

data class Wine (
    // Base information about the wine
    val id: String,
    val name: String,
    val producer: String,
    val year: Int,
    val grape: String,
    val isCuvee: Boolean,
    val cuveeComponents: List<String>,

    // Origin and class information
    val colour: WineColour,
    val sugar: Int,
    val sweetness: WineSweetness,
    val country: String,
    val region: String,

    // Technical details
    val alcoholPercentage: Double,
    val volume: Int,
    val description: String,
    val image: String,
)