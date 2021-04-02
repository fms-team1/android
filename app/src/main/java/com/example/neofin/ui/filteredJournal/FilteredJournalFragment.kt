package com.example.neofin.ui.filteredJournal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neofin.R
import com.example.neofin.adapters.FilteredJournalAdapter
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.filteredJournal.FilteredJournal
import com.example.neofin.retrofit.data.filteredJournal.FilteredJournalItem
import com.example.neofin.utils.logs
import kotlinx.android.synthetic.main.fragment_filtered_journal.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilteredJournalFragment : Fragment(R.layout.fragment_filtered_journal) {
    private val adapter by lazy { FilteredJournalAdapter() }
    var categoryId: Int?  = null
    var userId: Int? = null
    var agentId: Int? = null
    var walletId: Int? = null
    var section: String? = null
    var type: String? = null
    var date: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()

        setupAdapter()

        closeBT.setOnClickListener {
            findNavController().navigate(
                R.id.filtersFragment
            )
        }
        val isCategoryEmpty = arguments?.getBoolean("isEmptyCategory")
        val isAgentEmpty = arguments?.getBoolean("isEmptyAgent")
        val isUserEmpty = arguments?.getBoolean("isEmptyUser")
        val isWalletEmpty = arguments?.getBoolean("isEmptyWallet")
        val isPeriodEmpty = arguments?.getBoolean("isEmptyPeriod")
        val isSectionEmpty = arguments?.getBoolean("isEmptySection")
        val isTypeEmpty = arguments?.getBoolean("isEmptyType")

        when {
            isCategoryEmpty != true -> {
                categoryId = arguments?.getInt("categoryId")
            }
            isAgentEmpty != true -> {
                agentId = arguments?.getInt("userId")
            }
            isUserEmpty != true -> {
                userId = arguments?.getInt("userId")
            }
            isWalletEmpty != true -> {
                walletId = arguments?.getInt("walletId")
            }
            isSectionEmpty != true -> {
                section = arguments?.getString("section")
            }
            isPeriodEmpty != true -> {
                date = arguments?.getString("date")
            }
            isTypeEmpty != true -> {
                type = arguments?.getString("type")
            }
        }

        getFilteredJournal(categoryId, agentId, date?.substringAfter(' '),
            date?.substringBefore(' '), type, walletId, userId, walletId)
    }

    private fun getFilteredJournal(category: Int?, agent : Int?, endDate: String?, startDate: String?,
                                   type: String?, walletTo : Int?, user : Int?, walletFrom : Int?){
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getFiltered(token, category, agent, endDate, startDate,
            type, walletTo, user, walletFrom).enqueue(object : Callback<MutableList<FilteredJournalItem>> {
            override fun onResponse(
                call: Call<MutableList<FilteredJournalItem>>,
                response: Response<MutableList<FilteredJournalItem>>
            ) {
                response.body()?.let {
                    adapter.differ.submitList(it)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<MutableList<FilteredJournalItem>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setupAdapter() {
        filteredRecycler.adapter = adapter
        filteredRecycler.layoutManager = LinearLayoutManager(requireContext())
    }
}