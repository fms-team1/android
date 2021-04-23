package com.example.neofin.retrofit.data.transactionAdding

data class AddTransactionOrExpense(
    val amount: Int,
    val categoryId: Int,
    val comment: String,
//    val counterpartyId: Int,
    val counterpartyName: String,
    val date: String,
    val walletId: Int
)