package com.liderMinas.PCP

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.widget.*
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.liderMinas.PCP.database.*
import kotlinx.coroutines.delay


//import com.liderMinas.PCP.database.connectMSSQL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SQLiteHelper(this);
        var db = SQLiteHelper(this)




        val user = findViewById<EditText>(R.id.editTextUsername)
        val userView = findViewById<TextInputLayout>(R.id.viewUser)

        val pw = findViewById<EditText>(R.id.editTextPassword)
        val pwView = findViewById<TextInputLayout>(R.id.viewPassword)

        var progress = findViewById<ProgressBar>(R.id.progress)
        progress.setVisibility(INVISIBLE)


        var teste = findViewById<Button>(R.id.teste)
        teste.setOnClickListener(){
            queryExternalServerAE(this)
        }



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

        fun authUser() {
            var validation = confirmUnPw(user.text.toString(), pw.text.toString())
            if (validation == 201) {
                Snackbar.make(
                    elementsOnLogin,
                    "Usuário autenticado \nAtualizando tabelas...",
                    Snackbar.LENGTH_LONG
                ).show()
                query = "DELETE FROM Usuario WHERE username = '${user.text}'"
                db.externalExecSQL(query)
                query = "INSERT INTO Usuario(username, password) VALUES ('${user.text}', '${pw.text}')"
                db.externalExecSQL(query)
                queryProdutoExt(this)
                queryMotivoExt(this)

                var username = user.text.toString()
                var mainMenu = Intent(this, MainMenu::class.java).apply {
                    putExtra(EXTRA_MESSAGE, username)}
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
            } else if (validation == 901 || validation == 900) {
                query = "SELECT username FROM Usuario WHERE username = '${user.text}' AND password = '${pw.text}'"
                var auth = db.externalExecSQLSelect(user.text.toString(), pw.text.toString())
                //Log.d("Debug", "Cursor = $cursor")
                if (auth == true) {
                    /*queryProdutoExt(this)
                    queryMotivoExt(this)*/

                    var username = user.text.toString()
                    var mainMenu = Intent(this, MainMenu::class.java).apply {
                        putExtra(EXTRA_MESSAGE, username)}
                    startActivity(mainMenu)
                    finish()
                }
                else {
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
            authUser()
            true
        }

        val button: Button = findViewById(R.id.loginscreen_login)
        button.setOnClickListener {
            progress.setVisibility(VISIBLE)
            authUser()
        }


            //Variaveis testadas e funcionando
            //Por aqui o que vai faltar é comparar os dados
            // com o banco de dados para liberar ou negar o acesso



            /*


            val conn =
                "Select *NOME DE USUARIO*, *SENHA* from *NOME DA TABELA DE LOGINS* WHERE *NOME DE USUARIO* = '${user}' AND *SENHA* = '${pw}';";
            */
            //databaseLogin(user, pw)


            /*if (DBHelper.$queryResult.length == 0){
                val mySnackbar = Snackbar.make(R.id.elementsOnLogin, "Login ou senha incorretos", 5000)
                mySnackbar.show()
                Snackbar.make(
                    findViewById(R.id.elementsOnLogin),
                    "Login ou senha incorretos",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            elif databaseLogin()

             */

            /* val builder = AlertDialog.Builder(this)
            builder.setTitle("Androidly Alert")
            builder.setMessage("We have a message")


            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                Toast.makeText(applicationContext,
                    android.R.string.yes, Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton(android.R.string.no) { dialog, which ->
                Toast.makeText(applicationContext,
                    android.R.string.no, Toast.LENGTH_SHORT).show()
            }

            builder.setNeutralButton("Maybe") { dialog, which ->
                Toast.makeText(applicationContext,
                    user, Toast.LENGTH_SHORT).show()
            }
            builder.show() */



    }

    private fun setContentView(versionView: TextView?, version: String) {

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    var versionCode = BuildConfig.VERSION_CODE
    var versionName = BuildConfig.VERSION_NAME

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.versionView -> {
            // User chose the "Settings" item, show the app settings UI...

            Toast.makeText(applicationContext,
                "Versão: $versionName. Revisão: $versionCode", Toast.LENGTH_SHORT).show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }




}