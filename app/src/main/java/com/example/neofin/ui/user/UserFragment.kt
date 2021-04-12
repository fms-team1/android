package com.example.neofin.ui.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.ui.LoginActivity
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import androidx.navigation.fragment.findNavController
import com.example.neofin.retrofit.data.user.CurrentUser
import retrofit2.Callback


class UserFragment : Fragment(R.layout.fragment_user) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()
        getCurrentUser()

        logout.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                val preferences: SharedPreferences? =
                    requireActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE)

                preferences?.edit()?.remove("remember")?.apply();
                preferences?.edit()?.remove("password")?.apply();

                preferences?.edit()?.remove("email")?.apply()

            }

        }
        back_user.setOnClickListener {
            findNavController().navigate(R.id.navigation_home)
        }

        change_textView.setOnClickListener {
            findNavController().navigate(R.id.updatePasswordFragment)
        }
        addUser.setOnClickListener {
            findNavController().navigate(R.id.addNewUserFragment)
        }


    }


    private fun getCurrentUser() = CoroutineScope(Dispatchers.Main).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getCurrentUser(token).enqueue(object : Callback<CurrentUser> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<CurrentUser>, response: Response<CurrentUser>) {
                val name = response.body()?.name
                val surname = response.body()?.surname
                profile.text = "$name $surname"
                email.text = response.body()?.email
                response.body()?.groups?.forEach {
                    if (it.name == "neobis_member") {
                        groups.text = " Сотрудник Neobis"
                    } else {
                        groups.text = " Сотрудник Neolabs"
                    }

                }
                phoneProfile.text = response.body()?.phoneNumber
            }

            override fun onFailure(call: Call<CurrentUser>, t: Throwable) {

            }

        })

    }
}