package com.example.neofin.retrofit

object Common {
    private const val BASE_URL = "URL"
    val retrofitService: RetrofitService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)
}