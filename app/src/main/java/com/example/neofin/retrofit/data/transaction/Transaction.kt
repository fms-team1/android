package com.example.neofin.retrofit.data.transaction

data class Transaction(
    val incomesAndExpensesHomeModel: IncomesAndExpensesHomeModel,
    val transactionModels: List<LastFifteenTransaction>,
    val walletBalance: List<WalletBalance>
)