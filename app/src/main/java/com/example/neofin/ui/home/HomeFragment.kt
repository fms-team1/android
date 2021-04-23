package com.example.neofin.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
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
import com.example.neofin.utils.getMonth
import com.example.neofin.utils.getWeek
import com.example.neofin.utils.getYear
import com.example.neofin.utils.logs
import kotlinx.android.synthetic.main.activity_main.*
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
        toolbar?.hide()

        scrollView?.visibility = View.INVISIBLE
        homePb?.visibility = View.VISIBLE

        year?.setOnClickListener {
            yearTV?.setTextColor(Color.parseColor("#1778E9"))
            monthTV?.setTextColor(Color.parseColor("#6B6666"))
            weekTV?.setTextColor(Color.parseColor("#6B6666"))
            getHomePageByPeriod(getYear())
            scrollView?.visibility = View.VISIBLE
            scrollView?.visibility = View.INVISIBLE
            homePb?.visibility = View.VISIBLE
        }

        month?.setOnClickListener {
            yearTV?.setTextColor(Color.parseColor("#6B6666"))
            monthTV?.setTextColor(Color.parseColor("#1778E9"))
            weekTV?.setTextColor(Color.parseColor("#6B6666"))
            getHomePageByPeriod(getMonth())
            scrollView?.visibility = View.VISIBLE
            scrollView?.visibility = View.INVISIBLE
            homePb?.visibility = View.VISIBLE
        }

        week?.setOnClickListener {
            yearTV?.setTextColor(Color.parseColor("#6B6666"))
            monthTV?.setTextColor(Color.parseColor("#6B6666"))
            weekTV?.setTextColor(Color.parseColor("#1778E9"))
            getHomePageByPeriod(getWeek())
            scrollView?.visibility = View.VISIBLE
            scrollView?.visibility = View.INVISIBLE
            homePb?.visibility = View.VISIBLE
        }

        setupWalletRV()
        setupRecyclerview()
        getHomePage()
    }

    private fun getHomePage() = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        try {
            retIn.getHomePage(token).enqueue(object : Callback<Transaction> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<Transaction>, response: Response<Transaction>) {
                    if (response.isSuccessful) {
                        scrollView?.visibility = View.VISIBLE
                        homePb?.visibility = View.INVISIBLE
                        expense?.text =
                            "- ${response.body()?.incomesAndExpensesHomeModel?.expense} с"
                        income?.text =
                            "${response.body()?.incomesAndExpensesHomeModel?.income} с"

                        response.body()?.walletBalance?.let { listWallet ->
                            try {
                                adapterWallet.setData(listWallet)
                                adapterWallet.notifyDataSetChanged()
                            } catch (e: Exception) {
                                logs("Error")
                            }

                            response.body()?.transactionModels?.let { listTransaction ->
                                try {
                                    adapter.setData(listTransaction)
                                    adapter.notifyDataSetChanged()
                                } catch (e: Exception) {
                                    logs("Error")
                                }
                            }
                        }
                    } else {
                        logs("Error in HomeFragment, getHomePage")
                    }
                }
                override fun onFailure(call: Call<Transaction>, t: Throwable) {
                    scrollView?.visibility = View.INVISIBLE
                    homePb?.visibility = View.INVISIBLE
                    noInternet?.visibility = View.VISIBLE
                    noInternet?.text = "Нет интернет соединения"
                }
            })
        } catch (e: Exception) {
            logs("Error")
        }
    }

    private fun getHomePageByPeriod(period: String) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        try {
            retIn.getHomePageByPeriod(token, period).enqueue(object : Callback<Transaction> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<Transaction>, response: Response<Transaction>) {
                    if (response.isSuccessful) {
                        scrollView?.visibility = View.VISIBLE
                        homePb?.visibility = View.INVISIBLE
                        expense?.text =
                            "- ${response.body()?.incomesAndExpensesHomeModel?.expense} с"
                        income?.text =
                            "${response.body()?.incomesAndExpensesHomeModel?.income} с"

                        response.body()?.walletBalance?.let { listWallet ->
                            try {
                                adapterWallet.setData(listWallet)
                                adapterWallet.notifyDataSetChanged()
                            } catch (e: Exception) {
                                logs("Error")
                            }

                            response.body()?.transactionModels?.let { listTransaction ->
                                try {
                                    adapter.setData(listTransaction)
                                    adapter.notifyDataSetChanged()
                                } catch (e: Exception) {
                                    logs("Error")
                                }
                            }
                        }
                    } else {
                        logs("Error in Home, HomePageByPeriod")
                    }

                }

                override fun onFailure(call: Call<Transaction>, t: Throwable) {
                    scrollView?.visibility = View.INVISIBLE
                    homePb?.visibility = View.INVISIBLE
                    noInternet?.visibility = View.VISIBLE
                    noInternet?.text = "Нет интернет соединения"
                }
            })
        } catch (e: Exception) {
            logs("Error")
        }
    }

    private fun setupRecyclerview() = CoroutineScope(Dispatchers.Main).launch {
        recyclerViewHome.adapter = adapter
        recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupWalletRV() = CoroutineScope(Dispatchers.Main).launch {
        walletRV.adapter = adapterWallet
        walletRV.layoutManager = LinearLayoutManager(requireContext())
    }
}