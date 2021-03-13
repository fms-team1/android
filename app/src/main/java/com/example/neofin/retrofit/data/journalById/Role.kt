package com.example.neofin.retrofit.data.journalById

data class Role(
    val id: Int,
    val permissions: List<String>,
    val role: String
)