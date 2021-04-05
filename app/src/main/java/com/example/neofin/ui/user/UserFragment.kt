package com.example.neofin.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.addingResponse.AddResponse
import com.example.neofin.retrofit.data.getCurrentUser.Group
import com.example.neofin.retrofit.data.getCurrentUser.currentUser
import com.example.neofin.retrofit.data.tokenResponse.TokenResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.neofin.retrofit.data.changePassword.changePassword
import com.example.neofin.ui.MainActivity
import com.example.neofin.utils.logs
import com.example.neofin.utils.toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.neofin.retrofit.data.transactionAdding.AddTransactionOrExpense
import kotlinx.android.synthetic.main.dialog_update_password.*
import kotlinx.android.synthetic.main.dialog_update_password.view.*
import kotlinx.android.synthetic.main.fragment_user.*
import kotlin.math.log


class UserFragment : Fragment(R.layout.fragment_user) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        getCurrentUser()


        change_textView.setOnClickListener{
            val mDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_password,null)
            val mBuilder = context?.let { it1 ->
                AlertDialog.Builder(it1)
                    .setView(mDialogView)
                    .setTitle("Изменить пароль")
            }
            val  mAlertDialog = mBuilder?.show()
            mDialogView.change_Pass.setOnClickListener{
                mAlertDialog?.dismiss()
                ChangePassword("Admin1!","admin")
            }
        }
        }
 fun ChangePassword(NewPass: String,OldPass:String)= CoroutineScope(Dispatchers.Main).launch {
     val retIn = RetrofitBuilder.getInstance()
     val token = RetrofitBuilder.getToken()
     val changePass = changePassword(NewPass,OldPass)
     retIn.changePassword(token,changePass).enqueue(object : Callback<Void>{
         override fun onResponse(call: Call<Void>, response: Response<Void>) {
             toast(requireContext(),"Изменено")
         }

         override fun onFailure(call: Call<Void>, t: Throwable) {
            toast(requireContext(),"Ошибка")
         }
     })
 }


















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
                response.body()?.groups?.forEach {
                    if (it.name == "neobis_member") {
                        groups.text = " Сотрудник Neobis"
                    } else {
                        groups.text = " Сотрудник Neolabs"
                    }

                }
//                if (response.body()?.groups?.size != 0) {
//                    var result = "";
//                    for (group: Group in response.body()?.groups!!) {
//                        result += group.name + " "
//                    }
//                    groups.text = result.trim()
//                } else {
//                    groups.text = "не состоит в группе"
//                }
                phoneProfile.text = response.body()?.phoneNumber
            }




            override fun onFailure(call: Call<currentUser>, t: Throwable) {

            }

        })




            }
        }








