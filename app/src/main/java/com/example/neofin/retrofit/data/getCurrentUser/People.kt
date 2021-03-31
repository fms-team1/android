package com.example.neofin.retrofit.data.getCurrentUser

data class People(
    val createdDate: String,
    val deletedDate: String,
    val groupOfPeople: List<Any>,
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val surname: String
)