package com.example.fms.retrofit.data.transactios

data class Transactions(
    val currentBalance: Double,
    val lastFifteenTransactions: List<LastFifteenTransaction>
)