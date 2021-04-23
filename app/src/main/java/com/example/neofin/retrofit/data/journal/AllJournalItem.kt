package com.example.neofin.retrofit.data.journal

data class AllJournalItem(
    val amount: Double,
    val category: String,
    val createdDate: String,
    val id: Int,
    val transactionType: String
)