package com.example.neofin.ui.addTransactions

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.category.Category
import com.example.neofin.retrofit.data.transactionAdding.AddTransactionOrExpense
import com.example.neofin.retrofit.data.transactionAdding.AddTransfer
import com.example.neofin.retrofit.data.wallet.GetWallet
import com.example.neofin.ui.MainActivity
import com.example.neofin.ui.addTransactions.data.CategoryIdName
import com.example.neofin.ui.addTransactions.data.SectionName
import com.example.neofin.ui.addTransactions.data.WalletIdName
import com.example.neofin.utils.*
import kotlinx.android.synthetic.main.fragment_adding.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AddingFragment : Fragment(R.layout.fragment_adding) {
    private var walletId = 0
    private var categoryId = 0
    private var sectionType = -1
    private var type = 1
    private var walletFrom = 0
    private var walletTo = 0

    private val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("dd.MM.yyyy")
    val currentDate = sdf.format(Date())

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()
        try {


            backBT?.setOnClickListener {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }

            getWallet()

            getSection()

            setupDatePicker()

            addExpenseBT?.setOnClickListener {
                type = 1
                expenseOrIncome?.visibility = View.VISIBLE
                transferLayout?.visibility = View.GONE
                addExpenseBT.setTextColor(Color.parseColor("#1778E9"))
                addIncomeBT.setTextColor(Color.parseColor("#000000"))
                addTransferBT.setTextColor(Color.parseColor("#000000"))
                sectionAdd.setSelection(0)
            }

            addIncomeBT?.setOnClickListener {
                type = 0
                expenseOrIncome?.visibility = View.VISIBLE
                transferLayout?.visibility = View.GONE
                addIncomeBT.setTextColor(Color.parseColor("#1778E9"))
                addTransferBT.setTextColor(Color.parseColor("#000000"))
                addExpenseBT.setTextColor(Color.parseColor("#000000"))
                sectionAdd.setSelection(0)
            }

            addTransferBT?.setOnClickListener {
                expenseOrIncome?.visibility = View.GONE
                transferLayout?.visibility = View.VISIBLE
                addTransferBT.setTextColor(Color.parseColor("#1778E9"))
                addExpenseBT.setTextColor(Color.parseColor("#000000"))
                addIncomeBT.setTextColor(Color.parseColor("#000000"))
            }


            sendButtonTransfer.setOnClickListener {
                val sumTransfer = sumAddTransfer.text.toString()
                val commentTransfer = commentAddTransfer.text
                if (sumTransfer.isNotEmpty()) {
                formatDate(dateAddTextTransfer.text.toString())?.let { it1 ->
                    addTransfer(
                        sumTransfer.toInt(),
                        commentTransfer.toString(),
                        it1,
                        walletFrom,
                        walletTo)
                    }
                } else {
                    toast(requireContext(), "Введите сумму!")
                }
            }


            sendButton.setOnClickListener {
                val sum = sumAdd.text.toString()
                val comment = commentAdd.text
                val agent = agentAdd.text
                if (sum.isEmpty()){
                    toast(requireContext(), "Введите сумму!")
                } else {
                    formatDate(dateAddText.text.toString())?.let { it1 ->
                        addExpenseOrIncome(
                            sum.toInt(),
                            categoryId,
                            comment.toString(),
                            agent.toString(),
                            it1,
                            walletId
                        )
                    }
                }
            }
        } catch (e: Exception){
            logs(e.toString())
        }
    }

    private fun setupDatePicker() {
        dateAddTextTransfer?.text = currentDate
        dateAddText?.text = currentDate
        dateAddTextTransfer?.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                date(dateAddTextTransfer),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        dateAddText?.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                date(dateAddText),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun getSection(){
        spinnerSection(requireContext(), sectionAdd)
        sectionAdd?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                val sectionName: SectionName = parent.selectedItem as SectionName
                if (sectionName.backName != -1) {
                    categoryLayout?.visibility = View.VISIBLE
                    sectionType = sectionName.backName
                    getCategory(sectionType, type)
                } else {
                    categoryLayout?.visibility = View.GONE
                    sectionType = -1
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
                    categoriesArray.add(CategoryIdName(-1, "Выберите категорию"))
                    response.body()?.forEach {
                        categoriesArray.add(CategoryIdName(it.id, it.name))
                    }

                    if (categoryAdd != null) {
                        spinnerCategory(requireContext(), categoriesArray, categoryAdd)
                        categoryAdd?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val categoryIdName: CategoryIdName = parent.selectedItem as CategoryIdName
                                categoryId = categoryIdName.id
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    } else {
                        logs("categoryAdd is null")
                    }

                } else {
                    logs("Error in AddingFragment, getCategory")
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                logs(t.toString())
            }

        })
    }

    private fun getWallet() = CoroutineScope(Dispatchers.IO).launch{
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getWallets(token).enqueue(object : Callback<GetWallet> {
            override fun onResponse(call: Call<GetWallet>, response: Response<GetWallet>) {
                if (response.isSuccessful) {
                    val walletArray: ArrayList<WalletIdName> = ArrayList()
                    walletArray.add(WalletIdName(-1, "Выберите кошелек"))
                    response.body()?.forEach {
                        walletArray.add(WalletIdName(it.id, it.name))
                    }

                    if (walletAdd != null) {
                        spinnerWallet(requireContext(), walletArray, walletAdd)
                        walletAdd?.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?, position: Int, id: Long
                            ) {
                                val walletIdName: WalletIdName = parent.selectedItem as WalletIdName
                                walletId = walletIdName.id
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    } else {
                        logs("walletAdd is null")
                    }

                    if (walletAddFrom != null && walletAddTo != null) {
                        spinnerWallet(requireContext(), walletArray, walletAddFrom)
                        spinnerWallet(requireContext(), walletArray, walletAddTo)
                        walletAddFrom?.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?, position: Int, id: Long
                            ) {
                                val walletIdName: WalletIdName = parent.selectedItem as WalletIdName
                                walletFrom = walletIdName.id
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }

                        walletAddTo?.onItemSelectedListener = object :
                            AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?, position: Int, id: Long
                            ) {
                                val walletIdName: WalletIdName = parent.selectedItem as WalletIdName
                                walletTo = walletIdName.id
                            }

                            override fun onNothingSelected(parent: AdapterView<*>) {
                            }
                        }
                    } else {
                        logs("walletAddFrom and walletAddTo are null")
                    }

                } else {
                    logs("Error in AddingFragment, getWallet")
                }
            }

            override fun onFailure(call: Call<GetWallet>, t: Throwable) {
                logs(t.toString())
            }

        })
    }

    private fun addExpenseOrIncome(
        amount: Int,
        category: Int,
        comment: String,
//        agentId: Int,
        agentName: String,
        date: String,
        walletId: Int
    ) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val addItems = AddTransactionOrExpense(amount, category, comment, agentName, date, walletId)
        retIn.addIncomeOrExpense(token, addItems).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 404) {
                    snackbar(requireView(), "Недостаточно средст на данном кошелке!", Color.parseColor("#E11616"))
                } else if (response.code() == 200) {
                    snackbar(requireView(), "Транзакция добавлена успешна!", Color.parseColor("#4AAF39"))
                    findNavController().navigate(R.id.action_addingFragment_to_navigation_home)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                logs("$t")
            }

        })
    }

    private fun addTransfer(amount: Int, comment: String, date: String, walletFrom: Int, walletTo: Int) =
        CoroutineScope(Dispatchers.IO).launch{
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val addItems = AddTransfer(amount, comment, date, walletFrom, walletTo)
        retIn.addTransfer(token, addItems).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.code() == 200) {
                    snackbar(requireView(), "Транзакция добавлена успешна!", Color.parseColor("#4AAF39"))
                    findNavController().navigate(R.id.action_addingFragment_to_navigation_home)
                } else {
                    snackbar(requireView(), "Ошибка при добавлении транзакции!", Color.parseColor("#E11616"))
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                logs(t.toString())
            }

        })
    }
}