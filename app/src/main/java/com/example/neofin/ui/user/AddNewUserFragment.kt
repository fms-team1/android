package com.example.neofin.ui.user

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.addGroup.GroupAdd
import com.example.neofin.retrofit.data.addNewAccount.AddNewUser
import com.example.neofin.retrofit.data.allGroups.Groups
import com.example.neofin.ui.user.data.GroupsIdName
import com.example.neofin.utils.logs
import com.example.neofin.utils.snackbar
import com.example.neofin.utils.spinnerGroup
import kotlinx.android.synthetic.main.dialog_add_group.view.*
import kotlinx.android.synthetic.main.fragment_add_new_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddNewUserFragment: Fragment(R.layout.fragment_add_new_user) {

    val data: MutableList<Int> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()

        closeBT?.setOnClickListener {
            findNavController().navigate(R.id.navigation_user)
        }

        spinnerGroup(requireContext(), group_add)
        group_add?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?, position: Int, id: Long
            ) {
                when (parent.selectedItem) {
                    "Создать группу" -> {
                        dialogCreateGroup()
                    }
                    "Выбрать из существующих" -> {
                        searchLayout.visibility = View.VISIBLE
                        getGroups()
                    }
                    else -> {
                        searchLayout.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        try {
            add_user_BT?.setOnClickListener {
                addNewUser(
                    user_email.text.toString(),
                    data,
                    user_name.text.toString(),
                    user_pass.text.toString(),
                    "${user_phone_add.text} ${user_phone.text}",
                    user_surname.text.toString()
                )
            }
        }catch (e: Exception){
            logs(e.toString())
        }

    }

    private fun dialogCreateGroup() = CoroutineScope(Dispatchers.Main).launch {
        val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_group, null)
        val mBuilder = context?.let { it1 ->
            AlertDialog.Builder(it1)
                .setView(mDialogView)
        }
        val  mAlertDialog = mBuilder?.show()
        mDialogView?.cancel_group?.setOnClickListener {
            mAlertDialog?.dismiss()
        }

        mDialogView?.create_group?.setOnClickListener {
            val name = mDialogView.et_add_group.text.toString()
            addGroup(name)
            mAlertDialog?.dismiss()
        }
    }

    private fun addGroup(name: String) = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        val groupBody = GroupAdd(name)
        retIn.addGroup(token, groupBody).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                when {
                    response.code() == 404 -> {
                        snackbar(
                            requireView(),
                            "Такая группа уже существует!",
                            Color.parseColor("#E11616")
                        )
                    }
                    response.code() == 200 -> {
                        snackbar(
                            requireView(),
                            "Группа добавлена!",
                            Color.parseColor("#4AAF39")
                        )
                    }
                    else -> {
                        snackbar(requireView(), "Неизвестная ошибка!", Color.parseColor("#E11616"))
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                logs("Error in AddNewUserFr, addGroup")
            }

        })
    }

    private fun getGroups() = CoroutineScope(Dispatchers.IO).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getAllGroups(token).enqueue(object : Callback<List<Groups>> {
            override fun onResponse(call: Call<List<Groups>>, response: Response<List<Groups>>) {
                if (response.isSuccessful) {
                    val groupArray: ArrayList<GroupsIdName> = ArrayList()
                    response.body()?.forEach {
                        groupArray.add(GroupsIdName(it.id, it.name))
                    }
                    val arrayAdapter =
                        ArrayAdapter(
                            requireContext(),
                            R.layout.spinner,
                            groupArray
                        )
                    if (listViewGroup != null) {
                        listViewGroup.adapter = arrayAdapter
                    } else {
                        logs("listViewGroup, AddNewUserFr")
                    }

                    searchGroup?.setOnSearchClickListener {
                        listViewGroup?.visibility = View.VISIBLE
                        hintSearchGroup?.visibility = View.GONE
                    }

                    searchGroup?.setOnCloseListener {
                        listViewGroup?.visibility = View.GONE
                        hintSearchGroup?.visibility = View.VISIBLE
                        searchLayout?.visibility = View.GONE
                        group_add?.setSelection(0)
                        false
                    }

                    searchGroup?.setOnQueryTextListener(object :
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

                    if (listViewGroup != null) {
                        listViewGroup?.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, i, _ ->
                                arrayAdapter.getItem(i)?.id?.let { data.add(it) }
                                searchGroup?.setQuery("${arrayAdapter.getItem(i)?.name}", true)
                                listViewGroup?.visibility = View.GONE
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

    private fun addNewUser(
        email: String,
        group_ids: List<Int>,
        name: String,
        password: String,
        phoneNumber: String,
        surname: String
    ) = CoroutineScope(Dispatchers.IO).launch {
            val retIn = RetrofitBuilder.getInstance()
            val token = RetrofitBuilder.getToken()
            val addItems = AddNewUser(email, group_ids, name, password, phoneNumber, surname)
            retIn.addUser(token, addItems).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    findNavController().navigate(R.id.navigation_user)
                    if (response.code() == 200) {
                        snackbar(
                            requireView(),
                            "Пользователь добавлен!",
                            Color.parseColor("#4AAF39")
                        )
                    } else {
                        snackbar(
                            requireView(),
                            "Пользователь не добавлен!",
                            Color.parseColor("#E11616")
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                }

            })
        }
}