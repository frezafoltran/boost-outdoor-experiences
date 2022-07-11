package com.foltran.core_utils.observable

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class ActionLiveData<T> : MutableLiveData<T>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasObservers()) {
            throw Throwable("Only one observer at a time may subscribe to a ActionLiveData")
        }

        super.observe(owner, Observer { data ->
            if (data == null) return@Observer
            observer.onChanged(data)
            value = null
        })
    }

    // Just a nicely named method that wraps setting the value
    fun sendAction(data: T) {
        value = data
    }

    fun sendActionAsync(data: T){
        postValue(data)
    }
}