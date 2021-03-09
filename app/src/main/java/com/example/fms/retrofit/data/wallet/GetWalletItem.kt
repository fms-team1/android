package com.example.fms.retrofit.data.wallet

data class GetWalletItem(
    val availableBalance: Double,
    val id: Int,
    val name: String
)