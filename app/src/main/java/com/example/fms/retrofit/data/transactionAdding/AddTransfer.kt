package com.example.fms.retrofit.data.transactionAdding

data class AddTransfer(
    val amount: Int,
    val comment: String,
    val walletFromId: Int,
    val walletToId: Int
)