package com.example.neofin.ui.journal

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.ui.journal.neobis.NeobisJournalFragment
import com.example.neofin.ui.journal.neolabs.NeolabsJournalFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_journal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class JournalFragment : Fragment(R.layout.fragment_journal) {

    private var menu: Menu? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainScope().launch(Dispatchers.Main) {
            setUpTabs()
        }
    }

//    private fun configureTabLayout() {
//        tab_layout_journal.addTab(tab_layout_journal.newTab().setText("Neobis"))
//        tab_layout_journal.addTab(tab_layout_journal.newTab().setText("Neolabs"))
//        tab_layout_journal.isTabIndicatorFullWidth = false
//
//        val adapter = activity?.let {
//            AdapterTab(
//                it.supportFragmentManager,
//                tab_layout_journal.tabCount
//            )
//        }
//        pager_journal.adapter = adapter
//
//        pager_journal.addOnPageChangeListener(
//            TabLayout.TabLayoutOnPageChangeListener(tab_layout_journal)
//        )
//        tab_layout_journal.addOnTabSelectedListener(object :
//            TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                pager_journal.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//            }
//        })
//    }

    private fun setUpTabs() = CoroutineScope(Dispatchers.Main).launch{
        val adapter = activity?.let {
            AdapterTab(it.supportFragmentManager)
        }
        adapter?.addFragments(NeobisJournalFragment(), "Neobis")
        adapter?.addFragments(NeolabsJournalFragment(), "Neolabs")
        pager_journal.adapter = adapter
        tab_layout_journal.setupWithViewPager(pager_journal)
        tab_layout_journal.isTabIndicatorFullWidth = false

        tab_layout_journal.getTabAt(0)
//        tab_layout_journal.getTabAt(0)!!.setIcon(R.drawable.ic_add)
        tab_layout_journal.getTabAt(1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.open_filters, menu)
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.openFilter -> {
                findNavController().navigate(
                    R.id.action_navigation_journal_to_filtersFragment
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}