package com.foltran.feature_experience.feed.presentation

import android.content.Context
import com.foltran.core_map.images.utils.getStaticMap
import com.foltran.core_ui.components.RoundImageComponentParams
import com.foltran.core_utils.formatting.meterPerSecondToMinPerKmPace
import com.foltran.core_utils.formatting.meterToKm
import com.foltran.feature_experience.R
import com.foltran.core_experience.shared.data.model.ExperienceItem
import com.foltran.core_experience.feed.presentation.model.ExperienceFeedItem


data class MapPreviewParams(var polyline: String, var height: Int? = null, var width: Int? = null)

fun ExperienceFeedItem.ExperienceListItem.toUIModel(context: Context) = ItemUI(this.item, context)

class ItemUI(val item: ExperienceItem, val context: Context) {


    val imageUrls: List<String> = listOf(
        "https://i.ibb.co/pz0S12s/Screenshot-20220224-130345-Maps.jpg",
        "https://i.ibb.co/7X3sZkh/Screenshot-20220224-135221-Maps.jpg"
    )

    val date = "Sunday at 8:14"
    fun getLocation(context: Context) =
        context.getString(
            R.string.experience_location_tag,
            item.locationCity ?: "",
            "12"
        )

    val title = item.name

    val type: Int = R.string.experience_type_run

    val typeIcon: Int = R.drawable.ic_run_dark_small

    val temp = item.map.summaryPolyline ?: item.map.polyline ?: "~m~nCvux{GtAgD|C_Cd@c@sD]yCUm@Is@xFw@tGhBk@hAcA"
    val previewMap: String = getStaticMap(temp, null, null)

    val distanceParsed = item.distance.meterToKm()
    val distanceLabel = R.string.experience_label_distance

    val paceParsed = item.averageSpeed.meterPerSecondToMinPerKmPace()
    val paceLabel = R.string.experience_label_pace

    val profileImage = RoundImageComponentParams(drawableId = R.drawable.ic_logo_white, hasBorder = true)
}



