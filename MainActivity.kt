package com.min.test0506

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.min.test0506.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private val urlList = listOf(
        "https://naver.com"
        // 추가 웹사이트 URL
    )
    private var currentUrlIndex = 0

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)

        // WebViewClient 설정
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view?.setOverScrollMode(WebView.OVER_SCROLL_NEVER)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // 다음 URL 로드
                loadNextUrl()
            }
        }

        webView.settings.apply {
            javaScriptEnabled = true // 자바스크립트 사용여부
            setSupportMultipleWindows(true) // 새창 띄우기 허용여부
            javaScriptCanOpenWindowsAutomatically = true // 자바스크립트가 window.open()을 사용할 수 있도록 설정
            loadWithOverviewMode = true // html의 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
            useWideViewPort = true // 화면 사이즈 맞추기 허용여부
            setSupportZoom(false) // 화면 줌 허용여부
            domStorageEnabled = true // DOM(html 인식) 저장소 허용여부

            // 파일 허용
            allowContentAccess = true
            allowFileAccess = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            loadsImagesAutomatically = true
        }


        // 쿠키 및 데이터 삭제
        GlobalScope.launch(Dispatchers.IO) {
            CookieManager.getInstance().removeAllCookies(null)
            WebStorage.getInstance().deleteAllData()
        }

        // 첫 번째 URL 로드
        loadNextUrlInBackground()
    }

    private fun loadNextUrlInBackground() {
        GlobalScope.launch(Dispatchers.IO) {
            loadNextUrl()
        }
    }

    private fun loadNextUrl() {
        if (currentUrlIndex < urlList.size) {
            val url = urlList[currentUrlIndex]
            currentUrlIndex++
            runOnUiThread {
                webView.loadUrl(url)
            }
        }
    }
}