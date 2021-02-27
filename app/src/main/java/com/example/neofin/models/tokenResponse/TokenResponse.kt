package com.example.neofin.models.tokenResponse

import com.google.gson.annotations.SerializedName

data class TokenResponse(
        @SerializedName("token")
        val token: String,
        @SerializedName("expiration")
        val expiration: String
)
