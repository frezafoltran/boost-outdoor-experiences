package com.foltran.feature_experience.core.domain.model

import com.foltran.core_experience.shared.data.model.ExperienceHighlight
import com.foltran.core_experience.shared.data.model.ExperienceHighlightSet
import com.foltran.core_map.experience.models.highlight.PathHighlight
import com.foltran.core_map.experience.models.highlight.PathHighlightSet

fun ExperienceHighlight.toMapExperienceHighlight() =
    com.foltran.core_map.webview.ExperienceHighlight(
        startIndex = this.startIndex,
        endIndex = this.endIndex
    )

fun ExperienceHighlightSet.toMapExperienceHighlightSet() =
    com.foltran.core_map.core.utils.extensions.ExperienceHighlightSet(
        experienceHighlightSetId = this.experienceHighlightSetId,
        highlights = this.highlights.map { it.toMapExperienceHighlight() }
    )

fun ExperienceHighlight.toPathHighlight() =
    PathHighlight(
        startIndex = this.startIndex,
        endIndex = this.endIndex
    )

fun ExperienceHighlightSet.toPathHighlightSet() =
    PathHighlightSet(
        id = this.experienceHighlightSetId,
        highlights = this.highlights.map { it.toPathHighlight() }
    )