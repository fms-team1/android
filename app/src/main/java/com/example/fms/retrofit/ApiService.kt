package com.example.fms.retrofit

import com.example.fms.utils.Constants
import com.example.fms.retrofit.data.loginRequest.LoginRequest
import com.example.fms.retrofit.data.tokenResponse.TokenResponse
import com.example.fms.retrofit.data.transactios.Transactions
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST(Constants.LOGIN_URL)
    fun login(@Body request: LoginRequest): Call<TokenResponse>

    @Headers("Content-Type: application/json")
    @GET(Constants.HOME_URL)
    fun getHomePage(@Header("Authorization") token: String): Call<Transactions>
}