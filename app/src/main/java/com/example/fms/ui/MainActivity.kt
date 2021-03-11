package com.example.fms.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.fms.R
import com.example.fms.ui.chart.ChartFragment
import com.example.fms.ui.home.HomeFragment
import com.example.fms.ui.journal.JournalFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.fms.ui.addTransactions.AddingFragment
import com.example.fms.ui.user.UserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                        nav_view.visibility = View.VISIBLE
                    else -> nav_view.visibility = View.GONE
                }
            }
    }
}