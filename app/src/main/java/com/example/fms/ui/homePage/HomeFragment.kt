package com.example.fms.ui.homePage

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fms.R
import com.example.fms.adapters.HomeAdapter
import com.example.fms.retrofit.RetrofitBuilder
import com.example.fms.retrofit.data.transactios.Transactions
import com.example.fms.utils.logs
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(R.layout.fragment_home) {

    private val adapter by lazy { HomeAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)

        setupRecyclerview()
        getHomePage()
    }

    private fun getHomePage() = CoroutineScope(Dispatchers.Main).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getHomePage(token).enqueue(object : Callback<Transactions> {
            override fun onResponse(call: Call<Transactions>, response: Response<Transactions>) {
                balanceHome.text = response.body()?.currentBalance.toString()
                response.body()?.lastFifteenTransactions.let {
                    if (it != null) {
                        adapter.setData(it)
                    }
                }
            }
            override fun onFailure(call: Call<Transactions>, t: Throwable) {
                logs((t.toString()))
            }

        })
    }
    private fun setupRecyclerview() = CoroutineScope(Dispatchers.IO).launch {
        recyclerViewHome.adapter = adapter
        recyclerViewHome.layoutManager = LinearLayoutManager(requireContext())
    }
}