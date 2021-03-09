package com.example.fms.ui.journal.journalById

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fms.R
import com.example.fms.retrofit.RetrofitBuilder
import com.example.fms.retrofit.data.journalById.JournalById
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

        getJournalById()
    }

    private fun getJournalById() = CoroutineScope(Dispatchers.Main).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getJournalById(token, 1).enqueue(object : Callback<JournalById> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<JournalById>, response: Response<JournalById>) {
                val name = response.body()?.person?.name
                val surname = response.body()?.person?.surname

                title.text = response.body()?.category?.name
                date.text = response.body()?.createdDate
                category.text = response.body()?.category?.transactionType
                user.text = response.body()?.user?.email
                agent.text = "$name $surname"
                wallet.text = response.body()?.wallet?.name
                wallet2.text = response.body()?.wallet2?.name
                comment.text = response.body()?.comment
            }

            override fun onFailure(call: Call<JournalById>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}