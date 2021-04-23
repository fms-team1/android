package com.example.neofin.ui.journal

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.ui.journal.all.AllJournalFragment
import com.example.neofin.ui.journal.neobis.NeobisJournalFragment
import com.example.neofin.ui.journal.neolabs.NeolabsJournalFragment
import kotlinx.android.synthetic.main.fragment_new_journal.*
import kotlinx.android.synthetic.main.fragment_new_journal.backButton
import kotlinx.android.synthetic.main.fragment_new_journal.filterOpen


class NewJournalFragment : Fragment(R.layout.fragment_new_journal) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()

        filterOpen.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_journal_to_filtersFragment
            )
        }

        backButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_navigation_journal_to_navigation_home
            )
        }

        setUpTabs()
    }

    private fun setUpTabs() {
        val adapter = ViewPageAdapter(childFragmentManager)
        adapter.addFragment(NeobisJournalFragment(), "")
        adapter.addFragment(AllJournalFragment(), "")
        adapter.addFragment(NeolabsJournalFragment(), "")

        viewPager.adapter = adapter
        tablayout.setupWithViewPager(viewPager)

        tablayout.getTabAt(0)?.setIcon(R.drawable.neobis)
        tablayout.getTabAt(1)?.setIcon(R.drawable.all)
        tablayout.getTabAt(2)?.setIcon(R.drawable.neolabs)

        for (i in 0 until tablayout.tabCount) {
            tablayout.getTabAt(i)?.setCustomView(R.layout.custom_tab)
        }

    }
}