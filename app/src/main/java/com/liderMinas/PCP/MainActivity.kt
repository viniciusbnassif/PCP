package com.liderMinas.PCP

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.api.Context
import com.liderMinas.PCP.database.*
import com.liderMinas.PCP.database.Sync


//import com.liderMinas.PCP.database.connectMSSQL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SQLiteHelper(this);

        MaterialAlertDialogBuilder(this)
            .setTitle("Hello Amazon")
            .setMessage("This app is for internal use only, across most than one company. For this reason, a login is necessary and the sync will only work on the correct wi-fi network. Anyway, you can use the username 'test' and the password 'test' to look into the app." +
                    "(The app is in Brazilian Portuguese for now, because the company where it will be used are Brazilian)")
            .setNegativeButton("Dismiss") { dialog, which ->
                dialog.dismiss()
            }.show()



        var db = SQLiteHelper(this)

        var sync = Sync()




        val user = findViewById<EditText>(R.id.editTextUsername)
        val userView = findViewById<TextInputLayout>(R.id.viewUser)

        val pw = findViewById<EditText>(R.id.editTextPassword)
        val pwView = findViewById<TextInputLayout>(R.id.viewPassword)

        var progress = findViewById<ProgressBar>(R.id.progress)
        progress.setVisibility(INVISIBLE)





        //Criar barra de ações
        val toolbar = findViewById<Toolbar?>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowCustomEnabled(false)
        }


        //Exibir número de versão + revisão
        var versionCode = BuildConfig.VERSION_CODE
        var versionName = BuildConfig.VERSION_NAME
        var version = "Versão: $versionName. Revisão: $versionCode"

        var query: String





        //val user = String
        //val pw = String

        var elementsOnLogin = findViewById<LinearLayout>(R.id.elementsOnLogin)



        fun connectionView(): String{
            var result = Sync().testConnection()

            if (result == "Falha" ) {
                var xyz = Snackbar.make(findViewById(R.id.clMA),
                    getString(R.string.connectionErroResult1),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(Color.parseColor("#741919"))
                    .setTextColor(Color.WHITE)
                    .setActionTextColor(Color.WHITE)
                    .setAction("OK"){}.show()
                progress.setVisibility(INVISIBLE)
                return result
            } else if (result == "Sem Conexão") {
                progress.setVisibility(INVISIBLE)
                return result
            }else {
                return "Sucesso"
            }
        }

        fun authUser(ctxt: android.content.Context) {
            var result = connectionView()
            if (result == "Sucesso") {
                var validation = confirmUnPw(user.text.toString(), pw.text.toString())
                if (validation == 201) {
                    Snackbar.make(
                        elementsOnLogin,
                        "Usuário autenticado \nAtualizando tabelas...",
                        Snackbar.LENGTH_LONG
                    ).show()
                    query = "DELETE FROM Usuario WHERE username = '${user.text}'"
                    db.externalExecSQL(query)
                    query =
                        "INSERT INTO Usuario(username, password) VALUES ('${user.text}', '${pw.text}')"
                    db.externalExecSQL(query)
                    sync.sync(0, ctxt)

                    var username = user.text.toString()
                    var mainMenu = Intent(this, MainMenu::class.java).apply {
                        putExtra(EXTRA_MESSAGE, username)
                    }
                    startActivity(mainMenu)

                    finish()
                } else if (validation == 401) {

                    userView.setError(" ")
                    pwView.setError("Nome de usuário ou senha incorretos")
                    pw.setText("")
                    Snackbar.make(
                        elementsOnLogin,
                        "Nome de usuário e/ou senha incorretos",
                        Snackbar.LENGTH_LONG
                    ).show()
                    progress.setVisibility(INVISIBLE)
                }
            }
            if (result == "Falha" || result == "Sem Conexão") {
                query = "SELECT username FROM Usuario WHERE username = '${user.text}' AND password = '${pw.text}'"
                var auth = db.externalExecSQLSelect(user.text.toString(), pw.text.toString())
                //Log.d("Debug", "Cursor = $cursor")
                if (auth == true) {
                    var username = user.text.toString()
                    var mainMenu = Intent(this, MainMenu::class.java).apply {
                        putExtra(EXTRA_MESSAGE, username)}
                    startActivity(mainMenu)
                    finish()
                } else if (auth == false) {
                    Snackbar.make(
                        elementsOnLogin,
                        "Não foi possivel conectar ao servidor. Verifique as configurações de rede e tente novamente.",
                        Snackbar.LENGTH_LONG
                    ).setAction("Abrir Configurações") {
                        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    }.show()
                }
                progress.setVisibility(INVISIBLE)
            }
        }



        pw.setOnEditorActionListener{ v, actionId, event ->
            progress.setVisibility(VISIBLE)
            authUser(this)
            true
        }

        val button: Button = findViewById(R.id.loginscreen_login)
        button.setOnClickListener {
            progress.setVisibility(VISIBLE)
            authUser(this)
        }

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_actv, menu)
        return true
    }
    var versionCode = BuildConfig.VERSION_CODE
    var versionName = BuildConfig.VERSION_NAME

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.versionView -> {
            // User chose the "Settings" item, show the app settings UI...

            Toast.makeText(applicationContext,
                "Versão: $versionName.", Toast.LENGTH_SHORT).show()
            true
        }

        R.id.closeApp -> {
            finish()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }




}