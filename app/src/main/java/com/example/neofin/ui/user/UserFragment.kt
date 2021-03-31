package com.example.neofin.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.addingResponse.AddResponse
import com.example.neofin.retrofit.data.getCurrentUser.Group
import com.example.neofin.retrofit.data.getCurrentUser.currentUser
import com.example.neofin.retrofit.data.loginRequest.LoginRequest
import com.example.neofin.retrofit.data.tokenResponse.TokenResponse
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.neofin.retrofit.data.changePassword.changePassword
import kotlinx.android.synthetic.main.dialog_update_password.*


class UserFragment : Fragment(R.layout.fragment_user) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        getCurrentUser()


    }



//    private fun UpdatePassword(oldPassword: String,
//                               newPassword: String) =
//        CoroutineScope(Dispatchers.Main).launch {
//            val retIn = RetrofitBuilder.getInstance()
//            val token = RetrofitBuilder.getToken()
//            val addNewPassword = changePassword(oldPassword, newPassword)
//            retIn.changePassword(token, addNewPassword).enqueue(object : Call<String>){
//                override fun onResponse(call: Call<String>, response: Response<String>) {
//                    if (response.isSuccessful) {
//                        progress.setVisibility(View.GONE)
//                        tv_message.setVisibility(View.GONE)
//                    }
//                }
//
//
//            }
//        }




    private fun getCurrentUser() = CoroutineScope(Dispatchers.Main).launch {
        val retIn = RetrofitBuilder.getInstance()
        val token = RetrofitBuilder.getToken()
        retIn.getCurrentUser(token).enqueue(object : Callback<currentUser> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<currentUser>, response: Response<currentUser>) {
                val name = response.body()?.name
                val surname = response.body()?.surname
                profile.text = "$name $surname"
                email.text = response.body()?.email
                if (response.body()?.groups?.size != 0) {
                    var result = "";
                    for (group: Group in response.body()?.groups!!) {
                        result += group.name + " "
                    }
                    groups.text = result.trim()
                } else {
                    groups.text = "не состоит в группе"
                }
                phoneProfile.text = response.body()?.phoneNumber
            }




            override fun onFailure(call: Call<currentUser>, t: Throwable) {

            }

        })




            }
        }








