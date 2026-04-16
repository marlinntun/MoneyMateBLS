package com.example.moneymate

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.moneymate.model.Transaction
import com.example.moneymate.utils.AppConstants
import com.example.moneymate.utils.CurrencyHelper
import com.example.moneymate.viewmodel.TransactionViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var transaction: Transaction

    private lateinit var btnDeleteTransaction: Button
    private lateinit var tvDetailTitle: TextView
    private lateinit var tvDetailCategory: TextView
    private lateinit var tvDetailAmount: TextView
    private lateinit var tvDetailDate: TextView
    private lateinit var tvDetailType: TextView
    private lateinit var tvDetailNote: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.title = getString(R.string.detail_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        transaction = Transaction(
            id = intent.getIntExtra(AppConstants.EXTRA_ID, 0),
            title = intent.getStringExtra(AppConstants.EXTRA_TITLE).orEmpty(),
            category = intent.getStringExtra(AppConstants.EXTRA_CATEGORY).orEmpty(),
            amount = intent.getLongExtra(AppConstants.EXTRA_AMOUNT, 0L),
            date = intent.getStringExtra(AppConstants.EXTRA_DATE).orEmpty(),
            type = intent.getStringExtra(AppConstants.EXTRA_TYPE).orEmpty(),
            note = intent.getStringExtra(AppConstants.EXTRA_NOTE).orEmpty()
        )
        showDetail()

        btnDeleteTransaction.setOnClickListener {
            showDeleteConfirmation()
        }
    }

    private fun initViews() {
        btnDeleteTransaction = findViewById(R.id.btnDeleteTransaction)
        tvDetailTitle = findViewById(R.id.tvDetailTitle)
        tvDetailCategory = findViewById(R.id.tvDetailCategory)
        tvDetailAmount = findViewById(R.id.tvDetailAmount)
        tvDetailDate = findViewById(R.id.tvDetailDate)
        tvDetailType = findViewById(R.id.tvDetailType)
        tvDetailNote = findViewById(R.id.tvDetailNote)
    }

    private fun showDetail() {
        tvDetailTitle.text = transaction.title
        tvDetailCategory.text = transaction.category
        tvDetailAmount.text = CurrencyHelper.formatToRupiah(transaction.amount)
        tvDetailDate.text = transaction.date
        tvDetailType.text = transaction.type
        tvDetailNote.text = transaction.note.ifEmpty { "-" }

        val color = if (transaction.type == AppConstants.TYPE_INCOME) {
            ContextCompat.getColor(this, R.color.income_green)
        } else {
            ContextCompat.getColor(this, R.color.expense_red)
        }

        tvDetailAmount.setTextColor(color)
        tvDetailType.setBackgroundColor(color)
    }

    private fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_confirm_title))
            .setMessage(getString(R.string.delete_confirm_message))
            .setPositiveButton(getString(R.string.yes_delete)) { _, _ ->
                viewModel.deleteTransaction(transaction.id)
                Toast.makeText(this, "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}