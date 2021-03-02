package com.example.fms.retrofit.data.transactios

data class LastFifteenTransaction(
    val amount: Double,
    val comment: String,
    val createdDate: String,
    val deletedDate: Any,
    val id: Int,
    val lastName: String,
    val name: String,
    val wallet: Wallet
)