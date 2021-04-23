package com.example.neofin.ui.journal.journalById.updateJournalItem

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.category.Category
import com.example.neofin.retrofit.data.changeJournal.ChangeJournal
import com.example.neofin.retrofit.data.getAllAgents.AllAgents
import com.example.neofin.retrofit.data.wallet.GetWallet
import com.example.neofin.ui.addTransactions.data.CategoryIdName
import com.example.neofin.ui.addTransactions.data.WalletIdName
import com.example.neofin.ui.filter.data.AgentIdName
import com.example.neofin.ui.filter.data.WalletIdNameFrom
import com.example.neofin.ui.filter.data.WalletIdNameTo
import com.example.neofin.ui.journal.journalById.data.CategoryItem
import com.example.neofin.ui.journal.journalById.data.WalletItem
import com.example.neofin.utils.*
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlinx.android.synthetic.main.fragment_update_journal.*
import kotlinx.android.synthetic.main.fragment_update_journal.hintSearch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdateJournalFragment : Fragment(R.layout.fragment_update_journal) {
    var agent: Int? = null
    var categoryId: Int? = null
    var walletIdTo: Int? = null
    var walletIdFrom: Int? = null
    var walletId: Int? = null
    var commentChange: String? = null
    var amountChange: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()

        getAgent()
        getWallet()

        val isFiltered = arguments?.getBoolean("isFiltered")
        val idSingle = arguments?.getInt("updateId")
        val idFiltered = arguments?.getInt("filteredJournal")
        val isFromFiltered = arguments?.getBoolean("isFromFiltered")

        if (isFiltered == false) {
            val singleSection = arguments?.getInt("singleSectionId")
            val singleType = arguments?.getInt("singleTypeId")
            val singleAmount = arguments?.getInt("amountById")
            if (singleType != 2) {
                notTransfer?.visibility = View.VISIBLE
                walletTransferLayout?.visibility = View.GONE
                updateAmount?.hint = "Старое значение: $singleAmount"
                getCategory(singleSection!!, singleType!!)
                changeBT?.setOnClickListener {
                    if (updateAmount?.text.toString().isNotEmpty()) {
                        val amountText = updateAmount?.text.toString().trim()
                        amountChange = amountText.toInt()
                    }
                    if (updateComment?.text.toString().isNotEmpty()){
                        commentChange = updateComment?.text.toString()
                    }
                    updateItem(amountChange, categoryId, commentChange, agent,
                        idSingle!!, null, walletId)
                }
            } else {
                notTransfer.visibility = View.GONE
                walletTransferLayout?.visibility = View.VISIBLE
                updateAmount?.hint = "Старое значение: $singleAmount"
                changeBT?.setOnClickListener {
                    if (updateAmount?.text.toString().isNotEmpty()) {
                        val amountText = updateAmount?.text.toString().trim()
                        amountChange = amountText.toInt()
                    }
                    if (updateComment?.text.toString().isNotEmpty()){
                        commentChange = updateComment?.text.toString()
                    }
                    updateItem(amountChange, null, commentChange,
                        null, idSingle!!, walletIdTo, walletIdFrom)
                }
            }
        }

        if (isFromFiltered == true) {
            val filteredTypeId = arguments?.getInt("filteredType")
            val filteredSectionId = arguments?.getInt("filteredSection")
            val amountD = arguments?.getInt("amount")
            if (filteredTypeId != 2) {
                updateAmount?.hint = "Старое значение: $amountD"
                notTransfer?.visibility = View.VISIBLE
                walletTransferLayout?.visibility = View.GONE
                getCategory(filteredSectionId!!, filteredTypeId!!)
                changeBT?.setOnClickListener {
                    if (updateAmount?.text.toString().isNotEmpty()) {
                        val amountText = updateAmount?.text.toString()
                        amountChange = Integer.parseInt(amountText)
                    }
                    if (updateComment?.text.toString().isNotEmpty()){
                        commentChange = updateComment?.text.toString()
                    }
                    updateItem(amountChange, categoryId, commentChange, agent,
                        idFiltered!!, null, walletId)
                }
            } else {
                notTransfer?.visibility = View.GONE
                walletTransferLayout?.visibility = View.VISIBLE
                updateAmount?.hint = "Старое значение: $amountD"
                changeBT?.setOnClickListener {
                    if (updateAmount?.text.toString().isNotEmpty()) {
                        val amountText = updateAmount?.text.toString()
                        amountChange = Integer.parseInt(amountText)
                    }
                    if (updateComment?.text.toString().isNotEmpty()){
                        commentChange = updateComment?.text.toString()
                    }
                    updateItem(amountChange, null, commentChange,
                        null, idFiltered!!, walletIdTo, walletIdFrom)
                }
            }


        }

//        closeChangeBT.setOnClickListener {
//            if (isFiltered == false){
//                findNavController().navigate(R.id.journalByIdFragment)
//            }
//            if (isFromFiltered == true) {
//                findNavController().navigate(R.id.filtersFragment)
//            }
//        }

//        closeChangeBT.setOnClickListener {
////            findNavController().navigate(R.id.filtersFragment)
//            val singleSection = arguments?.getInt("singleSectionId")
//            val singleType = arguments?.getInt("singleTypeId")
//            val filteredTypeId = arguments?.getInt("filteredType")
//            val filteredSectionId = arguments?.getInt("filteredSection")
//            logs("Single id: $idSingle : Filtered id: $idFiltered")
//            logs("Single type: $singleType : Filtered type: $filteredTypeId")
//            logs("Single section: $singleSection : Filtered section: $filteredSectionId")
//        }
    }

    private fun getAgent() = CoroutineScope(Dispatchers.IO).launch {
        val retInt = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retInt.getAllAgents(token).enqueue(object : Callback<AllAgents> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<AllAgents>, response: Response<AllAgents>) {
                if (response.isSuccessful) {
                    val agentArray: ArrayList<AgentIdName> = ArrayList()
                    response.body()?.forEach {
                        if (it.surname == null) {
                            agentArray.add(AgentIdName(it.id, it.name))
                        } else {
                            agentArray.add(AgentIdName(it.id, "${it.name} ${it.surname}"))
                        }
                    }

                    val activity: FragmentActivity? = activity
                    if (activity != null) {
                        val arrayAdapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_list_item_1,
                                agentArray
                            )
                        if (listViewUpdate != null) {
                            listViewUpdate.adapter = arrayAdapter
                        } else {
                            logs("listView, FiltersFragment")
                        }

                        searchAgentChange?.setOnSearchClickListener {
                            listViewUpdate?.visibility = View.VISIBLE
                            hintSearch?.visibility = View.GONE
                        }

                        searchAgentChange?.setOnCloseListener {
                            listViewUpdate?.visibility = View.GONE
                            hintSearch?.visibility = View.VISIBLE
                            false
                        }

                        searchAgentChange.setOnQueryTextListener(object :
                            android.widget.SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                arrayAdapter.filter.filter(query)
                                return false
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                arrayAdapter.filter.filter(newText)
                                return false
                            }
                        })

                        listViewUpdate?.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, i, _ ->
                                agent = arrayAdapter.getItem(i)?.id
                                searchAgentChange.setQuery("${arrayAdapter.getItem(i)?.name}", true)
                                listViewUpdate?.visibility = View.GONE
                            }
                    }
                } else {
                    logs("Error in UpdateJournalFr, getAgent")
                }

            }

            override fun onFailure(call: Call<AllAgents>, t: Throwable) {
            }
        })
    }

    private fun getCategory(section: Int, type: Int) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getCategory(token, section, type).enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if (response.isSuccessful) {
                    val categoriesArray: ArrayList<CategoryItem> = ArrayList()
                    categoriesArray.add(CategoryItem(null, "Категория"))
                    response.body()?.forEach {
                        categoriesArray.add(CategoryItem(it.id, it.name))
                    }

                    if (categoryChange != null) {
                        spinnerCategoryChange(requireContext(), categoriesArray, categoryChange)
                        categoryChange?.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                                ) {
                                    val categoryName: CategoryItem = parent.selectedItem as CategoryItem
                                    categoryId = categoryName.id
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                }
                            }
                    } else {
                        logs("UpdateJournalFr is null")
                    }

                } else {
                    logs("Error in UpdateJournalFr, getCategory")
                }
            }


            override fun onFailure(call: Call<Category>, t: Throwable) {
                logs(t.toString())
            }

        })
    }

    private fun getWallet() = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getWallets(token).enqueue(object : Callback<GetWallet> {
            override fun onResponse(call: Call<GetWallet>, response: Response<GetWallet>) {
                if (response.isSuccessful) {
                    val walletArray: ArrayList<WalletItem> = ArrayList()

                    walletArray.add(WalletItem(null, "Кошелек"))

                    response.body()?.forEach {
                        walletArray.add(WalletItem(it.id, it.name))
                    }

                    if (walletChange != null) {
                        spinnerWalletChange(requireContext(), walletArray, walletChange)
                        walletChange?.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?, position: Int, id: Long
                            ) {
                                val walletIdName: WalletItem = parent.selectedItem as WalletItem
                                walletId = walletIdName.id
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    } else {
                        logs("walletFilter is null")
                    }

                    if (walletFrom != null && walletTo != null) {
                        spinnerWalletChange(requireContext(), walletArray, walletFrom)
                        spinnerWalletChange(requireContext(), walletArray, walletTo)
                        walletFrom?.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?, position: Int, id: Long
                            ) {
                                val walletIdName: WalletItem =
                                    parent.selectedItem as WalletItem
                                walletIdFrom = walletIdName.id
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }

                        walletTo?.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?, position: Int, id: Long
                            ) {
                                val walletIdName: WalletItem =
                                    parent.selectedItem as WalletItem
                                walletIdTo = walletIdName.id
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    } else {
                        logs("walletFilterFrom and walletFilterTo are null")
                    }
                } else {
                    logs("Error in UpdateJournalFr, getWallet")
                }
            }

            override fun onFailure(call: Call<GetWallet>, t: Throwable) {
                logs(t.toString())
            }

        })
    }

    private fun updateItem(amount: Int?,
                           category: Int?,
                           comment: String?,
                           agentId: Int?,
                           itemId: Int,
                           walletFrom: Int?,
                           walletTo: Int?) = CoroutineScope(Dispatchers.IO).launch {
        val retInt = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val updateBody = ChangeJournal(amount, category, comment, agentId, itemId, walletFrom, walletTo)
        retInt.changeJournalItem(token, updateBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when {
                    response.code() == 200 -> {
                        snackbar(
                            requireView(),
                            "Изменено!",
                            Color.parseColor("#4AAF39")
                        )
                    }
                    else -> {
                        snackbar(requireView(), "Неизвестная ошибка!", Color.parseColor("#E11616"))
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                logs("Error in UpdateJournalrFr, updateItem")
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.arguments?.clear()
    }
}