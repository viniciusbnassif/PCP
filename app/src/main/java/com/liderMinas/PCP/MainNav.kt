

package com.liderMinas.PCP

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MainNav : AppCompatActivity() {
    lateinit var username : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nav)

        username = intent.getStringExtra(EXTRA_MESSAGE)!!



        /*
        val MainMenu=MainMenu(username)
        val Estatistica=Estatistica()
        val Requisicao=Requisicao(username, this)

        setCurrentFragment(MainMenu)

        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu->setCurrentFragment(MainMenu)
                R.id.relatorioNav->setCurrentFragment(Estatistica)
                R.id.requisicaoNav->setCurrentFragment(Requisicao)
            }
            true
        }

         */

        if (username == "vinicius.nassif" || username == "guilherme.augusto"){
            val MainMenu=MainMenu(username)
            val Estatistica=Estatistica()
            val Requisicao=Requisicao(username, this)

            setCurrentFragment(MainMenu)

            var bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigationView.setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.menu->setCurrentFragment(MainMenu)
                    R.id.relatorioNav->setCurrentFragment(Estatistica)
                    R.id.requisicaoNav->setCurrentFragment(Requisicao)
                }
                true
            }
        } else {
            val MainMenu=MainMenu(username)
            val Estatistica=Estatistica()
            //val Requisicao=Requisicao(username, this)

            setCurrentFragment(MainMenu)

            var bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
            bottomNavigationView.setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.menu->setCurrentFragment(MainMenu)
                    R.id.relatorioNav->setCurrentFragment(Estatistica)
                    R.id.requisicaoNav->setCurrentFragment(MainMenu)
                }
                true
            }
        }



        //updateBadge(this, username)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    /*fun btmSheet() {
        bottomSheetFragment = BottomSheetFragment()
        bottomSheetFragment.show(supportFragmentManager, "BSDialogFragment")
    }*/

    fun restartFragment() {
        var mainMenu = Intent(this, MainNav::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, username)
        }
        startActivity(mainMenu)
        finish()
    }

    fun restartFragmentReq() {
        var mainMenu = Intent(this, MainNav::class.java).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, username)
        }
        startActivity(mainMenu)
        finish()
    }




    override fun onDestroy() {
        super.onDestroy()
        SQLiteHelper(this).close()
    }

    override fun onPause() {
        super.onPause()
        SQLiteHelper(this).close()
    }





    /*fun getCount(count: Int){
        updateBadge(count)
    }
    fun updateBadge(count: Int) {
        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        var badge = bottomNavigationView.getOrCreateBadge(R.id.notificacoes)
        if (count != 0) {
            badge.isVisible = true
            // An icon only badge will be displayed unless a number or text is set:
            badge.number = count
        } else {
            badge.isVisible = false
        }
    }*/



    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment_activity_main_nav,fragment)
            commit()
        }
    public val mainNavContext = this

}


