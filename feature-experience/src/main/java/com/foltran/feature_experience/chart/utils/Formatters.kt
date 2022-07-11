package com.foltran.feature_experience.chart.utils

fun List<Double>?.mapTakeEveryIntervaled(): List<Double> = ArrayList<Double>().let {
    if (this != null) {
        for (i in 0..this.size){
            if (i % 10 == 0) it.add(this[i])
        }
    }
    it
}

fun paceJSFormatter() = """
function () {
        return '' + parseInt(16.66666666667/this.y) 
        + ':' 
        + parseInt((16.66666666667/this.y - parseInt(16.66666666667/this.y)) * 60.0)
        + ' min/km';
        }
"""

fun heartRateJSFormatter() = """
function () {
        return '' + this.y + 'bpm'
        }
"""

fun cadenceRateJSFormatter() = """
function () {
        return '' + this.y + 'spm'
        }
"""

fun elevationRateJSFormatter() = """
function () {
        return '' + this.y + 'm'
        }
"""