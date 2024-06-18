/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Activity para mostrar los Fragments, configurar el Toolbar y configurar el BottonNavigationView.
 */

package com.farkuzio58.guitarcatalog.ui.main

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.farkuzio58.guitarcatalog.R
import com.farkuzio58.guitarcatalog.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    val toolbar: Toolbar get() = binding.toolbar
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment

        navController = navHostFragment.navController
        var bnv = findViewById<BottomNavigationView>(R.id.bottonNavigationView)
        bnv.setupWithNavController(navController!!)

        bnv.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.nav_favoritos -> {
                    val bundle = bundleOf("fav" to "si")
                    navController!!.navigate(R.id.guitarListFragment, bundle)
                    true
                }
                R.id.nav_home -> {
                    //setCurrentFragment(homeFragment)
                    navController!!.navigate(R.id.mainFragment)
                    true
                }
                R.id.nav_buscar -> {
                    val bundle = bundleOf("search" to "si", "shape" to 5)
                    navController!!.navigate(R.id.guitarListFragment, bundle)
                    true
                }
                else -> false
            }
        }
        appBarConfiguration =
            AppBarConfiguration.Builder(navController!!.graph).build()

        NavigationUI.setupWithNavController(
            binding.toolbar, navController!!, appBarConfiguration
        )
    }

    override fun attachBaseContext(newBase: Context) {
        val languageCode = newBase.getSharedPreferences("guitarCatalog", Context.MODE_PRIVATE).getString("lan", Locale.getDefault().language)
        val context = updateLocale(newBase, languageCode!!)
        super.attachBaseContext(context)
    }

    private fun updateLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_options -> {
                navController!!.navigate(R.id.optionsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}