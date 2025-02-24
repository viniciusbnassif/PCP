package com.liderMinas.PCP

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.liderMinas.PCP.database.Sync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt
import java.util.*


class ApontamentoPerdas : AppCompatActivity() {


    lateinit var db: SQLiteHelper

    var simpleCursorAdapterPrd: SimpleCursorAdapter? = null
    var cursorPrd: Cursor? = null
    var idPerdas: Int = 0
    var id: Int = 0

    lateinit var motivo1: AutoCompleteTextView
    lateinit var motivo2: AutoCompleteTextView
    lateinit var motivo3: AutoCompleteTextView
    lateinit var motivo4: AutoCompleteTextView
    lateinit var motivo5: AutoCompleteTextView
    lateinit var motivo6: AutoCompleteTextView
    lateinit var timeProtheus: String
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apontamento_perdas)


        var bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        KeyboardVisibilityEvent.setEventListener(this, object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                bottomAppBar.isVisible = !isOpen
            }
        })
        /*window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }*/

        val username = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE)

        var stats1 = findViewById<MaterialButton>(R.id.stats1)
        var stats2 = findViewById<MaterialButton>(R.id.stats2)
        var stats3 = findViewById<MaterialButton>(R.id.stats3)
        var stats4 = findViewById<MaterialButton>(R.id.stats4)
        var stats5 = findViewById<MaterialButton>(R.id.stats5)
        var stats6 = findViewById<MaterialButton>(R.id.stats6)


        var motivo1 = findViewById<AutoCompleteTextView>(R.id.motivo1)
        var motivo2 = findViewById<AutoCompleteTextView>(R.id.motivo2)
        var motivo3 = findViewById<AutoCompleteTextView>(R.id.motivo3)
        var motivo4 = findViewById<AutoCompleteTextView>(R.id.motivo4)
        var motivo5 = findViewById<AutoCompleteTextView>(R.id.motivo5)
        var motivo6 = findViewById<AutoCompleteTextView>(R.id.motivo6)

        var motivoID1 = findViewById<TextView>(R.id.motivo1IdSaver)
        var motivoID2 = findViewById<TextView>(R.id.motivo2IdSaver)
        var motivoID3 = findViewById<TextView>(R.id.motivo3IdSaver)
        var motivoID4 = findViewById<TextView>(R.id.motivo4IdSaver)
        var motivoID5 = findViewById<TextView>(R.id.motivo5IdSaver)
        var motivoID6 = findViewById<TextView>(R.id.motivo6IdSaver)

        var qtdPerda1 = findViewById<TextView>(R.id.qtdPerda1)
        var qtdPerda2 = findViewById<TextView>(R.id.qtdPerda2)
        var qtdPerda3 = findViewById<TextView>(R.id.qtdPerda3)
        var qtdPerda4 = findViewById<TextView>(R.id.qtdPerda4)
        var qtdPerda5 = findViewById<TextView>(R.id.qtdPerda5)
        var qtdPerda6 = findViewById<TextView>(R.id.qtdPerda6)


        var toggleKG1 = findViewById<MaterialButton>(R.id.tButtonKG1)
        var toggleKG2 = findViewById<MaterialButton>(R.id.tButtonKG2)
        var toggleKG3 = findViewById<MaterialButton>(R.id.tButtonKG3)
        var toggleKG4 = findViewById<MaterialButton>(R.id.tButtonKG4)
        var toggleKG5 = findViewById<MaterialButton>(R.id.tButtonKG5)
        var toggleKG6 = findViewById<MaterialButton>(R.id.tButtonKG6)


        var toggleUN1 = findViewById<MaterialButton>(R.id.tButtonUN1)
        var toggleUN2 = findViewById<MaterialButton>(R.id.tButtonUN2)
        var toggleUN3 = findViewById<MaterialButton>(R.id.tButtonUN3)
        var toggleUN4 = findViewById<MaterialButton>(R.id.tButtonUN4)
        var toggleUN5 = findViewById<MaterialButton>(R.id.tButtonUN5)
        var toggleUN6 = findViewById<MaterialButton>(R.id.tButtonUN6)

        var produtoSpinner = findViewById<AutoCompleteTextView>(R.id.produto)

        db = SQLiteHelper(this)

        // calling the action bar
        val toolbar = findViewById<BottomAppBar?>(R.id.bottomAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
        }

        intent = intent

        var name = intent.getStringExtra("messageIntent")


        fun updateTime(): String {

            val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
            val dty = dateFormatter.format(Date())

            findViewById<TextView>(R.id.finalDate).apply { text = dty }

            val dateFormatter0 = SimpleDateFormat("kk:mm")
            val time = dateFormatter0.format(Date())

            val dateFormatterProtheus = SimpleDateFormat("yyyyMMddkkmm")
            val timeProtheus = dateFormatterProtheus.format(Date())
            findViewById<TextView>(R.id.finalTime).apply { text = time }
            return timeProtheus
        }

        timeProtheus = updateTime().toString()
        Log.d("Debug OnCreate", "${timeProtheus}")


        var produtoID = findViewById<TextView>(R.id.produtoIdSaver)




        fun verifyErrors(number: Int){
            var primaryColorSuccess = getColor(R.color.suSuccessGreen)
            var resourcePrimaryColorSuccess = R.color.suSuccessGreen
            var backgroundColorSuccess = getColor(R.color.lightSuccessGreen)
            var iconSuccess = R.drawable.rounded_check_circle_24

            var primaryColorError = getColor(R.color.suErrorRed)
            var resourcePrimaryColorError = R.color.suErrorRed
            var backgroundColorError = getColor(R.color.lightErrorRed)
            var iconError = R.drawable.rounded_cancel_24

            var primaryColorNeutral = getColor(R.color.black)
            var resourcePrimaryColorNeutral = R.color.spacegray
            var backgroundColorNeutral = getColor(R.color.white)
            var iconNeutral = R.drawable.round_remove_circle_outline_24

            /*stats2.setTextColor(primaryColorSuccess)
            stats2.setBackgroundColor(backgroundColorSuccess)
            stats2.setIconResource(iconSuccess)
            stats2.setIconTintResource(resourcePrimaryColorSuccess)
            stats2.setStrokeColorResource(resourcePrimaryColorSuccess)*/


            if (number == 1){
                if (motivo1.length()==0 && motivoID1.length()==0 && qtdPerda1.length()==0 && toggleKG1.isChecked == false && toggleUN1.isChecked == false){
                    stats1.setTextColor(primaryColorNeutral)
                    stats1.setBackgroundColor(backgroundColorNeutral)
                    stats1.setIconResource(iconNeutral)
                    stats1.setIconTintResource(resourcePrimaryColorNeutral)
                    stats1.setStrokeColorResource(resourcePrimaryColorNeutral)
                    stats1.contentDescription = "neutral"
                } else if (motivo1.length()>0 && motivoID1.length()>0 && qtdPerda1.length()>0 && (toggleUN1.isChecked == true || toggleKG1.isChecked == true)){
                    stats1.setTextColor(primaryColorSuccess)
                    stats1.setBackgroundColor(backgroundColorSuccess)
                    stats1.setIconResource(iconSuccess)
                    stats1.setIconTintResource(resourcePrimaryColorSuccess)
                    stats1.setStrokeColorResource(resourcePrimaryColorSuccess)
                    stats1.contentDescription = "success"
                } else {
                    stats1.setTextColor(primaryColorError)
                    stats1.setBackgroundColor(backgroundColorError)
                    stats1.setIconResource(iconError)
                    stats1.setIconTintResource(resourcePrimaryColorError)
                    stats1.setStrokeColorResource(resourcePrimaryColorError)
                    stats1.contentDescription = "error"
                }
            }
            if (number == 2){
                if (motivo2.length()==0 && motivoID2.length()==0 && qtdPerda2.length()==0 && (toggleKG2.isChecked == false && toggleUN2.isChecked == false)){
                    stats2.setTextColor(primaryColorNeutral)
                    stats2.setBackgroundColor(backgroundColorNeutral)
                    stats2.setIconResource(iconNeutral)
                    stats2.setIconTintResource(resourcePrimaryColorNeutral)
                    stats2.setStrokeColorResource(resourcePrimaryColorNeutral)
                    stats2.contentDescription = "neutral"
                } else if (motivo2.length()>0 && motivoID2.length()>0 && qtdPerda2.length()>0 && (toggleKG2.isChecked == true || toggleUN2.isChecked == true)){
                    stats2.setTextColor(primaryColorSuccess)
                    stats2.setBackgroundColor(backgroundColorSuccess)
                    stats2.setIconResource(iconSuccess)
                    stats2.setIconTintResource(resourcePrimaryColorSuccess)
                    stats2.setStrokeColorResource(resourcePrimaryColorSuccess)
                    stats2.contentDescription = "success"
                } else {
                    stats2.setTextColor(primaryColorError)
                    stats2.setBackgroundColor(backgroundColorError)
                    stats2.setIconResource(iconError)
                    stats2.setIconTintResource(resourcePrimaryColorError)
                    stats2.setStrokeColorResource(resourcePrimaryColorError)
                    stats2.contentDescription = "error"
                }
            }
            if (number == 3){
                if (motivo3.length()==0 && motivoID3.length()==0 && qtdPerda3.length()==0 && (toggleKG3.isChecked == false && toggleUN3.isChecked == false)){
                    stats3.setTextColor(primaryColorNeutral)
                    stats3.setBackgroundColor(backgroundColorNeutral)
                    stats3.setIconResource(iconNeutral)
                    stats3.setIconTintResource(resourcePrimaryColorNeutral)
                    stats3.setStrokeColorResource(resourcePrimaryColorNeutral)
                    stats3.contentDescription = "neutral"
                } else if (motivo3.length()>0 && motivoID3.length()>0 && qtdPerda3.length()>0 && (toggleKG3.isChecked == true || toggleUN3.isChecked == true)){
                    stats3.setTextColor(primaryColorSuccess)
                    stats3.setBackgroundColor(backgroundColorSuccess)
                    stats3.setIconResource(iconSuccess)
                    stats3.setIconTintResource(resourcePrimaryColorSuccess)
                    stats3.setStrokeColorResource(resourcePrimaryColorSuccess)
                    stats3.contentDescription = "success"
                } else {
                    stats3.setTextColor(primaryColorError)
                    stats3.setBackgroundColor(backgroundColorError)
                    stats3.setIconResource(iconError)
                    stats3.setIconTintResource(resourcePrimaryColorError)
                    stats3.setStrokeColorResource(resourcePrimaryColorError)
                    stats3.contentDescription = "error"
                }
            }
            if (number == 4){
                if (motivo4.length()==0 && motivoID4.length()==0 && qtdPerda4.length()==0 && (toggleKG4.isChecked == false && toggleUN4.isChecked == false)){
                    stats4.setTextColor(primaryColorNeutral)
                    stats4.setBackgroundColor(backgroundColorNeutral)
                    stats4.setIconResource(iconNeutral)
                    stats4.setIconTintResource(resourcePrimaryColorNeutral)
                    stats4.setStrokeColorResource(resourcePrimaryColorNeutral)
                    stats4.contentDescription = "neutral"
                } else if (motivo4.length()>0 && motivoID4.length()>0 && qtdPerda4.length()>0 && (toggleKG4.isChecked == true || toggleUN4.isChecked == true)){
                    stats4.setTextColor(primaryColorSuccess)
                    stats4.setBackgroundColor(backgroundColorSuccess)
                    stats4.setIconResource(iconSuccess)
                    stats4.setIconTintResource(resourcePrimaryColorSuccess)
                    stats4.setStrokeColorResource(resourcePrimaryColorSuccess)
                    stats4.contentDescription = "success"
                } else {
                    stats4.setTextColor(primaryColorError)
                    stats4.setBackgroundColor(backgroundColorError)
                    stats4.setIconResource(iconError)
                    stats4.setIconTintResource(resourcePrimaryColorError)
                    stats4.setStrokeColorResource(resourcePrimaryColorError)
                    stats4.contentDescription = "error"
                }
            }
            if (number == 5){
                if (motivo5.length()==0 && motivoID5.length()==0 && qtdPerda5.length()==0 && (toggleKG5.isChecked == false && toggleUN5.isChecked == false)){
                    stats5.setTextColor(primaryColorNeutral)
                    stats5.setBackgroundColor(backgroundColorNeutral)
                    stats5.setIconResource(iconNeutral)
                    stats5.setIconTintResource(resourcePrimaryColorNeutral)
                    stats5.setStrokeColorResource(resourcePrimaryColorNeutral)
                    stats5.contentDescription = "neutral"
                } else if (motivo5.length()>0 && motivoID5.length()>0 && qtdPerda5.length()>0 && (toggleKG5.isChecked == true || toggleUN5.isChecked == true)){
                    stats5.setTextColor(primaryColorSuccess)
                    stats5.setBackgroundColor(backgroundColorSuccess)
                    stats5.setIconResource(iconSuccess)
                    stats5.setIconTintResource(resourcePrimaryColorSuccess)
                    stats5.setStrokeColorResource(resourcePrimaryColorSuccess)
                    stats5.contentDescription = "success"
                } else {
                    stats5.setTextColor(primaryColorError)
                    stats5.setBackgroundColor(backgroundColorError)
                    stats5.setIconResource(iconError)
                    stats5.setIconTintResource(resourcePrimaryColorError)
                    stats5.setStrokeColorResource(resourcePrimaryColorError)
                    stats5.contentDescription = "error"
                }
            }
            if (number == 6){
                if (motivo6.length()==0 && motivoID6.length()==0 && qtdPerda6.length()==0 && (toggleKG6.isChecked == false && toggleUN6.isChecked == false)){
                    stats6.setTextColor(primaryColorNeutral)
                    stats6.setBackgroundColor(backgroundColorNeutral)
                    stats6.setIconResource(iconNeutral)
                    stats6.setIconTintResource(resourcePrimaryColorNeutral)
                    stats6.setStrokeColorResource(resourcePrimaryColorNeutral)
                    stats6.contentDescription = "neutral"
                } else if (motivo6.length()>0 && motivoID6.length()>0 && qtdPerda6.length()>0 && (toggleKG6.isChecked == true || toggleUN6.isChecked == true)){
                    stats6.setTextColor(primaryColorSuccess)
                    stats6.setBackgroundColor(backgroundColorSuccess)
                    stats6.setIconResource(iconSuccess)
                    stats6.setIconTintResource(resourcePrimaryColorSuccess)
                    stats6.setStrokeColorResource(resourcePrimaryColorSuccess)
                    stats6.contentDescription = "success"
                } else {
                    stats6.setTextColor(primaryColorError)
                    stats6.setBackgroundColor(backgroundColorError)
                    stats6.setIconResource(iconError)
                    stats6.setIconTintResource(resourcePrimaryColorError)
                    stats6.setStrokeColorResource(resourcePrimaryColorError)
                    stats6.contentDescription = "error"
                }
            }
        }




        fun setOrRefreshSpinnerProd() {


            var idintern: Int
            var cursorProduto = db.getProdutos()
            var cursorArray = ArrayList<Any>()
            while (cursorProduto.moveToNext()) {
                cursorArray.add(cursorProduto.getString(1))
            }
            var simpleCursorAdapter =
                ArrayAdapter<Any>(this, android.R.layout.simple_dropdown_item_1line, cursorArray)
            produtoSpinner.setAdapter(simpleCursorAdapter) //spinner recebe os dados para exibição

            //ao selecionar um item
            produtoSpinner.onItemClickListener =

                AdapterView.OnItemClickListener { p0, view, position, _id ->
                    if (view?.context != null) {
                        produtoID.text = _id.toInt().toString()
                        verifyErrors(1)
                        //verifyErrors(2)
                        //verifyErrors(3)
                        //verifyErrors(4)
                        //verifyErrors(5)
                        //verifyErrors(6)
                    }
                }
        }


        fun setOrRefreshSpinnerPerdas(number: Int) {
            var cursorPrd = db.getMotivo()
            var t = 1
            var cursorArray = ArrayList<Any>()
            while (cursorPrd.moveToNext()) {
                cursorArray.add(cursorPrd.getString(1))
            }
            var simpleCursorAdapterPrd =
                ArrayAdapter<Any>(this, android.R.layout.simple_dropdown_item_1line, cursorArray)


            if (number == 0) { //coloca o adapter dentro de todos os AutoCompleteTextView
                motivo1.setAdapter(simpleCursorAdapterPrd) //spinner recebe os dados para exibição
                motivo2.setAdapter(simpleCursorAdapterPrd)
                motivo3.setAdapter(simpleCursorAdapterPrd)
                motivo4.setAdapter(simpleCursorAdapterPrd)
                motivo5.setAdapter(simpleCursorAdapterPrd)
                motivo6.setAdapter(simpleCursorAdapterPrd)
            }
            if (number == 1) {
                motivo1.setAdapter(simpleCursorAdapterPrd)
                verifyErrors(1)
            }
            if (number == 2) {
                motivo2.setAdapter(simpleCursorAdapterPrd)
                verifyErrors(2)
            }
            if (number == 3) {
                motivo3.setAdapter(simpleCursorAdapterPrd)
                verifyErrors(3)
            }
            if (number == 4) {
                motivo4.setAdapter(simpleCursorAdapterPrd)
                verifyErrors(4)
            }
            if (number == 5) {
                motivo5.setAdapter(simpleCursorAdapterPrd)
                verifyErrors(5)
            }
            if (number == 6) {
                motivo6.setAdapter(simpleCursorAdapterPrd)
                verifyErrors(6)
            }


            //ao selecionar um item
            motivo1.onItemClickListener =
                AdapterView.OnItemClickListener { p0, view, position, _id ->
                    if (view?.context != null) {
                        Log.d("Debug", "entrou no onItemClick")
                        motivoID1.text = _id.toInt().toString()
                        verifyErrors(1)
                    }
                }
            motivo2.onItemClickListener =
                AdapterView.OnItemClickListener { p0, view, position, _id ->
                    if (view?.context != null) {
                        Log.d("Debug", "entrou no onItemClick")
                        motivoID2.text = _id.toInt().toString()
                        verifyErrors(2)
                    }
                }
            motivo3.onItemClickListener =
                AdapterView.OnItemClickListener { p0, view, position, _id ->
                    if (view?.context != null) {
                        Log.d("Debug", "entrou no onItemClick")
                        motivoID3.text = _id.toInt().toString()
                        verifyErrors(3)
                    }
                }
            motivo4.onItemClickListener =
                AdapterView.OnItemClickListener { p0, view, position, _id ->
                    if (view?.context != null) {
                        Log.d("Debug", "entrou no onItemClick")
                        motivoID4.text = _id.toInt().toString()
                        verifyErrors(4)
                    }
                }
            motivo5.onItemClickListener =
                AdapterView.OnItemClickListener { p0, view, position, _id ->
                    if (view?.context != null) {
                        Log.d("Debug", "entrou no onItemClick")
                        motivoID5.text = _id.toInt().toString()
                        verifyErrors(5)
                    }
                }
            motivo6.onItemClickListener =
                AdapterView.OnItemClickListener { p0, view, position, _id ->
                    if (view?.context != null) {
                        Log.d("Debug", "entrou no onItemClick")
                        motivoID6.text = _id.toInt().toString()
                        verifyErrors(6)
                    }
                }


        }




        fun clearPerda(number: Int) {



            var textNull = ""
            if (number == 0) {
                produtoSpinner.setText("")
                produtoSpinner.setAdapter(null)
                setOrRefreshSpinnerProd()
                produtoID.text = ""
            }

            if (number == 1) {
                motivo1.setText("")
                motivo1.setAdapter(null)
                setOrRefreshSpinnerPerdas(1)
                qtdPerda1.apply { text = textNull }
                var view = this.currentFocus
                val manager: InputMethodManager = getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                if (view != null) {
                    manager
                        .hideSoftInputFromWindow(
                            view.windowToken, 0
                        )
                }
                motivoID1.text = ""
                motivo1.clearFocus()
                qtdPerda1.clearFocus()
                toggleKG1.isChecked = false
                toggleUN1.isChecked = false
                verifyErrors(1)
            } else if (number == 2) {
                motivo2.setText("")
                motivo2.setAdapter(null)
                setOrRefreshSpinnerPerdas(2)
                qtdPerda2.apply { text = textNull }
                var view = this.currentFocus
                val manager: InputMethodManager = getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                if (view != null) {
                    manager
                        .hideSoftInputFromWindow(
                            view.windowToken, 0
                        )
                }
                motivoID2.text = ""
                motivo2.clearFocus()
                qtdPerda2.clearFocus()
                toggleKG2.isChecked = false
                toggleUN2.isChecked = false
                verifyErrors(2)
            } else if (number == 3) {
                motivo3.setText("")
                motivo3.setAdapter(null)
                setOrRefreshSpinnerPerdas(3)
                qtdPerda3.apply { text = textNull }
                var view = this.currentFocus
                val manager: InputMethodManager = getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                if (view != null) {
                    manager
                        .hideSoftInputFromWindow(
                            view.windowToken, 0
                        )
                }
                motivoID3.text = ""
                motivo3.clearFocus()
                qtdPerda3.clearFocus()
                toggleKG3.isChecked = false
                toggleUN3.isChecked = false
                verifyErrors(3)
            } else if (number == 4) {
                motivo4.setText("")
                motivo4.setAdapter(null)
                setOrRefreshSpinnerPerdas(4)
                qtdPerda4.apply { text = textNull }
                var view = this.currentFocus
                val manager: InputMethodManager = getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                if (view != null) {
                    manager
                        .hideSoftInputFromWindow(
                            view.windowToken, 0
                        )
                }
                motivoID4.text = ""
                motivo4.clearFocus()
                qtdPerda4.clearFocus()
                toggleKG4.isChecked = false
                toggleUN4.isChecked = false
                verifyErrors(4)
            } else if (number == 5) {
                motivo5.setText("")
                motivo5.setAdapter(null)
                setOrRefreshSpinnerPerdas(5)
                qtdPerda5.apply { text = textNull }
                var view = this.currentFocus
                val manager: InputMethodManager = getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                if (view != null) {
                    manager
                        .hideSoftInputFromWindow(
                            view.windowToken, 0
                        )
                }
                motivoID5.text = ""
                motivo5.clearFocus()
                qtdPerda5.clearFocus()
                toggleKG5.isChecked = false
                toggleUN5.isChecked = false
                verifyErrors(5)
            } else if (number == 6) {
                motivo6.setText("")
                motivo6.setAdapter(null)
                setOrRefreshSpinnerPerdas(6)
                qtdPerda6.apply { text = textNull }
                var view = this.currentFocus
                val manager: InputMethodManager = getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                if (view != null) {
                    manager
                        .hideSoftInputFromWindow(
                            view.windowToken, 0
                        )
                }
                motivoID6.text = ""
                motivo6.clearFocus()
                qtdPerda6.clearFocus()
                toggleKG6.isChecked = false
                toggleUN6.isChecked = false
                verifyErrors(6)
            }
        }
        var btnApagarPerda1 = findViewById<Button>(R.id.btnApagarPerda1)
        btnApagarPerda1.setOnClickListener {
            clearPerda(1)
        }
        var btnApagarPerda2 = findViewById<Button>(R.id.btnApagarPerda2)
        btnApagarPerda2.setOnClickListener {
            clearPerda(2)
        }
        var btnApagarPerda3 = findViewById<Button>(R.id.btnApagarPerda3)
        btnApagarPerda3.setOnClickListener {
            clearPerda(3)
        }
        var btnApagarPerda4 = findViewById<Button>(R.id.btnApagarPerda4)
        btnApagarPerda4.setOnClickListener {
            clearPerda(4)
        }
        var btnApagarPerda5 = findViewById<Button>(R.id.btnApagarPerda5)
        btnApagarPerda5.setOnClickListener {
            clearPerda(5)
        }
        var btnApagarPerda6 = findViewById<Button>(R.id.btnApagarPerda6)
        btnApagarPerda6.setOnClickListener {
            clearPerda(6)
        }

        qtdPerda1.doOnTextChanged { text, start, before, count ->
            verifyErrors(1)
        }
        qtdPerda2.doOnTextChanged { text, start, before, count ->
            verifyErrors(2)
        }
        qtdPerda3.doOnTextChanged { text, start, before, count ->
            verifyErrors(3)
        }
        qtdPerda4.doOnTextChanged { text, start, before, count ->
            verifyErrors(4)
        }
        qtdPerda5.doOnTextChanged { text, start, before, count ->
            verifyErrors(5)
        }
        qtdPerda6.doOnTextChanged { text, start, before, count ->
            verifyErrors(6)
        }

        toggleKG1.setOnClickListener{
            verifyErrors(1)
            if (toggleKG1.isChecked){
                toggleKG1.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleKG1.setTextColor(getColor(R.color.black))
            }
        }
        toggleUN1.setOnClickListener{
            verifyErrors(1)
            if (toggleUN1.isChecked){
                toggleUN1.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleUN1.setTextColor(getColor(R.color.black))
            }
        }
        toggleKG2.setOnClickListener{
            verifyErrors(2)
            if (toggleKG2.isChecked){
                toggleKG2.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleKG2.setTextColor(getColor(R.color.black))
            }
        }
        toggleUN2.setOnClickListener{
            verifyErrors(2)
            if (toggleUN2.isChecked){
                toggleUN2.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleUN2.setTextColor(getColor(R.color.black))
            }
        }
        toggleKG3.setOnClickListener{
            verifyErrors(3)
            if (toggleKG3.isChecked){
                toggleKG3.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleKG3.setTextColor(getColor(R.color.black))
            }
        }
        toggleUN3.setOnClickListener{
            verifyErrors(3)
            if (toggleUN3.isChecked){
                toggleUN3.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleUN3.setTextColor(getColor(R.color.black))
            }
        }
        toggleKG4.setOnClickListener{
            verifyErrors(4)
            if (toggleKG4.isChecked){
                toggleKG4.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleKG4.setTextColor(getColor(R.color.black))
            }
        }
        toggleUN4.setOnClickListener{
            verifyErrors(4)
            if (toggleUN4.isChecked){
                toggleUN4.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleUN4.setTextColor(getColor(R.color.black))
            }
        }
        toggleKG5.setOnClickListener{
            verifyErrors(5)
            if (toggleKG5.isChecked){
                toggleKG5.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleKG5.setTextColor(getColor(R.color.black))
            }
        }
        toggleUN5.setOnClickListener{
            verifyErrors(5)
            if (toggleUN5.isChecked){
                toggleUN5.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleUN5.setTextColor(getColor(R.color.black))
            }
        }
        toggleKG6.setOnClickListener{
            verifyErrors(6)
            if (toggleKG6.isChecked){
                toggleKG6.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleKG6.setTextColor(getColor(R.color.black))
            }
        }
        toggleUN6.setOnClickListener{
            verifyErrors(6)
            if (toggleUN6.isChecked){
                toggleUN6.setTextColor(getColor(R.color.alternativeColorOnPrimaryContainer))
            } else {
                toggleUN6.setTextColor(getColor(R.color.black))
            }
        }

        setOrRefreshSpinnerPerdas(0)
        setOrRefreshSpinnerProd()


        suspend fun finalizarPerda(option: Int){

            var sync = Sync()

            var unit1: String
            var unit2: String
            var unit3: String
            var unit4: String
            var unit5: String
            var unit6: String

            var appBottomBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
            var viewProduto = findViewById<TextInputLayout>(R.id.viewProduto)


            if (produtoID.length() == 0){
                viewProduto.error = getString(R.string.campo_obrigatorio)
                viewProduto.requestFocus()
                produtoSpinner.showDropDown()
                return
            }else if (viewProduto.isErrorEnabled == true){
                viewProduto.isErrorEnabled = false
                return
            }

            if (stats1.contentDescription == "error" || stats2.contentDescription == "error" || stats3.contentDescription == "error"
                || stats4.contentDescription == "error" || stats5.contentDescription == "error" || stats6.contentDescription == "error"){
                //Toast.makeText(this, getString(R.string.APerdas_erro), Toast.LENGTH_LONG).show()
                Snackbar.make(appBottomBar, getString(R.string.APerdas_error), Snackbar.LENGTH_LONG).show()
                return
            }
            if (stats1.contentDescription == "neutral" && stats2.contentDescription == "neutral" && stats3.contentDescription == "neutral"
                && stats4.contentDescription == "neutral" && stats5.contentDescription == "neutral" && stats6.contentDescription == "neutral"){
                //Toast.makeText(this, getString(R.string.APerdas_erro), Toast.LENGTH_LONG).show()
                Snackbar.make(appBottomBar, getString(R.string.APerdas_neutral), Snackbar.LENGTH_LONG).show()
                return
            }
            if (stats1.contentDescription == "success") {
                unit1 = if(toggleKG1.isChecked)"kg"
                else {
                    "un"
                }
                var query =
                    "INSERT INTO ApontPerda (qtdPerda, unidPerda, dataHoraPerda, username, idProduto, idMotivo, statusSync)" +
                            "VALUES (${parseFloat(qtdPerda1.text.toString())}, '${unit1}', '${timeProtheus}', '${username}'," +
                            "${parseInt(produtoID.text.toString())}, ${parseInt(motivoID1.text.toString())}, 0 );"
                db.externalExecSQL(query)
            }
            if (stats2.contentDescription == "success") {
                unit2 = if(toggleKG2.isChecked == true)"kg"
                else {
                    "un"
                }

                var query =
                    "INSERT INTO ApontPerda (qtdPerda, unidPerda, dataHoraPerda, username, idProduto, idMotivo, statusSync)" +
                            "VALUES (${parseFloat(qtdPerda2.text.toString())}, '${unit2}', '${timeProtheus}', '${username}'," +
                            "${parseInt(produtoID.text.toString())}, ${parseInt(motivoID2.text.toString())}, 0);"
                db.externalExecSQL(query)
            }
            if (stats3.contentDescription == "success") {
                unit3 = if(toggleKG3.isChecked == true)"kg"
                else {
                    "un"
                }

                var query =
                    "INSERT INTO ApontPerda (qtdPerda, unidPerda, dataHoraPerda, username, idProduto, idMotivo, statusSync)" +
                            "VALUES (${parseFloat(qtdPerda3.text.toString())}, '${unit3}', '${timeProtheus}', '${username}'," +
                            "${parseInt(produtoID.text.toString())}, ${parseInt(motivoID3.text.toString())}, 0);"
                db.externalExecSQL(query)
            }
            if (stats4.contentDescription == "success") {
                unit4 = if(toggleKG4.isChecked == true)"kg"
                else {
                    "un"
                }

                var query =
                    "INSERT INTO ApontPerda (qtdPerda, unidPerda, dataHoraPerda, username, idProduto, idMotivo, statusSync)" +
                            "VALUES (${parseFloat(qtdPerda4.text.toString())}, '${unit4}', '${timeProtheus}', '${username}'," +
                            "${parseInt(produtoID.text.toString())}, ${parseInt(motivoID4.text.toString())}, 0);"
                db.externalExecSQL(query)
            }
            if (stats5.contentDescription == "success") {
                unit5 = if(toggleKG5.isChecked == true)"kg"
                else {
                    "un"
                }

                var query =
                    "INSERT INTO ApontPerda (qtdPerda, unidPerda, dataHoraPerda, username, idProduto, idMotivo, statusSync)" +
                            "VALUES (${parseFloat(qtdPerda5.text.toString())}, '${unit5}', '${timeProtheus}', '${username}'," +
                            "${parseInt(produtoID.text.toString())}, ${parseInt(motivoID5.text.toString())}, 0 );"
                db.externalExecSQL(query)
            }
            if (stats6.contentDescription == "success") {
                unit6 = if(toggleKG6.isChecked == true)"kg"
                else {
                    "un"
                }

                var query =
                    "INSERT INTO ApontPerda (qtdPerda, unidPerda, dataHoraPerda, username, idProduto, idMotivo, statusSync)" +
                            "VALUES (${parseFloat(qtdPerda6.text.toString())}, '${unit6}', '${timeProtheus}', '${username}'," +
                            "${parseInt(produtoID.text.toString())}, ${parseInt(motivoID6.text.toString())}, 0);"
                db.externalExecSQL(query)
            }
            if (option == 2){
                Toast.makeText(this, "Perdas salvas", Toast.LENGTH_LONG).show()
                sync.sync(1, this)
                finish()
                return
            }
            if (option == 1){

                clearPerda(0)
                clearPerda(1)
                clearPerda(2)
                clearPerda(3)
                clearPerda(4)
                clearPerda(5)
                clearPerda(6)
                timeProtheus = updateTime().toString()
                Snackbar.make(appBottomBar, "Perdas salvas", Snackbar.LENGTH_LONG).show()
                var ctxt = this
                withContext(Dispatchers.IO){ sync.sync(1, ctxt)}
            }
        }



        var fab1 = findViewById<Button>(R.id.fab1)
        fab1.setOnClickListener {
            MainScope().launch { finalizarPerda(1) }
        }


        var fab2 = findViewById<Button>(R.id.fab2)
        fab2.setOnClickListener {
            MainScope().launch { finalizarPerda(2) }
        }


    }


    override fun onBackPressed() {
        super.onBackPressed()
        MaterialAlertDialogBuilder(this)
            .setTitle("Parece que você está tentando sair...")
            .setMessage("Ao sair desta tela usando o botão voltar, todas as informações preenchidas serão perdidas. \nDeseja sair mesmo assim?")
            .setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Sair") { dialog, which ->
                super.finishAfterTransition()
            }
            .show()
    }
        /* When the activity is destroyed then close the cursor as it will not be used again */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}






    //método do botão de voltar da Action Bar



