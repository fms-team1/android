package com.example.neofin.ui.filter

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.category.Category
import com.example.neofin.retrofit.data.wallet.GetWallet
import com.example.neofin.ui.addTransactions.data.CategoryIdName
import com.example.neofin.ui.addTransactions.data.SectionName
import com.example.neofin.ui.addTransactions.data.WalletIdName
import com.example.neofin.ui.filter.data.AgentIdName
import com.example.neofin.ui.filter.data.Period
import com.example.neofin.ui.filter.data.TransactionType
import com.example.neofin.ui.filter.data.UserIdName
import com.example.neofin.utils.*
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlinx.android.synthetic.main.spinner_filter_wallet.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class FiltersFragment : Fragment(R.layout.fragment_filters) {
    var walletId = 0
    var categoryId = 0
    var type = ""
    var section = ""
    var datePeriod = ""
    var isPeriod = false
    var agent = 0
    var user = 0
    private val calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()

        getSection()
        getTransactionSpinner()
        getWallet()
        getPeriod()
        getUser()
        getAgent()

        closeButton.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_journal
            )
        }

        resetAll.setOnClickListener {
            sectionFilter.setSelection(0)
            periodFilter.setSelection(0)
            walletFilter.setSelection(0)
            categoryFilter.setSelection(0)
            operationFilter.setSelection(0)
            userFilter.setSelection(0)
            agentFilter.setSelection(0)
            categoryLayout.visibility = View.GONE
            section = ""
            type = ""
        }

        acceptFilters.setOnClickListener {
            val bundle = Bundle().apply {
                if (walletId == 0) {
                    putBoolean("isEmptyWallet", true)
                } else {
                    putInt("walletId", walletId)
                }

                if (agent == 0) {
                    putBoolean("isEmptyAgent", true)
                } else {
                    putInt("agentId", agent)
                }

                if (user == 0) {
                    putBoolean("isEmptyUser", true)
                } else {
                    putInt("userId", user)
                }

                if (categoryId == 0) {
                    putBoolean("isEmptyCategory", true)
                } else {
                    putInt("categoryId", categoryId)
                }

                if (section == "") {
                    putBoolean("isEmptySection", true)
                } else {
                    putString("section", section)
                }

                if (type == "") {
                    putBoolean("isEmptyType", true)
                } else {
                    putString("type", type)
                }

                if (datePeriod == "") {
                    putBoolean("isEmptyPeriod", true)
                } else {
                    if (isPeriod) {
                        putString("date", "${dateTo.text} ${dateFrom.text}")
                    } else {
                        putString("date", datePeriod)
                    }
                }

            }
            findNavController().navigate(
                R.id.filteredJournalFragment,
                bundle
            )
        }
    }

    private fun getSection() = CoroutineScope(Dispatchers.Main).launch {
        spinnerSectionFilter(requireContext(), sectionFilter)
        sectionFilter.onItemSelectedListener = object  :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val sectionName: SectionName = parent.selectedItem as SectionName
                if (sectionName.backName != "") {
                    section = sectionName.backName
                    sectionFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                    (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#FFFFFF"))
                    icSection.setImageResource(R.drawable.ic_close3)
                    icSection.setOnClickListener {
                        sectionFilter.setSelection(0)
                    }
                    if (type != "" && section != "") {
                        categoryLayout.visibility = View.VISIBLE
                        getCategory(section, type)
                    }
                } else {
                    sectionFilter.setBackgroundResource(R.drawable.spinner_filter_bg)
                    icSection.setImageResource(R.drawable.ic_down)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun getPeriod() = CoroutineScope(Dispatchers.Main).launch {
        spinnerPeriodFilter(requireContext(), periodFilter)
        periodFilter.onItemSelectedListener = object  :
            AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val period: Period = parent.selectedItem as Period
                if (period.period != "") {
                    datePeriod = period.period
                    if (period.name == "За период") {
                        isPeriod = true
                        periodLayout.visibility = View.VISIBLE

                        dateFrom.setOnClickListener {
                            DatePickerDialog(
                                requireContext(),
                                date(dateFrom),
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }

                        dateTo.setOnClickListener {
                            DatePickerDialog(
                                requireContext(),
                                date(dateTo),
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                    } else {
                        periodLayout.visibility = View.GONE
                    }
                    periodFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                    (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#FFFFFF"))
                    icPeriod.setImageResource(R.drawable.ic_close3)
                    icPeriod.setOnClickListener {
                        periodFilter.setSelection(0)
                    }
                } else {
                    periodFilter.setBackgroundResource(R.drawable.spinner_filter_bg)
                    icPeriod.setImageResource(R.drawable.ic_down)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun getTransactionSpinner() = CoroutineScope(Dispatchers.Main).launch {
        spinnerTransaction(requireContext(), operationFilter)
        operationFilter.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val transactionType: TransactionType = parent.selectedItem as TransactionType
                if (transactionType.id != ""){
                    type = transactionType.id
                    if (type != "" && section != "") {
                        categoryLayout.visibility = View.VISIBLE
                        getCategory(section, type)
                    }
                    operationFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                    (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#FFFFFF"))
                    icOperation.setImageResource(R.drawable.ic_close3)
                    icOperation.setOnClickListener {
                        operationFilter.setSelection(0)
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

    private fun getCategory(section: String, type: String) = CoroutineScope(Dispatchers.Main).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getCategory(token, section, type).enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                val categoriesArray: ArrayList<CategoryIdName> = ArrayList()
                categoriesArray.add(CategoryIdName(0, "Категория"))
                response.body()?.forEach {
                    categoriesArray.add(CategoryIdName(it.id, it.category))
                }
                spinnerCategoryFilter(requireContext(), categoriesArray, categoryFilter)

                categoryFilter.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View, position: Int, id: Long
                        ) {
                            val categoryIdName: CategoryIdName = parent.selectedItem as CategoryIdName
                            if (categoryIdName.id != 0) {
                                categoryId = categoryIdName.id
                                categoryFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                                (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#FFFFFF"))
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
            }


            override fun onFailure(call: Call<Category>, t: Throwable) {
                logs(t.toString())
            }

        })
    }

    private fun getWallet() = CoroutineScope(Dispatchers.Main).launch{
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getWallets(token).enqueue(object : Callback<GetWallet> {
            override fun onResponse(call: Call<GetWallet>, response: Response<GetWallet>) {
                val walletArray: ArrayList<WalletIdName> = ArrayList()
                walletArray.add(WalletIdName(0, "Кошелек"))
                response.body()?.forEach {
                    walletArray.add(WalletIdName(it.id, it.name))
                }
                spinnerWalletFilter(requireContext(), walletArray, walletFilter)

                walletFilter.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View, position: Int, id: Long
                    ) {
                        val walletIdName: WalletIdName = parent.selectedItem as WalletIdName
                        if (walletIdName.id != 0) {
                            walletId = walletIdName.id
                            walletFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                            (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#FFFFFF"))
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
            }

            override fun onFailure(call: Call<GetWallet>, t: Throwable) {
                logs(t.toString())
            }

        })
    }

    private fun getAgent() = CoroutineScope(Dispatchers.Main).launch {
        val agentArray: ArrayList<AgentIdName> = ArrayList()
        agentArray.add(AgentIdName(0, "Контрагент"))
        spinnerAgentFilter(requireContext(), agentArray, agentFilter)
        agentFilter.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val agentIdName: AgentIdName = parent.selectedItem as AgentIdName
                if (agentIdName.id != 0) {
                    agent = agentIdName.id
                    agentFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                    (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#FFFFFF"))
                    icAgent.setImageResource(R.drawable.ic_close3)
                    icAgent.setOnClickListener {
                        agentFilter.setSelection(0)
                    }
                } else {
                    agentFilter.setBackgroundResource(R.drawable.spinner_filter_bg)
                    icAgent.setImageResource(R.drawable.ic_down)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun getUser() = CoroutineScope(Dispatchers.Main).launch {
        val userArray: ArrayList<UserIdName> = ArrayList()
        userArray.add(UserIdName(0, "Пользователь"))
        spinnerUserFilter(requireContext(), userArray, userFilter)
        userFilter.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val userIdName: UserIdName = parent.selectedItem as UserIdName
                if (userIdName.id != 0) {
                    user = userIdName.id
                    userFilter.setBackgroundResource(R.drawable.spinner_filter_bg_selected)
                    (parent.getChildAt(0) as TextView).setTextColor(Color.parseColor("#FFFFFF"))
                    icUser.setImageResource(R.drawable.ic_close3)
                    icUser.setOnClickListener {
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
    }
}