package com.example.neofin.ui.filteredJournal

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neofin.R
import com.example.neofin.adapters.FilteredJournalAdapter
import com.example.neofin.retrofit.RetrofitBuilder
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
    var walletIdTo: Int? = null
    var walletIdFrom: Int? = null
    var section: Int? = null
    var type: Int? = null
    var date: String? = null
    var dateTransfer: String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()

        if (filteredJournalPB != null) {
            filteredJournalPB.visibility = View.VISIBLE
        } else {
            logs("Error FilteredJournal, PB")
        }

        setupAdapter()

        val isCategoryEmpty = arguments?.getBoolean("isEmptyCategory")
        val isAgentEmpty = arguments?.getBoolean("isEmptyAgent")
        val isUserEmpty = arguments?.getBoolean("isEmptyUser")
        val isWalletEmpty = arguments?.getBoolean("isEmptyWallet")
        val isPeriodEmpty = arguments?.getBoolean("isEmptyPeriod")
        val isSectionEmpty = arguments?.getBoolean("isEmptySection")
        val isTypeEmpty = arguments?.getBoolean("isEmptyType")
        val isNotTransfer = arguments?.getBoolean("isNotTransfer")
        val isEmptyPeriodTransfer = arguments?.getBoolean("isEmptyPeriodTransfer")


        if (isCategoryEmpty != true) {
            categoryId = arguments?.getInt("categoryId")
        }

        if (isAgentEmpty != true) {
            agentId = arguments?.getInt("agentId")
        }

        if (isUserEmpty != true) {
            userId = arguments?.getInt("userId")
        }

        if (isWalletEmpty != true) {
            walletId = arguments?.getInt("walletId")
        }

        if (isSectionEmpty != true) {
            section = arguments?.getInt("section")
        }

        if (isPeriodEmpty != true) {
            date = arguments?.getString("date")
        }

        if (isEmptyPeriodTransfer != true) {
            dateTransfer = arguments?.getString("dateTransfer")
        }

        if (isTypeEmpty != true) {
            type = arguments?.getInt("type")
        }

        if (isNotTransfer != true) {
            walletIdTo = arguments?.getInt("walletIdTo")
            walletIdFrom = arguments?.getInt("walletIdFrom")
            getFilteredJournal(null, null, dateTransfer?.substringAfter(' '), null,
                dateTransfer?.substringBefore(' '), type, walletIdTo, userId, walletIdFrom)
        } else {
            getFilteredJournal(categoryId, agentId, date?.substringAfter(' '), section,
                date?.substringBefore(' '), type, null, userId, walletId)
        }

        closeBT.setOnClickListener {
            findNavController().navigate(
                R.id.filtersFragment
            )
            arguments?.clear()
            status.visibility = View.GONE
        }
    }

    private fun getFilteredJournal(category: Int?, agent : Int?, endDate: String?, section: Int?,startDate: String?,
                                   type: Int?, walletTo : Int?, user : Int?, walletFrom : Int?){
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getFiltered(token, category, agent, endDate, section,startDate,
            type, walletTo, user, walletFrom).enqueue(object : Callback<MutableList<FilteredJournalItem>> {
            override fun onResponse(
                call: Call<MutableList<FilteredJournalItem>>,
                response: Response<MutableList<FilteredJournalItem>>
            ) {
                if (response.isSuccessful){
                    filteredJournalPB.visibility = View.INVISIBLE
                    response.body()?.let {
                        adapter.differ.submitList(it)
                        adapter.notifyDataSetChanged()
                        if (adapter.itemCount == 0) {
                            status.text = "По вашим запросам ничего не найдено!"
                            status.visibility = View.VISIBLE
                        }
                    }
                } else {
                    logs("Error in FilteredJournal, getFilteredJournal")
                }
            }
            override fun onFailure(call: Call<MutableList<FilteredJournalItem>>, t: Throwable) {
                filteredJournalPB.visibility = View.INVISIBLE
            }
        })
    }

    private fun setupAdapter() {
        filteredRecycler.adapter = adapter
        filteredRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.arguments?.clear()
    }


}