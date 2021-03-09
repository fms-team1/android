package com.example.fms.retrofit.data.journalById

data class Person(
    val createdDate: String,
    val deletedDate: Any,
    val groupOfPeople: List<Any>,
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val surname: String
)