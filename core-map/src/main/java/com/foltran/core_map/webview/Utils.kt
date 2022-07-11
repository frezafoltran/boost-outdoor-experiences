package com.foltran.core_map.webview

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import android.webkit.WebView
import android.webkit.WebViewClient
import com.foltran.core_map.experience.webview.BaseWebViewInterface
import java.io.InputStream

fun Context.injectJS(webView: WebView, filePath: String) {
    try {
        val inputStream: InputStream = assets.open(filePath)
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        inputStream.close()
        val encoded = Base64.encodeToString(buffer, Base64.NO_WRAP)
        webView.loadUrl(
            "javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var script = document.createElement('script');" +
                    "script.type = 'text/javascript';" +
                    "script.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(script)" +
                    "})()"
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@SuppressLint("JavascriptInterface")
fun Context.loadWebView(
    webView: WebView,
    mapPlayerWebViewInterface: BaseWebViewInterface,
    callbackOnLoaded: () -> Unit = {}
) {

    val mWebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mapPlayerWebViewInterface.jsPaths.forEach {
                injectJS(view, it)
            }
            callbackOnLoaded()
        }
    }

    with(webView) {
        webViewClient = mWebViewClient
        settings.javaScriptEnabled = true

        settings.allowFileAccessFromFileURLs = true;
        settings.allowUniversalAccessFromFileURLs = true;
//        settings.domStorageEnabled = true
//        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
//        settings.pluginState = WebSettings.PluginState.ON_DEMAND
//        settings.cacheMode = LOAD_CACHE_ELSE_NETWORK
//
//        setLayerType(View.LAYER_TYPE_HARDWARE, null)

        addJavascriptInterface(mapPlayerWebViewInterface, mapPlayerWebViewInterface.jsLabel)
        loadUrl("file:///android_asset/${mapPlayerWebViewInterface.htmlPath}")
    }
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.loadMap(context: Context) {
    val mWebViewClient = object : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            context.injectJS(view, "js/compose_map.js")
        }
    }

    webViewClient = mWebViewClient
    settings.javaScriptEnabled = true
    settings.allowFileAccessFromFileURLs = true;
    settings.allowUniversalAccessFromFileURLs = true;
    loadUrl("file:///android_asset/html/compose_map.html")

}

fun Context.clearWebView(webView: WebView) {
    webView.loadUrl("about:blank");
}