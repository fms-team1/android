package com.example.neofin.retrofit.data.updateUser

data class UpdateUser(
    val email: String?,
    val groupIds: List<Int>?,
    val id: Int,
    val name: String?,
    val phoneNumber: String?,
    val surname: String?,
    val userStatus: String?
)