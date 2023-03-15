package com.liderMinas.PCP

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.compose.ui.platform.InspectableModifier
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.liderMinas.PCP.database.Sync
import org.w3c.dom.Text

class MainMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)



        val toolbar = findViewById<Toolbar?>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowCustomEnabled(false)
        }

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

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setIcon(R.drawable.ic_baseline_logout_24_black)
            .setTitle("Você escolheu sair...")
            .setMessage("O aplicativo será encerrado e será necessário efetuar login novamente. \nDeseja sair mesmo assim?")
            .setNeutralButton("Retornar ao menu principal") { dialog, which ->
                dialog.dismiss()
            }
            .setNegativeButton("Fazer logout") { dialog, which ->
                super.onBackPressed()
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            .setPositiveButton("Fechar app") { dialog, which ->
                finish()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    var sync = Sync()
    //var btnSync = findViewById<MaterialButton>(R.id.syncBtn)

    @SuppressLint("ResourceAsColor")
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.syncBtn -> {
            // User chose the "Settings" item, show the app settings UI...

            //btnSync.setBackgroundColor(R.color.black)
            sync.sync(0, this)
            //btnSync.setBackgroundColor(R.color.blue_700)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    /* When the activity is destroyed then close the cursor as it will not be used again */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
