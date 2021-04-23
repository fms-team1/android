package com.example.neofin.retrofit.data.transaction

data class LastFifteenTransaction(
    val accountantName: String,
    val accountantSurname: String,
    val amount: Double,
    val categoryName: String,
    val comment: String,
    val counterpartyName: String,
    val counterpartySurname: String,
    val createdDate: String,
    val id: Int,
    val neoSection: String,
    val transactionType: String,
    val walletId: Int,
    val walletName: String
)