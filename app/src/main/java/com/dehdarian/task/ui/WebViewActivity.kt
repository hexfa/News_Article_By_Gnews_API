package com.dehdarian.task.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dehdarian.task.R

/**
 * Web view activity for opening an article
 */
class WebViewActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val url= intent.getStringExtra("url")
        webView = findViewById(R.id.webview)
        true.also { webView.settings.javaScriptEnabled = it }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
        }
        if (url != null) {
            webView.loadUrl(url)
        }
    }
}