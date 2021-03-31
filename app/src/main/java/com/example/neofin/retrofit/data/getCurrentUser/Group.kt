package com.example.neofin.retrofit.data.getCurrentUser

data class Group(
    val groupStatus: String,
    val id: Int,
    val name: String,
    val people: List<People>
)