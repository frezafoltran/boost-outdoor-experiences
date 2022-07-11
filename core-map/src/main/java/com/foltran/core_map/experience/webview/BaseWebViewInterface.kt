package com.foltran.core_map.experience.webview

import android.util.Log
import android.webkit.JavascriptInterface
import com.foltran.core_map.core.utils.style.defaultLineColor
import com.foltran.core_map.core.utils.style.highlightLineColor
import com.google.gson.Gson

/**
 * Base class for JS interfaces used by experience.
 *
 * @param jsLabel is the key used in the .js file to use the interface
 * @param htmlPath is the path for .html file used by interface
 * @param jsPaths is the list of paths of .js files used by interface
 */
abstract class BaseWebViewInterface {
    abstract val jsLabel: String
    abstract val htmlPath: String
    abstract val jsPaths: List<String>

    fun Any.jsonParse() = Gson().toJson(this)

    @JavascriptInterface
    fun getDefaultLineColor() = defaultLineColor

    @JavascriptInterface
    fun getHighlightLineColor() = highlightLineColor

    @JavascriptInterface
    fun logString(s: String) {
        Log.i("JVFF", "log from js interface $s")
    }
}