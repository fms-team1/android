package com.example.neofin.retrofit.data.filteredJournal

data class FilteredJournalItem(
    val accountantName: String,
    val accountantSurname: String,
    val amount: Int,
    val categoryId: Int,
    val categoryName: String,
    val comment: String,
    val counterpartyId: Int,
    val counterpartyName: String,
    val counterpartySurname: String,
    val createdDate: String,
    val id: Int,
    val neoSection: String,
    val neoSectionId: Int,
    val transactionType: String,
    val transactionTypeId: Int,
    val transferWalletId: Int,
    val transferWalletName: String,
    val walletId: Int,
    val walletName: String
)