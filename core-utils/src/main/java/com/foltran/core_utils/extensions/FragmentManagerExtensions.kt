package com.foltran.core_utils.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.lang.Exception
import java.lang.RuntimeException
import android.R




fun FragmentManager.replace(id: Int, fragment: Fragment, tag: String? = null) {
    this.beginTransaction().apply {
        replace(id, fragment)
        //addToBackStack(tag)
        commit()
    }
}


fun FragmentManager.replaceOnTheGo(id: Int, fragment: Fragment, tag: String? = null) {
    this.beginTransaction().apply {
        remove(fragment)
        val newInstance = recreateFragment(fragment)
        add(id, newInstance)
        commit()
    }
}


fun FragmentManager.recreateFragment(f: Fragment): Fragment {
    return try {
        val savedState: Fragment.SavedState = this.saveFragmentInstanceState(f)!!
        val newInstance = f.javaClass.newInstance()
        newInstance.setInitialSavedState(savedState)
        newInstance
    } catch (e: Exception) // InstantiationException, IllegalAccessException
    {
        throw RuntimeException("Cannot reinstantiate fragment " + f.javaClass.name, e)
    }
}
