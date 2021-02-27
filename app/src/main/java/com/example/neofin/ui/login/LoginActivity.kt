package com.example.neofin.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.neofin.MainActivity
import com.example.neofin.R
import com.example.neofin.models.loginRequest.LoginRequest
import com.example.neofin.models.tokenResponse.TokenResponse
import com.example.neofin.retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private val PREFS_NAME = "preferences"
    private val PREF_EMAIL = "email"
    private val PREF_PASSWORD = "password"

    private val defaultEmailValue = ""
    private var emailValue: String? = null

    private val defaultPasswordValue = ""
    private var passwordValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

        if (rememberMe.isChecked) {
            savePreferences()
            loadPreferences()
        }
    }

    private fun signIn(name: String, password: String) {

        val retIn = RetrofitBuilder.getInstance()
        val signInInfo = LoginRequest(name, password)
        retIn.login(signInInfo).enqueue(object : retrofit2.Callback<TokenResponse> {
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
//                val token = RetrofitBuilder.setToken(response.body()?.token ?: "")
//                Toast.makeText(requireContext(), token.toString(), Toast.LENGTH_SHORT).show()
                if (response.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@LoginActivity, "Problem", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Problem", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun savePreferences() {
        val settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = settings.edit()

        emailValue = emailET.text.toString()
        passwordValue = passwordET.text.toString()
        editor.putString(PREF_EMAIL, emailValue)
        editor.putString(PREF_PASSWORD, passwordValue)
        editor.apply()
    }

    private fun loadPreferences() {
        val settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        emailValue = settings.getString(PREF_EMAIL, defaultEmailValue)
        passwordValue = settings.getString(PREF_PASSWORD, defaultPasswordValue)
        emailET.setText(emailValue)
        passwordET.setText(passwordValue)
        signIn(emailValue.toString(), passwordValue.toString())
    }

    override fun onResume() {
        super.onResume()
        loadPreferences()
    }
}