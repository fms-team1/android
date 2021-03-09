package com.example.fms.ui.addTransactions

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fms.R
import com.example.fms.retrofit.RetrofitBuilder
import com.example.fms.retrofit.data.addingResponse.AddResponse
import com.example.fms.retrofit.data.category.Category
import com.example.fms.retrofit.data.currentUser.CurrentUser
import com.example.fms.retrofit.data.transactionAdding.AddTransactionOrExpense
import com.example.fms.retrofit.data.transactionAdding.AddTransfer
import com.example.fms.retrofit.data.wallet.GetWallet
import com.example.fms.ui.addTransactions.data.CategoryIdName
import com.example.fms.ui.addTransactions.data.SectionName
import com.example.fms.ui.addTransactions.data.WalletIdName
import com.example.fms.utils.*
import kotlinx.android.synthetic.main.fragment_adding.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class AddingFragment : Fragment(R.layout.fragment_adding) {
    private var walletId = 0
    private var categoryId = 0
    private var sectionType = ""
    private var agent = ""
    private var type = ""
    private var walletFrom = 0
    private var walletTo = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getWallet()
        getCurrentUser()

        addExpenseBT.setOnClickListener {
            type = "EXPENSE"
            expenseOrIncome.visibility = View.VISIBLE
            transferLayout.visibility = View.GONE
        }

        addIncomeBT.setOnClickListener {
            type = "INCOME"
            expenseOrIncome.visibility = View.VISIBLE
            transferLayout.visibility = View.GONE
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
        val commentTransfer = commentAddTransfer.text.toString()
        addTransferBT.setOnClickListener {
            expenseOrIncome.visibility = View.GONE
            transferLayout.visibility = View.VISIBLE
        }

        sendButtonTransfer.setOnClickListener {
            addTransfer(Integer.parseInt(sumTransfer.toString()), "commentTransfer", walletFrom, walletTo)
        }

        val sum = sumAdd.text
        val comment = commentAdd.text.toString()
        sendButton.setOnClickListener {
            addExpenseOrIncome(Integer.parseInt(sum.toString()), categoryId, comment, agent, walletId)
        }
    }

    private fun getCategory(section: String, type: String) {
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
                        toast(requireContext(), "id: ${categoryIdName.id} name: ${categoryIdName.name}")
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

    private fun getCurrentUser() {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getCurrentUser(token).enqueue(object : Callback<CurrentUser> {
            override fun onResponse(call: Call<CurrentUser>, response: Response<CurrentUser>) {
                currentUser.text = response.body()?.name
                agent = response.body()?.name.toString()
            }

            override fun onFailure(call: Call<CurrentUser>, t: Throwable) {

            }

        })
    }

    private fun getWallet() {
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
        walletId: Int) {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val addItems = AddTransactionOrExpense(amount,category, comment, agentName, walletId)
        retIn.addIncomeOrExpense(token, addItems).enqueue(object : Callback<AddResponse> {
            override fun onResponse(call: Call<AddResponse>, response: Response<AddResponse>) {
                if (response.isSuccessful) {
                    toast(requireContext(), "Добавлено")
                }
            }

            override fun onFailure(call: Call<AddResponse>, t: Throwable) {
                toast(requireContext(), "Добавлено")
                findNavController().navigate(R.id.action_addingFragment_to_navigation_home)
                logs("$t")
            }

        })
    }

    private fun addTransfer(amount: Int, comment: String, walletFrom: Int, walletTo: Int) {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val addItems = AddTransfer(amount, comment, walletFrom, walletTo)
        retIn.addTransfer(token, addItems).enqueue(object : Callback<AddResponse> {
            override fun onResponse(call: Call<AddResponse>, response: Response<AddResponse>) {
                if (response.isSuccessful) {
                    toast(requireContext(), "Successfully added")
                }
            }

            override fun onFailure(call: Call<AddResponse>, t: Throwable) {
                logs("$t")
            }

        })
    }

}