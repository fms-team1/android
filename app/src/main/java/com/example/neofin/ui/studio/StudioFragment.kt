package com.example.neofin.ui.studio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.neofin.R
import com.example.neofin.ui.neolabs.AdapterNeolabsTab
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_neolabs.*
import kotlinx.android.synthetic.main.fragment_studio.*

class StudioFragment : Fragment(R.layout.fragment_studio) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureTabLayout()
    }

    private fun configureTabLayout() {
        tab_layout_studio.addTab(tab_layout_studio.newTab().setText("History"))
        tab_layout_studio.addTab(tab_layout_studio.newTab().setText("Diagram"))
        tab_layout_studio

        val adapter = activity?.let { AdapterStudioTab(it.supportFragmentManager, tab_layout_studio.tabCount) }
        pager_studio.adapter = adapter

        pager_studio.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tab_layout_studio))
        tab_layout_studio.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager_studio.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }
}