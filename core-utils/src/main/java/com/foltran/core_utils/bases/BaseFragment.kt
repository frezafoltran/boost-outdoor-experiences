package com.foltran.core_utils.bases

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

open class BaseFragment(
    @LayoutRes val layoutId: Int = 0,
    vararg val modules: Module = emptyArray()
): Fragment(layoutId) {

    constructor(): this(modules = emptyArray())
    constructor(layoutId: Int): this(layoutId = layoutId, modules = emptyArray())
    constructor(layoutId: Int, modules: List<Module>): this(layoutId = layoutId, modules = modules.toTypedArray())

    override fun onAttach(context: Context) {
        loadKoinModules(modules.toList())
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
        unloadKoinModules(modules.toList())
    }
}