package com.liderMinas.PCP

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val username = intent.getStringExtra(EXTRA_MESSAGE)
        var saudacao = "Bem-vindo, ${username}"
        findViewById<TextView>(R.id.saudacao).apply { text = saudacao }

        var intent: Intent


        val buttonAE: Button = findViewById(R.id.apEmbalados)
        buttonAE.setOnClickListener {
            intent = Intent(this, ApontamentoEmbalados1::class.java)
                .apply {
                    putExtra(EXTRA_MESSAGE, username)}
            startActivity(intent)
        }



        val buttonAP: Button = findViewById(R.id.apPerdas)
        buttonAP.setOnClickListener {
            intent = Intent(this, ApontamentoPerdas::class.java)
                .apply {
                putExtra(EXTRA_MESSAGE, username)}
            startActivity(intent)
        }
    }
}
