package com.example.fms.retrofit.data.journal

data class JournalItem(
    val amount: Double,
    val category: String,
    val createdDate: String,
    val id: Int,
    val transactionType: String
)