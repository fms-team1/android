package com.example.neofin.ui.neolabs

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AdapterNeolabsTab(fm: FragmentManager, private var tabCount: Int) : FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    override fun getCount(): Int {
        return tabCount
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainNeolabsFragment()
            1 -> HistoryNeolabsFragment()
            2 -> DiagramNeolabsFragment()
            else -> throw IllegalStateException("position $position is invalid for this viewpager")
        }
    }
}