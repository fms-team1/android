package com.example.neofin.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import com.example.neofin.R
import com.example.neofin.retrofit.RetrofitBuilder
import com.example.neofin.retrofit.data.loginRequest.LoginRequest
import com.example.neofin.retrofit.data.tokenResponse.TokenResponse
import com.example.neofin.utils.toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        val preferences: SharedPreferences = getSharedPreferences("checkbox", Context.MODE_PRIVATE)
        val checkbox = preferences.getBoolean("remember", false)

        if (checkbox) {
            val email = preferences.getString("email", "")
            val password = preferences.getString("password", "")
            mainLogin?.visibility = View.INVISIBLE
            loginPb?.visibility = View.VISIBLE
            val actionBar = supportActionBar
            actionBar?.hide()
            if (email != null && password != null) {
                signIn(email, password)
            }
        }

        loginButton.setOnClickListener {
            loginPb?.visibility = View.VISIBLE
            mainLogin?.visibility = View.INVISIBLE
            MainScope().launch(Dispatchers.Main) {
                val email = emailET.text.toString().trim()
                val password = passwordET.text.toString().trim()
                if (email.isEmpty() || password.isEmpty()) {
                    errorText.text = "Поля не должны быть пустыми"
                    errorText?.visibility = View.VISIBLE
                    mainLogin?.visibility = View.VISIBLE
                    emailET.setBackgroundResource(R.drawable.error_edit_text)
                    emailET.requestFocus()
                    loginPb?.visibility = View.INVISIBLE
                    return@launch
                }
                errorText?.visibility = View.GONE
                signIn(email, password)
            }
        }

        rememberMe.setOnCheckedChangeListener { buttonView, _ ->
            if (buttonView.isChecked) {
                val settings = getSharedPreferences("checkbox", Context.MODE_PRIVATE)
                val editor = settings.edit()
                editor.putBoolean("remember", true)
                editor.putString("email", emailET.text.toString())
                editor.putString("password", passwordET.text.toString())
                editor.apply()
            } else if (!buttonView.isChecked) {
                val settings = getSharedPreferences("checkbox", Context.MODE_PRIVATE)
                val editor = settings.edit()
                editor.putBoolean("remember", false)
                editor.apply()
            }
        }
    }

    private fun signIn(name: String, password: String) {
        val retIn = RetrofitBuilder.getInstance()
        val signInInfo = LoginRequest(name, password)
        retIn.login(signInInfo).enqueue(object : Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                RetrofitBuilder.setToken(response.body()?.token ?: "")
                if (response.isSuccessful) {
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    loginPb?.visibility = View.INVISIBLE
                } else {
                    errorText.text = "Неверный логин или пароль"
                    errorText?.visibility = View.VISIBLE
                    mainLogin?.visibility = View.VISIBLE
                    loginPb?.visibility = View.INVISIBLE
                    emailET.setBackgroundResource(R.drawable.error_edit_text)
                    passwordET.setBackgroundResource(R.drawable.error_edit_text)
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                errorText.text = "Нет интернет соединения"
                errorText?.visibility = View.VISIBLE
                loginPb?.visibility = View.INVISIBLE
                mainLogin?.visibility = View.VISIBLE
            }
        })
    }
}