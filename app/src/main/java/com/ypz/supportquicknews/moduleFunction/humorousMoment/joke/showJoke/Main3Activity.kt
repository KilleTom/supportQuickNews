package com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.showJoke

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.http.SslError
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.transition.TransitionInflater
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.TextView
import com.jaeger.library.StatusBarUtil
import com.romainpiel.titanic.library.Titanic
import com.romainpiel.titanic.library.TitanicTextView
import com.ypz.supportquicknews.R
import com.ypz.supportquicknews.base.view.MyWebView

class Main3Activity : AppCompatActivity() {
    private var webView: MyWebView? = null
    private var isStart = false
    private var isTop = false
    private var backTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val transition = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_left)
        val exit = TransitionInflater.from(this).inflateTransition(android.R.transition.slide_right)
        window.enterTransition = transition
        window.exitTransition = exit
        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        val layout = findViewById<RelativeLayout>(R.id.tb_layout)
        val textView = findViewById<TextView>(R.id.tb_title)
        val loading = findViewById<TitanicTextView>(R.id.load)
        try {
            loading.typeface = com.ypz.supportquicknews.base.Typefaces.get(this, "Satisfy-Regular.ttf")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val titanic = Titanic()
        loading.visibility = View.GONE
        val translateAnimation = AnimationUtils.loadAnimation(this, R.anim.web_tb) as TranslateAnimation
        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                layout.visibility = View.GONE
                isStart = false
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        findViewById<View>(R.id.tb_black).setOnClickListener { view -> finish() }
        StatusBarUtil.setColor(this, Color.parseColor("#ff33b5e5"), 85)
        val url = intent.getStringExtra("url")
        textView.text = intent.getStringExtra("title")
        webView = findViewById(R.id.web)
        if (intent.getStringExtra("title") == "在线电影票") layout.visibility = View.GONE
        else
            webView!!.setMyWebViewScrollCallback { dx, dy ->
                Log.i("ypz", dy.toString() + "dy")
                if (dy > 0) {
                    Log.w("ypz", "isStart$isStart")
                    Log.w("ypz", "isTop$isTop")
                    if (!isStart && !isTop) {
                        runOnUiThread {
                            layout.startAnimation(translateAnimation)
                            isStart = true
                        }
                    }
                    if (isTop) isTop = false
                } else if (dy < 0) {

                } else {
                    layout.visibility = View.VISIBLE
                    isTop = true
                }
            }
        val webSettings = webView!!.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadsImagesAutomatically = true
        //缩放操作
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.domStorageEnabled = true
        /*
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        //其他细节操作
        webSettings.setAllowFileAccess(true); //设置可以访问文件
         //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式*/
        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }
        }
        if (!TextUtils.isEmpty(url)) webView!!.loadUrl(url)

        webView!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                Log.i("ypz", newProgress.toString() + "")
                if (newProgress == 100) {
                    webView!!.visibility = View.VISIBLE
                    loading.visibility = View.GONE
                    titanic.cancel()
                } else {
                    webView!!.visibility = View.GONE
                    loading.visibility = View.VISIBLE
                    if (!titanic.isStart) titanic.start(loading)
                }

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (System.currentTimeMillis() - backTime <= 400) {
            finish()
        } else {
            backTime = System.currentTimeMillis()
            if (webView!!.canGoBack())
                webView!!.canGoBack()
            else
                finish()
        }

    }
}
