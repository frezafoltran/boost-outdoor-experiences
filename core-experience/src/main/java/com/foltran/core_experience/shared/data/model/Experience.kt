package com.foltran.core_experience.shared.data.model

import android.os.Parcelable
import com.foltran.core_utils.date.formatToUTC
import com.foltran.core_utils.date.getRandomDate
import com.foltran.core_utils.date.randDoubleBetween
import kotlinx.parcelize.Parcelize


fun randomSpeed() = randDoubleBetween(1, 4)

val tempComparableExperiences = listOf<ExperienceSummary>(
    ExperienceSummary(
        averageSpeed = randomSpeed(),
        startDate = getRandomDate().formatToUTC()
    ),
    ExperienceSummary(
        averageSpeed = randomSpeed(),
        startDate = getRandomDate().formatToUTC()
    ),
    ExperienceSummary(
        averageSpeed = randomSpeed(),
        startDate = getRandomDate().formatToUTC()
    ),
    ExperienceSummary(
        averageSpeed = randomSpeed(),
        startDate = getRandomDate().formatToUTC()
    )
)

@Parcelize
data class Experience(
    val athlete: Athlete,
    val athleteCount: Int,
    val averageCadence: Double,
    val averageHeartRate: Double,
    val averageSpeed: Double,
    val averageTemp: Int,
    val bestEfforts: List<BestEffort>,
    val calories: Int,
    val commentCount: Int,
    val distance: Double,
    val elapsedTime: Int,
    val elevHigh: Double,
    val elevLow: Double,
    val endLatLng: List<Double>,
    val hasHeartRate: Boolean,
    val id: String,
    val laps: List<Lap>,
    val map: Map,
    val maxHeartRate: Int,
    val maxSpeed: Double,
    val movingTime: Int,
    val name: String,
    val photoCount: Int,
    val segmentEfforts: List<SegmentEffort>,
    val startDate: String,
    val startDateLocal: String,
    val startLatitude: Double,
    val startLatLng: List<Double>,
    val startLongitude: Double,
    val timezone: String,
    val totalElevationGain: Double,
    val type: String,
    val utcOffset: Int,
    val visibility: String? = null,
    val workoutType: String? = null,
    val locationCountry: String?,
    val locationState: String?,
    val locationCity: String?,
    val gearId: String? = null,
    val likeCount: Int? = null,
    val photos: List<Photo>? = null,
    val comparableExperiences: List<ExperienceSummary>? = tempComparableExperiences,
    val experienceHighlights: List<ExperienceHighlightSet>? = getSampleExperienceHighlights(id)
) : Parcelable