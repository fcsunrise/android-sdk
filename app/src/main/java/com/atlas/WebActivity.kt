package com.atlas

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : AppCompatActivity() {

    companion object {
        const val WEB_URL = "webUrl"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val webUrl   = intent.getStringExtra(WEB_URL)

        initWebView()
        loadWeb(webUrl!!)
    }

    private fun initWebView() {
        webView.webViewClient = WebViewClient()
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
    }

    private fun loadWeb(webUrl: String){
        webView.loadUrl(webUrl)
    }
}