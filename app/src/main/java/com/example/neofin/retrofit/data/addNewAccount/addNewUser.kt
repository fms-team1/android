package com.example.neofin.retrofit.data.addNewAccount

data class addNewUser(
    val email: String,
    val group_ids: List<Int>,
    val name: String,
    val password: String,
    val phoneNumber: String,
    val surname: String
)