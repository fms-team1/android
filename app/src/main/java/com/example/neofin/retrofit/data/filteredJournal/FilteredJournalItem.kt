package com.example.neofin.retrofit.data.filteredJournal

data class FilteredJournalItem(
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
    val transactionTypeId: Int,
    val transactionType: String,
    val transferWalletId: Int,
    val transferWalletName: String,
    val walletId: Int,
    val walletName: String
)