package hu.toliver.vinotes.domain.model

data class WineWithTastings(
    val wine: Wine,
    val tastings: List<Taste>,
)