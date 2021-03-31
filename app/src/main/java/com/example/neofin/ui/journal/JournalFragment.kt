package com.example.neofin.ui.journal

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neofin.R
import com.example.neofin.adapters.AllJournalAdapter
import com.example.neofin.adapters.JournalAdapter
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.journal.AllJournalItem
import com.example.neofin.retrofit.data.journal.JournalItem
import com.example.neofin.utils.logs
import kotlinx.android.synthetic.main.fragment_journal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JournalFragment : Fragment(R.layout.fragment_journal) {

    private var menu: Menu? = null
    private val adapter by lazy { JournalAdapter() }

    private val allAdapter by lazy { AllJournalAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        journalPb.visibility = View.VISIBLE
        constraintLayout.visibility = View.INVISIBLE
        journalRV.visibility = View.INVISIBLE

        MainScope().launch(Dispatchers.Main) {
            getJournal()
//            getJournalBySection("NEOBIS")
            setupAdapter()
            setupAllAdapter()
            neobis.setOnClickListener {
                neobisClicked.visibility = View.VISIBLE
                lineNeobis.visibility = View.VISIBLE
                neobis.visibility = View.INVISIBLE

                neolabs.visibility = View.VISIBLE
                neolabsClicked.visibility = View.GONE
                lineNeolabs.visibility = View.GONE

                all.visibility = View.VISIBLE
                allClicked.visibility = View.GONE
                lineAll.visibility = View.GONE

                getJournalBySection("NEOBIS")
                journalPb.visibility = View.VISIBLE
                journalRV.visibility = View.INVISIBLE
                allJournalRV.visibility = View.INVISIBLE
            }

            all.setOnClickListener {
                neobisClicked.visibility = View.INVISIBLE
                lineNeobis.visibility = View.INVISIBLE
                neobis.visibility = View.VISIBLE

                neolabs.visibility = View.VISIBLE
                neolabsClicked.visibility = View.INVISIBLE
                lineNeolabs.visibility = View.GONE

                all.visibility = View.INVISIBLE
                allClicked.visibility = View.VISIBLE
                lineAll.visibility = View.VISIBLE

                getJournal()
                journalPb.visibility = View.VISIBLE
                journalRV.visibility = View.INVISIBLE
                allJournalRV.visibility = View.INVISIBLE
            }

            neolabs.setOnClickListener {
                neobisClicked.visibility = View.INVISIBLE
                lineNeobis.visibility = View.INVISIBLE
                neobis.visibility = View.VISIBLE

                neolabs.visibility = View.INVISIBLE
                neolabsClicked.visibility = View.VISIBLE
                lineNeolabs.visibility = View.VISIBLE

                all.visibility = View.VISIBLE
                allClicked.visibility = View.INVISIBLE
                lineAll.visibility = View.INVISIBLE
                getJournalBySection("NEOLABS")
                journalPb.visibility = View.VISIBLE
                journalRV.visibility = View.INVISIBLE
                allJournalRV.visibility = View.INVISIBLE
            }

            allAdapter.setOnItemClickListener {
                val bundle = Bundle().apply {
                    putInt("idJournal", it.id)
                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_journalFragment_to_journalByIdFragment,
                    bundle
                )
            }

            adapter.setOnItemClickListener {
                val bundle = Bundle().apply {
                    putInt("idJournal", it.id)
                }
                Navigation.findNavController(requireView()).navigate(
                    R.id.action_journalFragment_to_journalByIdFragment,
                    bundle
                )
            }
        }
    }

    private fun getJournalBySection(section: String) = CoroutineScope(Dispatchers.Main).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getJournalBySection(token, section).enqueue(object : Callback<MutableList<JournalItem>> {
            override fun onResponse(
                call: Call<MutableList<JournalItem>>,
                response: Response<MutableList<JournalItem>>
            ) {
                response.body()?.let {
                    adapter.differ.submitList(it)
                    adapter.notifyDataSetChanged()
                    journalPb.visibility = View.INVISIBLE
                    constraintLayout.visibility = View.VISIBLE
                    journalRV.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<MutableList<JournalItem>>, t: Throwable) {
                logs(t.toString())
                journalPb.visibility = View.INVISIBLE
            }

        })
    }

    private fun getJournal() = CoroutineScope(Dispatchers.Main).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getJournal(token).enqueue(object : Callback<MutableList<AllJournalItem>> {
            override fun onResponse(call: Call<MutableList<AllJournalItem>>, response: Response<MutableList<AllJournalItem>>) {
                response.body()?.let {
                    allAdapter.differ.submitList(it)
                    allAdapter.notifyDataSetChanged()
                    journalPb.visibility = View.INVISIBLE
                    constraintLayout.visibility = View.VISIBLE
                    allJournalRV.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<MutableList<AllJournalItem>>, t: Throwable) {
                logs(t.toString())
                journalPb.visibility = View.INVISIBLE
            }

        })
    }

    private fun setupAdapter() {
        journalRV.adapter = adapter
        journalRV.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAllAdapter() {
        allJournalRV.adapter = allAdapter
        allJournalRV.layoutManager = LinearLayoutManager(requireContext())
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