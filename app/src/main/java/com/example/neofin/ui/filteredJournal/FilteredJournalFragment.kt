package com.example.neofin.ui.filteredJournal

import android.graphics.Color
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
import com.example.neofin.utils.snackbar
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()

        setupAdapter()

        val isCategoryEmpty = arguments?.getBoolean("isEmptyCategory")
        val isAgentEmpty = arguments?.getBoolean("isEmptyAgent")
        val isUserEmpty = arguments?.getBoolean("isEmptyUser")
        val isWalletEmpty = arguments?.getBoolean("isEmptyWallet")
        val isPeriodEmpty = arguments?.getBoolean("isEmptyPeriod")
        val isSectionEmpty = arguments?.getBoolean("isEmptySection")
        val isTypeEmpty = arguments?.getBoolean("isEmptyType")
        val isNotTransfer = arguments?.getBoolean("isNotTransfer")

//        walletIdTo = arguments?.getInt("walletIdTo")
//        walletIdFrom = arguments?.getInt("walletIdFrom")
//        val categoryId = arguments?.getInt("categoryId")
//        val agentId = arguments?.getInt("agentId")
//        val userId = arguments?.getInt("userId")
//        val walletId = arguments?.getInt("walletId")
//        val section = arguments?.getInt("section")
//        val date = arguments?.getString("date")
//        val type = arguments?.getInt("type")

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

        if (isTypeEmpty != true) {
            type = arguments?.getInt("type")
        }

        if (isNotTransfer != true) {
            walletIdTo = arguments?.getInt("walletIdTo")
            walletIdFrom = arguments?.getInt("walletIdFrom")
            getFilteredJournal(null, null, date?.substringAfter(' '), null,
                date?.substringBefore(' '), type, walletIdTo, userId, walletIdFrom)
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
                response.body()?.let {
                    adapter.differ.submitList(it)
                    adapter.notifyDataSetChanged()
                    if (adapter.itemCount == 0) {
                        status.text = "По вашим запросам ничего не найдено!"
                        status.visibility = View.VISIBLE
                    }

                }
            }

            override fun onFailure(call: Call<MutableList<FilteredJournalItem>>, t: Throwable) {

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