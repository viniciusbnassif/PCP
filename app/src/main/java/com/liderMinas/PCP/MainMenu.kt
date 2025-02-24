@file:Suppress("OverrideDeprecatedMigration")

package com.liderMinas.PCP


//import android.support.v7.app.AppCompatActivity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.graphics.Color
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.liderMinas.PCP.database.Sync
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainMenu : AppCompatActivity() {

    private lateinit var prefs: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.parent)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        prefs = PreferencesHelper(this)

        val username = prefs.getData("username", "guest")

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.appBar)
        this.setSupportActionBar(toolbar)
        //setHasOptionsMenu(true)

        var ctxt = this.applicationContext
        var cl = findViewById<ConstraintLayout>(R.id.CL)

        //image.setImageDrawable(drawable)

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
            }
        }


        //var sync = parseInt("")
        var customAlertDialogView : View = layoutInflater.inflate(R.layout.alertdialog_sync_running, null)
        var materialAlertSync =
            MaterialAlertDialogBuilder(this)
                .setView(customAlertDialogView)
                .setTitle("Sincronizando")
                .setCancelable(false)

        var syncBtn = findViewById<MaterialButton>(R.id.syncBtn)


        suspend fun doSync() {
            syncBtn.isEnabled = false
            syncBtn.isActivated = false
            var show = materialAlertSync.show()
            //var data = String
            // <- extension on current scope
            Log.d("context inside method from button sync", "${ctxt.toString()}")
            var data = Sync().sync(0, this?.applicationContext!!)

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
                MaterialAlertDialogBuilder(this)
                    .setTitle("Sincronizando")
                    .setMessage("Sincronizado com sucesso")
                    .setCancelable(false)
                    //.setNeutralButton("Fechar") { dialog, _ -> (requireActivity() as MainNav).restartFragment(R.id.menu) }.show()
                    .setNeutralButton("Fechar") { dialog, _ ->  }.show()
                syncBtn.isEnabled = true
                syncBtn.isActivated = true


            } else if (data == "Falha") {
                show.cancel()
                MaterialAlertDialogBuilder(this)
                    .setTitle("Falha")
                    .setMessage("Ocorreu um erro ao sincronizar. Verifique o estado da conexão e tente novamente.")
                    .setCancelable(false)
                    //.setNeutralButton("Fechar") { dialog, _ -> (requireActivity() as MainNav).restartFragment(R.id.menu) }.show()
                    .setNeutralButton("Fechar") { dialog, _ -> /*(requireActivity() as MainNav).restartFragment()*/ }.show()
                syncBtn.isEnabled = true
                syncBtn.isActivated = true

                //withContext(Dispatchers.Main) { connectionView() }
            } else {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Falha")
                    .setMessage("Ocorreu um erro ao sincronizar. Verifique o estado da conexão e tente novamente.")
                    .setCancelable(false)
                    //.setNeutralButton("Fechar") { dialog, _ -> (requireActivity() as MainNav).restartFragment(R.id.menu) }.show()
                    .setNeutralButton("Fechar") { dialog, _ -> /*(requireActivity() as MainNav).restartFragment()*/ }.show()
                show.cancel()
                syncBtn.isEnabled = true
                /* TO DO */
            }

        }

        syncBtn.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                (customAlertDialogView.parent as? ViewGroup)?.removeView(customAlertDialogView)
                //whileSync(true)
                doSync()
            }
        }
        var intent: Intent


        //val username = username

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

        var accountBtn = findViewById<MaterialButton>(R.id.accountBtn)
        accountBtn.setOnClickListener {
            val modalBottomSheet = ModalBottomSheet(username)
            modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
        }
    }



}
class ModalBottomSheet(username: String?) : BottomSheetDialogFragment() {
    //var user = username
    private lateinit var prefs: PreferencesHelper

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.bottom_sheet, container, false).apply {
        prefs = activity?.let { PreferencesHelper(it.applicationContext) }!!

        var ctxt = activity?.applicationContext

        var userview = findViewById<TextView>(R.id.textView)
        var guia = findViewById<MaterialButton>(R.id.btnGuia)
        var sair = findViewById<MaterialButton>(R.id.btnSair)
        var versao = findViewById<MaterialButton>(R.id.versionView)
        userview.text = prefs.getData("username", "Guest")
        guia.setOnClickListener{
            //val contentUri = FileProvider.getUriForFile(context, "com.liderMinas.PCP", getResources().openRawResource(R.drawable.guia);)
            /*var intent = Intent(ctxt, PdfViewer::class.java)
            startActivity(intent)*/
        }
        sair.setOnClickListener {
            var intent = Intent(ctxt, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }

        var pInfo: PackageInfo? = null
        try {
            pInfo = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        val versionName = pInfo!!.versionName //Version Name
        val versionCode = pInfo!!.versionCode //
        val verCode = pInfo.versionCode //Version Code

        versao.text = "Versão do aplicativo: ${versionName}"
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            behavior.setPeekHeight(resources.displayMetrics.heightPixels)
        }
    }


    companion object {
        const val TAG = "ModalBottomSheet"
    }
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


