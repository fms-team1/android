package com.example.neofin.retrofit.data.journal

data class JournalItem(
    val amount: Double,
    val categoryName: String,
    val createdDate: String,
    val id: Int,
    val transactionType: String
)