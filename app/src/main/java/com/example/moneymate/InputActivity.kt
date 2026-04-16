package com.example.moneymate

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moneymate.model.Transaction
import com.example.moneymate.utils.AppConstants
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class InputActivity : AppCompatActivity() {

    private lateinit var tilTitle: TextInputLayout
    private lateinit var tilAmount: TextInputLayout
    private lateinit var tilDate: TextInputLayout

    private lateinit var etTitle: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDate: EditText
    private lateinit var etNote: EditText

    private lateinit var spinnerCategory: Spinner
    private lateinit var radioGroupType: RadioGroup
    private lateinit var btnPreview: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        supportActionBar?.title = getString(R.string.input_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        setupCategorySpinner()
        setupDefaultDate()
        setupActions()
    }

    private fun initViews() {
        tilTitle = findViewById(R.id.tilTitle)
        tilAmount = findViewById(R.id.tilAmount)
        tilDate = findViewById(R.id.tilDate)

        etTitle = findViewById(R.id.etTitle)
        etAmount = findViewById(R.id.etAmount)
        etDate = findViewById(R.id.etDate)
        etNote = findViewById(R.id.etNote)

        spinnerCategory = findViewById(R.id.spinnerCategory)
        radioGroupType = findViewById(R.id.radioGroupType)
        btnPreview = findViewById(R.id.btnPreview)
    }

    private fun setupCategorySpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.transaction_categories,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter
    }

    private fun setupDefaultDate() {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID"))
        etDate.setText(formatter.format(Date()))
    }

    private fun setupActions() {
        etDate.setOnClickListener {
            showDatePicker()
        }

        btnPreview.setOnClickListener {
            moveToPreview()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val dialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format(
                    Locale.getDefault(),
                    "%02d/%02d/%04d",
                    dayOfMonth,
                    month + 1,
                    year
                )
                etDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        dialog.show()
    }

    private fun moveToPreview() {
        if (!validateInput()) return

        val selectedType = when (radioGroupType.checkedRadioButtonId) {
            R.id.rbIncome -> AppConstants.TYPE_INCOME
            R.id.rbExpense -> AppConstants.TYPE_EXPENSE
            else -> ""
        }

        val draftTransaction = Transaction(
            title = etTitle.text.toString().trim(),
            category = spinnerCategory.selectedItem.toString(),
            amount = etAmount.text.toString().trim().toLong(),
            date = etDate.text.toString().trim(),
            type = selectedType,
            note = etNote.text.toString().trim()
        )

        val intent = Intent(this, PreviewActivity::class.java)
        intent.putExtra(AppConstants.EXTRA_TITLE, draftTransaction.title)
        intent.putExtra(AppConstants.EXTRA_CATEGORY, draftTransaction.category)
        intent.putExtra(AppConstants.EXTRA_AMOUNT, draftTransaction.amount)
        intent.putExtra(AppConstants.EXTRA_DATE, draftTransaction.date)
        intent.putExtra(AppConstants.EXTRA_TYPE, draftTransaction.type)
        intent.putExtra(AppConstants.EXTRA_NOTE, draftTransaction.note)
        startActivity(intent)
    }

    private fun validateInput(): Boolean {
        tilTitle.error = null
        tilAmount.error = null
        tilDate.error = null

        val title = etTitle.text.toString().trim()
        val amount = etAmount.text.toString().trim()
        val date = etDate.text.toString().trim()
        val checkedTypeId = radioGroupType.checkedRadioButtonId

        var isValid = true

        if (title.isEmpty()) {
            tilTitle.error = "Judul transaksi wajib diisi"
            isValid = false
        }

        if (amount.isEmpty()) {
            tilAmount.error = "Nominal wajib diisi"
            isValid = false
        } else {
            val parsedAmount = amount.toLongOrNull()
            if (parsedAmount == null || parsedAmount <= 0L) {
                tilAmount.error = "Nominal harus berupa angka dan lebih dari 0"
                isValid = false
            }
        }

        if (date.isEmpty()) {
            tilDate.error = "Tanggal wajib diisi"
            isValid = false
        }

        if (checkedTypeId == -1) {
            Toast.makeText(this, "Pilih jenis transaksi terlebih dahulu", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}