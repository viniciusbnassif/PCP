package com.liderMinas.PCP


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.provider.AlarmClock
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.liderMinas.PCP.database.Sync
import com.liderMinas.PCP.database.confirmUnPw
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Integer.parseInt
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        prefs = PreferencesHelper(this)

        var progress = findViewById<LinearProgressIndicator>(R.id.syncStatusIndicator)
        var ctxt = this


        setContentView(R.layout.activity_main)
        SQLiteHelper(this)
        var db = SQLiteHelper(this)

        var sync = Sync()

        val user = findViewById<EditText>(R.id.usernameText)
        val userView = findViewById<TextInputLayout>(R.id.username)

        val pw = findViewById<EditText>(R.id.passwordText)
        val pwView = findViewById<TextInputLayout>(R.id.password)

        val username = user.text.toString()

        fun syncIsDone(){
            var mainMenu = Intent(this, MainMenu::class.java).apply {
                putExtra(AlarmClock.EXTRA_MESSAGE, username)
            }
            startActivity(mainMenu)

            finish()
        }

        fun showProgress(result: String) {
            var progress = findViewById<LinearProgressIndicator>(R.id.syncStatusIndicator)
            if (result == "true") {
                progress.visibility = VISIBLE
            } else if (result == "false") {
                progress.visibility = GONE
            }
        }




        suspend fun connectionView(): String {
            var result = Sync().testConnection()

            if (result == "Falha") {
                var xyz = Snackbar.make(
                    findViewById(R.id.saudacaoContainer),
                    getString(R.string.connectionErroResult1),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(Color.parseColor("#741919"))
                    .setTextColor(Color.WHITE)
                    .setActionTextColor(Color.WHITE)
                    .setAction("OK") {}.show()
                return result
            } else if (result == "Sem Conexão") {
                return result
            } else {
                return "Sucesso"
            }
        }


        suspend fun runSync(): String {
            //CoroutineScope(CoroutineName("SyncMainActivity")).async(Dispatchers.Unconfined) {
             var message = withContext(Dispatchers.IO) {
                try {
                    if (Looper.myLooper() == null) {
                        Looper.prepare()
                    }

                    var ret = sync.sync(0, ctxt)!!

                    return@withContext ret
                } catch (e: Exception) {
                    Log.d("SyncMainActivity (Thread)", "$e")
                    return@withContext "Falha"
                }
                /*MainScope().launch {
                    if (message == "Sucesso"){
                        var mainMenu = Intent(this, MainNav::class.java).apply {
                            putExtra(AlarmClock.EXTRA_MESSAGE, username)
                        }
                        startActivity(mainMenu)

                        finish()
                    } else {
                        showProgress("false")
                    }
                }*/
                //syncIsDone()
            }
            return message
        }

        suspend fun authUser(ctxt: android.content.Context) {
            showProgress("true")

            /*val user = findViewById<EditText>(R.id.editTextUsername)
            val userView = findViewById<TextInputLayout>(R.id.viewUser)

            val pw = findViewById<EditText>(R.id.editTextPassword)
            val pwView = findViewById<TextInputLayout>(R.id.viewPassword)*/

            val user = findViewById<EditText>(R.id.usernameText)
            val userView = findViewById<TextInputLayout>(R.id.username)

            val pw = findViewById<EditText>(R.id.passwordText)
            val pwView = findViewById<TextInputLayout>(R.id.password)

            var query: String
            val button = findViewById<Button>(R.id.loginBtn)

            var db = SQLiteHelper(this)

            var sync = Sync()

            var elementsOnLogin = findViewById<ConstraintLayout>(R.id.saudacaoContainer)

            var result = connectionView()
            if (result == "Sucesso") {
                var validation = confirmUnPw(user.text.toString(), pw.text.toString())
                if (validation == 201) {
                    user.isEnabled = false
                    pw.isEnabled = false
                    button.isEnabled = false


                    Snackbar.make(
                        elementsOnLogin,
                        "Usuário autenticado. Aguarde.",
                        Snackbar.LENGTH_LONG
                    ).show()
                    query = "DELETE FROM Usuario WHERE username = '${user.text}'"
                    db.externalExecSQL(query)
                    query =
                        "INSERT INTO Usuario(username, password) VALUES ('${user.text}', '${pw.text}')"
                    db.externalExecSQL(query)

                    var username = user.text.toString()
                    //var progress = findViewById<LinearProgressIndicator>(R.id.progressToolbar)
                    //progress.visibility = VISIBLE
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


                    var message = runSync()

                    if (message == "Sucesso"){
                        prefs.saveData("username", username)
                        prefs.saveBoolean("isLoggedIn", true)

                        var mainMenu = Intent(this, MainMenu::class.java).apply {
                            putExtra(AlarmClock.EXTRA_MESSAGE, username)
                        }
                        startActivity(mainMenu)



                        finish()
                    } else {
                        showProgress("false")
                    }




                } else if (validation == 401) {

                    userView.error = " "
                    pwView.error = "Nome de usuário ou senha incorretos"
                    pw.setText("")
                    Snackbar.make(
                        elementsOnLogin,
                        "Nome de usuário e/ou senha incorretos",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            if (result == "Falha" || result == "Sem Conexão") {
                query =
                    "SELECT username FROM Usuario WHERE username = '${user.text}' AND password = '${pw.text}'"
                var auth = db.externalExecSQLSelect(user.text.toString(), pw.text.toString())
                //Log.d("Debug", "Cursor = $cursor")
                if (auth == true) {
                    val username = user.text.toString()
                    var mainMenu = Intent(this, MainMenu::class.java).apply {
                        putExtra(AlarmClock.EXTRA_MESSAGE, username)
                    }
                    startActivity(mainMenu)
                    finish()
                } else if (auth == false) {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Não foi possivel sincronizar")
                        .setMessage("Não foi possivel conectar ao servidor. \nVerifique as configurações de rede e tente novamente.")
                        .setNegativeButton(
                            "Fechar") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setNeutralButton("Abrir Configurações de Wi-fi") { dialog, which ->
                            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                        }.show()
                    /*Snackbar.make(
                        elementsOnLogin,
                        "Não foi possivel conectar ao servidor. Verifique as configurações de rede e tente novamente.",
                        Snackbar.LENGTH_LONG
                    ).setAction("Abrir Configurações") {
                        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                    }.show()*/
                    showProgress("false")
                }
            }
        }
        val button: Button = findViewById(R.id.loginBtn)
        button.setOnClickListener {
            showProgress("true")
            var context = this
            MainScope().launch { authUser(context) }

        }
        var dateTime = "05/04/2023"
        var dty = dateTime.split("/").toTypedArray()
        var minute = "05:50"
        var dmi = minute.split(":").toTypedArray()

        var calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, parseInt(dty[0]))
        calendar.set(Calendar.MONTH, parseInt(dty[1]))
        calendar.set(Calendar.YEAR, parseInt(dty[2]))
        calendar.set(Calendar.HOUR_OF_DAY, parseInt(dmi[0]))
        calendar.set(Calendar.MINUTE, parseInt(dmi[1]))

        var date = "01/01/2023"//.toLong()
        //NotificationManager(this).NotificacaoErro( "produto", "Guilherme é um cagão","MensagemDetalhadaMensagemDetalhadaMensagemDetalhadaMensagemDetalhadaMensagemDetalhadaMensagemDetalhadaMensagemDetalhadaMensagemDetalhadaMensagemDetalhadaMensagemDetalhadaMensagemDetalhada", calendar.getTimeInMillis())
    }

    override fun onDestroy() {
        super.onDestroy()
        SQLiteHelper(this).close()
    }

    override fun onPause() {
        super.onPause()
        SQLiteHelper(this).close()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_actv, menu)
        return true
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.versionView -> {
            // User chose the "Settings" item, show the app settings UI...

            var toast = Toast.makeText(this, "Welcome to Android Teachers..!!",
                Toast.LENGTH_LONG)
            toast.show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }



}