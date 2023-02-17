package com.liderMinas.PCP

import android.annotation.SuppressLint
import android.database.Cursor
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cursoradapter.widget.SimpleCursorAdapter
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.liderMinas.PCP.database.queryMotivoExt
import java.util.*


class ApontamentoPerdas : AppCompatActivity() {


    lateinit var db: SQLiteHelper

    var simpleCursorAdapterPrd: SimpleCursorAdapter? = null
    var cursorPrd: Cursor? = null
    var idPerdas: Int = 0

    lateinit var motivo1: Spinner
    lateinit var motivo2: Spinner
    lateinit var motivo3: Spinner
    lateinit var motivo4: Spinner
    lateinit var motivo5: Spinner
    lateinit var motivo6: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.liderMinas.PCP.R.layout.activity_apontamento_perdas)

        queryMotivoExt(this)

        db = SQLiteHelper(this)
        motivo1 = findViewById(R.id.motivo1)
        motivo2 = findViewById(R.id.motivo2)
        motivo3 = findViewById(R.id.motivo3)
        motivo4 = findViewById(R.id.motivo4)
        motivo5 = findViewById(R.id.motivo5)
        motivo6 = findViewById(R.id.motivo6)



        // calling the action bar
        val toolbar = findViewById<BottomAppBar?>(com.liderMinas.PCP.R.id.bottomAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
        }


        /*val items = arrayOf("Item 1", "Item 2dois", "Item 3sasdasdasd", "Item 4aaaaaaaaaaaaaaaaaaa")
        (motivo1 as? MaterialAutoCompleteTextView)?.setSimpleItems(items)*/

        //motivo1.setInputType(InputType.TYPE_NULL)

        /*var back = findViewById<Button>(R.id.fabBack)
    back.setOnClickListener {


    }*/


/*var motivo1 = findViewById<AutoCompleteTextView>(R.id.motivo1)
val items = arrayOf("Item 1", "Item 2dois", "Item 3sasdasdasd", "Item 4aaaaaaaaaaaaaaaaaaa")
(motivo1 as? MaterialAutoCompleteTextView)?.setSimpleItems(items)

//motivo1.setInputType(InputType.TYPE_NULL)*/

        /*var back = findViewById<Button>(R.id.fabBack)
back.setOnClickListener {


}*/

        intent = getIntent();

        var name = intent.getStringExtra("messageIntent")


        val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
        val dty = dateFormatter.format(Date())

        findViewById<TextView>(R.id.finalDate).apply { text = dty }

        val dateFormatter0 = SimpleDateFormat("kk:mm")
        val time = dateFormatter0.format(Date())

        findViewById<TextView>(R.id.finalTime).apply { text = time }

        var adapter = setOrRefreshSpinnerPrd1()
        /*setOrRefreshSpinnerPrd2()
        setOrRefreshSpinnerPrd3()
        setOrRefreshSpinnerPrd4()
        setOrRefreshSpinnerPrd5()
        setOrRefreshSpinnerPrd6()*/

        var idP1: Int
        var idP2: Int = 0
        var idP3: Int = 0
        var idP4: Int = 0
        var idP5: Int = 0
        var idP6: Int = 0

        //cursorPrd = db.getMotivo()

        //val DESC_MOTIVO = "descMotivo"
    }


        fun setOrRefreshSpinnerPrd1() {
            cursorPrd = db.getMotivo()
            if (simpleCursorAdapterPrd == null) {
                simpleCursorAdapterPrd = SimpleCursorAdapter(
                    this,
                    android.R.layout.select_dialog_item,
                    cursorPrd,
                    arrayOf("descMotivo"),
                    intArrayOf(android.R.id.text1),
                    0
                )
                motivo1.adapter = simpleCursorAdapterPrd
                motivo2.adapter = simpleCursorAdapterPrd
                motivo3.adapter = simpleCursorAdapterPrd
                motivo4.adapter = simpleCursorAdapterPrd
                motivo5.adapter = simpleCursorAdapterPrd
                motivo6.adapter = simpleCursorAdapterPrd
            } else {
                /* if refreshing rather than setting up, then tell the adapter about the changed cursor */
                simpleCursorAdapterPrd!!.swapCursor(cursorPrd)
            }
            /*motivo1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                @SuppressLint("Range")
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    _id: Long
                ) {
                    if (view?.context != null) {
                        var idP1 = _id.toInt()
                        Toast.makeText(
                            view.context,
                            "You selected ${
                                cursorPrd!!.getString(cursorPrd!!.getColumnIndex("descMotivo"))
                            } with an id of $_id", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }*/
        }


        /*

    fun setOrRefreshSpinnerPrd2() {
        //cursorPrd = db.getMotivo()
        //cursorPrd = db.getMotivo()

        if (simpleCursorAdapterPrd == null) {
            simpleCursorAdapterPrd = SimpleCursorAdapter(
                this,
                android.R.layout.select_dialog_item,
                cursorPrd,
                arrayOf("descMotivo"),
                intArrayOf(android.R.id.text1),
                0
            )
            motivo2.adapter = simpleCursorAdapterPrd


        } else {
            /* if refreshing rather than setting up, then tell the adapter about the changed cursor */
            simpleCursorAdapterPrd!!.swapCursor(cursorPrd)
        }
        motivo2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("Range")
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                _id: Long
            ) {
                if (view?.context != null) {
                    var idP2 = _id.toInt()
                    Toast.makeText(
                        view.context,
                        "You selected ${
                            cursorPrd!!.getString(cursorPrd!!.getColumnIndex("descMotivo"))
                        } with an id of $_id", Toast.LENGTH_SHORT
                    ).show()


                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    fun setOrRefreshSpinnerPrd3() {

        //cursorPrd = db.getMotivo()
        if (simpleCursorAdapterPrd == null) {
            simpleCursorAdapterPrd = SimpleCursorAdapter(
                this,
                android.R.layout.select_dialog_item,
                cursorPrd,
                arrayOf("descMotivo"),
                intArrayOf(android.R.id.text1),
                0
            )
            motivo3.adapter = simpleCursorAdapterPrd


        } else {
            /* if refreshing rather than setting up, then tell the adapter about the changed cursor */
            simpleCursorAdapterPrd!!.swapCursor(cursorPrd)
        }
        motivo3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("Range")
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                _id: Long
            ) {
                if (view?.context != null) {
                    var idP3 = _id.toInt()
                    Toast.makeText(
                        view.context,
                        "You selected ${
                            cursorPrd!!.getString(cursorPrd!!.getColumnIndex("descMotivo"))
                        } with an id of $_id", Toast.LENGTH_SHORT
                    ).show()


                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    fun setOrRefreshSpinnerPrd4() {

        //cursorPrd = db.getMotivo()
        if (simpleCursorAdapterPrd == null) {
            simpleCursorAdapterPrd = SimpleCursorAdapter(
                this,
                android.R.layout.select_dialog_item,
                cursorPrd,
                arrayOf("descMotivo"),
                intArrayOf(android.R.id.text1),
                0
            )
            motivo4.adapter = simpleCursorAdapterPrd


        } else {
            /* if refreshing rather than setting up, then tell the adapter about the changed cursor */
            simpleCursorAdapterPrd!!.swapCursor(cursorPrd)
        }
        motivo4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("Range")
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                _id: Long
            ) {
                if (view?.context != null) {
                    var idP4 = _id.toInt()
                    Toast.makeText(
                        view.context,
                        "You selected ${
                            cursorPrd!!.getString(cursorPrd!!.getColumnIndex("descMotivo"))
                        } with an id of $_id", Toast.LENGTH_SHORT
                    ).show()


                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    fun setOrRefreshSpinnerPrd5() {

        if (simpleCursorAdapterPrd == null) {
            simpleCursorAdapterPrd = SimpleCursorAdapter(
                this,
                android.R.layout.select_dialog_item,
                cursorPrd,
                arrayOf("descMotivo"),
                intArrayOf(android.R.id.text1),
                0
            )
            motivo5.adapter = simpleCursorAdapterPrd


        } else {
            /* if refreshing rather than setting up, then tell the adapter about the changed cursor */
            simpleCursorAdapterPrd!!.swapCursor(cursorPrd)
        }
        motivo5.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("Range")
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                _id: Long
            ) {
                if (view?.context != null) {
                    var idP5 = _id.toInt()
                    Toast.makeText(
                        view.context,
                        "You selected ${
                            cursorPrd!!.getString(cursorPrd!!.getColumnIndex("descMotivo"))
                        } with an id of $_id", Toast.LENGTH_SHORT
                    ).show()


                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    fun setOrRefreshSpinnerPrd6() {

        if (simpleCursorAdapterPrd == null) {
            simpleCursorAdapterPrd = SimpleCursorAdapter(
                this,
                android.R.layout.select_dialog_item,
                cursorPrd,
                arrayOf("descMotivo"),
                intArrayOf(android.R.id.text1),
                0
            )
            motivo6.adapter = simpleCursorAdapterPrd


        } else {
            /* if refreshing rather than setting up, then tell the adapter about the changed cursor */
            simpleCursorAdapterPrd!!.swapCursor(cursorPrd)
        }
        motivo6.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("Range")
            override fun onItemSelected(
                p0: AdapterView<*>?,
                view: View?,
                position: Int,
                _id: Long
            ) {
                if (view?.context != null) {
                    var idP6 = _id.toInt()
                    Toast.makeText(
                        view.context,
                        "You selected ${
                            cursorPrd!!.getString(cursorPrd!!.getColumnIndex("descMotivo"))
                        } with an id of $_id", Toast.LENGTH_SHORT
                    ).show()


                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    */


        /*override fun onResume() {
        super.onResume()
        setOrRefreshSpinnerPrd()
    }*/

        /* When the activity is destroyed then close the cursor as it will not be used again */
        override fun onSupportNavigateUp(): Boolean {
            onBackPressed()
            Animatoo.animateSlideRight(this);
            return true
        }

}




    //método do botão de voltar da Action Bar


