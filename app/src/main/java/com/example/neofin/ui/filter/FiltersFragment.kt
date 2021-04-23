package com.example.neofin.ui.filter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.allUsers.AllUsers
import com.example.neofin.retrofit.data.category.Category
import com.example.neofin.retrofit.data.getAllAgents.AllAgents
import com.example.neofin.retrofit.data.wallet.GetWallet
import com.example.neofin.ui.addTransactions.data.CategoryIdName
import com.example.neofin.ui.addTransactions.data.SectionName
import com.example.neofin.ui.addTransactions.data.WalletIdName
import com.example.neofin.ui.filter.data.*
import com.example.neofin.utils.*
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FiltersFragment : Fragment(R.layout.fragment_filters) {
    private val calendar = Calendar.getInstance()

    var walletId: Int? = null
    var categoryId: Int? = null
    var type: Int? = null
    var section: Int? = null
    var date: String? = null
    var isPeriod = false
    var isPeriodTransfer = false
    var agent: Int? = null
    var user: Int? = null

    var isTransfer = false
    var walletIdTo: Int? = null
    var walletIdFrom: Int? = null

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()

        try {
            getSection()
            getTransactionSpinner()
            getWallet()
            getPeriod()
            getUser()
            getAgent()
        } catch (e: Exception) {
            logs("FiltersFragment: $e")
        }

        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())

        dateFromTransfer?.text = currentDate
        dateFrom?.text = currentDate

        closeButton.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_journal
            )
        }

        resetAll?.setOnClickListener {
            sectionFilter.setSelection(0)
            periodFilter.setSelection(0)
            walletFilter.setSelection(0)
            categoryFilter.setSelection(0)
            operationFilter.setSelection(0)
            userFilter.setSelection(0)
            categoryLayout.visibility = View.GONE
            section = null
            type = null
        }

        acceptFilters?.setOnClickListener {
            val bundle = Bundle().apply {
                if (walletId == null) {
                    putBoolean("isEmptyWallet", true)
                } else {
                    walletId?.let { it1 -> putInt("walletId", it1) }
                }

                if (agent == null) {
                    putBoolean("isEmptyAgent", true)
                } else {
                    agent?.let { it1 -> putInt("agentId", it1) }
                }

                if (user == null) {
                    putBoolean("isEmptyUser", true)
                } else {
                    user?.let { it1 -> putInt("userId", it1) }
                }

                if (categoryId == null) {
                    putBoolean("isEmptyCategory", true)
                } else {
                    categoryId?.let { it1 -> putInt("categoryId", it1) }
                }

                if (section == null) {
                    putBoolean("isEmptySection", true)
                } else {
                    section?.let { it1 -> putInt("section", it1) }
                }

                if (type == null) {
                    putBoolean("isEmptyType", true)
                } else {
                    type?.let { it1 -> putInt("type", it1) }
                }

                if (date == null) {
                    putBoolean("isEmptyPeriod", true)
                } else {
                    when {
                        isPeriod -> {
                            putString("date", "${formatDate(dateFrom.text as String?)} ${formatDate(
                                dateTo.text as String?
                            )}")
                        }
                        isPeriodTransfer -> {
                            putString("date", "${formatDate(dateFromTransfer.text as String?)} ${formatDate(
                                dateToTransfer.text as String?
                            )}")
                        }
                        else -> {
                            putString("date", date)
                        }
                    }
                }

                if (walletIdTo == null && walletIdFrom == null) {
                    putBoolean("isNotTransfer", true)
                } else {
                    walletIdTo?.let { it1 -> putInt("walletIdTo", it1) }
                    walletIdFrom?.let { it1 -> putInt("walletIdFrom", it1) }
                }
            }
            findNavController().navigate(
                R.id.filteredJournalFragment,
                bundle
            )
        }
    }

    private fun getSection() = CoroutineScope(Dispatchers.IO).launch {
        spinnerSectionFilter(requireContext(), sectionFilter)
        sectionFilter?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                val sectionName: SectionName = parent.selectedItem as SectionName
                if (sectionName.backName != -1) {
                    section = sectionName.backName
                    sectionFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                    (parent.getChildAt(0) as TextView?)?.setTextColor(Color.parseColor("#FFFFFF"))
                    icSection.setImageResource(R.drawable.ic_close3)
                    icSection?.setOnClickListener {
                        sectionFilter.setSelection(0)
                        categoryLayout?.visibility = View.GONE
                        section = null
                    }
                    if (type != null && section != null) {
                        categoryLayout?.visibility = View.VISIBLE
                        getCategory(section!!, type!!)
                    }
                } else {
                    sectionFilter?.setBackgroundResource(R.drawable.spinner_filter_bg)
                    icSection?.setImageResource(R.drawable.ic_down)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun getPeriod() = CoroutineScope(Dispatchers.IO).launch {
        spinnerPeriodFilter(requireContext(), periodFilter)
        spinnerPeriodFilter(requireContext(), periodFilterTransfer)

        periodFilter?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                val period: Period = parent.selectedItem as Period
                if (period.period != "") {
                    date = period.period
                    if (period.name == "За период") {
                        isPeriod = true
                        periodLayout?.visibility = View.VISIBLE

                        dateFrom?.setOnClickListener {
                            DatePickerDialog(
                                requireContext(),
                                date(dateFrom),
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }

                        dateTo?.setOnClickListener {
                            DatePickerDialog(
                                requireContext(),
                                date(dateTo),
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                    } else {
                        periodLayout?.visibility = View.GONE
                    }
                    periodFilter?.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                    (parent.getChildAt(0) as TextView?)?.setTextColor(Color.parseColor("#FFFFFF"))
                    icPeriod?.setImageResource(R.drawable.ic_close3)
                    icPeriod?.setOnClickListener {
                        periodFilter?.setSelection(0)
                        periodLayout?.visibility = View.GONE
                    }
                } else {
                    periodFilter?.setBackgroundResource(R.drawable.spinner_filter_bg)
                    icPeriod?.setImageResource(R.drawable.ic_down)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        periodFilterTransfer?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                val period: Period = parent.selectedItem as Period
                if (period.period != "") {
                    date = period.period
                    if (period.name == "За период") {
                        isPeriodTransfer = true
                        periodLayoutTransfer?.visibility = View.VISIBLE

                        dateFromTransfer?.setOnClickListener {
                            DatePickerDialog(
                                requireContext(),
                                date(dateFromTransfer),
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }

                        dateToTransfer?.setOnClickListener {
                            DatePickerDialog(
                                requireContext(),
                                date(dateToTransfer),
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                    } else {
                        periodLayoutTransfer.visibility = View.GONE
                    }
                    periodFilterTransfer?.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                    (parent.getChildAt(0) as TextView?)?.setTextColor(Color.parseColor("#FFFFFF"))
                    icPeriodTransfer?.setImageResource(R.drawable.ic_close3)
                    icPeriodTransfer?.setOnClickListener {
                        periodFilterTransfer.setSelection(0)
                        periodLayoutTransfer?.visibility = View.GONE
                    }
                } else {
                    periodFilterTransfer.setBackgroundResource(R.drawable.spinner_filter_bg)
                    icPeriodTransfer?.setImageResource(R.drawable.ic_down)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun getTransactionSpinner() = CoroutineScope(Dispatchers.Main).launch {
        spinnerTransaction(requireContext(), operationFilter)
        operationFilter?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                val transactionType: TransactionType = parent.selectedItem as TransactionType
                if (transactionType.id != -1) {
                    type = transactionType.id
                    if (type != null && section != null) {
                        categoryLayout.visibility = View.VISIBLE
                        getCategory(section!!, type!!)
                    }
                    operationFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                    (parent.getChildAt(0) as TextView?)?.setTextColor(Color.parseColor("#FFFFFF"))
                    icOperation.setImageResource(R.drawable.ic_close3)
                    icOperation.setOnClickListener {
                        operationFilter.setSelection(0)
                        categoryLayout.visibility = View.GONE
                        expenseIncomeLayout.visibility = View.VISIBLE
                        transferFilterLayout.visibility = View.GONE
                        type = null
                    }

                    if (transactionType.id == 2) {
                        isTransfer = true
                        expenseIncomeLayout.visibility = View.GONE
                        transferFilterLayout.visibility = View.VISIBLE
                    } else {
                        isTransfer = false
                        expenseIncomeLayout.visibility = View.VISIBLE
                        transferFilterLayout.visibility = View.GONE
                    }
                } else {
                    operationFilter.setBackgroundResource(R.drawable.spinner_filter_bg)
                    icOperation.setImageResource(R.drawable.ic_down)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun getCategory(section: Int, type: Int) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getCategory(token, section, type).enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if (response.isSuccessful) {
                    val categoriesArray: ArrayList<CategoryIdName> = ArrayList()
                    categoriesArray.add(CategoryIdName(-1, "Категория"))
                    response.body()?.forEach {
                        categoriesArray.add(CategoryIdName(it.id, it.name))
                    }

                    if (categoryFilter != null) {
                        spinnerCategoryFilter(requireContext(), categoriesArray, categoryFilter)
                        categoryFilter?.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                                ) {
                                    val categoryIdName: CategoryIdName =
                                        parent.selectedItem as CategoryIdName
                                    if (categoryIdName.id != -1) {
                                        categoryId = categoryIdName.id
                                        categoryFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                                        (parent.getChildAt(0) as TextView?)?.setTextColor(
                                            Color.parseColor(
                                                "#FFFFFF"
                                            )
                                        )
                                        icCategory.setImageResource(R.drawable.ic_close3)
                                        icCategory.setOnClickListener {
                                            categoryFilter.setSelection(0)
                                        }
                                    } else {
                                        categoryFilter.setBackgroundResource(R.drawable.spinner_filter_bg)
                                        icCategory.setImageResource(R.drawable.ic_down)
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {
                                }
                            }
                    } else {
                        logs("categoryFilter is null")
                    }

                } else {
                    logs("Error in FiltersFragment, getCategory")
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
                    val walletArray: ArrayList<WalletIdName> = ArrayList()
                    val walletArrayTo: ArrayList<WalletIdNameTo> = ArrayList()
                    val walletArrayFrom: ArrayList<WalletIdNameFrom> = ArrayList()

                    walletArray.add(WalletIdName(-1, "Кошелек"))
                    walletArrayFrom.add(WalletIdNameFrom(-1, "С кошелка"))
                    walletArrayTo.add(WalletIdNameTo(-1, "На кошелек"))

                    response.body()?.forEach {
                        walletArray.add(WalletIdName(it.id, it.name))
                        walletArrayFrom.add(WalletIdNameFrom(it.id, it.name))
                        walletArrayTo.add(WalletIdNameTo(it.id, it.name))
                    }

                    if (walletFilter != null) {
                        spinnerWalletFilter(requireContext(), walletArray, walletFilter)
                        walletFilter.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?, position: Int, id: Long
                            ) {
                                val walletIdName: WalletIdName = parent.selectedItem as WalletIdName
                                if (walletIdName.id != -1) {
                                    walletId = walletIdName.id
                                    walletFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                                    (parent.getChildAt(0) as TextView?)?.setTextColor(Color.parseColor("#FFFFFF"))
                                    icWallet.setImageResource(R.drawable.ic_close3)
                                    icWallet.setOnClickListener {
                                        walletFilter.setSelection(0)
                                    }
                                } else {
                                    walletFilter.setBackgroundResource(R.drawable.spinner_filter_bg)
                                    icWallet.setImageResource(R.drawable.ic_down)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    } else {
                        logs("walletFilter is null")
                    }

                    if (walletFilterFrom != null && walletFilterTo != null) {
                        spinnerWalletFilterFrom(requireContext(), walletArrayFrom, walletFilterFrom)
                        spinnerWalletFilterTo(requireContext(), walletArrayTo, walletFilterTo)
                        walletFilterTo.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?, position: Int, id: Long
                            ) {
                                val walletIdName: WalletIdNameTo = parent.selectedItem as WalletIdNameTo
                                if (walletIdName.id != -1) {
                                    walletIdTo = walletIdName.id
                                    walletFilterTo.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                                    (parent.getChildAt(0) as TextView?)?.setTextColor(Color.parseColor("#FFFFFF"))
                                    icWalletTo.setImageResource(R.drawable.ic_close3)
                                    icWalletTo.setOnClickListener {
                                        walletFilterTo.setSelection(0)
                                    }
                                } else {
                                    walletFilterTo.setBackgroundResource(R.drawable.spinner_filter_bg)
                                    icWalletTo.setImageResource(R.drawable.ic_down)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }

                        walletFilterFrom.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?, position: Int, id: Long
                            ) {
                                val walletIdName: WalletIdNameFrom =
                                    parent.selectedItem as WalletIdNameFrom
                                if (walletIdName.id != -1) {
                                    walletIdFrom = walletIdName.id
                                    walletFilterFrom.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                                    (parent.getChildAt(0) as TextView?)?.setTextColor(Color.parseColor("#FFFFFF"))
                                    icWalletFrom.setImageResource(R.drawable.ic_close3)
                                    icWalletFrom.setOnClickListener {
                                        walletFilterTo.setSelection(0)
                                    }
                                } else {
                                    walletFilterFrom.setBackgroundResource(R.drawable.spinner_filter_bg)
                                    icWalletFrom.setImageResource(R.drawable.ic_down)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    } else {
                        logs("walletFilterFrom and walletFilterTo are null")
                    }
                } else {
                    logs("Error in FiltersFragment, getWallet")
                }
            }

            override fun onFailure(call: Call<GetWallet>, t: Throwable) {
                logs(t.toString())
            }

        })
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
                            ArrayAdapter(requireContext(), R.layout.spinner,
                                agentArray
                            )

                        if (listView != null) {
                            listView?.adapter = arrayAdapter
                        } else {
                            logs("listView, FiltersFragment")
                        }

                        searchAgent?.setQuery("", false)
                        searchAgent?.clearFocus()
                        searchAgent?.isIconified = true

                        searchAgent?.setOnSearchClickListener {
                            listView?.visibility = View.VISIBLE
                            hintSearch?.visibility = View.GONE
                        }

                        searchAgent?.setOnCloseListener {
                            listView?.visibility = View.GONE
                            hintSearch?.visibility = View.VISIBLE
                            false
                        }

                        searchAgent?.setOnQueryTextListener(object :
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

                        listView?.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, i, _ ->
                                agent = arrayAdapter.getItem(i)?.id
                                searchAgent?.setQuery("${arrayAdapter.getItem(i)?.name}", true)
                                listView?.visibility = View.GONE
                            }
                    } else {
                        logs("Error in FiltersFr, AgentGet")
                    }

                } else {
                    logs("Error in FiltersFragment, getAgent")
                }

            }

            override fun onFailure(call: Call<AllAgents>, t: Throwable) {
            }
        })
    }

    private fun getUser() = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getAllUsers(token).enqueue(object : Callback<AllUsers> {
            override fun onResponse(call: Call<AllUsers>, response: Response<AllUsers>) {
                if (response.isSuccessful) {
                    val userArray: ArrayList<UserIdName> = ArrayList()
                    userArray.add(UserIdName(-1, "Пользователь"))
                    response.body()?.forEach {
                        userArray.add(UserIdName(it.id, "${it.name} ${it.surname}"))
                    }

                    if (userFilter != null) {
                        spinnerUserFilter(requireContext(), userArray, userFilter)
                        userFilter?.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?, position: Int, id: Long
                            ) {
                                val userIdName: UserIdName = parent.selectedItem as UserIdName
                                if (userIdName.id != -1) {
                                    user = userIdName.id
                                    userFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                                    (parent.getChildAt(0) as TextView?)?.setTextColor(Color.parseColor("#FFFFFF"))
                                    icUser.setImageResource(R.drawable.ic_close3)
                                    icUser?.setOnClickListener {
                                        userFilter.setSelection(0)
                                    }
                                } else {
                                    userFilter.setBackgroundResource(R.drawable.spinner_filter_bg)
                                    icUser.setImageResource(R.drawable.ic_down)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    } else {
                        logs("userFilter is null")
                    }

                    if (userFilterTransfer != null) {
                        spinnerUserFilter(requireContext(), userArray, userFilterTransfer)
                        userFilterTransfer?.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?, position: Int, id: Long
                            ) {
                                val userIdName: UserIdName = parent.selectedItem as UserIdName
                                if (userIdName.id != -1) {
                                    user = userIdName.id
                                    userFilterTransfer.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                                    (parent.getChildAt(0) as TextView?)?.setTextColor(Color.parseColor("#FFFFFF"))
                                    icUserTransfer.setImageResource(R.drawable.ic_close3)
                                    icUserTransfer?.setOnClickListener {
                                        userFilterTransfer.setSelection(0)
                                    }
                                } else {
                                    userFilterTransfer.setBackgroundResource(R.drawable.spinner_filter_bg)
                                    icUserTransfer.setImageResource(R.drawable.ic_down)
                                }
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    } else {
                        logs("userFilterTransfer is null")
                    }

                } else{
                    logs("Error in FiltersFragment, getUser")
                }

            }

            override fun onFailure(call: Call<AllUsers>, t: Throwable) {

            }

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        arguments?.clear()
    }
}