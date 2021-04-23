package com.example.neofin.retrofit.data.wallet

data class GetWalletItem(
    val availableBalance: Double,
    val id: Int,
    val name: String,
    val status: String
)