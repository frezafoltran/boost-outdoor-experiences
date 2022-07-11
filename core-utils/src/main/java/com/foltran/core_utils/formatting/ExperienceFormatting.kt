package com.foltran.core_utils.formatting

import com.foltran.core_utils.extensions.round


private fun Double.meterPerSecondToMinPerKm(): Double = if(this < 0.5) 10.0 else 1.0 / (this * 0.06)

fun Double.meterPerSecondToMinPerKmPace(): String{

    val minutePerKm = meterPerSecondToMinPerKm()
    val minutes: Int = minutePerKm.toInt()
    val seconds: Int = ((minutePerKm - minutes.toDouble()) * 60.0).round(0).toInt()
    return "$minutes:${if(seconds < 10) "0" else ""}$seconds /km"
}

fun Double.meterToKm(): String {
    val kms = (this / 1000.0).round(2)
    return "$kms km"
}

//From Strava, cadence is given in steps per 30s/
fun Double.cadence() = "${(this * 2.0).toInt()} spm"

fun Int.heartRate() = "$this bpm"

fun Double.meter() = "$this m"

fun Int.timeFromSecondsToMinSeconds() = "${(this.toDouble()/60.0).toInt()} min ${this % 60}s"


