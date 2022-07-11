package com.foltran.core_experience.shared.data.model

import android.os.Parcelable
import com.foltran.core_utils.date.utcToDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExperienceSummary(
    val averageSpeed: Double,
    val startDate: String
) : Parcelable

class CompareDates {
    companion object : Comparator<ExperienceSummary> {
        override fun compare(a: ExperienceSummary, b: ExperienceSummary) =
            a.startDate.utcToDate().compareTo(b.startDate.utcToDate())
    }
}

fun List<ExperienceSummary>.sortByDate() = this.sortedWith(CompareDates)
