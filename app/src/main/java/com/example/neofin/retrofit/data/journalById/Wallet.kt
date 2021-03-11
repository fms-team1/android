package com.example.neofin.retrofit.data.journalById

data class Wallet(
    val availableBalance: Double,
    val createdDate: String,
    val deletedDate: Any,
    val id: Int,
    val name: String,
    val walletStatus: String
)