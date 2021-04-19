package com.example.neofin.ui.journal.neolabs

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
import kotlinx.android.synthetic.main.fragment_neobis_journal.*
import kotlinx.android.synthetic.main.fragment_neolabs_journal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NeolabsJournalFragment : Fragment(R.layout.fragment_neolabs_journal) {
    private val adapter by lazy { JournalAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        getJournalBySection()

        neolabsPB?.visibility = View.VISIBLE
        neolabsJournalRV?.visibility = View.INVISIBLE

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
        retIn.getJournalBySection(token, "NEOLABS").enqueue(object :
            Callback<MutableList<JournalItem>> {
            override fun onResponse(
                call: Call<MutableList<JournalItem>>,
                response: Response<MutableList<JournalItem>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        adapter.differ.submitList(it)
                        adapter.notifyDataSetChanged()
                    }
                    neolabsPB?.visibility = View.GONE
                    neolabsJournalRV?.visibility = View.VISIBLE
                } else {
                    logs("Error in NeolabsJournalFr, getJournal")
                    neolabsPB?.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<MutableList<JournalItem>>, t: Throwable) {
                logs(t.toString())
                neolabsPB?.visibility = View.GONE
            }

        })
    }

    private fun setupAdapter() {
        neolabsJournalRV.adapter = adapter
        neolabsJournalRV.layoutManager = LinearLayoutManager(requireContext())
    }
}