package com.example.neofin.retrofit.data.currentUser

data class Role(
    val id: Int,
    val permissions: List<String>,
    val role: String
)