package com.example.neofin.retrofit.data.tokenResponse

import com.google.gson.annotations.SerializedName

data class TokenResponse(
        @SerializedName("jwt")
        val token: String,
)
