package com.liderMinas.PCP

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.bottomappbar.BottomAppBar
import java.util.*


class ApontamentoPerdas : AppCompatActivity() {
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


            // calling the action bar
        val toolbar = findViewById<BottomAppBar?>(R.id.bottomAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
        }


            /*var back = findViewById<Button>(R.id.fabBack)
        back.setOnClickListener {


        }*/

            intent = getIntent();

            var name = intent.getStringExtra("messageIntent")


            val dateFormatter = SimpleDateFormat("dd/MM/yyyy")
            val dty = dateFormatter.format(Date())

            //findViewById<TextView>(R.id.finalDate).apply { text = dty }

            val dateFormatter0 = SimpleDateFormat("kk:mm")
            val time = dateFormatter0.format(Date())

            //findViewById<TextView>(R.id.finalTime).apply { text = time }

        }






    //método do botão de voltar da Action Bar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        Animatoo.animateSlideRight(this);
        return true
    }
}