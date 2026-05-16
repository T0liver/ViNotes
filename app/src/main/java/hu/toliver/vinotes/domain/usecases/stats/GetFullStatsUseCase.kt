package hu.toliver.vinotes.domain.usecases.stats

import hu.toliver.vinotes.domain.model.FullStatsData
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.domain.repository.TasteRepository
import hu.toliver.vinotes.domain.repository.WineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.*
import javax.inject.Inject

class GetFullStatsUseCase @Inject constructor(
    private val wineRepository: WineRepository,
    private val tasteRepository: TasteRepository,
) {
    operator fun invoke(period: StatPeriod): Flow<FullStatsData> =
        combine(wineRepository.getAll(), tasteRepository.getAll()) { wines, tastes ->

            val periodStart: Date? = period.startDate()

            val totalWines = wines.size
            val totalTastings = tastes.size
            val avgRating = tastes.map { it.rating }.averageOrZero()
            val wouldDrinkPct = if (tastes.isEmpty()) 0
            else (tastes.count { it.wouldDrinkAgain } * 100 / tastes.size)

            val tastingDays = tastes
                .map { it.date.toLocalDateString() }
                .toSet()
            val currentStreak = calculateCurrentStreak(tastingDays)
            val longestStreak = calculateLongestStreak(tastingDays)

            val tastingsByDay = tastes
                .groupBy { it.date.toLocalDateString() }
                .mapValues { (_, v) -> v.size }

            val bucketBounds = listOf(50, 60, 70, 75, 80, 85, 90, 95)
            val ratingBuckets = bucketBounds.associateWith { lower ->
                val upper = bucketBounds.getOrElse(bucketBounds.indexOf(lower) + 1) { 101 }
                tastes.count { it.rating in lower until upper }
            }

            val wineMap = wines.associateBy { it.id }
            val colourDistribution = tastes
                .mapNotNull { wineMap[it.wineId]?.colour }
                .groupingBy { it }
                .eachCount()

            val topRegions = wines
                .filter { it.region.isNotBlank() }
                .groupingBy { it.region }
                .eachCount()
                .entries
                .sortedByDescending { it.value }
                .take(6)
                .map { it.key to it.value }

            val vintageDistribution = wines
                .groupingBy { it.year }
                .eachCount()
                .toSortedMap()

            val periodTastes = if (periodStart == null) tastes
            else tastes.filter { it.date >= periodStart }
            val tastingsInPeriod = periodTastes.size
            val avgRatingInPeriod = periodTastes.map { it.rating }.averageOrZero()

            FullStatsData(
                totalWines = totalWines,
                totalTastings = totalTastings,
                averageRating = avgRating,
                wouldDrinkAgainPct = wouldDrinkPct,
                currentStreak = currentStreak,
                longestStreak = longestStreak,
                tastingsByDay = tastingsByDay,
                ratingBuckets = ratingBuckets,
                colourDistribution = colourDistribution,
                topRegions = topRegions,
                vintageDistribution = vintageDistribution,
                tastingsInPeriod = tastingsInPeriod,
                avgRatingInPeriod = avgRatingInPeriod,
            )
        }

    private fun Date.toLocalDateString(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(this)
    }

    private fun List<Int>.averageOrZero(): Double =
        if (isEmpty()) 0.0 else average()

    private fun calculateCurrentStreak(days: Set<String>): Int {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US)
        var streak = 0
        val cal = Calendar.getInstance()
        while (true) {
            val key = sdf.format(cal.time)
            if (key in days) {
                streak++; cal.add(Calendar.DAY_OF_YEAR, -1)
            } else break
        }
        return streak
    }

    private fun calculateLongestStreak(days: Set<String>): Int {
        if (days.isEmpty()) return 0
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val sorted = days.map { sdf.parse(it)!! }.sorted()
        var longest = 1
        var current = 1
        for (i in 1 until sorted.size) {
            val diffMs = sorted[i].time - sorted[i - 1].time
            val diffDays = (diffMs / 86_400_000).toInt()
            if (diffDays == 1) {
                current++; longest = maxOf(longest, current)
            } else current = 1
        }
        return longest
    }
}

enum class StatPeriod {
    LAST_30_DAYS,
    LAST_3_MONTHS,
    THIS_YEAR,
    ALL_TIME;

    fun startDate(): Date? {
        val cal = Calendar.getInstance()
        return when (this) {
            LAST_30_DAYS -> {
                cal.add(Calendar.DAY_OF_YEAR, -30); cal.time
            }

            LAST_3_MONTHS -> {
                cal.add(Calendar.MONTH, -3); cal.time
            }

            THIS_YEAR -> {
                cal.set(Calendar.DAY_OF_YEAR, 1); cal.time
            }

            ALL_TIME -> null
        }
    }

    val label: String
        get() = when (this) {
            LAST_30_DAYS -> "30 days"
            LAST_3_MONTHS -> "3 months"
            THIS_YEAR -> "This year"
            ALL_TIME -> "All time"
        }
}

