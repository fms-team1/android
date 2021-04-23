package com.example.neofin.ui.user

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neofin.R
import com.example.neofin.adapters.ArchiveCategoryAdapter
import com.example.neofin.adapters.ArchiveGroupAdapter
import com.example.neofin.adapters.ArchiveUserAdapter
import com.example.neofin.adapters.ArchiveWalletAdapter
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.allGroups.Groups
import com.example.neofin.retrofit.data.allUsers.AllUsers
import com.example.neofin.retrofit.data.archiveCategory.ArchiveCategory
import com.example.neofin.retrofit.data.archiveGroups.ArchiveGroup
import com.example.neofin.retrofit.data.archiveWallet.ArchiveWallet
import com.example.neofin.retrofit.data.updateUser.UpdateUser
import com.example.neofin.retrofit.data.wallet.GetWallet
import com.example.neofin.ui.user.data.GroupsIdName
import com.example.neofin.ui.user.data.Section
import com.example.neofin.ui.user.data.StatusName
import com.example.neofin.utils.logs
import com.example.neofin.utils.snackbar
import com.example.neofin.utils.spinnerArchive
import com.example.neofin.utils.spinnerStatusUser
import kotlinx.android.synthetic.main.dialog_archive_category.view.*
import kotlinx.android.synthetic.main.dialog_archive_group.view.*
import kotlinx.android.synthetic.main.dialog_archive_user.view.*
import kotlinx.android.synthetic.main.dialog_archive_wallet.view.*
import kotlinx.android.synthetic.main.fragment_add_new_user.*
import kotlinx.android.synthetic.main.fragment_archive.*
import kotlinx.android.synthetic.main.fragment_archive.closeBT
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
    private val userAdapter by lazy { ArchiveUserAdapter() }
    var status : String? = null
    var nameUser : String? = null
    var surnameUser : String? = null
    var phoneUser : String? = null
    var emailUser : String? = null
    val data: MutableList<Int> = ArrayList()

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
                when (parent.selectedItem) {
                    "Кошелек" -> {
                        changeRV.visibility = View.VISIBLE
                        getWallets()
                    }
                    "Группа" -> {
                        changeRV.visibility = View.VISIBLE
                        getAllGroups()
                    }
                    "Категория" -> {
                        changeRV.visibility = View.VISIBLE
                        getAllCategory()
                    }
                    "Пользователь" -> {
                        changeRV.visibility = View.VISIBLE
                        getAllUser()
                    }
                    else -> {
                        changeRV.visibility = View.INVISIBLE
                    }
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

    fun getAllUser() {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getAllUsers(token).enqueue(object : Callback<AllUsers> {
            override fun onResponse(call: Call<AllUsers>, response: Response<AllUsers>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        changeRV?.adapter = userAdapter
                        changeRV?.layoutManager = LinearLayoutManager(requireContext())
                        userAdapter.differ.submitList(it)
                        userAdapter.notifyDataSetChanged()
                        userAdapter.setOnItemClickListener { adapter ->
                            val mDialogView = LayoutInflater.from(context)
                                .inflate(R.layout.dialog_archive_user, null)
                            val mBuilder = context?.let { it1 ->
                                AlertDialog.Builder(it1)
                                    .setView(mDialogView)
                            }
                            val mAlertDialog = mBuilder?.show()
                            mDialogView?.cancelUser?.setOnClickListener {
                                mAlertDialog?.dismiss()
                            }

                            spinnerUser(requireContext(), mDialogView.statusUser)
                            getGroups(mDialogView.searchGroupUser, mDialogView.listViewGroupUser, mDialogView.hintSearchUser)

                            mDialogView.et_archive_name.hint = "Старое имя: " + adapter.name
                            mDialogView.et_archive_surname.hint = "Старая фамилия: " + adapter.surname
                            mDialogView.et_archive_phone.hint = "Старый номер: " + adapter.phoneNumber

                            mDialogView?.changeUser?.setOnClickListener {
                                if (mDialogView.et_archive_name.text.isNotEmpty()){
                                    nameUser = mDialogView.et_archive_name.text.toString()
                                }
                                if (mDialogView.et_archive_surname.text.isNotEmpty()) {
                                    surnameUser = mDialogView.et_archive_surname.text.toString()
                                }
                                if (mDialogView.et_archive_phone.text.isNotEmpty()) {
                                    phoneUser = mDialogView.et_archive_phone.text.toString()
                                }
                                if (data.size > 0) {
                                    archiveUser(adapter.email, data, adapter.id, nameUser, phoneUser, surnameUser, status)
                                } else {
                                    archiveUser(adapter.email, null, adapter.id, nameUser, phoneUser, surnameUser, status)
                                }

                                mAlertDialog?.dismiss()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AllUsers>, t: Throwable) {
                logs("Error ArchiveFr, getAllUser")
            }

        })
    }

    private fun archiveUser(email : String?, groups : List<Int>?,
                            id : Int, name : String?,
                            number: String?, surname: String?,
                            status: String?) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val userBody = UpdateUser(email, groups, id, name, number, surname, status)
        retIn.changeUser(token, userBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when {
                    response.code() == 404 -> {
                        snackbar(requireView(), "Ошибка изменения!", Color.parseColor("#E11616"))
                    }
                    response.code() == 200 -> {
                        snackbar(
                            requireView(),
                            "Пользователь изменен!",
                            Color.parseColor("#4AAF39")
                        )
                        getAllUser()
                    }
                    else -> {
                        snackbar(requireView(), "Неизвестная ошибка!", Color.parseColor("#E11616"))
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                logs("Error ArchiveFr, archiveUser")
            }
        })
    }

    private fun getGroups(searchView: SearchView?, listView: ListView?, textView: TextView?) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getAllGroups(token).enqueue(object : Callback<List<Groups>> {
            override fun onResponse(call: Call<List<Groups>>, response: Response<List<Groups>>) {
                if (response.isSuccessful) {
                    val groupArray: ArrayList<GroupsIdName> = ArrayList()
                    groupArray.clear()
                    response.body()?.forEach {
                        groupArray.add(GroupsIdName(it.id, it.name))
                    }
                    val arrayAdapter =
                        ArrayAdapter(
                            requireContext(),
                            R.layout.spinner,
                            groupArray
                        )
                    if (listView != null) {
                        listView.adapter = arrayAdapter
                    } else {
                        logs("listViewGroup, AddNewUserFr")
                    }

                    searchView?.setOnSearchClickListener {
                        listView?.visibility = View.VISIBLE
                        textView?.visibility = View.GONE
                    }

                    searchView?.setOnCloseListener {
                        listView?.visibility = View.GONE
                        textView?.visibility = View.VISIBLE
                        false
                    }

                    searchView?.setOnQueryTextListener(object :
                        SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            arrayAdapter.filter.filter(query)
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            arrayAdapter.filter.filter(newText)
                            return false
                        }
                    })

                    if (listView != null) {
                        listView.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, i, _ ->
                                arrayAdapter.getItem(i)?.id?.let { data.add(it) }
                                searchView?.setQuery("${arrayAdapter.getItem(i)?.name}", true)
                                listView.visibility = View.GONE
                            }
                    } else {
                        logs("Error in AddNewUserFr, getGroups")
                    }
                } else {
                    logs("Error in AddNewUserFr, getGroups")
                }
            }

            override fun onFailure(call: Call<List<Groups>>, t: Throwable) {
                logs("Error in AddNewUserFr, getGroups")
            }

        })
    }

    fun spinnerUser(context: Context, spinner: Spinner?) {
        spinnerStatusUser(context, spinner)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val sectionName: StatusName = parent.selectedItem as StatusName
                status = sectionName.backName

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

}