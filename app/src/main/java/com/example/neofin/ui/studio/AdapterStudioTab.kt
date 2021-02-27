package com.example.neofin.ui.studio

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AdapterStudioTab(fm: FragmentManager, private var tabCount: Int) : FragmentPagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
){
    override fun getCount(): Int {
        return tabCount
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainStudioFragment()
            1 -> HistoryStudioFragment()
            2 -> DiagramStudioFragment()
            else -> throw IllegalStateException("position $position is invalid for this viewpager")
        }
    }
}