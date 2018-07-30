package com.ypz.supportquicknews.moduleFunction.humorousMoment.joke.showJoke

import android.net.http.SslError
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.webkit.*
import com.r0adkll.slidr.Slidr
import com.ypz.supportquicknews.R
import kotlinx.android.synthetic.main.activity_we_chat_joke.*

class WeChatJokeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_we_chat_joke)
        webview.settings.blockNetworkImage = true
        webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webview.settings.pluginsEnabled = true
        webview.settings.pluginState = com.tencent.smtt.sdk.WebSettings.PluginState.ON
        try {
            val webClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    runOnUiThread({view!!.loadUrl(request!!.url.toString())})
                    return true
                }

                override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                    super.onReceivedSslError(view, handler, error)
                    handler!!.proceed()
                }

            }
            Slidr.attach(this)

        } catch (e: Exception) {
            Log.i("ypz", e.message)
        }
        /*webview.setMyWebViewScrollCallback { dx, dy ->
            if (dy > 0) {
                tb_layout.visibility = View.GONE
            } else {
                tb_layout.visibility = View.VISIBLE
            }
            Log.i("ypz", "dx" + dx + "\ndy" + dy)
        }*/
        Log.i("ypz", intent.getStringExtra("url"))
        runOnUiThread({ webview.loadUrl("https://www.baidu.com") })
        tb_title.text = intent.getStringExtra("title")
        tb_black.setOnClickListener { finish() }
        /*webview.loadUrl("http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0716/3192.html")*/
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
