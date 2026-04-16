package com.example.moneymate

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.moneymate.model.Transaction
import com.example.moneymate.utils.AppConstants
import com.example.moneymate.utils.CurrencyHelper
import com.example.moneymate.viewmodel.TransactionViewModel

class PreviewActivity : AppCompatActivity() {

    private lateinit var viewModel: TransactionViewModel
    private lateinit var draftTransaction: Transaction

    private lateinit var tvPreviewTitle: TextView
    private lateinit var tvPreviewCategory: TextView
    private lateinit var tvPreviewAmount: TextView
    private lateinit var tvPreviewDate: TextView
    private lateinit var tvPreviewType: TextView
    private lateinit var tvPreviewNote: TextView
    private lateinit var btnSaveToList: Button
    private lateinit var btnEditAgain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        supportActionBar?.title = getString(R.string.preview_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        draftTransaction = Transaction(
            title = intent.getStringExtra(AppConstants.EXTRA_TITLE).orEmpty(),
            category = intent.getStringExtra(AppConstants.EXTRA_CATEGORY).orEmpty(),
            amount = intent.getLongExtra(AppConstants.EXTRA_AMOUNT, 0L),
            date = intent.getStringExtra(AppConstants.EXTRA_DATE).orEmpty(),
            type = intent.getStringExtra(AppConstants.EXTRA_TYPE).orEmpty(),
            note = intent.getStringExtra(AppConstants.EXTRA_NOTE).orEmpty()
        )
        showPreviewData()

        btnEditAgain.setOnClickListener {
            finish()
        }

        btnSaveToList.setOnClickListener {
            viewModel.saveTransaction(draftTransaction)
            Toast.makeText(this, "Transaksi berhasil disimpan", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ListActivity::class.java))
            finish()
        }
    }

    private fun initViews() {
        tvPreviewTitle = findViewById(R.id.tvPreviewTitle)
        tvPreviewCategory = findViewById(R.id.tvPreviewCategory)
        tvPreviewAmount = findViewById(R.id.tvPreviewAmount)
        tvPreviewDate = findViewById(R.id.tvPreviewDate)
        tvPreviewType = findViewById(R.id.tvPreviewType)
        tvPreviewNote = findViewById(R.id.tvPreviewNote)
        btnSaveToList = findViewById(R.id.btnSaveToList)
        btnEditAgain = findViewById(R.id.btnEditAgain)
    }

    private fun showPreviewData() {
        tvPreviewTitle.text = draftTransaction.title
        tvPreviewCategory.text = draftTransaction.category
        tvPreviewAmount.text = CurrencyHelper.formatToRupiah(draftTransaction.amount)
        tvPreviewDate.text = draftTransaction.date
        tvPreviewType.text = draftTransaction.type
        tvPreviewNote.text = draftTransaction.note.ifEmpty { "-" }

        val color = if (draftTransaction.type == AppConstants.TYPE_INCOME) {
            ContextCompat.getColor(this, R.color.income_green)
        } else {
            ContextCompat.getColor(this, R.color.expense_red)
        }

        tvPreviewAmount.setTextColor(color)
        tvPreviewType.setBackgroundColor(color)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}