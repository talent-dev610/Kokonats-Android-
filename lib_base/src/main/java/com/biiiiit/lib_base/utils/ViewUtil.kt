package com.biiiiit.lib_base.utils

import android.os.Build
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.biiiiit.lib_base.BuildConfig

/**
 * @Author yo_hack
 * @Date 2021.11.20
 * @Description
 **/

fun setDefaultWebSettings(webView: WebView?, client: WebViewClient = WebViewClient()) {
    if (webView == null) {
        return
    }
    val webSettings = webView.settings
    //5.0以上开启混合模式加载
    webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    webSettings.loadWithOverviewMode = true
    webSettings.useWideViewPort = true
    //允许js代码
    webSettings.javaScriptEnabled = true
    webSettings.javaScriptCanOpenWindowsAutomatically = true
    //允许SessionStorage/LocalStorage存储
    webSettings.domStorageEnabled = true
    //禁用放缩
    webSettings.displayZoomControls = false
    webSettings.builtInZoomControls = false
    //禁用文字缩放
    webSettings.textZoom = 100
    webSettings.cacheMode = WebSettings.LOAD_DEFAULT
    //10M缓存，api 18后，系统自动管理。
    webSettings.setAppCacheMaxSize(100 * ONE_MB)
    //允许缓存，设置缓存位置
    webSettings.setAppCachePath(getWebViewCacheDir().absolutePath)
    webSettings.setAppCacheEnabled(true)
    //允许WebView使用File协议
    webSettings.allowFileAccess = true
    //不保存密码
    webSettings.savePassword = false
    //设置UA
    webSettings.userAgentString += " plat=Android"
    //自动加载图片
    webSettings.loadsImagesAutomatically = true


    webSettings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    webSettings.blockNetworkImage = false

    webView.webViewClient = client
}
