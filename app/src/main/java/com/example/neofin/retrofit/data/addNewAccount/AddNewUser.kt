package com.example.neofin.retrofit.data.addNewAccount

data class AddNewUser(
    val email: String,
    val group_ids: List<Int>,
    val name: String,
    val password: String,
    val phoneNumber: String,
    val surname: String
)
