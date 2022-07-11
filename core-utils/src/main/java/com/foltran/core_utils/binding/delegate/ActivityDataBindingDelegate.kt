package com.foltran.core_utils.binding.delegate

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private const val CONTAINER_VIEW = 0

class ActivityDataBindingDelegate<T : ViewDataBinding> : ReadOnlyProperty<AppCompatActivity, T> {

    private lateinit var binding: T

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): T {
        if (!thisRef.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("OnCreate is not Called yet")
        } else if (::binding.isInitialized.not()) {
            binding = DataBindingUtil
                .bind(thisRef.findViewById<ViewGroup>(android.R.id.content)[CONTAINER_VIEW])!!
            binding.lifecycleOwner = thisRef
        }
        return binding
    }
}

fun <T : ViewDataBinding> AppCompatActivity.dataBinding() =
    ActivityDataBindingDelegate<T>()