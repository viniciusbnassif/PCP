package com.liderMinas.PCP

import android.annotation.SuppressLint
import android.app.Application.*
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.provider.AlarmClock
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import com.androidadvance.topsnackbar.TSnackbar
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.liderMinas.PCP.database.Sync
import com.liderMinas.PCP.database.queryProdutoExt
import java.lang.Integer.parseInt
import java.text.*
import java.time.LocalDate
import java.util.*


class ApontamentoEmbalados1 : AppCompatActivity() {
    lateinit var db: SQLiteHelper
    lateinit var spinner: Spinner
    //lateinit var spinnerPrd: AutoCompleteTextView
    var simpleCursorAdapter: SimpleCursorAdapter? = null
    var cursor: Cursor? = null
    var id: Int = 0
    //lateinit var connector: Connection()
    //lateinit var allProducts: java.sql.Array

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apontamento_embalados1)


        val username = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE)

        var viewTotal = findViewById<TextView>(R.id.total)

        var pilha: EditText = findViewById(R.id.finalResult)
        var btnValidade = findViewById<EditText>(R.id.btnValidade)
        spinner = findViewById<Spinner>(R.id.menu)
        var spinnerID = findViewById<TextView>(R.id.spinnerIdSaver)
        var transporteID = findViewById<TextView>(R.id.tipoTransporteID)
        transporteID.text = "0"

        //spinnerPrd = findViewById<AutoCompleteTextView>(R.id.spinnerPrd)
        val dateVal: TextInputEditText = findViewById(R.id.btnValidade)
        var lote = findViewById<EditText>(R.id.editTextLote)
        var qeProduto = findViewById<EditText>(R.id.editTextEmbalagemCaixa)

        db = SQLiteHelper(this)

        var valueCxAvulsa = "0"
        var valueQtAvulsa = "0"
        val cxAvulsa = findViewById<TextView>(R.id.caixasAvulsas)
        val qtdAvulsa = findViewById<TextView>(R.id.qtdAvulsas)

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

        var tipoTranporte = arrayOf<String>("Pilha", "Pallet")

        var SpTipoTransporte = findViewById<AutoCompleteTextView>(R.id.tipoTransporte)

        var adapterTransporte = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, tipoTranporte)
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
                        findViewById<TextInputLayout>(R.id.textInputLayout22).apply { hint = "Pallet" }
                    }
                    else if (id==0){
                        findViewById<TextInputLayout>(R.id.textInputLayout22).apply { hint = "Pilhas" }
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

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy") //formatar data no formato padrão
        val dty = dateFormatter.format(Date())

        val dateFormatter0 = SimpleDateFormat("kk:mm") //formatar tempo no formato 24h (kk)
        val time = dateFormatter0.format(Date())

        var time0 = findViewById<TextView>(R.id.editTextHora).apply { text = time }
        var dty0 = findViewById<TextView>(R.id.editTextData).apply { text = dty }


        val dateFormatterProtheus = SimpleDateFormat("yyyyMMdd") //formatar data no formato padrão
        val dtyProtheus = dateFormatterProtheus.format(Date())
        var dtytime0 = "$dtyProtheus" + "$time"



        dateVal.setOnClickListener {
            //Exibir barra de progresso, pois nos testes o Date Picker demorou entre 1 a 2 segundos para aparecer.
            val progress =
                findViewById<ProgressBar>(R.id.progressInd).apply { visibility = View.VISIBLE }

            //parametros para o popup de calendario
            var datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("Selecione a data de vencimento")
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
                val progress = findViewById<ProgressBar>(R.id.progressInd).apply {
                    visibility = View.INVISIBLE
                }
            }
            datePicker.addOnCancelListener {
                val progress = findViewById<ProgressBar>(R.id.progressInd).apply {
                    visibility = View.INVISIBLE
                }
            }
            datePicker.addOnDismissListener {
                val progress = findViewById<ProgressBar>(R.id.progressInd).apply {
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
            var result = pilha.text.toString().toInt()
            if (result >= 0) {
                var finalResult = result + 1
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

        plusRtn.setOnLongClickListener{
            //se o campo estiver vazio, para evitar crash ao pressionar -, preencher com 0
            if (pilha.length() == 0) {
                pilha.text = Editable.Factory.getInstance().newEditable(0.toString())
            }
            var result = pilha.text.toString().toInt()
            if (result >= 0) {
                var finalResult = result + 10
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
        val buttonFinalizar: FloatingActionButton = findViewById(R.id.startActivityApontamentoPerdas)
        buttonFinalizar.setOnClickListener {
            var sync = Sync()
            var tipoID = findViewById<TextView>(R.id.tipoTransporteID)

            if ((spinnerID.text != "")
                && (lote.length() != 0)
                && (pilha.length() != 0)
                && (qeProduto.length() != 0)
                && (dty0.length() != 0)
                && (time0.length() != 0)
                && (dateVal.length() != 0)
                && (username != null)
            ) {

                var tipoID = findViewById<TextView>(R.id.tipoTransporteID)

                var tipoIDD: String
                tipoIDD = (if (tipoID.text == "0") "Pilha" else "Pallet").toString()

                "name, 2012, 2017".split(",").toTypedArray()
                var validade = "${btnValidade.text}".split("/").toTypedArray()
                var validadeProtheus = (validade[2] + validade[1] + validade[0]).toInt()


                var finalQuery: String =
                    "INSERT INTO ApontEmbalado (qtdApontada, tipoUnitizador, dataHoraApontamento, lote, caixaAvulsa, unidadeAvulsa, validade, total, idProduto, qeProduto, validProduto, tipoVProduto, username, statusSync) " +
                            "VALUES ('${pilha.text}', '${tipoIDD}', '${dtytime0}', ${
                                Integer.parseInt(
                                    lote.text.toString()
                                )
                            }, ${parseInt(cxAvulsa.text.toString())}, ${parseInt(qtdAvulsa.text.toString())}, ${validadeProtheus}, ${
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
                Toast.makeText(this, "Apontamento salvo", Toast.LENGTH_LONG).show()
                sync.sync(1, this)
                finish()
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
            }
        }


        connectionView()

    }

    fun connectionView(){
        var result = Sync().testConnection()

        if (result == "Falha ao conectar (Host and port combination not valid)" || result == "Sem conexão com o servidor/rede") {
            Snackbar.make(findViewById(R.id.CL),
                "Não foi possível estabelecer uma conexão com o servidor",
                TSnackbar.LENGTH_INDEFINITE
            ).setBackgroundTint(Color.parseColor("#741919")).setTextColor(Color.WHITE).setActionTextColor(Color.WHITE).setAction("OK"){}.show()
        }
    }

    fun setTotal(){
        val spinner: Int
        val cxAvulsa = findViewById<TextView>(R.id.caixasAvulsas)
        val qtdAvulsa = findViewById<TextView>(R.id.qtdAvulsas)
        var viewTotal = findViewById<TextView>(R.id.total)

        var valueCxAvulsa = "0"
        var valueQtAvulsa = "0"

        var pilha: EditText = findViewById(R.id.finalResult)
        var tipoID = findViewById<TextView>(R.id.tipoTransporteID)

        //spinnerPrd = findViewById<AutoCompleteTextView>(R.id.spinnerPrd)
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

        if ((qeProduto.length() > 0) && ((tipoID.text == "0") or (tipoID.text == "1"))){
            if (tipoID.text == "0") {
                spinner = 10
                var total = (parseInt(qeProduto.text.toString()) * spinner * parseInt(pilha.text.toString())) + parseInt(valueQtAvulsa.toString()) + (parseInt(valueCxAvulsa.toString()) * parseInt(qeProduto.text.toString()))
                Log.d("Debug campo total", "$total")
                viewTotal.text = total.toString()
            }
            else if (tipoID.text == "1"){
                spinner = 54
                var total = (parseInt(qeProduto.text.toString()) * spinner * parseInt(pilha.text.toString())) + parseInt(valueQtAvulsa.toString()) + ((parseInt(valueCxAvulsa.toString()) * parseInt(qeProduto.text.toString())))
                Log.d("Debug campo total", "$total")
                viewTotal.text = total.toString()
            }

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
                    setTotal()
                }

            }

    }


    fun overrideDataBlock(id: Int) {

        var validProduto: Int = 0
        var tipoVProduto: String = ""
        var diasValidade: Int = 0
        var adcVal: Int
        val QE_PROD = "qeProduto"

        connectionView()


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
}



