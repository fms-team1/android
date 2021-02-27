package com.example.neofin.ui.neobis

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AdapterNeobisTab(fm: FragmentManager, private var tabCount: Int) : FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    override fun getCount(): Int {
        return tabCount
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainNeobisFragment()
            1 -> HistoryNeobisFragment()
            2 -> DiagramNeobisFragment()
            else -> throw IllegalStateException("position $position is invalid for this viewpager")
        }
    }
}