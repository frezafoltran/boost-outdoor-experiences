package com.foltran.feature_profile.feed.presentation.adapter

import com.foltran.core_experience.shared.data.model.ExperienceItem

val randomDateHeaders = listOf(
    "May 2021",
    "December 2020"
)

fun List<ExperienceItem>.toListByDate(): List<List<ExperienceItem>> {
    val out = ArrayList<List<ExperienceItem>>()

    out.add(this.subList(0, 3))
    out.add(this.subList(3, 6))

    return out
}