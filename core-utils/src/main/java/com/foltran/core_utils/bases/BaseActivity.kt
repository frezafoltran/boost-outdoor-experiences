package com.foltran.core_utils.bases


import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

open class BaseActivity(
    @LayoutRes val layoutId: Int = 0,
    vararg val modules: Module = emptyArray()
) : AppCompatActivity(layoutId) {

    constructor(): this(modules = emptyArray())
    constructor(layoutId: Int): this(layoutId = layoutId, modules = emptyArray())
    constructor(modules: List<Module>): this(modules = modules.toTypedArray())

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(modules.toList())
        super.onCreate(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(modules.toList())
    }
}