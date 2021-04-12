package com.example.neofin.retrofit.data.journalById

data class JournalById(
    val accountantName: String,
    val accountantSurname: String,
    val amount: Int,
    val categoryName: String,
    val comment: String,
    val counterpartyName: String,
    val counterpartySurname: String,
    val createdDate: String,
    val id: Int,
    val neoSection: String,
    val transactionType: String,
    val transferWalletId: Int,
    val transferWalletName: String,
    val walletId: Int,
    val walletName: String
)