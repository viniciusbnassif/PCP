package com.liderMinas.PCP

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
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
import com.liderMinas.PCP.database.MyHelper
import java.util.*


class ApontamentoEmbalados1 : AppCompatActivity() {
    private lateinit var idPrd: EditText
    private lateinit var descPrd: EditText
    private lateinit var qePrd: EditText
    private lateinit var validPrd: EditText
    private lateinit var tipoVPrd: EditText
    //private lateinit var validPrd: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apontamento_embalados1)




        /*
        VERSÃO 2, AGUARDE, DEVE ESTAR ERRADA.
        var helper = MyHelper(applicationContext)

        var db: SQLiteDatabase = helper.readableDatabase
        val produtoTodos = db.rawQuery("SELECT * FROM Produto", null)
        val produtosArray = resources.get(produtoTodos)

        val spinner = findViewById<Spinner>(R.id.menu)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, produtosArray)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        this@ApontamentoEmbalados1,
                        "Item selecionado" + produtosArray[position], Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }*/



                //var items = ArrayAdapter<Cursor>(this, android.R.layout.simple_spinner_item, produtoTodos)


        //Criar barra de ações
        val toolbar = findViewById<BottomAppBar?>(R.id.apontEmbaladosBottomBar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
        }

        //val items = ArrayAdapter(this@ApontamentoEmbalados1, R.layout.activity_apontamento_embalados1, R.id.menu, getAllProdutos)
        //optmenu.adapter = items
        //val optmenu = findViewById<MenuDropDownListView>(R.id.menu).apply { adapter= items }
        //(optmenu.editText as? MaterialAutoCompleteTextView)?.setSimpleItems(items)

        val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
        val dty = dateFormatter.format(Date())

        val dateFormatter0 = SimpleDateFormat("kk:mm")
        val time = dateFormatter0.format(Date())

        var time0 = findViewById<TextView>(R.id.editTextHora).apply { text = time }
        var dty0 = findViewById<TextView>(R.id.editTextData).apply {text = dty}


        val dateVal: TextInputEditText = findViewById(R.id.btnValidade)
        dateVal.setOnClickListener {
            //Exibir barra de progresso
            val progress = findViewById<ProgressBar>(R.id.progressInd).apply {visibility = View.VISIBLE}

            //parametros para o popup de calendario
            var datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("Selecione a data de vencimento")
                    .build()

            //abre popup
            datePicker.show(supportFragmentManager, "materialDatePicker")

            datePicker.addOnPositiveButtonClickListener {
                val dateFormatter : SimpleDateFormat
                val zone: Calendar
                val date : String


                dateFormatter = SimpleDateFormat("dd/MM/yyyy")
                //zone = Calendar.getInstance(TimeZone.getTimeZone("GMT-3"))
                date = dateFormatter.format(Date(it))

                //val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
                //val dty = dateFormatter.format(Date())

                //date = dateFormatter.format(Date(it))
                //val df = TimeZone(zone)
                //Toast.makeText(this, "$date is selected", Toast.LENGTH_LONG).show()


                var array = date.split("/").toTypedArray()

                var intArrayDia = array[0].toString().toInt()
                var intArrayMes = array[1].toString().toInt()
                var intArrayAno = array[2].toString().toInt()
                var arrayNbr = 1

                //Se o mês tiver 31 dias (jan, mar, mai, jul, ago, out, dez), verificar

                if ((array[1] == "01") or (array[1] == "03") or (array[1] == "05") or (array[1] == "07") or (array[1] == "08") or (array[1] == "10") or (array[1] == "12")){
                    //Se o Dia for entre 01 a 30 adicionar mais 1.
                    if ((intArrayDia >= 1) and (intArrayDia <= 30)){
                        intArrayDia += 1
                    } //Senão-se o dia for == 31, adiciona mais um ao mês (exceto se for dezembro), e faz o dia virar 01.
                    else if ((array [0] == "31")){
                        if ((array[1] != "12")){
                            intArrayMes += 1
                            intArrayDia = 1
                        }else{ //se o mês for dezembro (e o dia for 31), passa o mês janeiro, dia 01, ano seguinte
                            intArrayDia = 1
                            intArrayMes = 1
                            intArrayAno +=1
                        }

                    }
                    //Se o mês tiver apenas 30 dias, e se o dia for entre 01 a 29 adicionar mais um ao dia.
                }else if ((array[1] == "04") or (array[1] == "06") or (array[1] == "09") or (array[1] == "11")) {
                    if ((intArrayDia >= 1) and (intArrayDia <= 29)) {
                        intArrayDia += 1
                    } else if (array[0] == "30") {//Senão-se o dia for == 30, adiciona mais um ao mês (exceto se for dezembro), e faz o dia virar 01.
                        intArrayDia = 1
                        intArrayMes += 1
                    }
                }else if (array[1]== "02"){
                    if ((array[2] == "2024") or (array[2] == "2028") or (array[2] == "2032")){
                        if ((intArrayDia >= 1) and (intArrayDia <=28)){
                            intArrayDia+=1
                        }else if (intArrayDia == 29){
                            intArrayDia = 1
                            intArrayMes = 3
                        }
                    }else{
                        if ((intArrayDia >= 1) and (intArrayDia <=27)){
                            intArrayDia+=1
                        }else if (intArrayDia == 28){
                            intArrayDia = 1
                            intArrayMes = 3
                        }
                    }
                }

                //=====================================================


                var intArrayDiaSt = intArrayDia.toString()
                var intArrayMesSt = intArrayMes.toString()
                var intArrayAnoSt = intArrayAno.toString()

                if (intArrayDiaSt.length == 1){
                    intArrayDiaSt = "0$intArrayDia"
                }
                if (intArrayMesSt.length == 1){
                    intArrayMesSt = "0$intArrayMes"
                }

                var finalDate = "$intArrayDiaSt/$intArrayMesSt/$intArrayAnoSt"

                Toast.makeText(this, "$finalDate is selected", Toast.LENGTH_LONG).show()


                //val dateFinal.text = Editable.Factory.getInstance().newEditable(date.toString())
                val dateValReturn = findViewById<TextInputEditText>(R.id.btnValidade)
                dateValReturn.text = Editable.Factory.getInstance().newEditable(finalDate.toString())

                val progress = findViewById<ProgressBar>(R.id.progressInd).apply {visibility = View.INVISIBLE}
            }
            datePicker.addOnCancelListener{
                val progress = findViewById<ProgressBar>(R.id.progressInd).apply {visibility = View.INVISIBLE}
            }
            datePicker.addOnDismissListener{
                val progress = findViewById<ProgressBar>(R.id.progressInd).apply {visibility = View.INVISIBLE}
            }

        }

        //====================================================================================================

        val plusRtn: Button = findViewById(R.id.plusBtn)
        plusRtn.setOnClickListener{
            var pilha : EditText = findViewById(R.id.finalResult)

            //se o campo estiver vazio, para evitar crash, preencher com 0
            if (pilha.length() == 0){
                pilha.text = Editable.Factory.getInstance().newEditable(0.toString())
            }
            var result = pilha.text.toString().toInt()
            if (result>=0) {
                var finalResult = result + 1
                pilha.text = Editable.Factory.getInstance().newEditable(finalResult.toString())
                if (finalResult>0){
                    var minusBtnAble = findViewById<Button>(R.id.minusBtn).apply { isEnabled = true }
                }
                else if (finalResult<=0){
                    var minusBtnAble = findViewById<Button>(R.id.minusBtn).apply { isEnabled = false }
                }
            }
        }


        val minusRtn: Button = findViewById(R.id.minusBtn)
        minusRtn.setOnClickListener {
            var pilha: EditText = findViewById(R.id.finalResult)

            //se o campo estiver vazio, para evitar crash, preencher com 0
            if (pilha.length() == 0){
                pilha.text = Editable.Factory.getInstance().newEditable(0.toString())
            }
            var result = pilha.text.toString().toInt()
            if (result>=1){
                var finalResult = result - 1
                pilha.text = Editable.Factory.getInstance().newEditable(finalResult.toString())
                if (finalResult>0){
                    var minusBtnAble = findViewById<Button>(R.id.minusBtn).apply { isEnabled = true }
                }
                else if (finalResult<=0){
                    var minusBtnAble = findViewById<Button>(R.id.minusBtn).apply { isEnabled = false }
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

    /*private fun getProdutos(){
        val prdList = Database.getAllProdutos()

    }


    private fun initView(){
        idPrd = findViewById(R.id.idProduto)
        descPrd = findViewById(R.id.descProduto)
        qePrd = findViewById(R.id.qeProduto)
        validPrd = findViewById(R.id.validProduto)
        tipoVPrd = findViewById(R.id.tipoVProduto)





        //var optmenu = findViewById<MenuDropDownListView>(R.id.optmenu)
        val getAllProdutos = SQLiteHelper(this)
        getAllProdutos.getAllProdutos()
        Log.d("ResultadoSelectProduto =", getAllProdutos.getAllProdutos().toString())
        //val items = ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, ProdutoModelo<Any>())*/




    //método do botão de voltar da Action Bar
   override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
       return true
   }
}

