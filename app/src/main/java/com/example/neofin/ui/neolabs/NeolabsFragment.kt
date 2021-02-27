package com.example.neofin.ui.neolabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.neofin.R
import com.example.neofin.ui.neobis.AdapterNeobisTab
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_neobis.*
import kotlinx.android.synthetic.main.fragment_neobis.tab_layout_neobis
import kotlinx.android.synthetic.main.fragment_neolabs.*

class NeolabsFragment : Fragment(R.layout.fragment_neolabs) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureTabLayout()
    }

    private fun configureTabLayout() {
        tab_layout_neolabs.addTab(tab_layout_neolabs.newTab().setText("Main"))
        tab_layout_neolabs.addTab(tab_layout_neolabs.newTab().setText("History"))
        tab_layout_neolabs.addTab(tab_layout_neolabs.newTab().setText("Diagram"))
        tab_layout_neolabs

        val adapter = activity?.let { AdapterNeolabsTab(it.supportFragmentManager, tab_layout_neolabs.tabCount) }
        pager_neolabs.adapter = adapter

        pager_neolabs.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tab_layout_neolabs))
        tab_layout_neolabs.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager_neolabs.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }
}