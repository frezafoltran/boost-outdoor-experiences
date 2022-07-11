package com.foltran.core_map.core.models

data class CoordinateModel(
    val latitude: Double,
    val longitude: Double
)

fun List<CoordinateModel>.toMapboxFormat() = this.map {
    listOf(it.longitude, it.latitude)
}

fun List<CoordinateModel>.toDoubleFormat() = this.map {
    listOf(it.latitude, it.longitude)
}

fun List<List<Double>>.toCoordinateModel() = this.map {
    CoordinateModel(
        latitude = it[0],
        longitude = it[1]
    )
}
