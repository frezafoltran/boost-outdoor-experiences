package com.foltran.core_experience.shared.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExperienceHighlight(
    val startIndex: Int,
    val endIndex: Int
): Parcelable

@Parcelize
data class ExperienceHighlightSet(
    val experienceHighlightSetId: String,
    val highlights: List<ExperienceHighlight>
): Parcelable


fun getSampleExperienceHighlightSet(experienceId: String) = ExperienceHighlightSet(
    experienceHighlightSetId = "pacee$experienceId",
    highlights = listOf(
        ExperienceHighlight(startIndex = 100, endIndex = 350),
        ExperienceHighlight(startIndex = 500, endIndex = 600),
    )
)

fun getSampleExperienceHighlights(experienceId: String): List<ExperienceHighlightSet> = mutableListOf(
    ExperienceHighlightSet(
        experienceHighlightSetId = "pacee$experienceId",
        highlights = listOf(
            ExperienceHighlight(startIndex = 100, endIndex = 350),
            ExperienceHighlight(startIndex = 500, endIndex = 600),
        )
    )
)