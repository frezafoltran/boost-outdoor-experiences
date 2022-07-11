package com.foltran.feature_experience.core.presentation.chart


data class ExperienceChartCommands(
    val onClickExpandGraph: () -> Unit,
    val spinnerCategories: List<String>,
    val onSelectCategory: (categoryIndex: Int) -> Unit,
)