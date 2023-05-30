package com.liderMinas.PCP

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainNav : AppCompatActivity() {
    lateinit var username : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_nav)

        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));

            var view = getWindow().getDecorView();
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    getWindow().setNavigationBarColor(ContextCompat.getColor(this, com.google.android.material.R.color.material_dynamic_neutral_variant10));
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    getWindow().setNavigationBarColor(ContextCompat.getColor(this, com.google.android.material.R.color.material_dynamic_neutral_variant90));
                    view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
                }
            }

        }



        username = intent.getStringExtra(EXTRA_MESSAGE)!!


        val MainMenu=MainMenu(username)
        val Estatistica=Estatistica()
        val Requisicao=Requisicao()


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


        //updateBadge(this, username)
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