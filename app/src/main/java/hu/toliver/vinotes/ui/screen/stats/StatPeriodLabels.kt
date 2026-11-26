package hu.toliver.vinotes.ui.screen.stats

import androidx.annotation.StringRes
import hu.toliver.vinotes.R
import hu.toliver.vinotes.domain.usecases.stats.StatPeriod

@StringRes
fun StatPeriod.labelRes(): Int = when (this) {
    StatPeriod.LAST_30_DAYS -> R.string.stats_period_30_days
    StatPeriod.LAST_3_MONTHS -> R.string.stats_period_3_months
    StatPeriod.THIS_YEAR -> R.string.stats_period_this_year
    StatPeriod.ALL_TIME -> R.string.stats_period_all_time
}