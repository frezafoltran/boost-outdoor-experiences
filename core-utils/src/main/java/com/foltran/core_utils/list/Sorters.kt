package com.foltran.core_utils.list

import kotlin.math.abs


fun List<Double>.findClosestIndexToValue(value: Double): Int {

    var curProximity = Double.MAX_VALUE
    var bestIndex = 0
    forEachIndexed { index, d ->

        abs(d - value).let {
            if (it < curProximity){
                bestIndex = index
                curProximity = it
            }
        }
    }
    return bestIndex
}

fun List<Int>.findClosestIndexToValue(value: Int): Int {

    var curProximity = Int.MAX_VALUE
    var bestIndex = 0
    forEachIndexed { index, d ->

        abs(d - value).let {
            if (it < curProximity){
                bestIndex = index
                curProximity = it
            }
        }
    }
    return bestIndex
}