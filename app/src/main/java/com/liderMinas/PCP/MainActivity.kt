package com.liderMinas.PCP

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SQLiteHelper(this);


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




        //val user = String
        //val pw = String

        val button0: Button = findViewById(R.id.apontamentoAbrir)
        button0.setOnClickListener {
            val intent = Intent(this, ApontamentoEmbalados1::class.java)
            startActivity(intent)
        }

        val button1: Button = findViewById(R.id.apontamentoFinal)
        button1.setOnClickListener {
            val intent = Intent(this, ApontamentoPerdas::class.java)
            startActivity(intent)
        }

        val button: Button = findViewById(R.id.loginscreen_login)
        button.setOnClickListener {
            val user = findViewById<EditText>(R.id.editTextUsername).toString()
            val pw = findViewById<EditText>(R.id.editTextPassword).toString()


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