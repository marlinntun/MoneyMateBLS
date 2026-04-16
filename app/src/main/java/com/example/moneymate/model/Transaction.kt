package com.example.moneymate.model

data class Transaction(
    val id: Int = 0,
    val title: String,
    val category: String,
    val amount: Long,
    val date: String,
    val type: String,
    val note: String
)