package com.example.neofin.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val preferences: SharedPreferences = getSharedPreferences("checkbox", Context.MODE_PRIVATE)
        val checkbox = preferences.getBoolean("remember", false)

        if (checkbox) {
            val email = preferences.getString("email", "")
            val password = preferences.getString("password", "")
            if (email != null && password != null) {
                signIn(email, password)
            }
        }

        loginButton.setOnClickListener {
            MainScope().launch(Dispatchers.Main) {
                val email = emailET.text.toString().trim()
                val password = passwordET.text.toString().trim()

                if (email.isEmpty()) {
                    emailET.error = "Поле не должно быть пустым"
                    emailET.requestFocus()
                    Toast.makeText(
                        this@LoginActivity,
                        "Поле не должно быть пустым",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }
                if (password.isEmpty()) {
                    passwordET.error = "Поле не должно быть пустым"
                    passwordET.requestFocus()
                    Toast.makeText(
                        this@LoginActivity,
                        "Поле не должно быть пустым",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }
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
                } else {
                    toast(this@LoginActivity, "Неверный логин или пароль")
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                toast(this@LoginActivity, "Неверный логин или пароль")
            }
        })
    }
}