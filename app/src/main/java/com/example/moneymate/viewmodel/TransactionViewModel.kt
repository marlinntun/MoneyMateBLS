package com.example.moneymate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moneymate.model.Transaction
import com.example.moneymate.repository.TransactionRepository
import com.example.moneymate.utils.AppConstants

class TransactionViewModel : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    private val _totalIncome = MutableLiveData<Long>()
    val totalIncome: LiveData<Long> = _totalIncome

    private val _totalExpense = MutableLiveData<Long>()
    val totalExpense: LiveData<Long> = _totalExpense

    private val _balance = MutableLiveData<Long>()
    val balance: LiveData<Long> = _balance

    init {
        refreshTransactions()
    }

    fun refreshTransactions() {
        val data = TransactionRepository.getAllTransactions()
        _transactions.value = data

        val income = data
            .filter { it.type == AppConstants.TYPE_INCOME }
            .sumOf { it.amount }

        val expense = data
            .filter { it.type == AppConstants.TYPE_EXPENSE }
            .sumOf { it.amount }

        _totalIncome.value = income
        _totalExpense.value = expense
        _balance.value = income - expense
    }

    fun saveTransaction(draft: Transaction): Transaction {
        val saved = TransactionRepository.addTransaction(draft)
        refreshTransactions()
        return saved
    }

    fun deleteTransaction(id: Int) {
        TransactionRepository.deleteTransaction(id)
        refreshTransactions()
    }
}