package com.liderMinas.PCP

import android.database.Cursor
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Looper
import android.provider.AlarmClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.liderMinas.PCP.database.Sync
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import java.lang.Integer.parseInt
import java.time.LocalDate
import java.util.*


class ApontamentoEmbalados1 : AppCompatActivity(), LifecycleEventObserver {
    lateinit var db: SQLiteHelper
    var cursor: Cursor? = null
    var id: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apontamento_embalados1)

        // Hide the status bar.
        /*window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }*/

        val exceptionHandler = CoroutineExceptionHandler{_ , throwable->
            throwable.printStackTrace()
        }

//when you make request:

        CoroutineScope(Dispatchers.IO + exceptionHandler ).launch {

        }

        var bottomAppBar = findViewById<BottomAppBar>(R.id.apontEmbaladosBottomBar)
        KeyboardVisibilityEvent.setEventListener(this, object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                if (isOpen) {
                    bottomAppBar.isVisible = false
                } else {
                    bottomAppBar.isVisible = true
                }
            }
        })


        val username = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE)



        var tipoTransporte = ""

        val cxAvulsa = findViewById<TextView>(R.id.caixasAvulsas)
        val qtdAvulsa = findViewById<TextView>(R.id.qtdAvulsas)
        val viewTotal = findViewById<TextView>(R.id.total)
        val btnPilha = findViewById<MaterialButton>(R.id.tButtonPilha)
        val btnPallet = findViewById<MaterialButton>(R.id.tButtonPallet)

        btnPilha.addOnCheckedChangeListener { button, isChecked ->
            if (isChecked == true){
                tipoTransporte = "pilha"
                setTotal()
            } else if(btnPallet.isChecked == false) {
                tipoTransporte = ""
                viewTotal.setText("")
            }
        }


        btnPallet.addOnCheckedChangeListener { button, isChecked ->
            if (isChecked == true){
                tipoTransporte = "pallet"
                setTotal()
            } else if(btnPilha.isChecked == false) {
                tipoTransporte = ""
                viewTotal.setText("")
            }
        }

        val pilha: EditText = findViewById(R.id.finalResult)
        val btnValidade = findViewById<EditText>(R.id.btnValidade)
        //spinner = findViewById<Spinner>(R.id.menu)
        val spinnerID = findViewById<TextView>(R.id.spinnerIdSaver)
        val transporteID = findViewById<TextView>(R.id.tipoTransporteID)
        transporteID.text = "0"

        //spinnerPrd = findViewById<AutoCompleteTextView>(R.id.spinnerPrd)
        val dateVal: TextInputEditText = findViewById(R.id.btnValidade)
        val lote = findViewById<EditText>(R.id.editTextLote)
        val qeProduto = findViewById<EditText>(R.id.editTextEmbalagemCaixa)

        db = SQLiteHelper(this)



        //var toggleButton = findViewById<ToggleButton>(R.id.toggleButton)



        cxAvulsa.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setTotal()
            }

            override fun afterTextChanged(s: Editable?) {
                setTotal()
            }
        })

        qtdAvulsa.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                setTotal()
            }

            override fun afterTextChanged(s: Editable?) {
                setTotal()
            }
        })

        val tipoTranporte = arrayOf<String>(getString(R.string.pilhas), getString(R.string.pallet))

        val SpTipoTransporte = findViewById<AutoCompleteTextView>(R.id.tipoTransporte)

        val adapterTransporte = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, tipoTranporte)
        SpTipoTransporte.setAdapter(adapterTransporte)
        SpTipoTransporte.setText(SpTipoTransporte.adapter.getItem(0).toString(), false)
        SpTipoTransporte.onItemClickListener =
            AdapterView.OnItemClickListener { p0, view, position, _id ->
                if (view?.context != null) {
                    transporteID.text = _id.toInt().toString()
                    id = _id.toInt()
                    setTotal()
                    //findViewById<TextView>(R.id.tipoTransporteID).apply { text = "$id" }
                    if (id == 1){
                        findViewById<TextInputLayout>(R.id.textInputLayout22).apply { hint = context.getString(
                                                    R.string.pallet) }
                    }
                    else if (id==0){
                        findViewById<TextInputLayout>(R.id.textInputLayout22).apply { hint = context.getString(
                                                    R.string.pilhas) }
                    }
                    //inicia metodo passando o id do item selecionado

                }
            }


        setOrRefreshSpinner()

        //Criar barra de ações
        val toolbar = findViewById<BottomAppBar?>(R.id.apontEmbaladosBottomBar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
        }


        var dateFormatter = SimpleDateFormat()
        var dty = ""

        var dateFormatter0 = SimpleDateFormat()
        var time = ""

        var time0 = findViewById<TextView>(R.id.editTextHora)
        var dty0 = findViewById<TextView>(R.id.editTextData)

        var dateFormatterProtheus = SimpleDateFormat()
        var dtyProtheus = ""
        var dtytime0 = ""

        fun date() {

            dateFormatter = SimpleDateFormat("dd/MM/yyyy") //formatar data no formato padrão
            dty = dateFormatter.format(Date())

            dateFormatter0 = SimpleDateFormat("kk:mm") //formatar tempo no formato 24h (kk)
            time = dateFormatter0.format(Date())

            time0.setText(time)
            dty0.setText(dty)


            dateFormatterProtheus =
                SimpleDateFormat("yyyyMMdd") //formatar data no formato padrão
            dtyProtheus = dateFormatterProtheus.format(Date())
            dtytime0 = "$dtyProtheus" + "$time"
        }
        date()



        dateVal.setOnClickListener {
            //Exibir barra de progresso, pois nos testes o Date Picker demorou entre 1 a 2 segundos para aparecer.
            findViewById<ProgressBar>(R.id.progressInd).apply { visibility = View.VISIBLE }

            //parametros para o popup de calendario
            var datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText(getString(R.string.selecioneADataDeVencimento))
                    .build()

            //abre popup para inserção/alteração da data
            datePicker.show(supportFragmentManager, "materialDatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val dateFormatter: SimpleDateFormat
                val zone: Calendar
                val date: String


                dateFormatter = SimpleDateFormat("dd/MM/yyyy")
                date = dateFormatter.format(Date(it))



                var array = date.split("/").toTypedArray()

                var intArrayDia = array[0].toString().toInt()
                var intArrayMes = array[1].toString().toInt()
                var intArrayAno = array[2].toString().toInt()
                var arrayNbr = 1

                //Para o sistema de datas brasileiro, o Date Picker tem um erro que sempre mostra o dia anterior a seleção.
                //Código para correção "automática" a seguir.
                //Se o mês tiver 31 dias (jan, mar, mai, jul, ago, out, dez), verificar
                if ((array[1] == "01") or (array[1] == "03") or (array[1] == "05") or (array[1] == "07") or (array[1] == "08") or (array[1] == "10") or (array[1] == "12")) {
                    //Se o Dia for entre 01 a 30 adicionar mais 1.
                    if ((intArrayDia >= 1) and (intArrayDia <= 30)) {
                        intArrayDia += 1
                    } //Senão-se o dia for == 31, adiciona mais um ao mês (exceto se for dezembro), e faz o dia virar 01.
                    else if ((array[0] == "31")) {
                        if ((array[1] != "12")) {
                            intArrayMes += 1
                            intArrayDia = 1
                        } else { //se o mês for dezembro (e o dia for 31), passa o mês janeiro, dia 01, ano seguinte
                            intArrayDia = 1
                            intArrayMes = 1
                            intArrayAno += 1
                        }

                    }
                    //Se o mês tiver apenas 30 dias, e se o dia for entre 01 a 29 adicionar mais um ao dia.
                } else if ((array[1] == "04") or (array[1] == "06") or (array[1] == "09") or (array[1] == "11")) {
                    if ((intArrayDia >= 1) and (intArrayDia <= 29)) {
                        intArrayDia += 1
                    } else if (array[0] == "30") {//Senão-se o dia for == 30, adiciona mais um ao mês (exceto se for dezembro), e faz o dia virar 01.
                        intArrayDia = 1
                        intArrayMes += 1
                    }
                } else if (array[1] == "02") {
                    if ((array[2] == "2024") or (array[2] == "2028") or (array[2] == "2032")) {
                        if ((intArrayDia >= 1) and (intArrayDia <= 28)) {
                            intArrayDia += 1
                        } else if (intArrayDia == 29) {
                            intArrayDia = 1
                            intArrayMes = 3
                        }
                    } else {
                        if ((intArrayDia >= 1) and (intArrayDia <= 27)) {
                            intArrayDia += 1
                        } else if (intArrayDia == 28) {
                            intArrayDia = 1
                            intArrayMes = 3
                        }
                    }
                }

                //=====================================================

                // variavel com final St recebem a data corrigida para exibição
                var intArrayDiaSt = intArrayDia.toString()
                var intArrayMesSt = intArrayMes.toString()
                var intArrayAnoSt = intArrayAno.toString()

                //Se a data tiver apenas um número (exemplo: 05 de Janeiro: 5/1/2023)
                // corrigir colocando os zeros na frente para suprir as regras
                if (intArrayDiaSt.length == 1) {
                    intArrayDiaSt = "0$intArrayDia"
                }
                if (intArrayMesSt.length == 1) {
                    intArrayMesSt = "0$intArrayMes"
                }

                var finalDate = "$intArrayDiaSt/$intArrayMesSt/$intArrayAnoSt"

                Toast.makeText(this, "$finalDate is selected", Toast.LENGTH_LONG).show()


                //Exibição da data corrigida no campo correto.
                val dateValReturn = findViewById<TextInputEditText>(R.id.btnValidade)
                dateValReturn.text =
                    Editable.Factory.getInstance().newEditable(finalDate.toString())

                //após colocar o valor em exibição e fechar o DatePicker, é necessário remover a barra de progresso da UI
                findViewById<ProgressBar>(R.id.progressInd).apply {
                    visibility = View.INVISIBLE
                }
            }
            datePicker.addOnCancelListener {
                findViewById<ProgressBar>(R.id.progressInd).apply {
                    visibility = View.INVISIBLE
                }
            }
            datePicker.addOnDismissListener {
                findViewById<ProgressBar>(R.id.progressInd).apply {
                    visibility = View.INVISIBLE
                }
            }

        }

        //====================================================================================================

        /* Adicionar valor ao campo pilha quando clicar ou segurar o botão + */
        val plusRtn: Button = findViewById(R.id.plusBtn)
        plusRtn.setOnClickListener {

            //se o campo estiver vazio, para evitar crash ao pressionar +, preencher com 0
            if (pilha.length() == 0) {
                pilha.text = Editable.Factory.getInstance().newEditable(0.toString())
            }
            val result = pilha.text.toString().toInt()
            if (result >= 0) {
                val finalResult = result + 1
                pilha.text = Editable.Factory.getInstance().newEditable(finalResult.toString())
                setTotal()
                if (finalResult > 0) {
                    findViewById<Button>(R.id.minusBtn).apply { isEnabled = true }
                } else if (finalResult <= 0) {
                    findViewById<Button>(R.id.minusBtn).apply { isEnabled = false }
                }
            }
        }

        plusRtn.setOnLongClickListener{
            //se o campo estiver vazio, para evitar crash ao pressionar -, preencher com 0
            if (pilha.length() == 0) {
                pilha.text = Editable.Factory.getInstance().newEditable(0.toString())
            }
            val result = pilha.text.toString().toInt()
            if (result >= 0) {
                val finalResult = result + 10
                pilha.text = Editable.Factory.getInstance().newEditable(finalResult.toString())
                setTotal()
                if (finalResult > 0) {
                    var minusBtnAble =
                        findViewById<Button>(R.id.minusBtn).apply { isEnabled = true }
                } else if (finalResult <= 0) {
                    var minusBtnAble =
                        findViewById<Button>(R.id.minusBtn).apply { isEnabled = false }
                }
            }
            true
        }

        //Subtrair valor do campo pilha quando clicar ou segurar o botão -
        val minusRtn: Button = findViewById(R.id.minusBtn)
        minusRtn.setOnClickListener {

            //se o campo estiver vazio, para evitar crash ao pressionar -, preencher com 0
            if (pilha.length() == 0) {
                pilha.text = Editable.Factory.getInstance().newEditable(0.toString())
            }
            var result = pilha.text.toString().toInt()
            if (result >= 1) {
                var finalResult = result - 1
                pilha.text = Editable.Factory.getInstance().newEditable(finalResult.toString())
                setTotal()
                if (finalResult > 0) {
                    var minusBtnAble =
                        findViewById<Button>(R.id.minusBtn).apply { isEnabled = true }
                } else if (finalResult <= 0) {
                    var minusBtnAble =
                        findViewById<Button>(R.id.minusBtn).apply { isEnabled = false }
                }
            }
        }
        minusRtn.setOnLongClickListener{
            //se o campo estiver vazio, para evitar crash ao pressionar -, preencher com 0
            if (pilha.length() == 0) {
                pilha.text = Editable.Factory.getInstance().newEditable(0.toString())
            }
            var result = pilha.text.toString().toInt()
            if (result >= 10) {
                var finalResult = result - 10
                pilha.text = Editable.Factory.getInstance().newEditable(finalResult.toString())
                setTotal()
                if (finalResult > 0) {
                    var minusBtnAble =
                        findViewById<Button>(R.id.minusBtn).apply { isEnabled = true }
                } else if (finalResult <= 0) {
                    var minusBtnAble =
                        findViewById<Button>(R.id.minusBtn).apply { isEnabled = false }
                }
            }else if (result >= 1){
                result = 0
                pilha.text = Editable.Factory.getInstance().newEditable(result.toString())
            }
            true
        }

        fun clearAll(){
            var spinnerPrd = findViewById<AutoCompleteTextView>(R.id.spinnerPrd)


            cxAvulsa.setText("0")
            qtdAvulsa.setText("0")
            viewTotal.setText("")

            spinnerPrd.setText("")
            spinnerPrd.setAdapter(null)

            setOrRefreshSpinner()

            spinnerID.setText("")
            lote.setText("")

            btnPilha.isChecked = false
            btnPallet.isChecked = false
            pilha.setText("0")
            qeProduto.setText("")
            time0.setText("")

            date()

            dateVal.setText("")

        }



        suspend fun finalizar(command: Int){
            var sync = Sync()
            var tipoID = findViewById<TextView>(R.id.tipoTransporteID)

            if ((spinnerID.text != "")
                && (lote.length() != 0)
                && (pilha.length() != 0)
                && (qeProduto.length() != 0)
                && (dty0.length() != 0)
                && (time0.length() != 0)
                && (dateVal.length() != 0)
                && (viewTotal.length() != 0)
                && (cxAvulsa.length() != 0)
                && (qtdAvulsa.length() != 0)
                && (username != null)
            ) {
                MainScope().launch {
                    if (cxAvulsa.text == "") {
                        cxAvulsa.text = "0"
                    }
                    if (qtdAvulsa.text == "") {
                        qtdAvulsa.text = "0"
                    }
                }

                //var tipoID = findViewById<TextView>(R.id.tipoTransporteID)
                var tipoID = ""
                if (tipoTransporte == "pilha"){
                    tipoID = "0"
                } else if (tipoTransporte == "pallet"){
                    tipoID = "1"
                }

                var tipoIDD: String
                tipoIDD = (if (tipoID == "0") "Pilha" else "Pallet").toString()

                "name, 2012, 2017".split(",").toTypedArray()
                var validade = "${btnValidade.text}".split("/").toTypedArray()
                var validadeProtheus = (validade[2] + validade[1] + validade[0]).toInt()


                var finalQuery: String =
                    "INSERT INTO ApontEmbalado (qtdApontada, tipoUnitizador, dataHoraApontamento, lote, caixaAvulsa, unidadeAvulsa, validade, total, idProduto, qeProduto, validProduto, tipoVProduto, username, statusSync) " +
                            "VALUES ('${pilha.text}', '${tipoIDD}', '${dtytime0}', ${
                                Integer.parseInt(
                                    lote.text.toString()
                                )
                            }, ${parseInt(cxAvulsa.text.toString())}, " +
                            "${parseInt(qtdAvulsa.text.toString())}, " +
                            "${validadeProtheus}, ${
                                Integer.parseInt(
                                    findViewById<TextView>(R.id.total).text.toString()
                                )
                            }, ${Integer.parseInt(spinnerID.text.toString())}," +
                            " ${Integer.parseInt(qeProduto.text.toString())}, ${
                                parseInt(
                                    findViewById<TextView>(R.id.validProdutoSaver).text.toString()
                                )
                            }," +
                            "'${findViewById<TextView>(R.id.tipoVProdutoSaver).text}', '${username}', 0);"
                db.externalExecSQL(finalQuery)
                var ctxt = this
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(ctxt, "Apontamento salvo", Toast.LENGTH_LONG).show()
                }
                CoroutineScope(Dispatchers.IO).launch {
                    sync.sync(1, ctxt)
                }
                if (command == 2){
                    finish()
                } else if (command == 1){
                    clearAll()
                }

            } else {

                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show()

                if (pilha.length() == 0) {
                    pilha.setBackgroundColor(Color.parseColor("#FF8282"))
                }
                if (qeProduto.length() == 0) {
                    findViewById<TextInputLayout>(R.id.textInputLayout17).error = getString(R.string.campo_obrigatorio)
                    //qeProduto.setBackgroundColor(Color.parseColor("#FF8282"))
                }
                if (dty0.length() == 0) {
                    findViewById<TextInputLayout>(R.id.textInputLayout19).error = getString(R.string.campo_obrigatorio)
                    //dty0.setBackgroundColor(Color.parseColor("#FF8282"))
                }
                if (time0.length() == 0) {
                    findViewById<TextInputLayout>(R.id.textInputLayout18).error = getString(R.string.campo_obrigatorio)
                    //time0.setBackgroundColor(Color.parseColor("#FF8282"))
                }
                if (dateVal.length() == 0) {
                    findViewById<TextInputLayout>(R.id.textInputLayout21).error = getString(R.string.campo_obrigatorio)
                    //dateVal.setBackgroundColor(Color.parseColor("#FF8282"))
                }
                if (lote.length() == 0) {
                    findViewById<TextInputLayout>(R.id.textInputLayout20).error = getString(R.string.campo_obrigatorio)
                }
                if ((spinnerID.text == "")) {
                    findViewById<TextInputLayout>(R.id.viewSpinner).error = getString(R.string.campo_obrigatorio)
                }
                if ((viewTotal.text == "")) {
                    findViewById<TextInputLayout>(R.id.viewTotal).error = "Preencha todos os campos corretamente e tente novamente"
                }

            }
        }

        val buttonResetar: MaterialButton = findViewById(R.id.fab1)
        buttonResetar.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch { finalizar(1) }

        }

        val buttonFinalizar: MaterialButton = findViewById(R.id.fab2)
        buttonFinalizar.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch { finalizar(2) }
        }

        CoroutineScope(Dispatchers.Main).launch{
            connectionView()
        }
    }

    suspend fun connectionView(){
        CoroutineScope(Dispatchers.IO).launch {

            if (Looper.myLooper() == null) {
                Looper.prepare()
            }

            var result = CoroutineScope((Dispatchers.IO)).run {
                try {
                    var result = Sync().testConnection()
                } catch (ex: Exception) {
                    Log.d("Exeption", ex.toString())
                }
            }


            if (result == "Falha") {
                CoroutineScope(Dispatchers.Main).launch {
                    if (Looper.myLooper() == null) {
                        Looper.prepare()
                    }
                    val snackbar = Snackbar.make(
                        findViewById(R.id.CL),
                        "Não foi possível estabelecer uma conexão com o servidor",
                        Snackbar.LENGTH_INDEFINITE
                    ).setBackgroundTint(Color.parseColor("#741919"))
                        .setTextColor(Color.WHITE)
                        .setActionTextColor(Color.WHITE)
                        .setAction("OK") {}.show()
                }
            } else if (result == "Sem Conexão") {
                CoroutineScope(Dispatchers.Main).launch {
                    if (Looper.myLooper() == null) {
                        Looper.prepare()
                    }
                    Snackbar.make(
                        findViewById(R.id.CL),
                        "Não foi possível estabelecer uma conexão com o servidor (Endereço e porta indisponíveis para esta rede)",
                        Snackbar.LENGTH_INDEFINITE
                    ).setBackgroundTint(Color.parseColor("#E3B30C"))
                        .setTextColor(Color.WHITE)
                        .setActionTextColor(Color.WHITE)
                        .setAction("OK") {}.show()
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    if (Looper.myLooper() == null) {
                        Looper.prepare()
                    }
                    Snackbar.make(
                        findViewById(R.id.CL),
                        "Sincronizado com sucesso!",
                        Snackbar.LENGTH_LONG
                    ).setBackgroundTint(Color.parseColor("#197419"))
                        .setTextColor(Color.WHITE)
                        .setActionTextColor(Color.WHITE)
                        .setAction("OK") {}.show()
                }
            }
        }
    }


    fun setTotal(){

        val spinner: Int

        val cxAvulsa = findViewById<TextView>(R.id.caixasAvulsas)
        val qtdAvulsa = findViewById<TextView>(R.id.qtdAvulsas)
        var viewTotal = findViewById<TextView>(R.id.total)

        var valueCxAvulsa = "0"
        var valueQtAvulsa = "0"


        var btnPilha = findViewById<MaterialButton>(R.id.tButtonPilha)

        var btnPallet = findViewById<MaterialButton>(R.id.tButtonPallet)



        var tipoID = ""
        if (btnPilha.isChecked){
            tipoID = "0"
        } else if (btnPallet.isChecked){
            tipoID = "1"
        }

        var pilha: EditText = findViewById(R.id.finalResult)

        //var tipoID = findViewById<TextView>(R.id.tipoTransporteID)


        var qeProduto = findViewById<EditText>(R.id.editTextEmbalagemCaixa)

        if (cxAvulsa.text.length == 0){
            valueCxAvulsa = "0"
        }else if (cxAvulsa.text.length > 0){
            valueCxAvulsa = "${cxAvulsa.text}"
        }
        if (qtdAvulsa.text.length == 0){
            valueQtAvulsa = "0"
        }else if (qtdAvulsa.text.length > 0){
            valueQtAvulsa = "${qtdAvulsa.text}"
        }

        if ((qeProduto.length() > 0) && ((tipoID == "0") or (tipoID == "1"))){
            if (tipoID == "0") {
                spinner = 10
                var total = (parseInt(qeProduto.text.toString()) * spinner * parseInt(pilha.text.toString())) + parseInt(valueQtAvulsa.toString()) + (parseInt(valueCxAvulsa.toString()) * parseInt(qeProduto.text.toString()))
                Log.d("Debug campo total", "$total")
                viewTotal.text = total.toString()
            }
            else if (tipoID == "1"){
                spinner = 54
                var total = (parseInt(qeProduto.text.toString()) * spinner * parseInt(pilha.text.toString())) + parseInt(valueQtAvulsa.toString()) + ((parseInt(valueCxAvulsa.toString()) * parseInt(qeProduto.text.toString())))
                Log.d("Debug campo total", "$total")
                viewTotal.text = total.toString()
            }
        }else {
            viewTotal.text = ""
        }
    }





    fun setOrRefreshSpinner() {
        var spinnerID = findViewById<TextView>(R.id.spinnerIdSaver)
        var idintern: Int
        cursor = db.getProdutos()
        var t = 1
        var cursorArray = ArrayList<Any>()
        while (cursor!!.moveToNext()) {
            cursorArray.add(cursor!!.getString(1))
        }

        var spinnerPrd: AutoCompleteTextView = findViewById(R.id.spinnerPrd)
        val DESC_PROD = "descProduto"
        var simpleCursorAdapter =
            ArrayAdapter<Any>(this, android.R.layout.simple_dropdown_item_1line, cursorArray)
        spinnerPrd.setAdapter(simpleCursorAdapter) //spinner recebe os dados para exibição

        //ao selecionar um item
        spinnerPrd.onItemClickListener =
            AdapterView.OnItemClickListener { p0, view, position, _id ->
                if (view?.context != null) {
                    spinnerID.text = _id.toInt().toString()
                    id = _id.toInt()
                    overrideDataBlock(id) //inicia metodo passando o id do item selecionado
                    CoroutineScope(Dispatchers.Main).launch {
                        setTotal()
                    }
                }

            }

    }


    fun overrideDataBlock(id: Int) {

        var validProduto: Int = 0
        var tipoVProduto: String = ""
        var diasValidade: Int = 0
        var adcVal: Int
        //val QE_PROD = "qeProduto"

        var dateNow = Calendar.getInstance()
        dateNow.set(
            dateNow.get(Calendar.YEAR),
            dateNow.get(Calendar.MONTH),
            dateNow.get(Calendar.DAY_OF_MONTH)
        )

        var loteF: String
        var dayOfYear = LocalDate.now().dayOfYear

        var lote0 = Calendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()
        var lote1 = Calendar.getInstance().get(Calendar.YEAR).toString()
        var lote2 = lote0 + "${lote1.subSequence(2, 4)}"
        if (lote2.length <= 4) {
            loteF = "0${lote2}"
        } else {
            loteF = lote2}
        findViewById<EditText>(R.id.editTextLote).setText(loteF)


        var validF: String
        var btnValidade = findViewById<EditText>(R.id.btnValidade)


        var cursor2 = db.getDetailProdutos(id.toInt())
        cursor2.moveToFirst()
        findViewById<EditText>(R.id.editTextEmbalagemCaixa).setText("0")
        if (cursor2 != null) {
            //qeProduto = ("${cursor2.getInt(0)}")
            findViewById<EditText>(R.id.editTextEmbalagemCaixa).setText("${cursor2.getInt(1)}")
            validProduto = cursor2.getInt(2)
            tipoVProduto = cursor2.getString(3).toString()
            findViewById<TextView>(R.id.validProdutoSaver).apply { text = validProduto.toString() }
            findViewById<TextView>(R.id.tipoVProdutoSaver).apply { text = tipoVProduto }
        }


        if (validProduto != null) {
            if (validProduto != 0) {
                if (tipoVProduto == "D") {
                    adcVal = validProduto
                    dateNow.add(Calendar.DAY_OF_MONTH, adcVal).toString()
                    validF = SimpleDateFormat("dd/MM/yyyy").format(dateNow.time)
                    btnValidade.setText(validF)
                } else if (tipoVProduto == "S") {
                    adcVal = validProduto * 7
                    dateNow.add(Calendar.DAY_OF_MONTH, adcVal).toString()
                    validF = SimpleDateFormat("dd/MM/yyyy").format(dateNow.time)
                    btnValidade.setText(validF)
                } else if (tipoVProduto == "M") {
                    adcVal = validProduto
                    adcVal = validProduto
                    dateNow.add(Calendar.MONTH, adcVal).toString()
                    validF = SimpleDateFormat("dd/MM/yyyy").format(dateNow.time)
                    btnValidade.setText(validF)
                } else if (tipoVProduto == "A") {
                    adcVal = validProduto
                    dateNow.add(Calendar.YEAR, adcVal).toString()
                    validF = SimpleDateFormat("dd/MM/yyyy").format(dateNow.time)
                    btnValidade.setText(validF)
                }
            }
        }
    }

    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Parece que você está tentando sair...")
            .setMessage("Ao sair desta tela usando o botão voltar, todas as informações preenchidas serão perdidas. \nDeseja sair mesmo assim?")
            .setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Sair mesmo assim") { dialog, which ->
                super.onBackPressed()
                Animatoo.animateSlideRight(this)
            }
            .show()
    }
    /* When the activity is destroyed then close the cursor as it will not be used again */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        Animatoo.animateSlideRight(this)
        return true
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        TODO("Not yet implemented")
    }

}



