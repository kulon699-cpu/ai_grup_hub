package com.aigroup.hub

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var web: WebView
    private lateinit var addressLabel: TextView
    private val prefs by lazy { getSharedPreferences("agh", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(12, 17, 28)
        window.navigationBarColor = Color.rgb(12, 17, 28)
        buildScreen()
        val saved = prefs.getString("server_url", null)
        if (saved.isNullOrBlank()) showServerDialog()
        else loadServer(saved)
    }

    private fun buildScreen() {
        val outer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(12, 17, 28))
        }
        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(16, 10, 10, 10)
            setBackgroundColor(Color.rgb(17, 25, 39))
        }
        val title = TextView(this).apply {
            text = "✳  AI Group Hub"
            textSize = 18f
            setTextColor(Color.WHITE)
            typeface = android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.BOLD)
        }
        addressLabel = TextView(this).apply {
            text = "Belum terhubung"
            textSize = 10f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER_VERTICAL
        }
        val change = Button(this).apply {
            text = "Server"
            isAllCaps = false
            setOnClickListener { showServerDialog() }
        }
        val titleBox = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(title)
            addView(addressLabel)
        }
        bar.addView(titleBox, LinearLayout.LayoutParams(0, -2, 1f))
        bar.addView(change)
        web = WebView(this).apply {
            setBackgroundColor(Color.rgb(12, 17, 28))
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.allowFileAccess = false
            settings.allowContentAccess = false
            settings.javaScriptCanOpenWindowsAutomatically = false
            settings.setSupportMultipleWindows(false)
            settings.safeBrowsingEnabled = true
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    val url = request?.url ?: return true
                    return url.scheme != "https" && url.scheme != "http"
                }
            }
        }
        outer.addView(bar)
        outer.addView(web, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(outer)
    }

    private fun showServerDialog() {
        val input = EditText(this).apply {
            hint = "https://alamat-server-kamu.com"
            setSingleLine(true)
            setText(prefs.getString("server_url", ""))
            inputType = android.text.InputType.TYPE_CLASS_TEXT or
                android.text.InputType.TYPE_TEXT_VARIATION_URI
        }
        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 8, 24, 0)
            addView(input)
            addView(TextView(this@MainActivity).apply {
                text = "Masukkan alamat backend AI Group Hub yang sudah online. Contoh: https://app-domain-kamu.com"
                textSize = 12f
                setTextColor(Color.DKGRAY)
                setPadding(0, 12, 0, 0)
            })
        }
        AlertDialog.Builder(this)
            .setTitle("Hubungkan AI Group Hub")
            .setView(box)
            .setCancelable(false)
            .setPositiveButton("Hubungkan") { _, _ ->
                var url = input.text.toString().trim().trimEnd('/')
                if (!url.startsWith("https://")) {
                    Toast.makeText(this, "Gunakan alamat HTTPS untuk keamanan.", Toast.LENGTH_LONG).show()
                    showServerDialog()
                } else {
                    prefs.edit().putString("server_url", url).apply()
                    loadServer(url)
                }
            }
            .setNegativeButton("Batal") { _, _ ->
                if (prefs.getString("server_url", null).isNullOrBlank()) finish()
            }
            .show()
    }

    private fun loadServer(url: String) {
        addressLabel.text = url.removePrefix("https://")
        web.loadUrl(url)
    }

    override fun onBackPressed() {
        if (::web.isInitialized && web.canGoBack()) web.goBack()
        else super.onBackPressed()
    }
}
