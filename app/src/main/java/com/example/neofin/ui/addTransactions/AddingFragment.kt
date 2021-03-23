package com.example.neofin.ui.addTransactions

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.addingResponse.AddResponse
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
    private var sectionType = ""
    private var type = "EXPENSE"
    private var walletFrom = 0
    private var walletTo = 0

    private val calendar = Calendar.getInstance()
    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("yyyy-MM-dd")


    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            backBT.setOnClickListener {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }

            getWallet()

            val currentDate = sdf.format(Date())

            dateAddTextTransfer!!.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    date(dateAddTextTransfer),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            dateAddText!!.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    date(dateAddText),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            dateAddTextTransfer.text = currentDate
            dateAddText.text = currentDate

            addExpenseBT.setOnClickListener {
                type = "EXPENSE"
                expenseOrIncome.visibility = View.VISIBLE
                transferLayout.visibility = View.GONE
                addExpenseBT.setTextColor(Color.parseColor("#1778E9"))
                addIncomeBT.setTextColor(Color.parseColor("#000000"))
                addTransferBT.setTextColor(Color.parseColor("#000000"))
            }

            addIncomeBT.setOnClickListener {
                type = "INCOME"
                expenseOrIncome.visibility = View.VISIBLE
                transferLayout.visibility = View.GONE
                addIncomeBT.setTextColor(Color.parseColor("#1778E9"))
                addTransferBT.setTextColor(Color.parseColor("#000000"))
                addExpenseBT.setTextColor(Color.parseColor("#000000"))
            }


            spinnerSection(requireContext(), sectionAdd)
            sectionAdd.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    val sectionName: SectionName = parent.selectedItem as SectionName
                    sectionType = sectionName.backName
                    getCategory(sectionType, type)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }



            val sumTransfer = sumAddTransfer.text
            val commentTransfer = commentAddTransfer.text
            addTransferBT.setOnClickListener {
                expenseOrIncome.visibility = View.GONE
                transferLayout.visibility = View.VISIBLE
                addTransferBT.setTextColor(Color.parseColor("#1778E9"))
                addExpenseBT.setTextColor(Color.parseColor("#000000"))
                addIncomeBT.setTextColor(Color.parseColor("#000000"))
            }

            sendButtonTransfer.setOnClickListener {
                addTransfer(
                    Integer.parseInt(sumTransfer.toString()),
                    commentTransfer.toString(),
                    walletFrom,
                    walletTo
                )
            }

            val sum = sumAdd.text
            val comment = commentAdd.text
            val agent = agentAdd.text
            sendButton.setOnClickListener {
                addExpenseOrIncome(
                    Integer.parseInt(sum.toString()),
                    categoryId,
                    comment.toString(),
                    agent.toString(),
                    currentDate,
                    walletId
                )
            }
        } catch (e: Exception){
            logs(e.toString())
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
                spinnerCategory(requireContext(), categoriesArray, categoryAdd)

                categoryAdd.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                spinnerWallet(requireContext(), walletArray, walletAdd)
                spinnerWallet(requireContext(), walletArray, walletAddFrom)
                spinnerWallet(requireContext(), walletArray, walletAddTo)

                walletAdd.onItemSelectedListener = object :
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

                walletAddFrom.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View, position: Int, id: Long
                    ) {
                        val walletIdName: WalletIdName = parent.selectedItem as WalletIdName
                        walletFrom = walletIdName.id
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }

                walletAddTo.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View, position: Int, id: Long
                    ) {
                        val walletIdName: WalletIdName = parent.selectedItem as WalletIdName
                        walletTo = walletIdName.id
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

    private fun addExpenseOrIncome(
        amount: Int,
        category: Int,
        comment: String,
//        agentId: Int,
        agentName: String,
        date: String,
        walletId: Int
    ) = CoroutineScope(Dispatchers.Main).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val addItems = AddTransactionOrExpense(amount, category, comment, agentName, date, walletId)
        retIn.addIncomeOrExpense(token, addItems).enqueue(object : Callback<AddResponse> {
            override fun onResponse(call: Call<AddResponse>, response: Response<AddResponse>) {
                if (response.isSuccessful) {
                    toast(requireContext(), "Successfully added")
                }
            }

            override fun onFailure(call: Call<AddResponse>, t: Throwable) {
                toast(requireContext(), "Добавлено")
                findNavController().navigate(R.id.action_addingFragment_to_navigation_home)
                logs("$t")
            }

        })
    }

    private fun addTransfer(amount: Int, comment: String, walletFrom: Int, walletTo: Int) =
        CoroutineScope(Dispatchers.Main).launch{
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val addItems = AddTransfer(amount, comment, walletFrom, walletTo)
        retIn.addTransfer(token, addItems).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                findNavController().navigate(R.id.action_addingFragment_to_navigation_home)
                if (response.code() == 200) {
                    toast(requireContext(), "Successfully added")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                findNavController().navigate(R.id.action_addingFragment_to_navigation_home)
                logs(t.toString())
                toast(requireContext(), "Добавлено")
            }

        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private val spinnerOnTouch = OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            expenseOrIncome.visibility = View.VISIBLE
        }
        false
    }

}