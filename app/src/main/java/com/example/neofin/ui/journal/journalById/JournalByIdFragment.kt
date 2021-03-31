package com.example.neofin.ui.journal.journalById

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder

import com.example.neofin.retrofit.data.journalById2.JournalById
import kotlinx.android.synthetic.main.fragment_journal_by_id.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JournalByIdFragment : Fragment(R.layout.fragment_journal_by_id) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt("idJournal", 1)

        getJournalById(id!!)
    }

    private fun getJournalById(id : Int) = CoroutineScope(Dispatchers.Main).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getJournalById(token, id).enqueue(object : Callback<JournalById> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<JournalById>, response: Response<JournalById>) {
                val name = response.body()?.accountantName
                val surname = response.body()?.accountantSurname

                val nameAgent = response.body()?.counterpartyName
                val surnameAgent = response.body()?.counterpartySurname

                title.text = response.body()?.categoryName
                date.text = response.body()?.createdDate.toString()
                sum.text = response.body()?.amount.toString()
                user.text = "$name $surname"
                agent.text = "$nameAgent $surnameAgent"
                wallet.text = response.body()?.walletName
                wallet2.text = response.body()?.transferWalletName
                comment.text = response.body()?.comment

                when (response.body()?.transactionType) {
                    "INCOME" -> {
                        category.text = "Доход"
                    }
                    "EXPENSE" -> {
                        category.text = "Расход"
                    }
                    else -> {
                        category.text = "Перевод"
                    }
                }
            }


            override fun onFailure(call: Call<JournalById>, t: Throwable) {

            }

        })
    }
}