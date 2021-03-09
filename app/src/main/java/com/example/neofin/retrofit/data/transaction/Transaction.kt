package com.example.neofin.retrofit.data.transaction

data class Transaction(
    val incomesAndExpensesHomeModel: IncomesAndExpensesHomeModel,
    val lastFifteenTransactions: List<LastFifteenTransaction>,
    val walletBalance: List<WalletBalance>
)