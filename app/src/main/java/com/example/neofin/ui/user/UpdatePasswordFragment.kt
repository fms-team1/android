package com.example.neofin.ui.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.neofin.retrofit.data.changePassword.ChangePassword
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.utils.toast
import kotlinx.android.synthetic.main.fragment_update_password.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class UpdatePasswordFragment: Fragment(R.layout.fragment_update_password) {
    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)

        change_Pass.setOnClickListener {
            MainScope().launch(Dispatchers.Main) {

                val newPass = et_new_password.text.toString().trim()
                val oldPass = et_old_password.text.toString().trim()
                if (newPass == confirm_new_password.text.toString()) {
                    if (newPass.isEmpty() || oldPass.isEmpty()) {
                        error_update.text = "Поля не должны быть пустыми"
                        error_update.visibility = View.VISIBLE
                        et_old_password.setBackgroundResource(R.drawable.error_edit_text)
                        et_old_password.requestFocus()
                        return@launch
                    }
                    error_update.visibility = View.GONE
                    changePassword(newPass, oldPass)
                    context?.let { it1 -> toast(it1,"Изменено")
                    }

                    findNavController().navigate(R.id.navigation_user)

                    val preferences: SharedPreferences? =
                        requireActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE)

                    preferences?.edit()?.remove("password")?.apply()
                    val editor = preferences?.edit()
                    val password = et_new_password.text.toString().trim()
                    if (password.isEmpty()) {
                        toast(requireContext(),"Нет пароля")
                    } else {
                        editor?.putString("password",password)
                        editor?.apply()
                    }
                } else {
                    confirm_new_password.setBackgroundResource(R.drawable.error_edit_text)
                    confirm_new_password.requestFocus()
                    et_new_password.setBackgroundResource(R.drawable.error_edit_text)
                    et_new_password.requestFocus()
                    error_update.visibility  = View.VISIBLE
                    error_update.text = "Пароли не совпадают!"
                }
            }
        }
    }

    private fun changePassword(newPass: String, oldPass: String) =
        CoroutineScope(Dispatchers.Main).launch {
            val retIn = RetrofitBuilder.getInstance()
            val token = RetrofitBuilder.getToken()
            val changePass = ChangePassword(newPass, oldPass)
            retIn.changePassword(token, changePass).enqueue(object : Callback<Void> {
                @SuppressLint("CommitPrefEdits")
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                }
            })
        }
}