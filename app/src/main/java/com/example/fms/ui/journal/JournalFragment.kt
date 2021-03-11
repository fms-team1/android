package com.example.fms.ui.journal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fms.R
import com.example.fms.adapters.JournalAdapter
import com.example.fms.adapters.JournalsAdapter
import com.example.fms.retrofit.RetrofitBuilder
import com.example.fms.retrofit.data.journal.Journal
import com.example.fms.retrofit.data.journal.JournalItem
import com.example.fms.utils.logs
import com.example.fms.utils.toast
import kotlinx.android.synthetic.main.fragment_journal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JournalFragment : Fragment(R.layout.fragment_journal) {

    private val adapter by lazy { JournalAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        getJournal()

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

    private fun getJournal() = CoroutineScope(Dispatchers.Main).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getJournal(token).enqueue(object : Callback<MutableList<JournalItem>> {
            override fun onResponse(call: Call<MutableList<JournalItem>>, response: Response<MutableList<JournalItem>>) {
                response.body()?.let {
                    adapter.differ.submitList(it)
                }
            }

            override fun onFailure(call: Call<MutableList<JournalItem>>, t: Throwable) {
                logs(t.toString())
            }

        })
    }

    private fun setupAdapter() {
        journalRV.adapter = adapter
        journalRV.layoutManager = LinearLayoutManager(requireContext())
    }
}