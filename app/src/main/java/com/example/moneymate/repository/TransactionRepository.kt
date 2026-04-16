package com.example.moneymate.repository

import com.example.moneymate.model.Transaction
import com.example.moneymate.utils.AppConstants

object TransactionRepository {

    private val transactions = mutableListOf<Transaction>()
    private var nextId = 1

    init {
        if (transactions.isEmpty()) {
            seedDummyData()
        }
    }

    fun getAllTransactions(): List<Transaction> {
        return transactions.sortedByDescending { it.id }
    }

    fun addTransaction(draft: Transaction): Transaction {
        val savedTransaction = draft.copy(id = nextId++)
        transactions.add(savedTransaction)
        return savedTransaction
    }

    fun deleteTransaction(id: Int) {
        transactions.removeAll { it.id == id }
    }

    fun getTransactionById(id: Int): Transaction? {
        return transactions.firstOrNull { it.id == id }
    }

    private fun seedDummyData() {
        addTransaction(
            Transaction(
                title = "Uang Saku Bulanan",
                category = "Pemasukan Lain",
                amount = 500_000,
                date = "01/04/2026",
                type = AppConstants.TYPE_INCOME,
                note = "Uang saku dari orang tua"
            )
        )

        addTransaction(
            Transaction(
                title = "Beli Makan Siang",
                category = "Makanan",
                amount = 20_000,
                date = "02/04/2026",
                type = AppConstants.TYPE_EXPENSE,
                note = "Ayam geprek dan es teh"
            )
        )

        addTransaction(
            Transaction(
                title = "Bayar Parkir",
                category = "Transportasi",
                amount = 5_000,
                date = "02/04/2026",
                type = AppConstants.TYPE_EXPENSE,
                note = "Parkir kampus"
            )
        )
    }
}