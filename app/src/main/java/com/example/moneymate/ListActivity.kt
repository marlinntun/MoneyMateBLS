package com.example.moneymate

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneymate.adapter.TransactionAdapter
import com.example.moneymate.utils.AppConstants
import com.example.moneymate.utils.CurrencyHelper
import com.example.moneymate.viewmodel.TransactionViewModel

class ListActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var transactionAdapter: TransactionAdapter

    private lateinit var btnAddTransactionFromList: Button
    private lateinit var rvTransactions: RecyclerView
    private lateinit var tvEmptyState: TextView
    private lateinit var tvIncome: TextView
    private lateinit var tvExpense: TextView
    private lateinit var tvBalance: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        supportActionBar?.title = getString(R.string.list_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        setupRecyclerView()
        observeData()

        btnAddTransactionFromList.setOnClickListener {
            startActivity(Intent(this, InputActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshTransactions()
    }

    private fun initViews() {
        btnAddTransactionFromList = findViewById(R.id.btnAddTransactionFromList)
        rvTransactions = findViewById(R.id.rvTransactions)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        tvIncome = findViewById(R.id.tvIncome)
        tvExpense = findViewById(R.id.tvExpense)
        tvBalance = findViewById(R.id.tvBalance)
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter { transaction ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(AppConstants.EXTRA_ID, transaction.id)
            intent.putExtra(AppConstants.EXTRA_TITLE, transaction.title)
            intent.putExtra(AppConstants.EXTRA_CATEGORY, transaction.category)
            intent.putExtra(AppConstants.EXTRA_AMOUNT, transaction.amount)
            intent.putExtra(AppConstants.EXTRA_DATE, transaction.date)
            intent.putExtra(AppConstants.EXTRA_TYPE, transaction.type)
            intent.putExtra(AppConstants.EXTRA_NOTE, transaction.note)
            startActivity(intent)
        }

        rvTransactions.layoutManager = LinearLayoutManager(this)
        rvTransactions.adapter = transactionAdapter
    }

    private fun observeData() {
        viewModel.transactions.observe(this) { transactions ->
            transactionAdapter.submitList(transactions)

            val isEmpty = transactions.isEmpty()
            tvEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
            rvTransactions.visibility = if (isEmpty) View.GONE else View.VISIBLE
        }

        viewModel.totalIncome.observe(this) {
            tvIncome.text = CurrencyHelper.formatToRupiah(it)
        }

        viewModel.totalExpense.observe(this) {
            tvExpense.text = CurrencyHelper.formatToRupiah(it)
        }

        viewModel.balance.observe(this) {
            tvBalance.text = CurrencyHelper.formatToRupiah(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}