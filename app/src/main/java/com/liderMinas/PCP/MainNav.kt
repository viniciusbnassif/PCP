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
import com.google.android.material.navigationrail.NavigationRailView


class MainNav : AppCompatActivity() {
    lateinit var username : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nav)

        username = intent.getStringExtra(EXTRA_MESSAGE)!!



        val MainMenu=MainMenu()
        val Estatistica=Estatistica()
        val Requisicao=Requisicao(username, this)

        setCurrentFragment(Estatistica)

        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu->setCurrentFragment(Estatistica)
                R.id.relatorioNav->setCurrentFragment(Estatistica)
                R.id.requisicaoNav->setCurrentFragment(Requisicao)
            }
            true
        }
        var navRail = findViewById<NavigationRailView>(R.id.nav_viewRail)
        navRail.setOnItemSelectedListener{
            when(it.itemId){
                R.id.menu->setCurrentFragment(Estatistica)
                R.id.relatorioNav->setCurrentFragment(Estatistica)
                R.id.requisicaoNav->setCurrentFragment(Requisicao)
            }
            true
        }

        //updateBadge(this, username)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        var navRail = findViewById<NavigationRailView>(R.id.nav_viewRail)
        var correction = findViewById<View>(R.id.viewCorrection)


        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            navRail.visibility = View.VISIBLE
            correction.visibility = View.VISIBLE
            bottomNavigationView.visibility = View.GONE
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            navRail.visibility = View.GONE
            correction.visibility = View.GONE
            bottomNavigationView.visibility = View.VISIBLE
        }
    }


    fun restartFragment() {
        var mainMenu = Intent(this, MainNav::class.java).apply {
            putExtra(EXTRA_MESSAGE, username)
        }
        startActivity(mainMenu)
        finish()
    }

    fun restartFragmentReq() {
        var mainMenu = Intent(this, MainNav::class.java).apply {
            putExtra(EXTRA_MESSAGE, username)
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

    private fun setCurrentFragment(fragment: Fragment) {
        var name = fragment.toString().substring(0, 5)
        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        var navRail = findViewById<NavigationRailView>(R.id.nav_viewRail)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment_activity_main_nav, fragment)
            commit()
        }
        if (name == "MainM"){
            bottomNavigationView.getMenu().findItem(R.id.menu).setChecked(true)
            navRail.getMenu().findItem(R.id.menu).setChecked(true)
        } else if (name == "Estat"){
            bottomNavigationView.getMenu().findItem(R.id.relatorioNav).setChecked(true)
            navRail.getMenu().findItem(R.id.relatorioNav).setChecked(true)
        } else if (name == "Requi"){
            bottomNavigationView.getMenu().findItem(R.id.requisicaoNav).setChecked(true)
            navRail.getMenu().findItem(R.id.requisicaoNav).setChecked(true)
        }
    }
    val mainNavContext = this

}


