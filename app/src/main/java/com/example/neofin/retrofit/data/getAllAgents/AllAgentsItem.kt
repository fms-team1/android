package com.example.neofin.retrofit.data.getAllAgents

data class AllAgentsItem(
    val groupOfPeople: List<Any>,
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val surname: String
)