package com.liderMinas.PCP

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.api.Context
import com.liderMinas.PCP.database.*
import com.liderMinas.PCP.database.Sync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.SQLException


//import com.liderMinas.PCP.database.connectMSSQL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /*window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }*/


        setContentView(R.layout.activity_main)
        SQLiteHelper(this);
        var db = SQLiteHelper(this)

        var sync = Sync()




        val user = findViewById<EditText>(R.id.editTextUsername)
        val userView = findViewById<TextInputLayout>(R.id.viewUser)

        val pw = findViewById<EditText>(R.id.editTextPassword)
        val pwView = findViewById<TextInputLayout>(R.id.viewPassword)






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
        var query: String





        //val user = String
        //val pw = String

        var elementsOnLogin = findViewById<LinearLayout>(R.id.elementsOnLogin)



        suspend fun connectionView(): String{
            var result = Sync().testConnection()

            if (result == "Falha" ) {
                var xyz = Snackbar.make(findViewById(R.id.clMA),
                    getString(R.string.connectionErroResult1),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(Color.parseColor("#741919"))
                    .setTextColor(Color.WHITE)
                    .setActionTextColor(Color.WHITE)
                    .setAction("OK"){}.show()
                return result
            } else if (result == "Sem Conexão") {
                return result
            }else {
                return "Sucesso"
            }
        }

        fun afterSync(){
            var username = user.text.toString()
            var MainNav = Intent(this, MainNav::class.java).apply {
                putExtra(EXTRA_MESSAGE, username)
            }
            startActivity(MainNav)

            finish()
        }

        suspend fun authUser(ctxt: android.content.Context) {
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
                    return withContext(Dispatchers.IO) {
                        try {
                            sync.sync(0, ctxt)
                        } catch (e: SQLException){
                            Log.e("Sync MainActivity", e.toString())
                        }
                        withContext(Dispatchers.Main) {
                            afterSync()
                        }

                    }

                    var username = user.text.toString()
                    var MainNav = Intent(this, MainNav::class.java).apply {
                        putExtra(EXTRA_MESSAGE, username)
                    }
                    startActivity(MainNav)

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
                }
            }
            if (result == "Falha" || result == "Sem Conexão") {
                query = "SELECT username FROM Usuario WHERE username = '${user.text}' AND password = '${pw.text}'"
                var auth = db.externalExecSQLSelect(user.text.toString(), pw.text.toString())
                //Log.d("Debug", "Cursor = $cursor")
                if (auth == true) {
                    var username = user.text.toString()
                    var MainNav = Intent(this, MainNav::class.java).apply {
                        putExtra(EXTRA_MESSAGE, username)}
                    startActivity(MainNav)
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
            }
        }



        pw.setOnEditorActionListener{ v, actionId, event ->
            var ctxt = this
            MainScope().launch {
                authUser(ctxt)
            }
            true
        }

        val button: Button = findViewById(R.id.loginscreen_login)
        button.setOnClickListener {
            var ctxt = this
            MainScope().launch {
                authUser(ctxt)
            }
        }

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_actv, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {



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