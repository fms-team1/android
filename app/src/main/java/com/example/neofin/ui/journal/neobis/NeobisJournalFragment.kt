package com.example.neofin.ui.journal.neobis

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neofin.R
import com.example.neofin.adapters.JournalAdapter
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.journal.JournalItem
import com.example.neofin.utils.logs
import kotlinx.android.synthetic.main.fragment_all_journal.*
import kotlinx.android.synthetic.main.fragment_neobis_journal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NeobisJournalFragment : Fragment(R.layout.fragment_neobis_journal) {

    private val adapter by lazy { JournalAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        getJournalBySection()

        neobisPB?.visibility = View.VISIBLE
        neobisJournalRV?.visibility = View.INVISIBLE


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

    private fun getJournalBySection() = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getJournalBySection(token, "NEOBIS").enqueue(object :
            Callback<MutableList<JournalItem>> {
            override fun onResponse(
                call: Call<MutableList<JournalItem>>,
                response: Response<MutableList<JournalItem>>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        adapter.differ.submitList(it)
                        adapter.notifyDataSetChanged()
                    }
                    neobisPB?.visibility = View.GONE
                    neobisJournalRV?.visibility = View.VISIBLE
                } else {
                    logs("Error in NeobisJournalFr, getJournal")
                    neobisPB?.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<MutableList<JournalItem>>, t: Throwable) {
                logs(t.toString())
                neobisPB?.visibility = View.GONE
            }

        })
    }

    private fun setupAdapter() {
        neobisJournalRV.adapter = adapter
        neobisJournalRV.layoutManager = LinearLayoutManager(requireContext())
    }
}