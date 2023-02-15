package com.liderMinas.PCP

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDate
import java.util.*
import java.text.*


class ApontamentoEmbalados1 : AppCompatActivity() {
    lateinit var db: SQLiteHelper
    lateinit var spinner: Spinner
    var simpleCursorAdapter: SimpleCursorAdapter? = null
    var cursor: Cursor? = null
    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apontamento_embalados1)

        db = SQLiteHelper(this)

        spinner = findViewById<Spinner>(R.id.menu)
        var adapter =
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


        val dateVal: TextInputEditText = findViewById(R.id.btnValidade)
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

        val plusRtn: Button = findViewById(R.id.plusBtn)
        plusRtn.setOnClickListener {
            var pilha: EditText = findViewById(R.id.finalResult)

            //se o campo estiver vazio, para evitar crash ao pressionar +, preencher com 0
            if (pilha.length() == 0) {
                pilha.text = Editable.Factory.getInstance().newEditable(0.toString())
            }
            var result = pilha.text.toString().toInt()
            if (result >= 0) {
                var finalResult = result + 1
                pilha.text = Editable.Factory.getInstance().newEditable(finalResult.toString())
                if (finalResult > 0) {
                    var minusBtnAble =
                        findViewById<Button>(R.id.minusBtn).apply { isEnabled = true }
                } else if (finalResult <= 0) {
                    var minusBtnAble =
                        findViewById<Button>(R.id.minusBtn).apply { isEnabled = false }
                }
            }
        }


        val minusRtn: Button = findViewById(R.id.minusBtn)
        minusRtn.setOnClickListener {
            var pilha: EditText = findViewById(R.id.finalResult)

            //se o campo estiver vazio, para evitar crash ao pressionar -, preencher com 0
            if (pilha.length() == 0) {
                pilha.text = Editable.Factory.getInstance().newEditable(0.toString())
            }
            var result = pilha.text.toString().toInt()
            if (result >= 1) {
                var finalResult = result - 1
                pilha.text = Editable.Factory.getInstance().newEditable(finalResult.toString())
                if (finalResult > 0) {
                    var minusBtnAble =
                        findViewById<Button>(R.id.minusBtn).apply { isEnabled = true }
                } else if (finalResult <= 0) {
                    var minusBtnAble =
                        findViewById<Button>(R.id.minusBtn).apply { isEnabled = false }
                }
            }
        }

        val messageIntent = ""

        val buttonAvancar: FloatingActionButton = findViewById(R.id.startActivityApontamentoPerdas)
        buttonAvancar.setOnClickListener {
            val intent = Intent(this, ApontamentoPerdas::class.java)
            intent.putExtra("messageIntent", messageIntent)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }


    }



    //Insere dados no Spinner
    fun setOrRefreshSpinner() {
        cursor = db.getProdutos()
        val DESC_PROD = "descProduto"
        if (simpleCursorAdapter == null) {
            simpleCursorAdapter = SimpleCursorAdapter(
                this,
                android.R.layout.select_dialog_item,
                cursor,
                arrayOf(DESC_PROD),
                intArrayOf(android.R.id.text1),
                0
            )
            spinner.adapter = simpleCursorAdapter //spinner recebe os dados para exibição

            //ao selecionar um item
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                @SuppressLint("Range")
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    _id: Long
                ) {
                    if (view?.context != null) {
                        id = _id.toInt()
                        overrideDataBlock(id) //inicia metodo passando o id do item selecionado
                        Toast.makeText(
                            view.context,
                            "Você selecionou ${
                                cursor!!.getString(cursor!!.getColumnIndex("descProduto"))
                            } com o código $_id", Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        } else {
            /* if refreshing rather than setting up, then tell the adapter about the changed cursor */
            simpleCursorAdapter!!.swapCursor(cursor)
        }
    }

    override fun onResume() {
        super.onResume()
        setOrRefreshSpinner()
    }

    /* When the activity is destroyed then close the cursor as it will not be used again */
    override fun onDestroy() {
        super.onDestroy()
        if (!cursor!!.isClosed) {
            cursor!!.close()
        }
    }


    fun overrideDataBlock(id: Int) {
        var qeProduto: String //findViewById<EditText>(R.id.editTextEmbalagemCaixa)
        var validProduto: Int = 0
        var tipoVProduto: String = ""
        var diasValidade: Int = 0
        var adcVal: Int
        val QE_PROD = "qeProduto"


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
        var lote2 = lote0 + "${lote1.subSequence(2, 4).toString()}"
        if (lote2.length <= 4) {
            loteF = "0${lote2}"
        } else {
            loteF = lote2
        }
        findViewById<EditText>(R.id.editTextLote).setText(loteF)


        var validF: String
        var btnValidade = findViewById<EditText>(R.id.btnValidade)


        var cursor2 = db.getDetailProdutos(id.toInt())
        cursor2.moveToFirst()
        findViewById<EditText>(R.id.editTextEmbalagemCaixa).setText("0")
        if (cursor2 != null) {
            //qeProduto = ("${cursor2.getInt(0)}")
            findViewById<EditText>(R.id.editTextEmbalagemCaixa).setText("${cursor2!!.getInt(1)}")
            validProduto = cursor2!!.getInt(2)
            tipoVProduto = cursor2!!.getString(3).toString()
        }


        if (validProduto != null) {
            if (validProduto != 0) {
                if (tipoVProduto == "D") {
                    adcVal = validProduto
                    dateNow.add(Calendar.DAY_OF_MONTH, adcVal).toString()
                    validF = SimpleDateFormat("dd/MM/yyyy").format(dateNow.getTime())
                    btnValidade.setText(validF)
                } else if (tipoVProduto == "S") {
                    adcVal = validProduto * 7
                    dateNow.add(Calendar.DAY_OF_MONTH, adcVal).toString()
                    validF = SimpleDateFormat("dd/MM/yyyy").format(dateNow.getTime())
                    btnValidade.setText(validF)
                } else if (tipoVProduto == "M") {
                    adcVal = validProduto
                    adcVal = validProduto
                    dateNow.add(Calendar.MONTH, adcVal).toString()
                    validF = SimpleDateFormat("dd/MM/yyyy").format(dateNow.getTime())
                    btnValidade.setText(validF)
                } else if (tipoVProduto == "A") {
                    adcVal = validProduto
                    dateNow.add(Calendar.YEAR, adcVal).toString()
                    validF = SimpleDateFormat("dd/MM/yyyy").format(dateNow.getTime())
                    btnValidade.setText(validF)
                }
            }
        }
    }

    //método do botão de voltar da Action Bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}



