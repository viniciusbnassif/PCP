package com.liderMinas.PCP

import android.R
import android.app.Activity
import android.database.Cursor
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.bottomappbar.BottomAppBar
import java.util.*


class ApontamentoPerdas : AppCompatActivity() {
    lateinit var db: SQLiteHelper
    //lateinit var simpleCursorAdapterPrd: SimpleCursorAdapter
    var simpleCursorAdapterPrd: SimpleCursorAdapter? = null
    //lateinit var spinnerM1: AutoCompleteTextView
    /*lateinit var spinnerM2: AutoCompleteTextView
    lateinit var spinnerM3: AutoCompleteTextView
    lateinit var spinnerM4: AutoCompleteTextView
    lateinit var spinnerM5: AutoCompleteTextView
    lateinit var spinnerM6: AutoCompleteTextView*/


    var cursorPrd: Cursor? = null
    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apontamento_perdas)

        // Hide the status bar.
        /*window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }*/

        db = SQLiteHelper(this)


        /*

        spinnerM2 = this.findViewById(R.id.motivo2)
        spinnerM3 = this.findViewById(R.id.motivo3)
        spinnerM4 = this.findViewById(R.id.motivo4)
        spinnerM5 = this.findViewById(R.id.motivo5)
        spinnerM6 = this.findViewById(R.id.motivo6)*/
        setOrRefreshSpinnerPrd()


            // calling the action bar
        val toolbar = findViewById<BottomAppBar?>(R.id.bottomAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
        }

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

        }


    private fun setOrRefreshSpinnerPrd() {
        // Get AutoCompleteTextView
        cursorPrd = db.getMotivo()

        var spinnerM2 = findViewById<AutoCompleteTextView>(R.id.motivo2)
        // Define from/to info
        //val nsColumns = arrayOf<String>(nameColumn)
        //val nsTo = intArrayOf(R.id.simpleDropdownItem)
        // Create adapter. Cursor set in setFilterQueryProvider() below.
        if (simpleCursorAdapterPrd == null) {
            simpleCursorAdapterPrd = SimpleCursorAdapter(
                this, android.R.layout.select_dialog_item,
                cursorPrd,
                arrayOf("descMotivo"),
                intArrayOf(android.R.id.text1), 0
            )
            // Set adapter on view.
            spinnerM2.setAdapter(simpleCursorAdapterPrd)

            // OnItemClickListener - User selected value from DropDown
            spinnerM2.setOnItemClickListener { listView, view, position, id ->
                // Get the cursor. Positioned to the corresponding row in the result set.
                val cursor = listView.getItemAtPosition(position) as Cursor
                // Get the name selected
                val selectedName = cursor.getString(cursor.getColumnIndexOrThrow("descMotivo"))
                // Do something with this value...
            }

            // Set the CursorToStringconverter, to provide the values for the choices to be displayed
            // in the AutoCompleteTextview.
            simpleCursorAdapterPrd!!.setCursorToStringConverter(object :
                SimpleCursorAdapter.CursorToStringConverter {
                override fun convertToString(cursor: Cursor): CharSequence? {
                    return cursor.getString(cursor.getColumnIndexOrThrow("descMotivo"))
                }
            })
        }

        // Set the FilterQueryProvider, to run queries for choices
        /*SimpleCursorAdapterPrd.setFilterQueryProvider(FilterQueryProvider { constraint ->
            dbHelper.getMatchingNames(
                outingId, checklistId, nameColumn,
                constraint?.toString()
            )
        })*/
    }

    /*fun setOrRefreshSpinner() {
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
            simpleCursorAdapter!!.setDropDownViewResource(android.R.layout.select_dialog_singlechoice)
            spinner.adapter = simpleCursorAdapter

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
                        overrideDataBlock()
                        Toast.makeText(
                            view.context,
                            "You selected ${
                                cursor!!.getString(cursor!!.getColumnIndex("descProduto"))
                            } with an id of $_id", Toast.LENGTH_SHORT
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

    override fun onResume() {
        super.onResume()
        setOrRefreshSpinnerPrd()
    }

    /* When the activity is destroyed then close the cursor as it will not be used again */
    override fun onDestroy() {
        super.onDestroy()
        if (!cursorPrd!!.isClosed) {
            cursorPrd!!.close()
        }
    }*/







    //método do botão de voltar da Action Bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        Animatoo.animateSlideRight(this);
        return true
    }
}

