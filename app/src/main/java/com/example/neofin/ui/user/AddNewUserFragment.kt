package com.example.neofin.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.addNewAccount.AddNewUser
import com.example.neofin.ui.addTransactions.data.SectionName
import com.example.neofin.utils.logs
import com.example.neofin.utils.spinnerSection
import com.example.neofin.utils.toast
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

        spinnerSection(requireContext(), group_add)
        group_add.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val sectionName: SectionName = parent.selectedItem as SectionName
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
                addNewUser(
                    user_email.text.toString(), data,  user_name.text.toString(),user_pass.text.toString(),
                    user_phone.text.toString(), user_surname.text.toString())
            }
        }catch (e: Exception){
            logs(e.toString())
        }

    }


    private fun addNewUser(
        email: String,
        group_ids: List<Int>,
        name: String,
        password: String,
        phoneNumber: String,
        surname: String
    ) = CoroutineScope(Dispatchers.Main).launch {
            val retIn = RetrofitBuilder.getInstance()
            val token = RetrofitBuilder.getToken()
            val addItems = AddNewUser(email, group_ids, name, password, phoneNumber, surname)
            retIn.addUser(token, addItems).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                    findNavController().navigate(R.id.navigation_user)
                    if (response.code() == 200) {
                        toast(requireContext(), "Successfully added")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {

                }

            })
        }
}