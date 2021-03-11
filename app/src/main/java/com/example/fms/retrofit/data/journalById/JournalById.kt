package com.example.fms.retrofit.data.journalById

data class JournalById(
    val amount: Double,
    val category: Category,
    val comment: String,
    val createdDate: String,
    val id: Int,
    val person: Person,
    val transactionStatus: String,
    val user: User,
    val wallet: Wallet,
    val wallet2: Wallet2
)