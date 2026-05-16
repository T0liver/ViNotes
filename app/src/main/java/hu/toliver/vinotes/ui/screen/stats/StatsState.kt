package hu.toliver.vinotes.ui.screen.stats

import hu.toliver.vinotes.domain.model.FullStatsData
import hu.toliver.vinotes.domain.usecases.stats.StatPeriod

data class StatsState(
    val isLoading:      Boolean       = true,
    val period:         StatPeriod    = StatPeriod.ALL_TIME,
    val data:           FullStatsData? = null,
    val errorMessage:   String?       = null,
    val selectedDay:    String?       = null,
    val selectedDayCount: Int         = 0,
)

