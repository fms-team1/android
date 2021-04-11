package com.example.neofin.ui.user

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.transactionAdding.AddTransfer
import com.example.neofin.utils.logs
import com.example.neofin.utils.toast
import kotlinx.android.synthetic.main.fragment_user.*
import com.example.neofin.retrofit.data.addNewAccount.addNewUser;
import com.example.neofin.retrofit.data.addingResponse.AddResponse
import com.example.neofin.retrofit.data.category.Category
import com.example.neofin.ui.addTransactions.data.CategoryIdName
import com.example.neofin.ui.addTransactions.data.SectionName
import com.example.neofin.utils.spinnerCategory
import com.example.neofin.utils.spinnerSection
import kotlinx.android.synthetic.main.add_users.*
import kotlinx.android.synthetic.main.fragment_adding.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AddNewUserFragment: Fragment(R.layout.add_users) {


    val data: MutableList<Int> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)

        spinnerSection(requireContext(), group_add)
        group_add.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val sectionName: SectionName = parent.selectedItem as SectionName
//                sectionType = sectionName.backName
                data.add(sectionName.backName)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

try {


    val email = user_email.text.toString()
    val password = user_pass.text.toString()
    val user_n = user_name.text.toString()
    val user_p = user_phone.text.toString()
    val surname = user_surname.text.toString()

    add_user_BT.setOnClickListener {
        AddNewUser(
            user_email.text.toString(), data,  user_name.text.toString(),user_pass.text.toString(),
            user_phone.text.toString(), user_surname.text.toString())

    }
}catch (e: Exception){
    logs(e.toString())
}







    }


    private fun AddNewUser(
        email: String,
        group_ids: List<Int>,
        name: String,
        password: String,
        phoneNumber: String,
        surname: String
    ) =
        CoroutineScope(Dispatchers.Main).launch {
            val retIn = RetrofitBuilder.getInstance()
            val token = RetrofitBuilder.getToken()
            val addItems = addNewUser(email, group_ids, name, password, phoneNumber, surname)
            retIn.addUser(token, addItems).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {


                    findNavController().navigate(R.id.action_AddNewUserFragment_to_navigation_user)
                    if (response.code() == 200) {
                        toast(requireContext(), "Successfully added")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                }

            })
        }
}