@file:Suppress("OverrideDeprecatedMigration")

package com.liderMinas.PCP


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.liderMinas.PCP.database.Sync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext


class MainMenu(var username: String) : Fragment() {
    @SuppressLint("ResourceAsColor", "MissingInflatedId")

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    = inflater.inflate(R.layout.activity_main_menu, container, false).apply {


        /*window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }*/
        var ctxt = activity?.applicationContext


        /*val toolbar = findViewById<Toolbar>(R.id.appBar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(false)

        }
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_baseline_logout_24)*/


        var coordinator = findViewById<ConstraintLayout>(R.id.parent)
        var cl = findViewById<ConstraintLayout>(R.id.CL)

        suspend fun connectionView(){
            var result = Sync().testConnection()
            if (result == "Falha" ) {
                Snackbar.make(
                    cl,
                    "Não foi possível estabelecer uma conexão com o servidor",
                    Snackbar.LENGTH_INDEFINITE
                ).setBackgroundTint(Color.parseColor("#741919")).setTextColor(Color.WHITE)
                    .setActionTextColor(Color.WHITE).setAction("OK") {}.show()
            } else if (result == "Sem Conexão") {
                CoroutineScope(Dispatchers.Main).launch {
                    Snackbar.make(
                        cl,
                        "Não foi possível estabelecer uma conexão com o servidor (Endereço e porta indisponíveis para esta rede)",
                        Snackbar.LENGTH_INDEFINITE
                    ).setBackgroundTint(Color.parseColor("#E3B30C")).setTextColor(Color.WHITE)
                        .setActionTextColor(Color.WHITE).setAction("OK") {}.show()
                }
            }/* else {
                Snackbar.make(cl,
                    "Sincronizado com sucesso!",
                    Snackbar.LENGTH_INDEFINITE
                ).setBackgroundTint(Color.parseColor("#197419")).setTextColor(Color.WHITE).setActionTextColor(Color.WHITE).setAction("OK"){}.show()

            }*/

            /*if (result == "Falha" ) {
                val snackbar = Snackbar.make(cl,
                    "Não foi possível estabelecer uma conexão com o servidor",
                    Snackbar.LENGTH_INDEFINITE
                ).setBackgroundTint(Color.parseColor("#741919")).setTextColor(Color.WHITE).setActionTextColor(Color.WHITE).setAction("OK"){}.show()
            } else if (result == "Sem Conexão") {
                Snackbar.make(cl,
                    "Não foi possível estabelecer uma conexão com o servidor (Endereço e porta indisponíveis para esta rede)",
                    Snackbar.LENGTH_INDEFINITE
                ).setBackgroundTint(Color.parseColor("#E3B30C")).setTextColor(Color.WHITE).setActionTextColor(Color.WHITE).setAction("OK"){}.show()
            }else {
                Snackbar.make(cl,
                    "Sincronizado com sucesso!",
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(Color.parseColor("#197419")).setTextColor(Color.WHITE).setActionTextColor(Color.WHITE).setAction("OK"){}.show()
            }*/
        }

         MainScope().launch{connectionView()}


        //var sync = parseInt("")
        var customAlertDialogView : View = layoutInflater.inflate(R.layout.alertdialog_sync_running, null)
        var materialAlertSync =
            MaterialAlertDialogBuilder(context)
                .setView(customAlertDialogView)
                .setTitle("Sincronizando")
                .setCancelable(false)

        /*fun whileSync(sync: Boolean){
            val dialog = materialAlertSync
            if (sync) {
                dialog.show()
            } else {
                dialog.show()?.dismiss()
                //dialog?.show()?.dismiss()
            }


        }*/

        var syncBtn = findViewById<MaterialButton>(R.id.syncBtn)


        suspend fun doSync() {
            syncBtn.isEnabled = false
            var show = materialAlertSync.show()
            //var data = String
            // <- extension on current scope
            Log.d("context inside method from button sync", "${ctxt.toString()}")
            var data = Sync().sync(0, activity?.applicationContext!!)



            //val result = data.await()
            if (data == "Sucesso") {
                show.cancel()
                //whileSync(false)
                /*Snackbar.make(
                    cl,
                    "Sincronizado com sucesso!",
                    Snackbar.LENGTH_SHORT
                ).setBackgroundTint(Color.parseColor("#197419")).setTextColor(Color.WHITE)
                    .setActionTextColor(Color.WHITE).setAction("OK") {}.show()*/
                MaterialAlertDialogBuilder(context)
                    .setTitle("Sincronizando")
                    .setMessage("Sincronizado com sucesso")
                    .setCancelable(false)
                    //.setNeutralButton("Fechar") { dialog, _ -> (requireActivity() as MainNav).restartFragment(R.id.menu) }.show()
                    .setNeutralButton("Fechar") { dialog, _ -> (requireActivity() as MainNav).restartFragment() }.show()




            } else if (data == "Falha") {
                show.cancel()
                withContext(Dispatchers.Main) { connectionView() }
            } else {
                show.cancel()
                /* TO DO */
            }

        }
        fun restartNav(){

        }

        syncBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                //whileSync(true)
                doSync()
            }

        }
        var intent: Intent

        var sairBtn = findViewById<Button>(R.id.sair)
        sairBtn.setOnClickListener {
            MainScope().launch {
                intent = Intent(ctxt, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
        }

        //val username = username
        var saudacao = "Bem-vindo, ${username}"
        findViewById<TextView>(R.id.saudacao).apply { text = saudacao }






        val buttonAE: Button = findViewById(R.id.apEmbalados)
        buttonAE.setOnClickListener {
            intent = Intent(ctxt, ApontamentoEmbalados1::class.java)
                .apply {
                    putExtra(EXTRA_MESSAGE, username)}
            startActivity(intent)
        }



        val buttonAP: Button = findViewById(R.id.apPerdas)
        buttonAP.setOnClickListener {
            intent = Intent(ctxt, ApontamentoPerdas::class.java)
                .apply {
                    putExtra(EXTRA_MESSAGE, username)}
            startActivity(intent)
        }
    }

    //var btnSync = findViewById<MaterialButton>(R.id.syncBtn)

    /*@SuppressLint("ResourceAsColor")
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
    }*/


    /* When the activity is destroyed then close the cursor as it will not be used again */
    /*override fun onBackPressed() {
        MaterialAlertDialogBuilder(ctxt)
            .setIcon(R.drawable.ic_baseline_logout_24_black)
            .setTitle("Você escolheu sair...")
            .setMessage("O aplicativo será encerrado e será necessário efetuar login novamente. \nDeseja sair mesmo assim?")
            .setNeutralButton("Permanecer no menu") { dialog, which ->
                dialog.dismiss()
            }
            .setNegativeButton("Fazer logout") { dialog, which ->
                super.onBackPressed()
                intent = Intent(ctxt, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            .setPositiveButton("Fechar app") { dialog, which ->
                finish()
            }
            .show()
    }

    /* When the activity is destroyed then close the cursor as it will not be used again */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/
}

/*class FloatingActionButtonBehavior(context: Context?, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<FloatingActionButton?>() {
    fun layoutDependsOn(
        parent: CoordinatorLayout?,
        child: FloatingActionButton?,
        dependency: View?
    ): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    fun onDependentViewChanged(
        parent: CoordinatorLayout?,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        val translationY =
            Math.min( dependency.getTranslationY().toFloat(), dependency.getHeight().toFloat())
        child.translationY = translationY
        return true
    }
}*/


