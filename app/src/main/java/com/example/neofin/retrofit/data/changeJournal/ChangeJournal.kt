package com.example.neofin.retrofit.data.changeJournal

data class ChangeJournal(
    val amount: Int?,
    val categoryId: Int?,
    val comment: String?,
    val counterpartyId: Int?,
    val id: Int,
    val transferWalletId: Int?,
    val walletId: Int?
)