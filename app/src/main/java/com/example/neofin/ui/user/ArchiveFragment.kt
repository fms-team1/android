package com.example.neofin.ui.user

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neofin.R
import com.example.neofin.adapters.ArchiveCategoryAdapter
import com.example.neofin.adapters.ArchiveGroupAdapter
import com.example.neofin.adapters.ArchiveWalletAdapter
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.archiveCategory.ArchiveCategory
import com.example.neofin.retrofit.data.archiveGroups.ArchiveGroup
import com.example.neofin.retrofit.data.archiveWallet.ArchiveWallet
import com.example.neofin.retrofit.data.wallet.GetWallet
import com.example.neofin.utils.logs
import com.example.neofin.utils.snackbar
import com.example.neofin.utils.spinnerArchive
import kotlinx.android.synthetic.main.dialog_archive_category.view.*
import kotlinx.android.synthetic.main.dialog_archive_group.view.*
import kotlinx.android.synthetic.main.dialog_archive_wallet.view.*
import kotlinx.android.synthetic.main.fragment_archive.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ArchiveFragment : Fragment(R.layout.fragment_archive) {
    private val categoryAdapter by lazy { ArchiveCategoryAdapter() }
    private val groupAdapter by lazy { ArchiveGroupAdapter() }
    private val walletAdapter by lazy { ArchiveWalletAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()

        closeBT?.setOnClickListener {
            findNavController().navigate(R.id.navigation_user)
        }

        spinnerArchive(requireContext(), chooseOption)
        chooseOption?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                if (parent.selectedItem == "Кошелек") {
                    changeRV.visibility = View.VISIBLE
                    getWallets()
                } else if (parent.selectedItem == "Группа") {
                    changeRV.visibility = View.VISIBLE
                    getAllGroups()
                } else if (parent.selectedItem == "Категория"){
                    changeRV.visibility = View.VISIBLE
                    getAllCategory()
                } else {
                    changeRV.visibility = View.INVISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    fun getAllCategory() {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getAllCategory(token).enqueue(object : Callback<List<ArchiveCategory>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<List<ArchiveCategory>>,
                response: Response<List<ArchiveCategory>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        changeRV?.adapter = categoryAdapter
                        changeRV?.layoutManager = LinearLayoutManager(requireContext())
                        categoryAdapter.differ.submitList(it)
                        categoryAdapter.notifyDataSetChanged()

                        categoryAdapter.setOnItemClickListener { adapter ->
                            val mDialogView = LayoutInflater.from(context)
                                .inflate(R.layout.dialog_archive_category, null)
                            val mBuilder = context?.let { it1 ->
                                AlertDialog.Builder(it1)
                                    .setView(mDialogView)
                            }
                            val mAlertDialog = mBuilder?.show()
                            mDialogView?.cancel_category?.setOnClickListener {
                                mAlertDialog?.dismiss()
                            }

                            mDialogView.categoryTitle.text = "Название категории: ${adapter.name}"
                            if (adapter.categoryStatus == "ARCHIVED") {
                                mDialogView.isArchiveCategory.isChecked = true
                            }

                            mDialogView?.create_category?.setOnClickListener {
                                val name = mDialogView.et_archive_category_name.text.toString()
                                if (name.isNotEmpty()) {
                                    archiveCategory(
                                        getBoolean(mDialogView.isArchiveCategory.isChecked),
                                        adapter.id,
                                        name,
                                        adapter.neoSection,
                                        adapter.transactionType
                                    )
                                } else {
                                    archiveCategory(
                                        getBoolean(mDialogView.isArchiveCategory.isChecked),
                                        adapter.id,
                                        adapter.name,
                                        adapter.neoSection,
                                        adapter.transactionType
                                    )
                                }
                                mAlertDialog?.dismiss()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ArchiveCategory>>, t: Throwable) {
                logs("Error ArchiveFr, getAllCat")
            }

        })
    }

    private fun archiveCategory(status: String?, id: Int?, name: String?, section: String?, type: String?)
    = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val categoryBody = ArchiveCategory(status, id, name, section, type)
        retIn.archiveCategory(token, categoryBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when {
                    response.code() == 404 -> {
                        snackbar(requireView(), "Ошибка изменения!", Color.parseColor("#E11616"))
                    }
                    response.code() == 200 -> {
                        snackbar(
                            requireView(),
                            "Категория изменена!",
                            Color.parseColor("#4AAF39")
                        )
                        getAllCategory()
                    }
                    else -> {
                        snackbar(requireView(), "Неизвестная ошибка!", Color.parseColor("#E11616"))
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }

        })
    }

    fun getBoolean(boolean: Boolean?): String {
        return if (boolean == true) {
            "ARCHIVED"
        } else {
            "ACTIVE"
        }
    }

    fun getBooleanWallet(boolean: Boolean?): String {
        return if (boolean == true) {
            "BLOCKED"
        } else {
            "ACCESSIBLE"
        }
    }
    
    fun getName(text: String, oldName: String, newName: String): String {
        return if (text == ""){
            oldName
        } else {
            newName
        }
    }

    fun getAllGroups() {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getAllGroupsArchive(token).enqueue(object : Callback<List<ArchiveGroup>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<List<ArchiveGroup>>,
                response: Response<List<ArchiveGroup>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        changeRV?.adapter = groupAdapter
                        changeRV?.layoutManager = LinearLayoutManager(requireContext())
                        groupAdapter.differ.submitList(it)
                        groupAdapter.notifyDataSetChanged()
                        groupAdapter.setOnItemClickListener { adapter ->
                            val mDialogView = LayoutInflater.from(context)
                                .inflate(R.layout.dialog_archive_group, null)
                            val mBuilder = context?.let { it1 ->
                                AlertDialog.Builder(it1)
                                    .setView(mDialogView)
                            }
                            val mAlertDialog = mBuilder?.show()
                            mDialogView?.cancel_group?.setOnClickListener {
                                mAlertDialog?.dismiss()
                            }

                            mDialogView.groupTitle.text = "Название группы: ${adapter.name}"
                            if (adapter.groupStatus == "ARCHIVED") {
                                mDialogView.isArchiveGroup.isChecked = true
                            }
                            mDialogView?.create_group?.setOnClickListener {
                                val name = mDialogView.et_archive_group.text.toString()
                                if (name.isNotEmpty()){
                                    archiveGroup(
                                        getBoolean(mDialogView.isArchiveGroup.isChecked),
                                        adapter.id,
                                        name
                                    )
                                } else {
                                    adapter.name?.let { it1 ->
                                        archiveGroup(
                                            getBoolean(mDialogView.isArchiveGroup.isChecked),
                                            adapter.id,
                                            it1
                                        )
                                    }
                                }
                                mAlertDialog?.dismiss()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ArchiveGroup>>, t: Throwable) {
                logs("Error ArchiveFr, getAllGroup")
            }

        })
    }

    private fun archiveGroup(boolean: String, id: Int, name: String) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val groupBody = ArchiveGroup(boolean, id, name)
        retIn.archiveGroup(token, groupBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when {
                    response.code() == 404 -> {
                        snackbar(requireView(), "Ошибка изменения!", Color.parseColor("#E11616"))
                    }
                    response.code() == 200 -> {
                        snackbar(
                            requireView(),
                            "Группа изменена!",
                            Color.parseColor("#4AAF39")
                        )
                        getAllGroups()
                    }
                    else -> {
                        snackbar(requireView(), "Неизвестная ошибка!", Color.parseColor("#E11616"))
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }

        })
    }

    fun getWallets() {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getAllWallets(token).enqueue(object : Callback<List<ArchiveWallet>> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<List<ArchiveWallet>>,
                response: Response<List<ArchiveWallet>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        changeRV?.adapter = walletAdapter
                        changeRV?.layoutManager = LinearLayoutManager(requireContext())
                        it.let {
                            walletAdapter.differ.submitList(it)
                        }
                        walletAdapter.notifyDataSetChanged()
                        walletAdapter.setOnItemClickListener { adapter ->
                            val mDialogView = LayoutInflater.from(context)
                                .inflate(R.layout.dialog_archive_wallet, null)
                            val mBuilder = context?.let { it1 ->
                                AlertDialog.Builder(it1)
                                    .setView(mDialogView)
                            }
                            val mAlertDialog = mBuilder?.show()
                            mDialogView?.cancel_wallet?.setOnClickListener {
                                mAlertDialog?.dismiss()
                            }

                            mDialogView.walletTitle.text = "Название кошелка: " + adapter.name
                            mDialogView.et_archive_wallet_balance.hint = "Старый баланс: ${adapter.availableBalance}"

                            if (adapter.status == "BLOCKED") {
                                mDialogView.isArchiveWallet.isChecked = true
                            }
                            mDialogView?.change_wallet?.setOnClickListener {
                                val name = mDialogView.et_archive_wallet_name.text.toString()
                                val balance = mDialogView.et_archive_wallet_balance.text.toString()
                                if (name.isNullOrEmpty() && balance.isNullOrEmpty()) {
                                    archiveWallet(
                                        getBooleanWallet(mDialogView.isArchiveWallet.isChecked),
                                        adapter.id,
                                       null,
                                        null
                                    )
                                } else {
                                    archiveWallet(
                                        getBooleanWallet(mDialogView.isArchiveWallet.isChecked),
                                        adapter.id,
                                        adapter.name?.let { it1 -> getName(name, it1, name) },
                                        try {
                                            balance.toInt()
                                        } catch (e: Exception) {
                                            adapter.availableBalance
                                        }
                                    )
                                }
                                mAlertDialog?.dismiss()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ArchiveWallet>>, t: Throwable) {
                logs("Error ArchiveFr, getAllGroup")
            }

        })
    }

    private fun archiveWallet(boolean: String, id: Int, name: String?, balance: Int?) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val walletBody = ArchiveWallet(balance, id, name, boolean)
        retIn.archiveWallet(token, walletBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when {
                    response.code() == 404 -> {
                        snackbar(requireView(), "Ошибка изменения!", Color.parseColor("#E11616"))
                    }
                    response.code() == 200 -> {
                        snackbar(
                            requireView(),
                            "Кошелек изменен!",
                            Color.parseColor("#4AAF39")
                        )
                        getWallets()
                    }
                    else -> {
                        snackbar(requireView(), "Неизвестная ошибка!", Color.parseColor("#E11616"))
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                logs("Error ArchiveFr, getAllGroup")
            }

        })
    }
}