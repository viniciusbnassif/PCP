package com.liderMinas.PCP

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.ActionBarContainer
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.*
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.liderMinas.PCP.R
import java.util.Collections.EMPTY_LIST
import java.util.Collections.list


class Estatistica : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    = inflater.inflate(R.layout.activity_estatistica, container, false).apply {

        /*window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }*/

        val progress = findViewById<CircularProgressIndicator>(R.id.pIndicator)
        progress.visibility = VISIBLE

        val myWebView: WebView = findViewById(R.id.webView)
        myWebView.webViewClient = WebViewClient()
        myWebView.clearCache(true)
        myWebView.clearHistory()
        myWebView.settings.javaScriptEnabled = true
        myWebView.loadUrl("http://192.168.1.10/report_server/Pages/ReportViewer.aspx?%2fLiderMinas%2fRLM0077&rs:Command=Render")
        progress.visibility = INVISIBLE


        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        var refresh = findViewById<ImageView>(R.id.refresh)

        refresh.setOnClickListener{
            myWebView.reload()
        }
        


        /*var contentContainer = findViewById<TableView>(R.id.content_container)

        var ch = "test"
        var rh = "test"
        var c = "test"

        var list =


        var adapter = contentContainer.setAdapter()

        contentContainer.setAdapter(adapter)*/
    }
}