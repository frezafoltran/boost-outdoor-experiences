package com.foltran.core_map.core.presentation.model

import androidx.lifecycle.MutableLiveData

sealed class ExperiencePlayerStream(
    open val curIndex: MutableLiveData<Int>,
    open val curValStr: MutableLiveData<String>
) {
    data class Elevation(
        override val curIndex: MutableLiveData<Int> = MutableLiveData(),
        override val curValStr: MutableLiveData<String> = MutableLiveData(),
        val stream: MutableLiveData<List<Double>> = MutableLiveData()
    ) : ExperiencePlayerStream(curIndex, curValStr) {

        fun setStream(stream: List<Double>?) {
            this.stream.value = getSimplifiedStream(stream.orEmpty())
        }
    }

    data class Coordinates(
        override val curIndex: MutableLiveData<Int> = MutableLiveData(),
        override val curValStr: MutableLiveData<String> = MutableLiveData(),
        val stream: MutableLiveData<List<List<Double>>> = MutableLiveData()
    ) : ExperiencePlayerStream(curIndex, curValStr) {

        fun setStream(stream: List<List<Double>>?) {
            this.stream.value = getSimplifiedStream(stream.orEmpty())
        }
    }

    data class Speed(
        override val curIndex: MutableLiveData<Int> = MutableLiveData(),
        override val curValStr: MutableLiveData<String> = MutableLiveData(),
        val stream: MutableLiveData<List<Double>> = MutableLiveData()
    ) : ExperiencePlayerStream(curIndex, curValStr) {

        var maxSpeed: Double = 0.0
        var minSpeed: Double = 0.0

        val curRatioOfSpeed = MutableLiveData<Double>() //between 0 and 1, 0 being minSpeed and 1 maxSpeed

        fun setStream(stream: List<Double>?) {
            maxSpeed = stream?.maxOrNull() ?: 0.0
            minSpeed = stream?.minOrNull() ?: 0.0

            this.stream.value = getSimplifiedStream(stream.orEmpty())
        }
    }

    data class Distance(
        override val curIndex: MutableLiveData<Int> = MutableLiveData(),
        override val curValStr: MutableLiveData<String> = MutableLiveData(),
        val stream: MutableLiveData<List<Double>> = MutableLiveData()
    ) : ExperiencePlayerStream(curIndex, curValStr) {

        var distanceDelta: Double = 0.0
        var maxDistance: Double = 0.0
        var minDistance: Double = 0.0
        val curProgress = MutableLiveData<Int>()

        fun setStream(stream: List<Double>?) {
            this.stream.value = getSimplifiedStream(stream.orEmpty())

            maxDistance = this.stream.value?.maxOrNull() ?: 0.0
            minDistance = (this.stream.value?.minOrNull() ?: 0.0)
            distanceDelta = maxDistance - minDistance

        }
    }

    fun <T>getSimplifiedStream(stream: List<T>): List<T> {
        return stream
//        val out = ArrayList<T>()
//        val step = getStepSize(stream)
//
//        for (i in (0 until streamSize)){
//            out.add(stream[(step * i).toInt()])
//        }
//
//        out.add(stream[stream.size - 1])
//
//        return out
    }


    private fun <T>getStepSize(stream: List<T>) = (stream.size-1).toDouble()/(streamSize -1).toDouble()

    companion object {
        const val streamSize = 500  //actual size will be streamSize + 1 since we add the last element
    }
}