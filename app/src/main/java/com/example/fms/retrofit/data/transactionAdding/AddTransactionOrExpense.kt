package com.example.fms.retrofit.data.transactionAdding

data class AddTransactionOrExpense(
    val amount: Int,
    val categoryId: Int,
    val comment: String,
//    val counterpartyId: Int,
    val counterpartyName: String,
    val walletId: Int
)