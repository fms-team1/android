package com.example.neofin.retrofit.data.user

data class CurrentUser (
    val email: String,
    val groups: List<Group>,
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val role: Role,
    val surname: String,
    val userStatus: String
)