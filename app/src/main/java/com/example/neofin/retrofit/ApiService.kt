package com.example.neofin.retrofit

import com.example.neofin.models.loginRequest.LoginRequest
import com.example.neofin.models.tokenResponse.TokenResponse
import com.example.neofin.utils.Constants
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<TokenResponse>
}