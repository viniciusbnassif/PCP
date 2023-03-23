package com.liderMinas.PCP

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.evrencoskun.tableview.TableView
import com.evrencoskun.tableview.adapter.AbstractTableAdapter
import com.evrencoskun.tableview.adapter.*
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.liderMinas.PCP.R
import java.util.Collections.EMPTY_LIST
import java.util.Collections.list


class Estatistica : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estatistica)

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
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowCustomEnabled(false)

        }
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_baseline_logout_24)

        var refresh = findViewById<ImageView>(R.id.refresh)

        refresh.setOnClickListener{
            myWebView.reload()
        }

        var close = findViewById<ImageView>(R.id.close)
        close.setOnClickListener{
            finish()
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