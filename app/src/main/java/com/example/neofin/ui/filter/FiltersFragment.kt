package com.example.neofin.ui.filter

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.category.Category
import com.example.neofin.retrofit.data.wallet.GetWallet
import com.example.neofin.ui.addTransactions.data.CategoryIdName
import com.example.neofin.ui.addTransactions.data.SectionName
import com.example.neofin.ui.addTransactions.data.WalletIdName
import com.example.neofin.ui.filter.data.Period
import com.example.neofin.ui.filter.data.TransactionType
import com.example.neofin.utils.*
import kotlinx.android.synthetic.main.fragment_adding.*
import kotlinx.android.synthetic.main.fragment_filters.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class FiltersFragment : Fragment(R.layout.fragment_filters) {
    private var walletId = 0
    private var categoryId = 0
    private var type = ""
    private var section = ""
    private var datePeriod = ""
    private val calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()

        getSection()
        getTransactionSpinner()
        getWallet()
        getCategory(section, type)
        getPeriod("${dateFrom}/${dateTo}")

        closeButton.setOnClickListener {
            findNavController().navigate(
                R.id.navigation_journal
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
                section = sectionName.backName
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun getPeriod(date: String) = CoroutineScope(Dispatchers.Main).launch {
        spinnerPeriodFilter(requireContext(), periodFilter, date)
        periodFilter.onItemSelectedListener = object  :
            AdapterView.OnItemSelectedListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val period: Period = parent.selectedItem as Period
                datePeriod = period.period
                if (period.name == "За период") {
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
                type = transactionType.id
                getCategory(section, type)
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
                response.body()?.forEach {
                    categoriesArray.add(CategoryIdName(it.id, it.category))
                }
                spinnerCategoryFilter(requireContext(), categoriesArray, categoryFilter)

                categoryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        val categoryIdName: CategoryIdName = parent.selectedItem as CategoryIdName
                        categoryId = categoryIdName.id
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
                        walletId = walletIdName.id
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
}