package com.example.neofin.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.neofin.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var floatingActionButton: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.floatingActionButton = fab

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_chart, R.id.navigation_journal, R.id.navigation_user
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        nav_host_fragment.findNavController()
            .addOnDestinationChangedListener { _, destination, _ ->
                when(destination.id){
                    R.id.navigation_home,
                    R.id.navigation_chart,
                    R.id.navigation_journal,
                    R.id.navigation_user ->
                        bottomAppBar.visibility = View.VISIBLE
                    else -> bottomAppBar.visibility = View.GONE
                }

                when(destination.id){
                    R.id.navigation_home,
                    R.id.navigation_chart,
                    R.id.navigation_journal,
                    R.id.navigation_user ->
                        fab.visibility = View.VISIBLE
                    else -> fab.visibility = View.GONE
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        floatingActionButton.setOnClickListener {
            navController.navigate(
                R.id.addingFragment
            )
        }
    }
}