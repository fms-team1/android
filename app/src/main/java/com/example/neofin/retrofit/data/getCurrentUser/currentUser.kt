package com.example.neofin.retrofit.data.getCurrentUser

import com.example.neofin.retrofit.data.currentUser.Group
import com.example.neofin.retrofit.data.currentUser.Role

data class currentUser(
    val email: String,
    val groups: List<Group>,
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val role: Role,
    val surname: String,
    val userStatus: String
)