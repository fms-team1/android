package com.example.neofin.ui.journal.journalById

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.journalById.JournalById
import com.example.neofin.utils.formatDate
import com.example.neofin.utils.formatDateAdapters
import kotlinx.android.synthetic.main.fragment_journal_by_id.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class JournalByIdFragment : Fragment(R.layout.fragment_journal_by_id) {
    var sectionId: Int? = null
    var typeId: Int? = null
    var amountById: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.hide()

        val id = arguments?.getInt("idJournal", 1)

        getJournalById(id!!)

        back?.setOnClickListener {
            findNavController().navigate(R.id.navigation_journal)
        }

        change_button?.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("updateId", id)
                putBoolean("isFiltered", false)
                sectionId?.let { it1 -> putInt("singleSectionId", it1) }
                typeId?.let { it1 -> putInt("singleTypeId", it1) }
                amountById?.let { it1 -> putInt("amountById", it1) }
            }
            findNavController().navigate(R.id.updateJournalFragment, bundle)
        }
    }

    private fun getJournalById(id : Int) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getJournalById(token, id).enqueue(object : Callback<JournalById> {
            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onResponse(call: Call<JournalById>, response: Response<JournalById>) {
                val name = response.body()?.accountantName
                val surname = response.body()?.accountantSurname

                val nameAgent = response.body()?.counterpartyName
                val surnameAgent = response.body()?.counterpartySurname

                sectionId = response.body()?.neoSectionId
                typeId = response.body()?.transactionTypeId
                amountById = response.body()?.amount

                title?.text = response.body()?.categoryName
                date?.text = formatDateAdapters(response.body()?.createdDate.toString().substringBefore('T'))
                sum?.text = response.body()?.amount.toString()
                user?.text = "$name $surname"
                if (surnameAgent == null) {
                    agent?.text = "$nameAgent"
                } else {
                    agent?.text = "$nameAgent $surnameAgent"
                }


                when (response.body()?.neoSection) {
                    "NEOBIS" -> section?.text = "Neobis"
                    "NEOLABS" -> section?.text = "Neolabs"
                }


                comment?.text = response.body()?.comment
                if(response.body()?.comment == null || response.body()?.comment == ""){
                    comment?.text = "Нет примечаний"
                }

                when (response.body()?.transactionType) {
                    "INCOME" -> {
                        category?.text = "Доход"
                        transferLayout?.visibility = View.GONE
                        sectionLay?.visibility = View.VISIBLE
                        agentLay?.visibility = View.VISIBLE
                        expenseIncomeLayout?.visibility = View.VISIBLE
                        wallet?.text = response.body()?.walletName
                    }
                    "EXPENSE" -> {
                        category?.text = "Расход"
                        transferLayout?.visibility = View.GONE
                        sectionLay?.visibility = View.VISIBLE
                        agentLay?.visibility = View.VISIBLE
                        expenseIncomeLayout?.visibility = View.VISIBLE
                        wallet?.text = response.body()?.walletName
                    }
                    else -> {
                        category?.text = "Перевод"
                        transferLayout?.visibility = View.VISIBLE
                        expenseIncomeLayout?.visibility = View.GONE
                        sectionLay?.visibility = View.GONE
                        agentLay?.visibility = View.GONE
                        wallet1?.text = response.body()?.walletName
                        wallet2?.text = response.body()?.transferWalletName
                    }
                }
            }


            override fun onFailure(call: Call<JournalById>, t: Throwable) {

            }

        })
    }
}