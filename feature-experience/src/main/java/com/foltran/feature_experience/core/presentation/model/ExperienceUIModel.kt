package com.foltran.feature_experience.core.presentation.model

import androidx.lifecycle.MutableLiveData
import com.foltran.core_utils.formatting.*
import com.foltran.feature_experience.R
import com.foltran.core_experience.shared.data.model.Experience
import com.foltran.core_experience.shared.data.model.Lap
import com.foltran.feature_experience.feed.data.pictureUrlsSample

fun Experience.toUIModel() = ExperienceUIModel(this)


data class ExperienceDataFieldPager(
    val content: List<Pair<Int, String>>,
    val opacity: MutableLiveData<Double> = MutableLiveData(1.0),
    val isTabBarVisible: MutableLiveData<Boolean> = MutableLiveData(true)
)

class ExperienceUIModel(val item: Experience? = null) {

    val location: String = item?.locationCity ?: ""
    val username: String = item?.athlete?.id ?: ""
    val name = item?.name

    val nameLabelResId: Int = R.string.experience_label_segment

    val type: Int = R.string.experience_type_run

    val typeIcon: Int = R.drawable.ic_run_dark_small

    val pictureUrls: List<String> = pictureUrlsSample

    val dataFieldsForPagerTop: List<Pair<Int, String>> = item?.let {
        listOf(
            Pair(
                R.string.experience_label_pace,
                item.averageSpeed.meterPerSecondToMinPerKmPace()
            ),
            Pair(
                R.string.experience_label_distance,
                item.distance.meterToKm()
            ),
            Pair(
                R.string.experience_label_cadence,
                item.averageCadence.cadence()
            )
        )
    } ?: listOf()

    val dataFieldsForPagerBottom: List<Pair<Int, String>> = item?.let {
        listOf(
            Pair(
                R.string.experience_label_best_pace,
                item.maxSpeed.meterPerSecondToMinPerKmPace()
            ),
            Pair(
                R.string.experience_label_max_heart_rate,
                item.maxHeartRate.heartRate()
            ),
            Pair(
                R.string.experience_label_elevation_gain,
                item.totalElevationGain.meter()
            )
        )
    } ?: listOf()

    val dataFieldsForPager: List<Pair<Int, String>> =
        dataFieldsForPagerTop + dataFieldsForPagerBottom

}
