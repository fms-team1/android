package com.example.neofin.models

import com.google.gson.annotations.SerializedName

data class User(
        @SerializedName("email")
        var email: String,

        @SerializedName("password")
        var password: String
)