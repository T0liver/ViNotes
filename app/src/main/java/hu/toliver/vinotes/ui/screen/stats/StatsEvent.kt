package hu.toliver.vinotes.ui.screen.stats

import hu.toliver.vinotes.domain.usecases.stats.StatPeriod

sealed interface StatsEvent {
    data object LoadData                               : StatsEvent
    data class  PeriodChanged(val period: StatPeriod)  : StatsEvent
    data class  HeatmapDayTapped(val day: String, val count: Int) : StatsEvent
    data object HeatmapSelectionCleared                : StatsEvent
}

