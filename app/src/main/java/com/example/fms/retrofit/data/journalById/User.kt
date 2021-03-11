package com.example.fms.retrofit.data.journalById

data class User(
    val email: String,
    val password: String,
    val person: PersonX,
    val role: Role,
    val userStatus: String
)