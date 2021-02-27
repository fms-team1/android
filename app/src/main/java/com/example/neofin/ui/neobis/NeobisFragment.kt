package com.example.neofin.ui.neobis

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.neofin.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_neobis.*

class NeobisFragment : Fragment(R.layout.fragment_neobis) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureTabLayout()
    }

    private fun configureTabLayout() {
        tab_layout_neobis.addTab(tab_layout_neobis.newTab().setText("History"))
        tab_layout_neobis.addTab(tab_layout_neobis.newTab().setText("Diagram"))
        tab_layout_neobis

        val adapter = activity?.let { AdapterNeobisTab(it.supportFragmentManager, tab_layout_neobis.tabCount) }
        pager_neobis.adapter = adapter

        pager_neobis.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tab_layout_neobis))
        tab_layout_neobis.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager_neobis.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }
}