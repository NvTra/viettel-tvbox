package com.viettel.tvbox.widgets

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun PlayBlacknutWebViewScreen(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                // WebView settings similar to PlayGameActivity
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
//                    databaseEnabled = true
                    allowContentAccess = true
                    allowFileAccess = true
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    javaScriptCanOpenWindowsAutomatically = true
                    mediaPlaybackRequiresUserGesture = false
                    setSupportMultipleWindows(true)
                }
                webChromeClient = android.webkit.WebChromeClient()
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: android.webkit.WebResourceRequest?
                    ): Boolean {
                        return false
                    }
                }
                loadUrl(url)
            }
        },
        update = { webView ->
            if (webView.url != url) {
                webView.loadUrl(url)
            }
        }
    )
}