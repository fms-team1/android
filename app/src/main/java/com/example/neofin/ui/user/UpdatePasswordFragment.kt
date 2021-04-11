package com.example.neofin.ui.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.changePassword.changePassword
import com.example.neofin.ui.LoginActivity
import com.example.neofin.ui.MainActivity
import com.example.neofin.utils.logs
import com.example.neofin.utils.toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.errorText
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.update_password.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class UpdatePasswordFragment: Fragment(R.layout.update_password) {
    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
//        val preferences: SharedPreferences? =
//            this.activity?.getSharedPreferences("checkbox", Context.MODE_PRIVATE)
//
////        preferences?.edit()?.remove("remember")?.apply();
//
//        preferences?.edit()?.remove("password")?.apply();
//        val editor = preferences?.edit()
//        val password = et_new_password.text.toString().trim()
//        if(password.isEmpty()){
//            toast(requireContext(),"Нет пароля")
//        }else{
//            editor?.putString("password",password)
//            editor?.apply()
//        }



//        editor?.putBoolean("remember",false)




        change_Pass.setOnClickListener {
            MainScope().launch(Dispatchers.Main) {

                val new_pass = et_new_password.text.toString().trim()
                val old_pass = et_old_password.text.toString().trim()
                if(new_pass == confirm_new_password.text.toString()){
                    if (new_pass.isEmpty() || old_pass.isEmpty()) {
                        error_update.text = "Поля не должны быть пустыми"
                        error_update.visibility = View.VISIBLE
                        et_old_password.setBackgroundResource(R.drawable.error_edit_text)
                        et_old_password.requestFocus()
                        return@launch
                    }
                    error_update.visibility = View.GONE
                    ChangePassword(new_pass, old_pass)
                    context?.let { it1 -> toast(it1,"Изменено")
                    }
                    findNavController().navigate(R.id.navigation_user)
                    val preferences: SharedPreferences? =
                        requireActivity().getSharedPreferences("checkbox", Context.MODE_PRIVATE)

//        preferences?.edit()?.remove("remember")?.apply();

                    preferences?.edit()?.remove("password")?.apply();
                    val editor = preferences?.edit()
                    val password = et_new_password.text.toString().trim()
                    if(password.isEmpty()){
                        toast(requireContext(),"Нет пароля")
                    }else{
                        editor?.putString("password",password)
                        editor?.apply()
                    }
                }else{
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
        private fun ChangePassword(NewPass: String, OldPass: String) =
            CoroutineScope(Dispatchers.Main).launch {
                val retIn = RetrofitBuilder.getInstance()
                val token = RetrofitBuilder.getToken()
                val changePass = changePassword(NewPass, OldPass)
                retIn.changePassword(token, changePass).enqueue(object : Callback<Void> {
                    @SuppressLint("CommitPrefEdits")
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if(response.isSuccessful){
//                            val preferences: SharedPreferences? =
//                                requireActivity().getSharedPreferences("checkbox",
//                                    Context.MODE_PRIVATE)
//
//                            preferences?.edit()?.remove("remember")?.apply();
//
//                            preferences?.edit()?.remove("password")?.apply();
//                            val editor = preferences?.edit()
//                            editor!!.putString("password", et_new_password.text.toString())
//
//                            editor.putBoolean("remember",false)
//                            editor.apply()

                        }else{
                            logs(
                                "Ошибка в изменении пароля!"
                            )
                        }

                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {

                    }

                })
            }

}