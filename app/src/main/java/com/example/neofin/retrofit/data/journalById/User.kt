package com.example.neofin.retrofit.data.journalById

import com.example.neofin.retrofit.data.journalById.PersonX
import com.example.neofin.retrofit.data.journalById.Role

data class User(
    val email: String,
    val password: String,
    val person: PersonX,
    val role: Role,
    val userStatus: String
)