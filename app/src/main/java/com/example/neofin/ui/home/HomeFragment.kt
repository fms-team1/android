package com.example.neofin.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neofin.adapters.HomeAdapter
import com.example.neofin.adapters.WalletAdapter
import com.example.neofin.retrofit.data.transaction.Transaction
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.utils.logs
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(R.layout.fragment_home) {

    private val adapter by lazy { HomeAdapter() }
    private val adapterWallet by lazy { WalletAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addingFragment)
        }

        try {
            setupWalletRV()
            setupRecyclerview()
            getHomePage()
        } catch (e: Exception) {
            logs("Error")
        }

    }

    private fun getHomePage() = CoroutineScope(Dispatchers.Main).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        try {
            retIn.getHomePage(token).enqueue(object : Callback<Transaction> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<Transaction>, response: Response<Transaction>) {
                    if (response.isSuccessful) {
                        expense.text =
                            "Расход\n${response.body()?.incomesAndExpensesHomeModel?.expense}"
                        income.text =
                            "Доход\n${response.body()?.incomesAndExpensesHomeModel?.income}"

                        response.body()?.walletBalance?.let { listWallet ->
                            try {
                                adapterWallet.setData(listWallet)
                            } catch (e: Exception) {
                                logs("Error")
                            }

                            response.body()?.lastFifteenTransactions?.let { listTransaction ->
                                try {
                                    adapter.setData(listTransaction)
                                } catch (e: Exception) {
                                    logs("Error")
                                }
                            }
                        }
                    } else {
                        logs("Error")
                    }

                }

                override fun onFailure(call: Call<Transaction>, t: Throwable) {
                    logs((t.toString()))
                }
            })
        } catch (e: Exception) {
            logs("Error")
        }
    }

    private fun setupRecyclerview() = CoroutineScope(Dispatchers.IO).launch {
        recyclerViewHome.adapter = adapter
        recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupWalletRV() = CoroutineScope(Dispatchers.IO).launch {
        walletRV.adapter = adapterWallet
        walletRV.layoutManager = LinearLayoutManager(requireContext())
    }
}